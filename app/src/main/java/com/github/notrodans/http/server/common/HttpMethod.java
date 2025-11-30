package com.github.notrodans.http.server.common;

import java.util.Arrays;

public enum HttpMethod {
	GET("get"), POST("post");

	private final String type;

	private HttpMethod(final String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static HttpMethod fromType(final String type) {
		return Arrays
			.stream(values())
			.filter(it -> it.type.equalsIgnoreCase(type))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Exception on get http method"));
	}
}
