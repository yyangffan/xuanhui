package com.xuanhui.first.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.xuanhui.first.R;
import com.xuanhui.first.base.BaseActivity;
import com.xuanhui.first.databinding.ActivityMainBinding;
import com.xuanhui.first.lock.MainLock;

public class MainActivity extends BaseActivity {
    private MainLock mMainLock;
    private ActivityMainBinding mMainBinding;

    @Override
    public void setContent(Bundle savedInstanceState) {
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainLock=new MainLock(this,mMainBinding);
        mMainBinding.setLock(mMainLock);
    }
}
