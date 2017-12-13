package com.zinc.librouter.impl;

import android.net.Uri;
import android.os.Bundle;

import com.zinc.librouter.RouteCallback;

import java.io.Serializable;

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
        if(requestCode < 0){
            this.requestCode = INVALID_REQUEST_CODE;
        }else{
            this.requestCode = requestCode;
        }
    }
}
