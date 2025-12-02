package com.github.notrodans.http.server.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final public class HttpHeaders {
	private final Map<String, List<String>> headers;

	public HttpHeaders(final Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public String getFirst(final String key) {
		final List<String> values = headers.get(key.toLowerCase());
		return values != null && !values.isEmpty() ? values.get(0) : null;
	}

	public List<String> get(final String key) {
		return headers.get(key.toLowerCase());
	}

	public void add(final String key, final String value) {
		final List<String> currentValue = headers.computeIfAbsent(key.toLowerCase(), k -> new ArrayList<>());
		currentValue.add(value);
	}

	public void addAll(final String key, final List<String> values) {
		final List<String> currentValues = headers.computeIfAbsent(key.toLowerCase(), k -> new ArrayList<>());
		currentValues.addAll(values);
	}

	public void addAll(final Map<String, List<String>> newValues) {
		newValues.forEach(this::addAll);
	}

	public void set(final String key, final String value) {
		final List<String> newValueList = new ArrayList<>();
		newValueList.add(value);
		headers.put(key.toLowerCase(), newValueList);
	}

	public void setAll(final Map<String, String> values) {
		values.forEach(this::set);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final Map.Entry<String, List<String>> entry : headers.entrySet()) {
			final String key = entry.getKey();
			final List<String> values = entry.getValue();
			sb.append(key).append(": ");
			for (int i = 0; i < values.size(); i++) {
				sb.append(values.get(i));
				if (i < values.size() - 1) {
					sb.append(", ");
				}
			}
			sb.append("\r\n");
		}
		return sb.toString();
	}
}
