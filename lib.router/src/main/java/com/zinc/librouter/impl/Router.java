package com.zinc.librouter.impl;

import android.net.Uri;

import com.zinc.librouter.IRouter;
import com.zinc.librouter.modle.Configuration;
import com.zinc.librouter.utils.RLog;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

public class Router {

    public static final String RAW_URI = "raw_uri";

    public static void initialize(Configuration configuration){
        RLog.showLog(configuration.isDebuggable());
        AptHub.registerModules(configuration.getModules());
    }

    public static IRouter build(String path){

        return build(path == null? null : Uri.parse(path));

    }

    public static IRouter build(Uri uri){
        return RealRouter.getInstance().build(uri);
    }

    public static void injectParams(Object obj){
        RealRouter.getInstance().injectParams(obj);
    }

}
