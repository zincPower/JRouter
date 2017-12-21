package com.zinc.jrouter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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

        Router.addCommonParams("name","zinc");
        Router.addCommonParams("pwd","zincPower");
        Router.addCommonParams("nick","jpy");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_toSec:
//                Router.build("sec").addInterceptors("MyTestInterceptor").with("myName", "zinc").go(this);
                Router.build("sec").with("myName", "zinc").go(this);
                break;
            case R.id.btn_toLibFir:
                Router.build("jfir").go(this);
                break;
            case R.id.btn_toLibSec:
                Router.build("jsec").go(this);
                break;
            case R.id.btn_toBrowser:
                String content = ((EditText)findViewById(R.id.edit_text)).getText().toString();
                if(TextUtils.isEmpty(content)){
                    return;
                }
                Router.build(content).go(this);
//                Router.build("http://www.baidu.com?user=zinc").go(this);
                break;
        }

    }
}
