package com.zinc.librouter.matcher;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zinc.librouter.impl.RouteRequest;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/10
 * @description
 */

public interface Matcher extends Comparable<Matcher> {

    /**
     *
     * @param context
     * @param uri
     * @param route route Table 中的路径
     * @param routeRequest
     * @return 如果匹配到返回true
     */
    boolean match(Context context, Uri uri, @Nullable String route, RouteRequest routeRequest);

    /**
     * 当 {@link #match(Context, Uri, String, RouteRequest)} 返回true，调用此方法
     *
     * @param context
     * @param uri
     * @param target route table中对应的视图（activity或者fragment）
     * @return intent／fragment
     */
    Object generate(Context context, Uri uri, @Nullable Class<?> target);

}
