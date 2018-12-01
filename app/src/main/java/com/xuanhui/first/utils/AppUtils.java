package com.xuanhui.first.utils;

import android.content.Context;

/**
 * Created by user on 2018/11/26.
 */

public class AppUtils {
    /**
     * 利用反射获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
