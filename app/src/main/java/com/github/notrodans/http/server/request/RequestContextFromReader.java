package com.github.notrodans.http.server.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import com.github.notrodans.http.server.common.FromHeaderList;
import com.github.notrodans.http.server.common.HttpHeaders;
import com.github.notrodans.http.server.common.HttpMethod;
import com.github.notrodans.http.server.common.HttpMethodFromType;

public final class RequestContextFromReader implements Supplier<RequestContext> {
	private final BufferedReader reader;

	public RequestContextFromReader(final BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public RequestContext get() {
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
			final HttpHeaders httpHeaders = new FromHeaderList(headers).get();
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

	private AbstractMap.SimpleEntry<HttpMethod, String> extractMethodAndPath(
		final String requestLine) {
		final var parts = requestLine.split(" ");
		return new AbstractMap.SimpleEntry<>(new HttpMethodFromType(parts[0]).get(), parts[1]);
	}
}
