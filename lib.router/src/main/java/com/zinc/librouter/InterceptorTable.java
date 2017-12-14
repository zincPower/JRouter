package com.zinc.librouter;

import java.util.Map;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/14
 * @description
 */

public interface InterceptorTable {

    /**
     *
     * @param map name: 拦截器
     */
    void handle(Map<String, Class<? extends RouteInterceptor>> map);

}
