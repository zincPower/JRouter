package com.zinc.librouter.matcher;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zinc.librouter.impl.RouteRequest;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/10
 * @description 用于匹配uri和解析带有参的uri
 * [scheme:][//authority][path][?query][#fragment]
 */

public class SchemeMatcher extends AbsExplicitMatcher {

    public SchemeMatcher(int priority) {
        super(priority);
    }

    @Override
    public boolean match(Context context, Uri uri, @Nullable String route, RouteRequest routeRequest) {

        if (isEmpty(route)) {
            return false;
        }

        Uri routeUri = Uri.parse(route);

        if (uri.isAbsolute() && routeUri.isAbsolute()) { // scheme != null
            if (!uri.getScheme().equals(routeUri.getScheme())) {
                //http != https
                return false;
            }

            if (isEmpty(uri.getAuthority()) && isEmpty(routeUri.getAuthority())) {
                //host1 = host2 = empty
                return true;
            }

            if (!isEmpty(uri.getAuthority()) && !isEmpty(routeUri.getAuthority())
                    && uri.getAuthority().equals(routeUri.getAuthority())) {

                if(!cutSlash(uri.getPath()).equals(cutSlash(routeUri.getPath()))){
                    return false;
                }

                if(uri.getQuery() != null){
                    parseParams(uri, routeRequest);
                }

                return true;

            }

        }

        return false;
    }

    /**
     * @date 创建时间 2017/12/11
     * @author Jiang zinc
     * @Description 剔除path的/分割
     * @version 1.0
     */
    private String cutSlash(String path) {

        if (path.startsWith("/")) {
            return cutSlash(path.substring(1));
        }

        if (path.endsWith("/")) {
            return cutSlash(path.substring(0, path.length() - 1));
        }

        return path;

    }

}
