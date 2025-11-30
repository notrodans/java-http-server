package com.github.notrodans.http.server.intercept;

import com.github.notrodans.http.server.request.RequestContext;
import com.github.notrodans.http.server.response.ResponseContext;

public interface Interceptor {
	void beforeSendResponse(final RequestContext requestContext,
		final ResponseContext responseContext);
}
