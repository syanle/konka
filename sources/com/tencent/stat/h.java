package com.tencent.stat;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

class h extends DefaultConnectionKeepAliveStrategy {
    final /* synthetic */ g a;

    h(g gVar) {
        this.a = gVar;
    }

    public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
        long keepAliveDuration = h.super.getKeepAliveDuration(httpResponse, httpContext);
        if (keepAliveDuration == -1) {
            return 30000;
        }
        return keepAliveDuration;
    }
}
