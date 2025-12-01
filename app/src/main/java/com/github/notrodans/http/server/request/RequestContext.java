package com.github.notrodans.http.server.request;

import java.util.Arrays;
import java.util.List;
import com.github.notrodans.http.server.common.HttpHeaders;
import com.github.notrodans.http.server.common.HttpMethod;

final public class RequestContext {
	private final HttpMethod method;
	private final String path;
	private final HttpHeaders headers;
	private final List<String> pathParts;
	private String body;

	public RequestContext(final HttpMethod method, final String path, final HttpHeaders headers) {
		this.path = path;
		this.method = method;
		this.headers = headers;
		this.pathParts = Arrays.stream(path.split("/")).toList();
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public String getPath() {
		return path;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public boolean hasPath() {
		return path != null && !path.isBlank();
	}

	public String getPart(final int index) {
		if (pathParts.size() < index) {
			return null;
		}

		return pathParts.get(index);
	}

	public String getLastPart() {
		return getPart(pathParts.size() - 1);
	}

	@Override
	public String toString() {
		return "RequestContext{method=" + method + ", path=" + path + "}";
	}

	public boolean pathsIsEqualsTo(final String actualPath) {
		return hasPath() && path.equals(actualPath);
	}
}
