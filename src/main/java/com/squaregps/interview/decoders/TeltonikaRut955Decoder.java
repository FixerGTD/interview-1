package com.squaregps.interview.decoders;

import com.squaregps.interview.LocationMessage;
import com.squaregps.interview.MessageDecoder;
import com.squaregps.interview.model.AVL;
import com.squaregps.interview.model.AVLData;
import com.squaregps.interview.model.GPSElement;
import com.squaregps.interview.model.IOElement;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.squaregps.interview.tools.HexUtils.hexToByteBuffer;

/**
 * Message decoder for <a href="https://wiki.teltonika-networks.com/view/RUT955_GPS_Protocols">*
 * Teltonika RUT955 protocol (codec 8).</a>
 */
public class TeltonikaRut955Decoder implements MessageDecoder {

  public static final String ZULU_TIME = "Z";
  public static final double PRECISION = 1e7;

  @Override
  public List<LocationMessage> decode(@Nonnull ByteBuffer buf) {

    byte[] byteArray = buf.array();

    int[] offset = new int[1]; /* pass int as reference */

    int codecId =
        byteArray[offset[0]++] & 0XFF; /* & 0xFF -> ensures 1 byte is converted to unsigned int */
    int numberOfData = byteArray[offset[0]++] & 0XFF;

    AVL avl = new AVL(codecId, numberOfData);

    List<LocationMessage> locationMessages = new ArrayList<>();

    for (int i = 0; i < numberOfData && offset[0] < byteArray.length; i++) {
      AVLData avlData = createAVLData(byteArray, offset);
      avl.getAvlData().add(avlData);
      locationMessages.add(createLocationMessage(avlData));
    }
    System.out.println(avl);
    return locationMessages;
  }

  private LocationMessage createLocationMessage(AVLData avlData) {
    return new LocationMessage(
        avlData.getZonedDateTime(),
        avlData.getGpsElement().getLongitude(),
        avlData.getGpsElement().getLatitude(),
        avlData.getGpsElement().getAltitude(),
        avlData.getGpsElement().getAngle(),
        avlData.getGpsElement().getSatellites(),
        avlData.getGpsElement().getSpeed(),
        avlData.getIoElement().getDigitalInputStatus1(),
        avlData.getIoElement().getDigitalInputStatus2(),
        avlData.getIoElement().getAnalogInput1(),
        avlData.getIoElement().getGsmLevel());
  }

  private static AVLData createAVLData(byte[] byteArray, int[] offset) {
    /* zonedDateTime */
    long timestamp = ByteBuffer.wrap(byteArray, offset[0], 8).getLong();
    offset[0] += 8;
    ZonedDateTime zonedDateTime =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of(ZULU_TIME));

    /* priority */
    int priority =
        byteArray[offset[0]++]
            & 0xFF; /* ensures 1 byte is converted to unsigned int; & 0xFF ensures we get the value as an unsigned int (range: 0 to 255) */

    /* GPS Element */
    /* longitude */
    int longitude = ByteBuffer.wrap(byteArray, offset[0], 4).getInt();
    offset[0] += 4;

    /* latitude */
    int latitude = ByteBuffer.wrap(byteArray, offset[0], 4).getInt();
    offset[0] += 4;

    /* altitude */
    int altitude =
        ByteBuffer.wrap(byteArray, offset[0], 2).getShort()
            & 0xFFFF; /* & 0xFFFF -> ensure 2 bytes are converted to unsigned int; & 0xFFFF promotes the signed short to an unsigned int (range: 0 to 65535) */
    offset[0] += 2;

    /* angle */
    int angle = ByteBuffer.wrap(byteArray, offset[0], 2).getShort() & 0xFFFF;
    offset[0] += 2;

    /* satellites */
    int satellites = byteArray[offset[0]++] & 0xFF;

    /* speed */
    int speed = ByteBuffer.wrap(byteArray, offset[0], 2).getShort() & 0xFFFF;
    offset[0] += 2;

    /* I/O Element */
    boolean digitalInputStatus1 = byteArray[offset[0]++] != 0;
    boolean digitalInputStatus2 = byteArray[offset[0]++] != 0;
    int analogInput1 = ByteBuffer.wrap(byteArray, offset[0], 2).getShort() & 0xFFFF;
    offset[0] += 2;
    int gsmLevel = byteArray[offset[0]++] & 0xFF;

    IOElement ioElement =
        new IOElement(digitalInputStatus1, digitalInputStatus2, (double) analogInput1, gsmLevel);

    GPSElement gpsElement =
        new GPSElement(
            longitude / PRECISION, latitude / PRECISION, altitude, angle, satellites, speed);
    return new AVLData(zonedDateTime, priority, gpsElement, ioElement);
  }

  public static void main(String[] args) {
    ByteBuffer data =
        hexToByteBuffer(
            ""
                + "08040000015C1A473FC0000E3BD4A520B53DC300570167070000000403020101001504010"
                + "9158500000000015C1A475348000E3BD4AE20B53DC0005701670800000004030201010015"
                + "040109158500000000015C1A4766D0000E3BD4AE20B53DBF0057016708000000040302010"
                + "10015040109158500000000015C1A477A58000E3BD4B120B53DBD00570167080000000403"
                + "02010100150401091585000004");
    TeltonikaRut955Decoder t = new TeltonikaRut955Decoder();
    t.decode(data);
  }
}
