package com.zinc.librouter;

import android.content.Context;
import android.net.Uri;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */


public interface IRouter {

    String TAG = "JRouter";

    IRouter build(Uri uri);

    void go(Context context);

}
