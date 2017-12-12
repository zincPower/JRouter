package com.zinc.librouter.impl;

import android.net.Uri;
import android.os.Bundle;

import com.zinc.librouter.IRouter;

import static com.zinc.librouter.impl.Router.RAW_URI;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

abstract class AbsRouter implements IRouter {
    protected RouteRequest mRouteRequest;

    @Override
    public IRouter build(Uri uri) {
        mRouteRequest = new RouteRequest(uri);
        Bundle bundle = new Bundle();
        bundle.putString(RAW_URI, uri.toString());
        mRouteRequest.setBundle(bundle);
        return this;
    }

}