package com.github.notrodans.http.server.exception;

final public class InterceptorException extends RuntimeException {
	public InterceptorException() {
	}

	public InterceptorException(final String message) {
		super(message);
	}

	public InterceptorException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public InterceptorException(final Throwable cause) {
		super(cause);
	}
}
