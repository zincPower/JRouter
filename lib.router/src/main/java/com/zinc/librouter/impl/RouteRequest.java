package com.zinc.librouter.impl;

import android.net.Uri;
import android.os.Bundle;

import java.io.Serializable;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

public class RouteRequest implements Serializable {

    private Uri uri;

    private Bundle bundle;

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
}
