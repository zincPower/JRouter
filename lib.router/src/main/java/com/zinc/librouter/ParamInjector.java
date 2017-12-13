package com.zinc.librouter;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/13
 * @description 参数的注入
 */

public interface ParamInjector {

    /**
     * 注入参数
     *
     * @param obj activity 或 fragment 的的实例
     */
    void inject(Object obj);

}
