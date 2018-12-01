package com.xuanhui.first.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.xuanhui.first.R;
import com.xuanhui.first.base.BaseActivity;
import com.xuanhui.first.databinding.ActivityMapViewBinding;
import com.xuanhui.first.lock.MapViewLock;

public class MapViewActivity extends BaseActivity {
    private MapViewLock mLock;
    private ActivityMapViewBinding mBinding;


    @Override
    public void setContent(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map_view);
        mLock = new MapViewLock(this, mBinding);
        mBinding.setLock(mLock);
        mBinding.mapview.onCreate(savedInstanceState);
        mBinding.mapview.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.mapview.onDestroy();
    }
}
