package com.xuanhui.first.ui;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.xuanhui.first.R;
import com.xuanhui.first.base.BaseActivity;
import com.xuanhui.first.databinding.ActivityStartBinding;
import com.xuanhui.first.lock.StartLock;
import com.xuanhui.first.utils.permission.IRequestPermissions;
import com.xuanhui.first.utils.permission.IRequestPermissionsResult;
import com.xuanhui.first.utils.permission.PermissionUtils;
import com.xuanhui.first.utils.permission.RequestPermissions;
import com.xuanhui.first.utils.permission.RequestPermissionsResultSetApp;

public class StartActivity extends BaseActivity {
    private IRequestPermissions mIRequestPermissions;
    private IRequestPermissionsResult mIRequestPermissionsResult;
    private StartLock mStartLock;
    private ActivityStartBinding mMbinding;

    @Override
    public void setContent(Bundle savedInstanceState) {
        mMbinding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        mStartLock = new StartLock(this, mMbinding);
        mMbinding.setLock(mStartLock);
        mIRequestPermissions= RequestPermissions.getInstance();
        mIRequestPermissionsResult= RequestPermissionsResultSetApp.getInstance();
        requestPermissions();

    }
    //请求权限
    public boolean requestPermissions(){
        //需要请求的权限
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //开始请求权限
        return mIRequestPermissions.requestPermissions(this, permissions, PermissionUtils.ResultCode1);
    }

    //用户授权操作结果（可能授权了，也可能未授权）
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //用户给APP授权的结果
        //判断grantResults是否已全部授权，如果是，执行相应操作，如果否，提醒开启权限
        if(mIRequestPermissionsResult.doRequestPermissionsResult(this, permissions, grantResults)){
            //请求的权限全部授权成功，此处可以做自己想做的事了
            //输出授权结果
            Toast.makeText(StartActivity.this,"授权成功，请重新点击刚才的操作！",Toast.LENGTH_LONG).show();
        }else{
            //输出授权结果
            Toast.makeText(StartActivity.this,"请给APP授权，否则功能无法正常使用！",Toast.LENGTH_LONG).show();
        }
    }



}
