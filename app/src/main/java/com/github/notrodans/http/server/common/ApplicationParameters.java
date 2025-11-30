package com.github.notrodans.http.server.common;

final public class ApplicationParameters {
	private static volatile ApplicationParameters INSTANCE;

	public static ApplicationParameters getInstance() {
		if (INSTANCE == null) {
			synchronized (ApplicationParameters.class) {
				if (INSTANCE == null) {
					INSTANCE = new ApplicationParameters();
					return INSTANCE;
				}
			}
		}

		return INSTANCE;
	}

	private String fileDirectory;

	private ApplicationParameters() {

	}

	public void setFileDirectory(final String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--directory")) {
				fileDirectory = args[i + 1];
			}
		}
	}

	public boolean isDirectoryExists() {
		return fileDirectory != null && !fileDirectory.isBlank();
	}

	public String getFileDirectory() {
		return fileDirectory;
	}
}
