package com.wuyueshangshui.yuanxinkangfu.utils;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by lilfi on 2017/8/21.
 */

public class LogUtils {
    private static boolean isOpenLog=true;
    public static void showi(String logContent){
        if (isOpenLog){
            Log.i("==",logContent);
        }

    }
    public static void showe(String logContent){
        if (isOpenLog){
            Log.e("==",logContent);
        }

    }



}
