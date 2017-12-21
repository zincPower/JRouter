package com.zinc.jrouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zinc.libannotation.Param;
import com.zinc.libannotation.Route;
import com.zinc.librouter.impl.Router;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/2
 * @description
 */

@Route(value = "sec", interceptors = "MyTestInterceptor")
public class SecActivity extends AppCompatActivity {

    @Param(key = "myName")
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);
        Router.injectParams(this);

        Fragment fragment = (Fragment) Router.build("sec_fragment").getFragment(this);
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment).commit();
        }
//        ((TextView)findViewById(R.id.tv_content)).setText(name);

    }

}
