package com.zinc.libjsec;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zinc.libannotation.Route;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/12
 * @description
 */

@Route("jsec")
public class JSecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_layout);
    }
}
