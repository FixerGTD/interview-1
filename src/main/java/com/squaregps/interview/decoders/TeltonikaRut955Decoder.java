package com.squaregps.interview.decoders;

import com.github.snksoft.crc.CRC;
import com.squaregps.interview.LocationMessage;
import com.squaregps.interview.MessageDecoder;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TeltonikaRut955Decoder implements MessageDecoder {

  private static final int IO_DIGITAL_INPUT_1 = 1;
  private static final int IO_DIGITAL_INPUT_2 = 2;
  private static final int IO_ANALOG_INPUT_1 = 9;
  private static final int IO_GSM_LEVEL = 21;

  @Override
  public List<LocationMessage> decode(@Nonnull ByteBuffer buf) {
    if (buf.remaining() < 1) {
      return Collections.emptyList();
    }

    int originalPosition = buf.position();
    int dataLength = 0;
    int crcExpected = 0;

    if (buf.remaining() >= 8) {
      int possiblePreamble = buf.getInt();

      if (possiblePreamble == 0) {
        dataLength = buf.getInt();

        if (buf.remaining() < dataLength) {
          return Collections.emptyList(); // Not enough data
        }

        // Slice buffer for CRC check
        ByteBuffer crcBuf = buf.slice();
        crcBuf.limit(dataLength - 4); // exclude last 4 bytes (CRC)
        buf.position(buf.position() + dataLength - 4); // move to CRC

        crcExpected = buf.getInt();
        long crcCalculated =
            CRC.calculateCRC(
                CRC.Parameters.CRC16, crcBuf.array(), crcBuf.position(), crcBuf.remaining());

        if ((crcCalculated & 0xFFFF) == (crcExpected & 0xFFFF)) {
          log.warn("CRC mismatch: expected {}, calculated {}", crcExpected, crcCalculated);
          return Collections.emptyList();
        }

        // Go to start position of a codec section
        buf.position(originalPosition + 8);
      } else {
        buf.position(originalPosition); // rewind, not a framed message
      }
    }

    byte codecId = buf.get();
    if (codecId != 8) {
      return Collections.emptyList();
    }

    byte recordCount = buf.get();
    List<LocationMessage> messages = new ArrayList<>(recordCount);

    for (int i = 0; i < recordCount; i++) {
      if (buf.remaining() < 8) { // Minimum size for a timestamp
        break;
      }

      LocationMessage message = parseDataRecord(buf);
      if (message != null) {
        messages.add(message);
      }
    }

    // Confirm the record count
    if (buf.remaining() < 1) {
      return Collections.emptyList();
    }

    byte expectedRecordCount = buf.get();
    if (expectedRecordCount != recordCount) {
      return Collections.emptyList();
    }

    return messages;
  }

  private LocationMessage parseDataRecord(ByteBuffer buf) {
    try {
      LocationMessage message = new LocationMessage();

      // Timestamp (8 bytes)
      long timestamp = buf.getLong();
      message.setDateTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Z")));

      // Priority (1 byte) - we can skip this
      buf.get();

      // GPS data
      // Longitude (4 bytes)
      int longRaw = buf.getInt();
      message.setLongitude(longRaw / 10000000.0);

      // Latitude (4 bytes)
      int latRaw = buf.getInt();
      message.setLatitude(latRaw / 10000000.0);

      // Altitude (2 bytes)
      message.setAltitude((int) buf.getShort());

      // Angle (2 bytes)
      message.setAngle((int) buf.getShort());

      // Satellites (1 byte)
      message.setSatellites((int) buf.get() & 0xFF);

      // Speed (2 bytes)
      message.setSpeed((int) buf.getShort());

      // Parse I/O Events
      parseIOElements(buf, message);

      return message;
    } catch (Exception e) {
      log.error("There has been error during message parsing");
      return null;
    }
  }

  private void parseIOElements(ByteBuffer buf, LocationMessage message) {
    // Event IO ID (1 byte) - we can skip this
    buf.get();

    // Number of total IO elements (1 byte)
    int totalElements = buf.get() & 0xFF;

    // Read 1-byte elements
    int count1 = buf.get() & 0xFF;
    for (int i = 0; i < count1; i++) {
      if (buf.remaining() < 2) {
        return; // Not enough data remaining
      }

      int id = buf.get() & 0xFF;
      byte value = buf.get();

      switch (id) {
        case IO_DIGITAL_INPUT_1:
          message.setDigitalInputStatus1(value == 1);
          break;
        case IO_DIGITAL_INPUT_2:
          message.setDigitalInputStatus2(value == 1);
          break;
        case IO_GSM_LEVEL:
          message.setGsmLevel((int) value & 0xFF);
          break;
        default:
          log.info("Unknown or unneeded IO element");
          break;
      }
    }

    // Read 2-byte elements
    if (buf.remaining() < 1) {
      return; // Not enough data
    }

    int count2 = buf.get() & 0xFF;
    for (int i = 0; i < count2; i++) {
      if (buf.remaining() < 3) {
        return; // Not enough data remaining
      }

      int id = buf.get() & 0xFF;
      short value = buf.getShort();

      if (id == IO_ANALOG_INPUT_1) {
        // Convert the analog input to voltage usually in mV, so divide by 1000 for Volts
        message.setAnalogInput1(value / 1000.0);
      }
    }

    // Read 4-byte elements
    if (buf.remaining() < 1) {
      return; // Not enough data
    }

    int count4 = buf.get() & 0xFF;
    for (int i = 0; i < count4; i++) {
      if (buf.remaining() < 5) {
        return; // Not enough data remaining
      }

      int id = buf.get() & 0xFF;
      int value = buf.getInt();

      if (id == IO_ANALOG_INPUT_1) {
        // In case analog input is stored as a 4-byte value
        message.setAnalogInput1(value / 1000.0);
      }
    }

    // Read 8-byte elements
    if (buf.remaining() < 1) {
      return; // Not enough data
    }

    int count8 = buf.get() & 0xFF;
    for (int i = 0; i < count8; i++) {
      if (buf.remaining() < 9) {
        return; // Not enough data remaining
      }

      int id = buf.get() & 0xFF;
      buf.getLong(); // Skip value, we don't need 8-byte elements
    }
  }
}
