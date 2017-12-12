package com.zinc.librouter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.zinc.librouter.matcher.AbsMatcher;
import com.zinc.librouter.matcher.MatcherRegister;
import com.zinc.librouter.utils.RLog;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

final class RealRouter extends AbsRouter {

    private static RealRouter mInstance;

    private RealRouter() {

    }

    public static RealRouter getInstance() {
        if (mInstance == null) {
            mInstance = new RealRouter();
        }
        return mInstance;
    }


    @Override
    public void go(Context context) {
        Intent intent = getIntent(context);

        if (intent == null) {
            return;
        }

        Bundle options = null;

        if (context instanceof Activity) {

            ActivityCompat.startActivityForResult((Activity) context, intent, -1, options);

        } else {

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ContextCompat.startActivity(context, intent, options);

        }

    }

    private Intent getIntent(Context context) {

        if (mRouteRequest.getUri() == null) {
            // TODO: 2017/12/2 有回调时需要改
            Log.e(TAG, "uri == null");
            return null;
        }

        List<AbsMatcher> matcherList = MatcherRegister.getMatcher();
        if (matcherList.isEmpty()) {
            return null;
        }

        Set<Map.Entry<String, Class<?>>> entries = AptHub.routeTable.entrySet();

        for (AbsMatcher matcher : matcherList) {
            if (AptHub.routeTable.isEmpty()) {
                if (matcher.match(context, mRouteRequest.getUri(), null, mRouteRequest)) {
                    RLog.i("Caught by" + matcher.getClass().getCanonicalName());
                    return finalizeIntent(context, matcher, null);
                }
            } else {
                for (Map.Entry<String, Class<?>> entry : entries) {
                    if (matcher.match(context, mRouteRequest.getUri(), entry.getKey(), mRouteRequest)) {
                        RLog.i("Caught by" + matcher.getClass().getCanonicalName());
                        return finalizeIntent(context, matcher, entry.getValue());
                    }
                }
            }
        }

        return null;

    }

    private Intent finalizeIntent(Context context, AbsMatcher matcher, @Nullable Class<?> target) {
//        if(intercept()){
//
//        }

        Object intent = matcher.generate(context, mRouteRequest.getUri(), target);
        if (intent instanceof Intent) {
            assembleIntent((Intent) intent);
            return (Intent) intent;
        } else { //没有匹配到
            return null;
        }

    }

    /**
     * @date 创建时间 2017/12/11
     * @author Jiang zinc
     * @Description 添加extras, flags, data, type, action
     * @version
     */
    private void assembleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

//        if(mRouteRequest){
//
//        }
    }

}
