package com.zinc.librouter.impl;

import com.zinc.librouter.RouteTable;
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

    static Map<String, Class<?>> routeTable = new HashMap<>();

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
