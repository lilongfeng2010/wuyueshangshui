package com.wuyueshangshui.yuanxinkangfu.utils.Net;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.DialogUtils;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;

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

    public static void ResponseError(Activity context, String errorId){
        ToastUtils.dismissLoadingToast();
        switch (errorId){
            case "500":
               ToastUtils.Toast(context,"服务器响应出错");
                LogUtils.showe("===============吐司应该被突出");
                break;
        }
    }

}
