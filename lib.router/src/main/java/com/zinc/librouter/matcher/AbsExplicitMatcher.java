package com.zinc.librouter.matcher;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/10
 * @description fragment和activity的匹配器
 */

public abstract class AbsExplicitMatcher extends AbsMatcher {

    public AbsExplicitMatcher(int priority) {
        super(priority);
    }

    @Override
    public Object generate(Context context, Uri uri, @Nullable Class<?> target) {

        if (target == null) {
            return null;
        }

        Object result = null;

        if (Activity.class.isAssignableFrom(target)) {
            result = new Intent(context, target);
        } else if (Fragment.class.isAssignableFrom(target)) {
            try {
                result = target.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (android.support.v4.app.Fragment.class.isAssignableFrom(target)) {
            try {
                result = target.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
