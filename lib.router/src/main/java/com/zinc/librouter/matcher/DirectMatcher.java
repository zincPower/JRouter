package com.zinc.librouter.matcher;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zinc.librouter.impl.RouteRequest;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/10
 * @description
 */

public class DirectMatcher extends AbsExplicitMatcher {

    public DirectMatcher(int priority) {
        super(priority);
    }

    @Override
    public boolean match(Context context, Uri uri, @Nullable String route, RouteRequest routeRequest) {
        return !TextUtils.isEmpty(route) && uri.toString().equals(route);
    }

}
