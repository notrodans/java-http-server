package com.github.notrodans.http.server.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class FromHeaderList implements Supplier<HttpHeaders> {
	private final Iterable<String> headerLines;

	public FromHeaderList(final Iterable<String> headerLines) {
		this.headerLines = headerLines;
	}

	@Override
	public HttpHeaders get() {
		final Map<String, List<String>> headers = new HashMap<>();
		for (final String line : headerLines) {
			if (line == null || line.isBlank()) {
				continue;
			}
			final int colonIndex = line.indexOf(":");
			if (colonIndex == -1) {
				continue;
			}
			final String name = line.substring(0, colonIndex).trim();
			final String value = line.substring(colonIndex + 1).trim();
			headers.computeIfAbsent(name.toLowerCase(), key -> new ArrayList<>()).add(value);
		}
		return new HttpHeaders(headers);
	}
}

