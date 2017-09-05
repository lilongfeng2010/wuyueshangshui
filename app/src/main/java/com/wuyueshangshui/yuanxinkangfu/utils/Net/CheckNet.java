package com.wuyueshangshui.yuanxinkangfu.utils.Net;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by lilfi on 2017/8/22.
 */

public class CheckNet {
    public static boolean netIsOk(Context context){
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo()!=null){
            return cm.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
