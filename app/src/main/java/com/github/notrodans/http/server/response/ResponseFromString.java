package com.github.notrodans.http.server.response;

import java.util.Objects;
import java.util.function.Supplier;
import com.github.notrodans.http.server.common.HttpHeaders;
import com.github.notrodans.http.server.common.HttpStatus;

public final class ResponseFromString implements Supplier<ResponseContext> {
	private final HttpStatus status;
	private final HttpHeaders headers;
	private final String body;

	public ResponseFromString(final HttpStatus status, final HttpHeaders headers,
			final String body) {
		this.status = Objects.requireNonNull(status);
		this.headers = headers;
		this.body = body;
	}

	@Override
	public ResponseContext get() {
		final byte[] bytes = body == null ? null : body.getBytes();
		return new ResponseFromBytes(status, headers, bytes).get();
	}
}
