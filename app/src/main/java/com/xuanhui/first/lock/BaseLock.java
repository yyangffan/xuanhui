package com.xuanhui.first.lock;

import android.content.Context;

/**
 * Created by user on 2018/11/29.
 */

public abstract class BaseLock<T> {
    public Context mContext;
    public T mBinding;

    public BaseLock(Context context, T t) {
        mContext = context;
        this.mBinding = t;
        init();
    }

    public abstract void init();



}
