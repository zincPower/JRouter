package com.zinc.librouter.matcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zinc.librouter.impl.RouteRequest;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/11
 * @description
 */

public class ImplicitMatcher extends AbsImplicitMatcher {

    public ImplicitMatcher(int priority) {
        super(priority);
    }

    @Override
    public boolean match(Context context, Uri uri, @Nullable String route, RouteRequest routeRequest) {
        if (uri.toString().startsWith("http://") || uri.toString().startsWith("https://")) {
            return false;
        }

        //查看是否有uri的隐式intent
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(
                new Intent(Intent.ACTION_VIEW, uri), PackageManager.MATCH_DEFAULT_ONLY);

        if(resolveInfo != null){

            if(uri.getQuery() != null){
                parseParams(uri, routeRequest);
            }

            return true;

        }

        return false;
    }
}
