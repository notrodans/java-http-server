package com.github.notrodans.http.server;

import java.util.List;
import com.github.notrodans.http.server.bind.HandlerHolder;
import com.github.notrodans.http.server.common.ApplicationParameters;
import com.github.notrodans.http.server.controller.ApplicationController;
import com.github.notrodans.http.server.intercept.InterceptorHolder;
import com.github.notrodans.http.server.service.HandlerMethodResolver;

final public class Main {
	public static void main(final String[] args) {
		final String directory = args.length > 1 && "--directory".equals(args[0]) ? args[1]
				: "/home/notrodans/java-http-server/tmp";

		new Server(
			8080,
			new HandlerMethodResolver(
				new HandlerHolder(
					List.of(
						new ApplicationController(
							new ApplicationParameters(
								directory
							)
						)
					)
				)
			),
			new InterceptorHolder()
		).start();
	}
}
