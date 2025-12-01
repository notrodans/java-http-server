package com.github.notrodans.http.server.common;

import java.util.Arrays;
import java.util.function.Supplier;

public final class HttpMethodFromType implements Supplier<HttpMethod> {
	private final String type;

	public HttpMethodFromType(final String type) {
		this.type = type;
	}

	@Override
	public HttpMethod get() {
		return Arrays
			.stream(HttpMethod.values())
			.filter(it -> it.getType().equalsIgnoreCase(type))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Exception on get http method"));
	}
}
