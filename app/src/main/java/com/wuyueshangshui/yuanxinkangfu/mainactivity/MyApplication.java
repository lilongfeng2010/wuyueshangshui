package com.wuyueshangshui.yuanxinkangfu.mainactivity;

import android.app.Application;
import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wuyueshangshui.yuanxinkangfu.utils.Net.CheckNet;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by lilfi on 2017/8/23.
 */

public class MyApplication extends Application {
    //微信官网申请的appid
    public static final String APP_ID="wxefd4941cb12bede4";
    //IWXAPI是第三方app和微信通信的openapi接口
    public static IWXAPI api;
    public static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        api= WXAPIFactory.createWXAPI(this,APP_ID,false);
        appContext=this;
    }

}
