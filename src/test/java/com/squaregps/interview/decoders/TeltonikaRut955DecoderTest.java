package com.squaregps.interview.decoders;

import com.squaregps.interview.LocationMessage;
import com.squaregps.interview.MessageDecoder;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.List;

import static com.squaregps.interview.tools.HexUtils.hexToByteBuffer;
import static org.junit.jupiter.api.Assertions.assertEquals;

/** Test for {@link TeltonikaRut955Decoder} */
class TeltonikaRut955DecoderTest {

  private final MessageDecoder decoder = new TeltonikaRut955Decoder();

  @Test
  void decodeSimpleMessagePack() {
    ByteBuffer data =
        hexToByteBuffer(
            ""
                + "08040000015C1A473FC0000E3BD4A520B53DC300570167070000000403020101001504010"
                + "9158500000000015C1A475348000E3BD4AE20B53DC0005701670800000004030201010015"
                + "040109158500000000015C1A4766D0000E3BD4AE20B53DBF0057016708000000040302010"
                + "10015040109158500000000015C1A477A58000E3BD4B120B53DBD00570167080000000403"
                + "02010100150401091585000004");

    List<LocationMessage> messages = decoder.decode(data);
    assertEquals(4, messages.size());

    LocationMessage m = messages.get(0);
    assertEquals(ZonedDateTime.parse("2017-05-18T06:38:16Z"), m.getDateTime());
    assertEquals(23.8802085, m.getLongitude(), 1.0e-7);
    assertEquals(54.8748739, m.getLatitude(), 1.0e-7);
    assertEquals(87, m.getAltitude());
    assertEquals(359, m.getAngle());
    assertEquals(7, m.getSatellites());
    assertEquals(0, m.getSpeed());

    assertEquals(Boolean.FALSE, m.getDigitalInputStatus1());
    assertEquals(Boolean.TRUE, m.getDigitalInputStatus2());
    assertEquals(Double.valueOf(5.509), m.getAnalogInput1());
    assertEquals(4, m.getGsmLevel());
  }
}
