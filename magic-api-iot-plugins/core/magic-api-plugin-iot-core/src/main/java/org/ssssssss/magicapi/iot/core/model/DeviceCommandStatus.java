package org.ssssssss.magicapi.iot.core.model;

public enum DeviceCommandStatus {
    CREATED,
    ROUTING,
    SENT,
    ACCEPTED,
    EXECUTING,
    SUCCEEDED,
    REJECTED,
    FAILED,
    TIMED_OUT,
    CANCELED;

    public boolean terminal() {
        return this == SUCCEEDED || this == REJECTED || this == FAILED || this == TIMED_OUT || this == CANCELED;
    }
}

