package com.github.notrodans.http.server;

import com.github.notrodans.http.server.common.ApplicationParameters;

final public class Main {
	public static void main(final String[] args) {
		ApplicationParameters
			.getInstance()
			// .setFileDirectory(args);
			.setFileDirectory(new String[] {"--directory", "/home/notrodans/java-http-server/tmp"});
		System.out.println(ApplicationParameters.getInstance().getFileDirectory());
		final var server = new Server(8080);
		server.start();
	}
}
