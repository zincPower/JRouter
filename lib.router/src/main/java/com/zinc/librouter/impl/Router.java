package com.zinc.librouter.impl;

import android.net.Uri;

import com.zinc.librouter.IRouter;
import com.zinc.librouter.RouteInterceptor;
import com.zinc.librouter.modle.Configuration;
import com.zinc.librouter.utils.RLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

public class Router {

    public static final String RAW_URI = "raw_uri";

    //全局的拦截器
    public static List<RouteInterceptor> globalInterceptors = new ArrayList<>();

    //链接通用参数
    public static Map<String, String> commonParams = new HashMap<>();

    public static void initialize(Configuration configuration) {
        RLog.showLog(configuration.isDebuggable());
        AptHub.registerModules(configuration.getModules());
    }

    public static IRouter build(String path) {

        return build(path == null ? null : Uri.parse(path));

    }

    public static IRouter build(Uri uri) {
        return RealRouter.getInstance().build(uri);
    }

    public static void injectParams(Object obj) {
        RealRouter.getInstance().injectParams(obj);
    }

    public static List<RouteInterceptor> getGlobalInterceptors() {
        return globalInterceptors;
    }

    public static void addGlobalInterceptor(RouteInterceptor globalInterceptor) {
        globalInterceptors.add(globalInterceptor);
    }

    public static void addCommonParams(String key, String value) {
        commonParams.put(key, value);
    }

    public static void removeCommonParams(String key) {
        commonParams.remove(key);
    }

    public static Map<String, String> getCommonParams(){
        return commonParams;
    }

}
