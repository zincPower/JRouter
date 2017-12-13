package com.zinc.jrouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

@Route("sec")
public class SecActivity extends AppCompatActivity {

    @Param(key = "myName")
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);
        Router.injectParams(this);

        ((TextView)findViewById(R.id.tv_content)).setText(name);

    }

}
