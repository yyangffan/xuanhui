package com.xuanhui.first.base;

import android.app.Application;

import com.yanzhenjie.nohttp.NoHttp;

/**
 * Created by user on 2018/11/30.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
//        InitializationConfig config = InitializationConfig.newBuilder(this)
//                // 全局连接服务器超时时间，单位毫秒，默认10s。
//                .connectionTimeout(30 * 1000)
//                // 全局等待服务器响应超时时间，单位毫秒，默认10s。
//                .readTimeout(30 * 1000)
//                // 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
//                .cacheStore(
//                        // 如果不使用缓存，setEnable(false)禁用。
//                        new DBCacheStore(this).setEnable(true)
//                )
//                // 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现CookieStore接口。
//                .cookieStore(
//                        // 如果不维护cookie，setEnable(false)禁用。
//                        new DBCookieStore(this).setEnable(true)
//                )
//                // 配置网络层，默认URLConnectionNetworkExecutor，如果想用OkHttp：OkHttpNetworkExecutor。
//                .networkExecutor()
//                // 全局通用Header，add是添加，多次调用add不会覆盖上次add。
//                .addHeader()
//                // 全局通用Param，add是添加，多次调用add不会覆盖上次add。
//                .addParam()
//                .sslSocketFactory() // 全局SSLSocketFactory。
//                .hostnameVerifier() // 全局HostnameVerifier。
//                .retry(x) // 全局重试次数，配置后每个请求失败都会重试x次。
//                .build();
    }
}
