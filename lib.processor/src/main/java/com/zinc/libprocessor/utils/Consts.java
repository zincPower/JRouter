package com.zinc.libprocessor.utils;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/11/29
 * @description
 */

public class Consts {

    //module的名
    public static final String OPTION_MODULE_NAME = "moduleName";
    //Route注解类的路径
    public static final String ROUTE_ANNOTATION_TYPE = "com.zinc.libannotation.Route";
    //Param注解类的路径
    public static final String PARAM_ANNOTATION_TYPE = "com.zinc.libannotation.Param";
    //Intercepor注解类的路径
    public static final String INTERCEPTOR_ANNOTATION_TYPE = "com.zinc.libannotation.Interceptor";

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
    //路由表类名
    public static final String ROUTER_TABLE = "RouteTable";

    //拦截器类名
    public static final String INTERCEPTOR_TABLE = "InterceptorTable";
    //路由类
    public static final String ROUTE_FULL_NAME = PACKAGE_NAME + DOT + ROUTER_TABLE;

    //注释
    public static final String CLASS_JAVA_DOC = "JRoute auto generate.Don't edit.";

    //视图属性内部类 XXXActivity$$JRouter$$ParamInjector
    public static final String INNER_CLASS_NAME = "$$JRouter$$ParamInjector";

    //内部类方法名
    public static final String METHOD_INJECT = "inject";

    public static final String INTERCEPTOR_INTERFACE = PACKAGE_NAME+DOT+"RouteInterceptor";

    public static final String TABLE_INTERCEPTORS = "TargetInterceptors";
}
