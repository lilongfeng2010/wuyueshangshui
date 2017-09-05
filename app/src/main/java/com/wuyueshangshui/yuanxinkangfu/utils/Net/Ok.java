package com.wuyueshangshui.yuanxinkangfu.utils.Net;

import android.telephony.gsm.GsmCellLocation;

import com.wuyueshangshui.yuanxinkangfu.common.GlobalURL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lilfi on 2017/8/21.
 */
public class Ok {
    private static OkHttpClient ourInstance = new OkHttpClient();


    private static OkHttpClient getInstance() {
        return ourInstance;
    }

    private Ok() {
    }

    /*public static void get(Callback callback){
        Request request=new Request.Builder()
                .url(GlobalURL.BaseURL)
                .build();
        Call call=ourInstance.newCall(request);
        call.enqueue(callback);
    }*/

    public static void post(RequestBody requestBody,Callback callback){
        Request request=new Request.Builder()
                .url(GlobalURL.BaseURL)
                .post(requestBody)
                .build();
        Call call=ourInstance.newCall(request);
        call.enqueue(callback);

    }


}
