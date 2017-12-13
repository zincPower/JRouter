package com.zinc.jrouter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zinc.librouter.impl.Router;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void gotoSEC(View view){

        Router.build("sec").with("myName","zinc").go(this);
//        Router.build("android://sec").go(this);

    }

}
