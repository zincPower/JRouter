package com.zinc.libprocessor.utils;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/11/29
 * @description
 */

public class Consts {

    //module的名
    public static final String OPTION_MODULE_NAME = "moduleName";
    //Route的路径
    public static final String ROUTE_ANNOTATION_TYPE = "com.zinc.libannotation.Route";

    //activity的类路径
    public static final String ACTIVITY_FULL_NAME = "android.app.Activity";
    //fragment的类路径
    public static final String FRAGMENT_FULL_NAME = "android.app.Fragment";
    //v4包的fragment的类路径
    public static final String FRAGMENT_V4_FULL_NAME = "android.support.v4.app.Fragment";

    //方法名
    public static final String HANDLE = "handle";

    //包名
    public static final String PACKAGE_NAME = "com.zinc.librouter";
    //分隔符
    public static final String DOT = ".";
    //类名
    public static final String ROUTER_TABLE = "RouteTable";

    //路由类
    public static final String ROUTE_FULL_NAME = PACKAGE_NAME+DOT+ROUTER_TABLE;

    //注释
    public static final String CLASS_JAVA_DOC = "类注释";

}
