package com.xuanhui.first.lock;

import android.content.Context;
import android.view.View;

import com.xuanhui.first.databinding.ActivityStartBinding;
import com.xuanhui.first.ui.StartActivity;
import com.xuanhui.first.utils.UpdateManager;

/**
 * Created by user on 2018/11/30.
 */

public class StartLock extends BaseLock<ActivityStartBinding> {

    public StartLock(Context context, ActivityStartBinding activityStartBinding) {
        super(context, activityStartBinding);
    }

    @Override
    public void init() {


    }

    public void startServic(View v) {
        if(((StartActivity)mContext).requestPermissions()){
            UpdateManager updateManager = new UpdateManager(mContext, false);
            updateManager.showUpdateDialog("请跟新到最新版本!");
        }


    }


}
