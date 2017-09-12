package com.wuyueshangshui.yuanxinkangfu.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by yebidaxiong on 2017/9/7.
 */

public class ScreenUtils {
    public static int dp2px(Context context,float dipValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int) (dipValue*scale+0.5f);
    }
    public static int getScreenWidth(Context context){
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
}
