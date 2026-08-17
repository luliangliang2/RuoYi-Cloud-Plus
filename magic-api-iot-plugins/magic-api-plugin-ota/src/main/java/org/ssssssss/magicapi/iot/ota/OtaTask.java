package org.ssssssss.magicapi.iot.ota;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import java.net.URI;
import java.time.Instant;

public record OtaTask(String taskId, DeviceIdentity device, String firmwareVersion, URI downloadUri,
                      String checksum, Status status, int progress, Instant updatedAt) {
    public enum Status { CREATED, SCHEDULED, DOWNLOADING, INSTALLING, SUCCEEDED, FAILED, ROLLED_BACK, CANCELED }
}

