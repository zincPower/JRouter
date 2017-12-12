package com.zinc.libjfir;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.libannotation.Route;
import com.zinc.librouter.impl.Router;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/12
 * @description
 */

@Route("jfir")
public class JFirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fir_layout);
    }

    public void gotoSec(View view){
        Router.build("jsec").go(this);
    }
}
