package org.base.exception;

public class LogicException extends AppException {

    public LogicException(int code, String message) {
        super(code, message);
    }

    public LogicException(String message) {
        super(message);
    }
}
