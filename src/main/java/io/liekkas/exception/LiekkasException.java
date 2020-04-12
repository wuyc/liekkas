package io.liekkas.exception;

public class LiekkasException extends RuntimeException {

    public LiekkasException(String message) {
        super(message);
    }

    public LiekkasException(String message, Throwable cause) {
        super(message, cause);
    }

}
