package com.zinc.librouter;

import java.util.Map;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

public interface RouteTable {

    void handle(Map<String, Class<?>> map);

}
