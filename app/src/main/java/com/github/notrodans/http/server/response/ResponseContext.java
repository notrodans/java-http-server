package com.github.notrodans.http.server.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import com.github.notrodans.http.server.common.HttpHeaders;
import com.github.notrodans.http.server.common.HttpStatus;
import com.github.notrodans.http.server.exception.ResponseContextException;

public class ResponseContext {
	public static ResponseContext build(final HttpStatus status) {
		return build(status, null, null);
	}

	public static ResponseContext build(final HttpStatus status, final HttpHeaders headers) {
		return build(status, headers, null);
	}

	public static ResponseContext build(final HttpStatus status, final HttpHeaders headers,
		final String body) {
		return buildWithBytes(status, headers, body == null ? null : body.getBytes());
	}

	public static ResponseContext buildWithBytes(final HttpStatus status, final HttpHeaders headers,
		final byte[] responseBody) {
		final var context = new ResponseContext();
		context.setStatus(status);
		context.setHeaders(headers);
		context.setResponseBody(responseBody);
		return context;
	}

	private HttpStatus status;

	private HttpHeaders headers;

	private byte[] responseBody;

	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(final HttpHeaders headers) {
		this.headers = headers;
	}

	public byte[] getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(final byte[] responseBody) {
		this.responseBody = responseBody;
	}

	public byte[] getResponseAsBytes() {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			outputStream
				.write(
					("HTTP/1.1 " + status.getCode() + " " + status.getReasonPhrase() + "\r\n")
						.getBytes());

			if (headers != null) {
				outputStream.write(headers.toString().getBytes());
			}

			outputStream.write("\r\n".getBytes());

			if (responseBody != null) {
				outputStream.write(responseBody);
			}

			return outputStream.toByteArray();
		} catch (final IOException e) {
			throw new ResponseContextException(
				"ResponseContextException trying to get bytes for response", e);
		}
	}

	public void setStatus(final HttpStatus status) {
		this.status = Objects.requireNonNull(status);
	}

	public HttpStatus getStatus() {
		return status;
	}
}
