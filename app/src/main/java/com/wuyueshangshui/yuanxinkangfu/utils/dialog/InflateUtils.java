package com.wuyueshangshui.yuanxinkangfu.utils.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuyueshangshui.yuanxinkangfu.mainactivity.MyApplication;

/**
 * Created by lilfi on 2017/9/1.
 */

public class InflateUtils {
    public static View inflate(int resId, ViewGroup root, boolean attachToRoot){
        return LayoutInflater.from(MyApplication.appContext).inflate(resId, root, attachToRoot);
    }

}
