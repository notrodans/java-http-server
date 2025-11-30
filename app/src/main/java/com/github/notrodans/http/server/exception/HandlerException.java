package com.github.notrodans.http.server.exception;

final public class HandlerException extends RuntimeException {
	public HandlerException() {}

	public HandlerException(final String message) {
		super(message);
	}

	public HandlerException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public HandlerException(final Throwable cause) {
		super(cause);
	}
}
