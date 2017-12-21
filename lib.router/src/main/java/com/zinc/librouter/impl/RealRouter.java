package com.zinc.librouter.impl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.zinc.libannotation.Param;
import com.zinc.libannotation.Route;
import com.zinc.librouter.ParamInjector;
import com.zinc.librouter.RouteInterceptor;
import com.zinc.librouter.RouteResult;
import com.zinc.librouter.matcher.AbsImplicitMatcher;
import com.zinc.librouter.matcher.AbsMatcher;
import com.zinc.librouter.matcher.MatcherRegister;
import com.zinc.librouter.utils.RLog;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

final class RealRouter extends AbsRouter {

    public static final String PARAM_CLASS_SUFFIX = "$$JRouter$$ParamInjector";

    private static RealRouter mInstance;

    private Map<String, RouteInterceptor> mInterceptorInstance = new HashMap<>();

    private RealRouter() {

    }

    public static RealRouter getInstance() {
        if (mInstance == null) {
            mInstance = new RealRouter();
        }
        return mInstance;
    }

    /**
     * @date 创建时间 2017/12/14
     * @author Jiang zinc
     * @Description 页面跳转
     * @version 1.0
     */
    @Override
    public void go(Context context) {
        Intent intent = getIntent(context);

        if (intent == null) {
            return;
        }

        Bundle options = null;

        if (context instanceof Activity) {

            ActivityCompat.startActivityForResult((Activity) context, intent, mRouteRequest.getRequestCode(), options);

        } else {

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ContextCompat.startActivity(context, intent, options);

        }

        callback(RouteResult.SUCCEED, null);

    }

    @Override
    public Object getFragment(Context context) {
        if (mRouteRequest.getUri() == null) {
            callback(RouteResult.FAILED, "uri == null.");
            return null;
        }

        if (!mRouteRequest.isSkipInterceptors()) {
            for (RouteInterceptor interceptor : Router.getGlobalInterceptors()) {
                if (interceptor.intercept(context, mRouteRequest)) {
                    callback(RouteResult.INTERCEPTED, "Intercepted by global interceptor.");
                    return null;
                }
            }
        }

        List<AbsMatcher> matcherList = MatcherRegister.getMatcher();
        if (matcherList.isEmpty()) {
            callback(RouteResult.FAILED, "The MatcherRegistry contains no Matcher.");
            return null;
        }

        // fragment only matches explicit route
        if (AptHub.routeTable.isEmpty()) {
            callback(RouteResult.FAILED, "The route table contains no mapping.");
            return null;
        }

        Set<Map.Entry<String, Class<?>>> entries = AptHub.routeTable.entrySet();

        for (AbsMatcher matcher : matcherList) {
            if (matcher instanceof AbsImplicitMatcher) { // Ignore implicit matcher.
                continue;
            }
            for (Map.Entry<String, Class<?>> entry : entries) {
                if (matcher.match(context, mRouteRequest.getUri(), entry.getKey(), mRouteRequest)) {
                    RLog.i("Caught by " + matcher.getClass().getCanonicalName());
                    if (intercept(context, entry.getValue())) {
                        return null;
                    }
                    Object result = matcher.generate(context, mRouteRequest.getUri(), entry.getValue());
                    if (result instanceof android.support.v4.app.Fragment) {
                        android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) result;
                        Bundle bundle = mRouteRequest.getBundle();
                        if (bundle != null && !bundle.isEmpty()) {
                            fragment.setArguments(bundle);
                        }
                        return fragment;
                    } else if (result instanceof android.app.Fragment) {
                        android.app.Fragment fragment = (android.app.Fragment) result;
                        Bundle bundle = mRouteRequest.getBundle();
                        if (bundle != null && !bundle.isEmpty()) {
                            fragment.setArguments(bundle);
                        }
                        return fragment;
                    } else {
                        callback(RouteResult.FAILED, String.format(
                                "The matcher can't generate a fragment instance for uri: %s",
                                mRouteRequest.getUri().toString()));
                        return null;
                    }
                }
            }
        }

        callback(RouteResult.FAILED, String.format(
                "Can not find an Fragment that matches the given uri: %s", mRouteRequest.getUri()));
        return null;
    }

    private Intent getIntent(Context context) {

        if (mRouteRequest.getUri() == null) {
            callback(RouteResult.FAILED, "uri == null");
            return null;
        }

        if (!mRouteRequest.isSkipInterceptors()) {
            for (RouteInterceptor interceptor : Router.getGlobalInterceptors()) {
                if (interceptor.intercept(context, mRouteRequest)) {
                    callback(RouteResult.INTERCEPTED, "Intercepted by global interceptor.");
                    return null;
                }
            }
        }

        List<AbsMatcher> matcherList = MatcherRegister.getMatcher();
        if (matcherList.isEmpty()) {
            callback(RouteResult.FAILED, "The MatcherRegistry contains no Matcher.");
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

        callback(RouteResult.FAILED, String.format("Can not find an Activity that matches the given uri: %s", mRouteRequest.getUri()));
        return null;

    }

    /**
     * 1、进行拦截器过滤
     * 2、通过matcher获取intent
     * 3、添加参数
     *
     * @param context
     * @param matcher
     * @param target
     * @return
     */
    private Intent finalizeIntent(Context context, AbsMatcher matcher, @Nullable Class<?> target) {

        if (intercept(context, target)) {
            return null;
        }

        Object intent = matcher.generate(context, mRouteRequest.getUri(), target);
        if (intent instanceof Intent) {
            assembleIntent((Intent) intent);
            return (Intent) intent;
        } else { //没有匹配到
            callback(RouteResult.FAILED, String.format("The matcher can't generate an intent for uri: %s", mRouteRequest.getUri().toString()));
            return null;
        }

    }

    /**
     * @date 创建时间 2017/12/14
     * @author Jiang zinc
     * @Description 是否需要进行拦截【true：拦截   false：放过】
     * @version 1.0
     */
    private boolean intercept(Context context, Class<?> target) {

        if (mRouteRequest.isSkipInterceptors()) {
            return false;
        }

        Set<String> finalInterceptors = new HashSet<>();

        if (target != null) {

            //添加全局的拦截器
            String[] baseInterceptors = AptHub.targetInterceptors.get(target);
            if (baseInterceptors != null && baseInterceptors.length > 0) {
                Collections.addAll(finalInterceptors, baseInterceptors);
            }

            //移除removedInterceptors的拦截器
            if (mRouteRequest.getRemovedInterceptors() != null) {
                finalInterceptors.removeAll(mRouteRequest.getRemovedInterceptors());
            }

        }

        //添加视图addedInterceptors的拦截器
        if (mRouteRequest.getAddedInterceptors() != null) {
            finalInterceptors.addAll(mRouteRequest.getAddedInterceptors());
        }

        if (!finalInterceptors.isEmpty()) {
            for (String name : finalInterceptors) {
                RouteInterceptor interceptor = mInterceptorInstance.get(name);
                if (interceptor == null) {
                    Class<? extends RouteInterceptor> clz = AptHub.interceptorTable.get(name);
                    Constructor<? extends RouteInterceptor> constructor = null;
                    try {
                        constructor = clz.getConstructor();
                        interceptor = constructor.newInstance();
                    } catch (Exception e) {
                        RLog.e(String.format("Can't construct a interceptor with name: %s", name));
                        e.printStackTrace();
                    }

                    if (interceptor != null && interceptor.intercept(context, mRouteRequest)) {
                        callback(RouteResult.INTERCEPTED, String.format("Intercepted:{uri: %s, interceptor: %s}", mRouteRequest.getUri().toString(), name));
                        return true;
                    }
                }
            }
        }
        return false;

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

        if (mRouteRequest.getBundle() != null && !mRouteRequest.getBundle().isEmpty()) {
            intent.putExtras(mRouteRequest.getBundle());
        }

        if (mRouteRequest.getFlags() != 0) {
            intent.addFlags(mRouteRequest.getFlags());
        }

        if (mRouteRequest.getData() != null) {
            intent.setData(mRouteRequest.getData());
        }

        if (mRouteRequest.getAction() != null) {
            intent.setAction(mRouteRequest.getAction());
        }

        if (mRouteRequest.getType() != null) {
            intent.setType(mRouteRequest.getType());
        }

    }

    void injectParams(Object obj) {
        if (obj instanceof Activity || obj instanceof Fragment || obj instanceof android.support.v4.app.Fragment) {
            String key = obj.getClass().getCanonicalName();
            Class<ParamInjector> clz;

            if (!AptHub.injectors.containsKey(key)) {
                try {
                    clz = (Class<ParamInjector>) Class.forName(key + PARAM_CLASS_SUFFIX);
                    AptHub.injectors.put(key, clz);
                } catch (ClassNotFoundException e) {
                    RLog.e("Inject params faield.", e);
                    return;
                }
            } else {
                clz = AptHub.injectors.get(key);
            }

            try {
                ParamInjector injector = clz.newInstance();
                injector.inject(obj);
            } catch (Exception e) {
                RLog.e("Inject params failed.", e);
            }

        } else {
            RLog.e("The obj you passed must be an instance of Activity or Fragment.");
        }

    }

    private void callback(RouteResult result, String msg) {
        if (result != RouteResult.SUCCEED) {
            RLog.w(msg);
        }
        if (mRouteRequest.getCallback() != null) {
            mRouteRequest.getCallback().callback(result, mRouteRequest.getUri(), msg);
        }
    }

}
