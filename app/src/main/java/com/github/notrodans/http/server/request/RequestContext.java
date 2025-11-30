package com.github.notrodans.http.server.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.github.notrodans.http.server.common.HttpHeaders;
import com.github.notrodans.http.server.common.HttpMethod;

final public class RequestContext {
	public static RequestContext buildContext(final BufferedReader reader) {
		try {
			final var requestLine = reader.readLine();
			if (requestLine == null || requestLine.isBlank()) {
				return null;
			}
			final var methodWithPath = extractMethodAndPath(requestLine);

			final List<String> headers = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null && !line.isEmpty()) {
				headers.add(line);
			}
			System.out.println("HTTP request headers: " + headers);
			final var httpHeaders = HttpHeaders.fromHeaderList(headers);
			final var requestContext =
				new RequestContext(methodWithPath.getKey(), methodWithPath.getValue(), httpHeaders);
			final var contentLength = httpHeaders.getFirst("Content-Length");
			if (contentLength != null && !contentLength.isEmpty()) {
				final int bodySize = Integer.parseInt(contentLength);
				final char[] bodyBuffer = new char[bodySize];
				reader.read(bodyBuffer);
				requestContext.setBody(new String(bodyBuffer));
			}

			return requestContext;
		} catch (final IOException e) {
			System.out.println("Exception trying to build request context");
			throw new RuntimeException(e);
		}
	}

	private static AbstractMap.SimpleEntry<HttpMethod, String> extractMethodAndPath(
		final String requestLine) {
		final var parts = requestLine.split(" ");
		return new AbstractMap.SimpleEntry<>(HttpMethod.fromType(parts[0]), parts[1]);
	}

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
