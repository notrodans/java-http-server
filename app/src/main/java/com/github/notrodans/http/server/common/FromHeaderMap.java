package com.github.notrodans.http.server.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class FromHeaderMap implements Supplier<HttpHeaders> {
	private final Map<String, String> headerMap;

	public FromHeaderMap(final Map<String, String> headerMap) {
		this.headerMap = new HashMap<>(headerMap);
	}

	@Override
	public HttpHeaders get() {
		final var httpHeader = new HttpHeaders(new HashMap<>());
		httpHeader.setAll(headerMap);
		return httpHeader;
	}
}

