package com.github.notrodans.http.server;

import com.github.notrodans.http.server.common.ApplicationParameters;

public class Main {
	public static void main(String[] args) {
		ApplicationParameters
			.getInstance()
			// .setFileDirectory(args);
			.setFileDirectory(new String[] {"--directory", "/home/notrodans/java-http-server/tmp"});
		System.out.println(ApplicationParameters.getInstance().getFileDirectory());
		var server = new Server(8080);
		server.start();
	}
}
