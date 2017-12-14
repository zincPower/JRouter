package com.zinc.librouter.impl;

import android.net.Uri;
import android.os.Bundle;

import com.zinc.librouter.RouteCallback;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

public class RouteRequest implements Serializable {
    private static final int INVALID_REQUEST_CODE = -1;

    private Uri uri;

    private Bundle bundle;

    private int flags;
    private Uri data;
    private String type;
    private String action;

    private RouteCallback callback;

    private int requestCode = INVALID_REQUEST_CODE;

    //添加的拦截器
    private Set<String> addedInterceptors;
    //移除的拦截器
    private Set<String> removedInterceptors;
    //是否跳过过滤
    private boolean isSkipInterceptors;

    public RouteRequest(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public Uri getData() {
        return data;
    }

    public void setData(Uri data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public RouteCallback getCallback() {
        return callback;
    }

    public void setCallback(RouteCallback callback) {
        this.callback = callback;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        if (requestCode < 0) {
            this.requestCode = INVALID_REQUEST_CODE;
        } else {
            this.requestCode = requestCode;
        }
    }

    public Set<String> getAddedInterceptors() {
        return addedInterceptors;
    }

    public void setInterceptor(String... interceptors) {
        if (interceptors == null || interceptors.length <= 0) {
            return;
        }
        if (this.addedInterceptors == null) {
            this.addedInterceptors = new HashSet<>(interceptors.length);
        }
        this.addedInterceptors.addAll(Arrays.asList(interceptors));
    }

    public Set<String> getRemovedInterceptors() {
        return removedInterceptors;
    }

    public void setRemovedInterceptors(String... interceptors) {
        if (interceptors == null || interceptors.length <= 0) {
            return;
        }
        if(this.removedInterceptors == null){
            this.removedInterceptors = new HashSet<>(interceptors.length);
        }
        this.removedInterceptors.addAll(Arrays.asList(interceptors));
    }

    public boolean isSkipInterceptors() {
        return isSkipInterceptors;
    }

    public void setSkipInterceptors(boolean skipInterceptors) {
        isSkipInterceptors = skipInterceptors;
    }
}
