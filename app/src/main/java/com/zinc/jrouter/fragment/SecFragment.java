package com.zinc.jrouter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zinc.jrouter.R;
import com.zinc.libannotation.Route;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/17
 * @description
 */

@Route("sec_fragment")
public class SecFragment extends Fragment {

    public static SecFragment newInstance() {

        Bundle args = new Bundle();

        SecFragment fragment = new SecFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SecFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sec, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
