/*
 * Copyright 2013-2022 Signal Messenger, LLC
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package org.whispersystems.textsecuregcm.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.whispersystems.textsecuregcm.entities.ECSignedPreKey;

class DeviceTest {

  @ParameterizedTest
  @MethodSource
  void testIsEnabled(final boolean primary, final boolean fetchesMessages, final String apnId, final String gcmId,
      final ECSignedPreKey signedPreKey, final Duration timeSinceLastSeen, final boolean expectEnabled) {

    final long lastSeen = System.currentTimeMillis() - timeSinceLastSeen.toMillis();

    final Device device = new Device();
    device.setId(primary ? Device.PRIMARY_ID : Device.PRIMARY_ID + 1);
    device.setFetchesMessages(fetchesMessages);
    device.setApnId(apnId);
    device.setGcmId(gcmId);
    device.setSignedPreKey(signedPreKey);
    device.setCreated(lastSeen);
    device.setLastSeen(lastSeen);

    assertEquals(expectEnabled, device.isEnabled());
  }

  private static Stream<Arguments> testIsEnabled() {
    return Stream.of(
        //             primary fetchesMessages apnId     gcmId     signedPreKey              lastSeen             expectEnabled
        Arguments.of(true, false, null, null, null, Duration.ofDays(60), false),
        Arguments.of(true, false, null, null, null, Duration.ofDays(1), false),
        Arguments.of(true, false, null, null, mock(ECSignedPreKey.class), Duration.ofDays(60), false),
        Arguments.of(true, false, null, null, mock(ECSignedPreKey.class), Duration.ofDays(1), false),
        Arguments.of(true, false, null, "gcm-id", null, Duration.ofDays(60), false),
        Arguments.of(true, false, null, "gcm-id", null, Duration.ofDays(1), false),
        Arguments.of(true, false, null, "gcm-id", mock(ECSignedPreKey.class), Duration.ofDays(60), true),
        Arguments.of(true, false, null, "gcm-id", mock(ECSignedPreKey.class), Duration.ofDays(1), true),
        Arguments.of(true, false, "apn-id", null, null, Duration.ofDays(60), false),
        Arguments.of(true, false, "apn-id", null, null, Duration.ofDays(1), false),
        Arguments.of(true, false, "apn-id", null, mock(ECSignedPreKey.class), Duration.ofDays(60), true),
        Arguments.of(true, false, "apn-id", null, mock(ECSignedPreKey.class), Duration.ofDays(1), true),
        Arguments.of(true, true, null, null, null, Duration.ofDays(60), false),
        Arguments.of(true, true, null, null, null, Duration.ofDays(1), false),
        Arguments.of(true, true, null, null, mock(ECSignedPreKey.class), Duration.ofDays(60), true),
        Arguments.of(true, true, null, null, mock(ECSignedPreKey.class), Duration.ofDays(1), true),
        Arguments.of(false, false, null, null, null, Duration.ofDays(60), false),
        Arguments.of(false, false, null, null, null, Duration.ofDays(1), false),
        Arguments.of(false, false, null, null, mock(ECSignedPreKey.class), Duration.ofDays(60), false),
        Arguments.of(false, false, null, null, mock(ECSignedPreKey.class), Duration.ofDays(1), false),
        Arguments.of(false, false, null, "gcm-id", null, Duration.ofDays(60), false),
        Arguments.of(false, false, null, "gcm-id", null, Duration.ofDays(1), false),
        Arguments.of(false, false, null, "gcm-id", mock(ECSignedPreKey.class), Duration.ofDays(60), false),
        Arguments.of(false, false, null, "gcm-id", mock(ECSignedPreKey.class), Duration.ofDays(1), true),
        Arguments.of(false, false, "apn-id", null, null, Duration.ofDays(60), false),
        Arguments.of(false, false, "apn-id", null, null, Duration.ofDays(1), false),
        Arguments.of(false, false, "apn-id", null, mock(ECSignedPreKey.class), Duration.ofDays(60), false),
        Arguments.of(false, false, "apn-id", null, mock(ECSignedPreKey.class), Duration.ofDays(1), true),
        Arguments.of(false, true, null, null, null, Duration.ofDays(60), false),
        Arguments.of(false, true, null, null, null, Duration.ofDays(1), false),
        Arguments.of(false, true, null, null, mock(ECSignedPreKey.class), Duration.ofDays(60), false),
        Arguments.of(false, true, null, null, mock(ECSignedPreKey.class), Duration.ofDays(1), true)
    );
  }
}
