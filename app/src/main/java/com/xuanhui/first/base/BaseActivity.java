package com.xuanhui.first.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by user on 2018/11/26.
 */

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(savedInstanceState);
    }

    public abstract void setContent(Bundle savedInstanceState);

}
