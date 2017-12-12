package com.zinc.jrouter;

import android.app.Application;
import android.os.Build;

import com.zinc.librouter.impl.Router;
import com.zinc.librouter.modle.Configuration;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/12
 * @description
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Router.initialize(new Configuration.Builder()
                .setDebuggable(BuildConfig.DEBUG)
                .registerModules("app","lib.JFir","lib.JSec")
                .build());

    }

}