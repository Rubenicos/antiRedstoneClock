package com.trafalcraft.anti_redstone_clock.exception;

public class DuplicateRedstoneClockObjectException extends Exception {

    private static final long serialVersionUID = 3251215030910170782L;

    public DuplicateRedstoneClockObjectException(String message) {
        super(message);
    }

    public DuplicateRedstoneClockObjectException(Throwable cause) {
        super(cause);
    }

    public DuplicateRedstoneClockObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
