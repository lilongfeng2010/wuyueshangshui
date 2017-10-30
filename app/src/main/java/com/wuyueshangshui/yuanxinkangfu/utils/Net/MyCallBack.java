package com.wuyueshangshui.yuanxinkangfu.utils.Net;

import okhttp3.Response;

/**
 * Created by lilfi on 2017/8/23.
 */

public abstract class MyCallBack extends com.zhy.http.okhttp.callback.Callback<String>{
    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        String string=response.body().string();

        return string;
    }


}
