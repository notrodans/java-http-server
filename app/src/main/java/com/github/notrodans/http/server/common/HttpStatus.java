package com.github.notrodans.http.server.common;

public enum HttpStatus {
	OK(200, Series.SUCCESSFUL, "OK"), CREATED(201, Series.SUCCESSFUL, "CREATED"), NOT_FOUND(404,
			Series.CLIENT_ERROR, "Not Found");

	public enum Series {
		INFORMTIONAL(1), SUCCESSFUL(2), REDIRECTION(3), CLIENT_ERROR(4), SERVER_ERROR(5);

		private final int value;

		private Series(final int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private final int code;
	private final Series series;
	private final String reasonPhrase;

	private HttpStatus(final int code, final Series series, final String reasonPhrase) {
		this.code = code;
		this.series = series;
		this.reasonPhrase = reasonPhrase;
	}

	public int getCode() {
		return code;
	}

	public Series getSeries() {
		return series;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public boolean is1xxInformational() {
		return series == Series.INFORMTIONAL;
	}

	public boolean is2xxSuccessful() {
		return series == Series.SUCCESSFUL;
	}

	public boolean is3xxRedirection() {
		return series == Series.REDIRECTION;
	}

	public boolean is4xxClientError() {
		return series == Series.CLIENT_ERROR;
	}

	public boolean is5xxServerError() {
		return series == Series.SERVER_ERROR;
	}

	public boolean isError() {
		return is4xxClientError() || is5xxServerError();
	}
}
