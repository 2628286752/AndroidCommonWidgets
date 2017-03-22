package com.hsj.example.androidcommonwidgets.xialakuang;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.hsj.example.androidcommonwidgets.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {

    private CustomSpinner mCsShowSpinner;

    private List<String> listData;

    private List<String> listSelected = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置Activity全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_test);

        initData();
        initView();
        initListener();


    }

    private void initListener() {


    }

    private void initView() {

        mCsShowSpinner = (CustomSpinner) findViewById(R.id.cs_at_showspinner);
        listSelected = mCsShowSpinner.getBackList();
        mCsShowSpinner.setData(listData,listSelected);
    }

    private void initData() {

        listData = new ArrayList<String>();
        for (int i = 0;i < 10;i++){

            listData.add("这个数字是" + i + "。你知道吗？");
        }
    }

}
