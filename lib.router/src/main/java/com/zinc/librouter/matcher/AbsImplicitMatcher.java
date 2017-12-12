package com.zinc.librouter.matcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/11
 * @description
 */

public abstract class AbsImplicitMatcher extends AbsMatcher {

    public AbsImplicitMatcher(int priority) {
        super(priority);
    }

    @Override
    public Object generate(Context context, Uri uri, @Nullable Class<?> target) {
        return new Intent(Intent.ACTION_VIEW, uri);
    }
}
