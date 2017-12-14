package com.zinc.librouter.impl;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.SparseArray;

import com.zinc.librouter.IRouter;
import com.zinc.librouter.utils.RLog;

import java.io.Serializable;
import java.util.ArrayList;

import static com.zinc.librouter.impl.Router.RAW_URI;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

abstract class AbsRouter implements IRouter {
    protected RouteRequest mRouteRequest;

    @Override
    public IRouter build(Uri uri) {
        mRouteRequest = new RouteRequest(uri);
        Bundle bundle = new Bundle();
        bundle.putString(RAW_URI, uri.toString());
        mRouteRequest.setBundle(bundle);
        return this;
    }

    //    @RequiresApi(21)
    @Override
    public IRouter with(Bundle bundle) {
        if (bundle != null && !bundle.isEmpty()) {
            Bundle extras = mRouteRequest.getBundle();
            if (extras == null) {
                extras = new Bundle();
            }
            extras.putAll(bundle);
            mRouteRequest.setBundle(extras);
        }
        return this;
    }

    @Override
    public IRouter with(String key, Object value) {
        if (value == null) {
            RLog.w("Ignored: The extra value is null.");
            return this;
        }

        Bundle bundle = mRouteRequest.getBundle();

        if(bundle == null){
            bundle = new Bundle();
        }

        if (value instanceof Bundle) {
            bundle.putBundle(key, (Bundle) value);
        } else if (value instanceof Byte) {
            bundle.putByte(key, (byte) value);
        } else if (value instanceof Short) {
            bundle.putShort(key, (short) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (int) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (long) value);
        } else if (value instanceof Character) {
            bundle.putChar(key, (char) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (boolean) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (float) value);
        } else if (value instanceof Double) {
            bundle.putDouble(key, (double) value);
        } else if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof CharSequence) {
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof short[]) {
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof int[]) {
            bundle.putIntArray(key, (int[]) value);
        } else if (value instanceof long[]) {
            bundle.putLongArray(key, (long[]) value);
        } else if (value instanceof char[]) {
            bundle.putCharArray(key, (char[]) value);
        } else if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof double[]) {
            bundle.putDoubleArray(key, (double[]) value);
        } else if (value instanceof String[]) {
            bundle.putStringArray(key, (String[]) value);
        } else if (value instanceof CharSequence[]) {
            bundle.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof IBinder) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bundle.putBinder(key, (IBinder) value);
            } else {
                RLog.e("putBinder() requires api 18.");
            }
        } else if (value instanceof ArrayList) {
            if (!((ArrayList) value).isEmpty()) {
                Object obj = ((ArrayList) value).get(0);
                if (obj instanceof Integer) {
                    bundle.putIntegerArrayList(key, (ArrayList<Integer>) value);
                } else if (obj instanceof String) {
                    bundle.putStringArrayList(key, (ArrayList<String>) value);
                } else if (obj instanceof CharSequence) {
                    bundle.putCharSequenceArrayList(key, (ArrayList<CharSequence>) value);
                } else if (obj instanceof Parcelable) {
                    bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) value);
                }
            }
        } else if (value instanceof SparseArray) {
            bundle.putSparseParcelableArray(key, (SparseArray<? extends Parcelable>) value);
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Parcelable[]) {
            bundle.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) value);
        } else {
            RLog.w("Unknown object type.");
        }

        mRouteRequest.setBundle(bundle);
        return this;
    }

    @Override
    public IRouter addInterceptors(String... interceptors) {
        mRouteRequest.setInterceptor(interceptors);
        return this;
    }
}