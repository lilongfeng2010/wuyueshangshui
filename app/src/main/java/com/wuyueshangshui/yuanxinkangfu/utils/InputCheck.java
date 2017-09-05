package com.wuyueshangshui.yuanxinkangfu.utils;

import android.content.Context;

import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lilfi on 2017/8/22.
 */

public class InputCheck {
    public static boolean isPhone(String s){
        String regExp = "^[0-9]{11}$";
        Pattern p=Pattern.compile(regExp);
        Matcher m=p.matcher(s);
        return m.find();
    }

    public static boolean checkPhone(Context context, String s){
        boolean result=isPhone(s);
        if (!result){
            ToastUtils.showMiddleToast(context, "您输入的电话号码格式错误");
        }
        return result;
    }

}
