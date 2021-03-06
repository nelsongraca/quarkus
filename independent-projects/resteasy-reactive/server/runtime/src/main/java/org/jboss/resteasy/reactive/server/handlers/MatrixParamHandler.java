package org.jboss.resteasy.reactive.server.handlers;

import org.jboss.resteasy.reactive.server.core.ResteasyReactiveRequestContext;

public class MatrixParamHandler implements ServerRestHandler {
    @Override
    public void handle(ResteasyReactiveRequestContext requestContext) throws Exception {
        if (requestContext.getPath().contains(";")) {
            requestContext.initPathSegments();
        }
    }
}
