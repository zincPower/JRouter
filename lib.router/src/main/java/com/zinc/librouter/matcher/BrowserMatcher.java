package com.zinc.librouter.matcher;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zinc.librouter.impl.RouteRequest;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/11
 * @description
 */

public class BrowserMatcher extends AbsImplicitMatcher {
    public BrowserMatcher(int priority) {
        super(priority);
    }

    @Override
    public boolean match(Context context, Uri uri, @Nullable String route, RouteRequest routeRequest) {
        return (uri.toString().toLowerCase().startsWith("http://")
                || uri.toString().toLowerCase().startsWith("https://"));
    }
}
