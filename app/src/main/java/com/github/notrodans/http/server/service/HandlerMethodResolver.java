package com.github.notrodans.http.server.service;

import com.github.notrodans.http.server.bind.HandlerHolder;
import com.github.notrodans.http.server.bind.HandlerMethod;
import com.github.notrodans.http.server.request.RequestContext;

final public class HandlerMethodResolver {
	public HandlerMethod resolve(final RequestContext context) {
		return HandlerHolder
			.getInstance()
			.getHandlerMethods()
			.stream()
			.filter(
				it -> context.getMethod() == it.getMethod()
					&& PathPattern.path(it.getPath()).match(context.getPath()))
			.findFirst()
			.orElseGet(() -> {
				System.out.println("Handler by context not found: " + context);
				return null;
			});
	}
}
