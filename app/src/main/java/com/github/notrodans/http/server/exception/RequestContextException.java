package com.github.notrodans.http.server.exception;

final public class RequestContextException extends RuntimeException {
	public RequestContextException() {}

	public RequestContextException(final String message) {
		super(message);
	}

	public RequestContextException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public RequestContextException(final Throwable cause) {
		super(cause);
	}
}
