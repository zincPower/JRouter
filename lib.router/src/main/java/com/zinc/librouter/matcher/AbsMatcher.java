package com.zinc.librouter.matcher;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zinc.librouter.impl.RouteRequest;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/10
 * @description 抽象的匹配器
 */

public abstract class AbsMatcher implements Matcher {

    private int priority = 10;

    public AbsMatcher(int priority) {
        this.priority = priority;
    }

    protected void parseParams(Uri uri, RouteRequest routeRequest) {
        if (uri.getQuery() != null) {
            Bundle bundle = routeRequest.getBundle();
            //填补未设置bundle情况
            if (bundle == null) {
                bundle = new Bundle();
                routeRequest.setBundle(bundle);
            }

            Set<String> keys = uri.getQueryParameterNames();    //取key值
            Iterator<String> keysIterator = keys.iterator();
            while (keysIterator.hasNext()) {
                String key = keysIterator.next();
                List<String> values = uri.getQueryParameters(key);
                // TODO: 2017/12/10 这里需要看看
                if (values.size() > 1) {
                    bundle.putStringArray(key, values.toArray(new String[0]));
                } else if (values.size() == 1) {
                    bundle.putString(key, values.get(0));
                }
            }

        }
    }

    /**
     * @date 创建时间 2017/12/10
     * @author Jiang zinc
     * @Description 进行匹配起优先级排序
     * @version 1.0
     */
    @Override
    public int compareTo(@NonNull Matcher matcher) {

        if (this == matcher) {
            return 0;
        }

        if (matcher instanceof AbsMatcher) {
            if (this.priority > ((AbsMatcher) matcher).priority) {
                return -1;
            } else {
                return 1;
            }
        }

        return matcher.compareTo(this);
    }

    /**
     * {@link android.text.TextUtils#isEmpty(CharSequence)}
     *
     * @param str
     * @return
     */
    protected boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

}
