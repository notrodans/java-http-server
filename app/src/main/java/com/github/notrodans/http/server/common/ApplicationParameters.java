package com.github.notrodans.http.server.common;

final public class ApplicationParameters {
	private final String fileDirectory;

	public ApplicationParameters(final String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}

	public boolean isDirectoryExists() {
		return fileDirectory != null && !fileDirectory.isBlank();
	}

	public String getFileDirectory() {
		return fileDirectory;
	}
}
