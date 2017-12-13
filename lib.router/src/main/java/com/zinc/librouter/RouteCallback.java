package com.zinc.librouter;

import android.net.Uri;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/13
 * @description
 */

public interface RouteCallback {

    /**
     *
     * @param state {@link RouteResult}
     * @param uri Uri
     * @param message 提示信息
     */
    void callback(RouteResult state, Uri uri, String message);

}
