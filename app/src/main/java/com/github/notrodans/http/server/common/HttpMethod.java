package com.github.notrodans.http.server.common;

public enum HttpMethod {
	GET("get"), POST("post");

	private final String type;

	private HttpMethod(final String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
