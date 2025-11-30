package com.github.notrodans.http.server.service;

final public class PathPattern {
	private static class Segment {
		private final boolean isParam;
		private final String param;
		private final String constant;

		public Segment(final boolean isParam, final String value) {
			this.isParam = isParam;
			if (isParam) {
				param = value;
				constant = null;
			} else {
				constant = value;
				param = null;
			}
		}

	}

	public static PathPattern path(final String path) {
		return new PathPattern(path);
	}

	final Segment[] segments;
	private final String originalPath;

	private PathPattern(final String originalPath) {
		this.originalPath = originalPath;
		final String[] inSegments = originalPath.split("/");
		segments = new Segment[inSegments.length];
		for (int i = 0; i < segments.length; i++) {
			if (inSegments[i].startsWith("{") && inSegments[i].endsWith("}")) {
				segments[i] =
					new Segment(true, inSegments[i].substring(1, inSegments[i].length() - 1));
			} else {
				segments[i] = new Segment(false, inSegments[i]);
			}
		}
	}

	public boolean match(final String input) {
		var modifiedInput = input;
		if (modifiedInput.equals("/") && !originalPath.equals("/")) {
			return false;
		}
		if (originalPath.equals("/") && !modifiedInput.equals("/")) {
			return false;
		}
		for (int segmentIndex = 0; segmentIndex < segments.length; segmentIndex++) {
			int i = modifiedInput.indexOf("/");
			int j = i + 1;
			if (i == -1) {
				i = modifiedInput.length();
				j = modifiedInput.length();
				if (segmentIndex != segments.length - 1) {
					return false;
				}
			} else {
				if (segmentIndex == segments.length - 1) {
					return false;
				}
			}

			final Segment segment = segments[segmentIndex];
			if (!segment.isParam && !modifiedInput.substring(0, i).equals(segment.constant)) {
				return false;
			}

			modifiedInput = modifiedInput.substring(j);
		}
		return true;
	}
}
