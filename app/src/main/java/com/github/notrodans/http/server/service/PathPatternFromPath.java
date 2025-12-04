package com.github.notrodans.http.server.service;

import java.util.function.Supplier;

public final class PathPatternFromPath implements Supplier<PathPattern> {
	private final String path;

	public PathPatternFromPath(final String path) {
		this.path = path;
	}

	@Override
	public PathPattern get() {
		return new PathPattern(path);
	}
}
