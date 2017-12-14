package com.zinc.librouter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */


public interface IRouter {

    String TAG = "JRouter";

    IRouter build(Uri uri);

    void go(Context context);

    /**
     * bundle.putXXX(key, value)
     * @param key
     * @param value
     */
    IRouter with(String key, Object value);

    /**
     * @see Bundle#putAll(Bundle)
     * @param bundle
     */
    IRouter with(Bundle bundle);

    IRouter addInterceptors(String ...interceptors);

}
