package com.zinc.jrouter;

import android.content.Context;
import android.widget.Toast;

import com.zinc.libannotation.Interceptor;
import com.zinc.librouter.RouteInterceptor;
import com.zinc.librouter.impl.RouteRequest;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/14
 * @description
 */

@Interceptor("MyTestInterceptor")
public class MyTestInterceptor implements RouteInterceptor {
    @Override
    public boolean intercept(Context context, RouteRequest routeRequest) {

        Toast.makeText(context, "拦截。。。", Toast.LENGTH_SHORT).show();

        return false;
    }
}
