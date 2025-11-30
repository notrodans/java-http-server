package com.github.notrodans.http.server.intercept;

import java.util.ArrayList;
import java.util.List;
import com.github.notrodans.http.server.request.RequestContext;
import com.github.notrodans.http.server.response.ResponseContext;

final public class InterceptorHolder {
	private static volatile InterceptorHolder INSTANCE;
	private final List<Interceptor> interceptors;

	private InterceptorHolder() {
		this.interceptors = new ArrayList<>();
		interceptors.add(new EncodeInterceptor());
	}

	public static InterceptorHolder getInstance() {
		if (INSTANCE == null) {
			synchronized (InterceptorHolder.class) {
				if (INSTANCE == null) {
					INSTANCE = new InterceptorHolder();
					return INSTANCE;
				}
			}
		}

		return INSTANCE;
	}

	public void beforeSendResponse(final RequestContext requestContext,
		final ResponseContext responseContext) {
		interceptors.forEach(it -> it.beforeSendResponse(requestContext, responseContext));
	}
}
