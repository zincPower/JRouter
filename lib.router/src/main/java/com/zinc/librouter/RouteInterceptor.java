package com.zinc.librouter;

import android.content.Context;

import com.zinc.librouter.impl.RouteRequest;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/14
 * @description
 */

public interface RouteInterceptor {

    /**
     *
     * @param context
     * @param routeRequest
     * @return 为true时，表示拦截；为false时，表示不拦截
     */
    boolean intercept(Context context, RouteRequest routeRequest);

}
