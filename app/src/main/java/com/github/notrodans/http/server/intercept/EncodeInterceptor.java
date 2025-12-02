package com.github.notrodans.http.server.intercept;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;
import com.github.notrodans.http.server.exception.InterceptorException;
import com.github.notrodans.http.server.request.RequestContext;
import com.github.notrodans.http.server.response.ResponseContext;

public class EncodeInterceptor implements Interceptor {
	@Override
	public void beforeSendResponse(final RequestContext requestContext,
			final ResponseContext responseContext) {
		final var acceptEncoding = requestContext.getHeaders().getFirst("Accept-Encoding");
		if (acceptEncoding != null && !acceptEncoding.isBlank()
				&& responseContext.getResponseBody() != null) {
			final var parts = acceptEncoding.split(",");
			Arrays
					.stream(parts)
					.filter(it -> it.trim().equalsIgnoreCase("gzip"))
					.findFirst()
					.ifPresent(gzipString -> {
						final byte[] responseBody = compressResponseBody(responseContext.getResponseBody());
						responseContext
								.getHeaders()
								.set("Content-Length", String.valueOf(responseBody.length));
						responseContext.getHeaders().set("Content-Encoding", "gzip");
						responseContext.setResponseBody(responseBody);
					});
		}
	}

	private byte[] compressResponseBody(final byte[] responseBody) {
		try {
			final var outputStream = new ByteArrayOutputStream();
			try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
				gzipOutputStream.write(responseBody);
			}
			return outputStream.toByteArray();
		} catch (final IOException e) {
			throw new InterceptorException(
				"Exception trying to gzip response body!" + new String(responseBody)
			);
		}
	}
}
