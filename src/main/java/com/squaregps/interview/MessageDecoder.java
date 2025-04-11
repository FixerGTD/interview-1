package com.squaregps.interview;

import javax.annotation.Nonnull;
import java.nio.ByteBuffer;
import java.util.List;

/** Декодер геолокационного сообщения */
public interface MessageDecoder {

  /**
   * Парсит полученный с устройства пакет данных Parses the data packet received from the device
   *
   * @param buf входящий пакет сырых данных
   * @return список полученных сообщений (как правило, одно)
   */
  List<LocationMessage> decode(@Nonnull ByteBuffer buf);
}
