package com.github.notrodans.http.server.exception;

final public class ResponseContextException extends RuntimeException {
	public ResponseContextException() {}

	public ResponseContextException(final String message) {
		super(message);
	}

	public ResponseContextException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ResponseContextException(final Throwable cause) {
		super(cause);
	}
}
