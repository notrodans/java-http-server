package com.github.notrodans.http.server.response;

import java.util.Objects;
import java.util.function.Supplier;
import com.github.notrodans.http.server.common.HttpHeaders;
import com.github.notrodans.http.server.common.HttpStatus;

public final class ResponseFromBytes implements Supplier<ResponseContext> {
	private final HttpStatus status;
	private final HttpHeaders headers;
	private final byte[] body;

	public ResponseFromBytes(
			final HttpStatus status,
			final HttpHeaders headers,
			final byte[] body) {
		this.status = Objects.requireNonNull(status);
		this.headers = headers;
		this.body = body;
	}

	@Override
	public ResponseContext get() {
		final var context = new ResponseContext();
		context.setStatus(status);
		context.setHeaders(headers);
		context.setResponseBody(body);
		return context;
	}
}
