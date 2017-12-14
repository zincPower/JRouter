package com.zinc.librouter.impl;

import com.zinc.librouter.InterceptorTable;
import com.zinc.librouter.ParamInjector;
import com.zinc.librouter.RouteInterceptor;
import com.zinc.librouter.RouteTable;
import com.zinc.librouter.TargetInterceptors;
import com.zinc.librouter.modle.Configuration;
import com.zinc.librouter.utils.RLog;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

public class AptHub {

    private static final String PACKAGE_NAME = "com.zinc.librouter";
    private static final String DOT = ".";
    private static final String ROUTE_TABLE = "RouteTable";

    private static final String INTERCEPTOR_TABLE = "InterceptorTable";
    private static final String TARGET_INTERCEPTORS = "TargetInterceptors";

    //uri: Activity/Fragment
    static Map<String, Class<?>> routeTable = new HashMap<>();

    //Activity/Fragment: interceptorTable's name
    static Map<Class<?>, String[]> targetInterceptors = new HashMap<>();

    //interceptor's name: interceptor
    static Map<String, Class<? extends RouteInterceptor>> interceptorTable = new HashMap<>();

    static Map<String, Class<ParamInjector>> injectors = new HashMap<>();

    synchronized static void registerModules(String... modules) {
        if (modules == null || modules.length == 0) {
            RLog.w("empty modules.");
        } else {

            validateModulesName(modules);
            String routeTableName;

            //遍历所有module，加载所有route类
            for (String module : modules) {

                try {
                    routeTableName = PACKAGE_NAME + DOT + capitalize(module) + ROUTE_TABLE;
                    Class<?> routeTableClz = Class.forName(routeTableName);
                    Constructor constructor = routeTableClz.getConstructor();
                    RouteTable instance = (RouteTable) constructor.newInstance();
                    instance.handle(routeTable);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            RLog.i("RouteTable", routeTable.toString());

            //TargetInterceptors
            String targetInterceptorsName;
            for (String moduleName : modules) {
                try {
                    targetInterceptorsName = PACKAGE_NAME + DOT + capitalize(moduleName) + TARGET_INTERCEPTORS;
                    Class<?> clz = Class.forName(targetInterceptorsName);
                    Constructor constructor = clz.getConstructor();
                    TargetInterceptors instance = (TargetInterceptors) constructor.newInstance();
                    instance.handle(targetInterceptors);
                } catch (ClassNotFoundException e) {
                    RLog.i(String.format("There is no TargetInterceptors in module: %s.", moduleName));
                } catch (Exception e) {
                    RLog.w(e.getMessage());
                }
            }
            if (!targetInterceptors.isEmpty()) {
                RLog.i("TargetInterceptors", targetInterceptors.toString());
            }

            //InterceptorTable
            String interceptorName;
            for (String moduleName : modules) {
                try {
                    interceptorName = PACKAGE_NAME + DOT + capitalize(moduleName) + INTERCEPTOR_TABLE;
                    Class<?> clz = Class.forName(interceptorName);
                    Constructor constructor = clz.getConstructor();
                    InterceptorTable instance = (InterceptorTable) constructor.newInstance();
                    instance.handle(interceptorTable);
                } catch (ClassNotFoundException e) {
                    RLog.i(String.format("There is no InterceptorTable in module: %s.", moduleName));
                } catch (Exception e) {
                    RLog.w(e.getMessage());
                }
            }
            if (!interceptorTable.isEmpty()) {
                RLog.i("InterceptorTable", interceptorTable.toString());
            }

        }
    }

    /**
     *
     * @date 创建时间 2017/12/12
     * @author Jiang zinc
     * @Description 格式化为Xxxxxx
     * @version
     *
     */
    private static String capitalize(CharSequence moduleName) {

        return moduleName.length() == 0 ?
                "" : "" + Character.toUpperCase(moduleName.charAt(0)) + moduleName.subSequence(1, moduleName.length());

    }

    /**
     * @date 创建时间 2017/12/12
     * @author Jiang zinc
     * @Description 格式化modules名
     * @version
     */
    private static void validateModulesName(String... modules) {
        for (int i = 0; i < modules.length; ++i) {
            modules[i] = modules[i].replace(".", "_").replace("-", "_");
        }
    }

}
