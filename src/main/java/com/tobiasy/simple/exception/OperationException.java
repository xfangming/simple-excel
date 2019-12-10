package com.tobiasy.simple.exception;

/**
 * @author tobiasy
 * @date 2019/7/18
 */
public class OperationException extends RuntimeException {
    public OperationException() {
    }

    public OperationException(String msg) {
        super(msg);
    }

    public OperationException(String msg, Object... args) {
        super(String.format(msg, args));
    }

    public OperationException(Throwable throwable) {
        super(throwable);
    }
}
