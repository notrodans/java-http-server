package com.github.notrodans.http.server.intercept;

import java.util.ArrayList;
import java.util.List;
import com.github.notrodans.http.server.request.RequestContext;
import com.github.notrodans.http.server.response.ResponseContext;

final public class InterceptorHolder {
	private final List<Interceptor> interceptors;

	public InterceptorHolder() {
		this.interceptors = new ArrayList<>();
		interceptors.add(new EncodeInterceptor());
	}

	public void beforeSendResponse(final RequestContext requestContext,
		final ResponseContext responseContext) {
		interceptors.forEach(it -> it.beforeSendResponse(requestContext, responseContext));
	}
}
