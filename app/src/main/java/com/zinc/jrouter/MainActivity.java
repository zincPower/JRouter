package com.zinc.jrouter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zinc.librouter.impl.Router;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_toSec).setOnClickListener(this);
        findViewById(R.id.btn_toLibFir).setOnClickListener(this);
        findViewById(R.id.btn_toLibSec).setOnClickListener(this);
        findViewById(R.id.btn_toBrowser).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_toSec:
                Router.build("sec").addInterceptors("MyTestInterceptor").with("myName", "zinc").go(this);
                break;
            case R.id.btn_toLibFir:
                Router.build("jfir").go(this);
                break;
            case R.id.btn_toLibSec:
                Router.build("jsec").go(this);
                break;
            case R.id.btn_toBrowser:
                Router.build("http://www.baidu.com").go(this);
                break;
        }

    }
}
