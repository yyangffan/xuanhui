package com.xuanhui.first.lock;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Toast;

import com.xuanhui.first.bean.MainBean;
import com.xuanhui.first.databinding.ActivityMainBinding;

/**
 * Created by user on 2018/11/26.
 */

public class MainLock extends BaseLock<ActivityMainBinding>{
    private MainBean mMainBean;
    private AnimationDrawable mAnimationDrawable;

    public MainLock(Context context, ActivityMainBinding activityMainBinding) {
        super(context, activityMainBinding);
    }

    @Override
    public void init() {
        getMsg();
    }

    private void getMsg() {
        mMainBean = new MainBean();
        mMainBean.setAge("18");
        mMainBean.setName("小明");
        mBinding.setUser(mMainBean);
        mAnimationDrawable = (AnimationDrawable)mBinding.imgv.getBackground();
        mAnimationDrawable.start();

    }

    public void OnTxClick(View v) {
        Toast.makeText(mContext, "当前值为：" + mMainBean.getAge(), Toast.LENGTH_SHORT).show();
    }


}
