package org.jboss.resteasy.reactive.server.handlers;

import org.jboss.resteasy.reactive.server.core.ResteasyReactiveRequestContext;
import org.jboss.resteasy.reactive.spi.EndpointInvoker;

public class InvocationHandler implements ServerRestHandler {
    private final EndpointInvoker invoker;
    private final boolean requireCDIRequestScope;

    public InvocationHandler(EndpointInvoker invoker, boolean requireCDIRequestScope) {
        this.invoker = invoker;
        this.requireCDIRequestScope = requireCDIRequestScope;
    }

    @Override
    public void handle(ResteasyReactiveRequestContext requestContext) throws Exception {
        if (requestContext.getResult() != null) {
            //processing was aborted
            //but we still follow through with the handler chain
            return;
        }
        //suspend processing
        //need to do it here to avoid a race
        boolean async = requestContext.getAsyncResponse() != null;
        if (async) {
            requestContext.suspend();
        }
        if (requireCDIRequestScope) {
            requestContext.requireCDIRequestScope();
        }
        try {
            Object result = invoker.invoke(requestContext.getEndpointInstance(), requestContext.getParameters());
            if (!async) {
                requestContext.setResult(result);
            }
        } catch (Throwable t) {
            // passing true since the target doesn't change and we want response filters to be able to know what the resource method was
            requestContext.handleException(t, true);
            if (async) {
                requestContext.resume();
            }
        }
    }
}
