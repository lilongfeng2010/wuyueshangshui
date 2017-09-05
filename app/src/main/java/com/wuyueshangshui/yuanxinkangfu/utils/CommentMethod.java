package com.wuyueshangshui.yuanxinkangfu.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.wuyueshangshui.yuanxinkangfu.bean.UserBean;
import com.wuyueshangshui.yuanxinkangfu.bean.WXUserBean;
import com.wuyueshangshui.yuanxinkangfu.common.Constant;

import org.json.JSONObject;

/**
 * Created by yebidaxiong on 2017/9/5.
 */

public class CommentMethod {
    public static void loginSPUtils(JSONObject root, Gson gson, Context context) {
        UserBean user = gson.fromJson(root.optJSONObject(Constant.body).optJSONObject(Constant.user).toString(), UserBean.class);
        LogUtils.showe("==user==" + root.optJSONObject(Constant.body).optJSONObject(Constant.user).toString());
        //保存用户的ID
        if (!TextUtils.isEmpty(user.getId() + "")) {
            SPUtils.setString(context, Constant.user_id, user.getId() + "");
        }
        //保存用户的头像url
        if (!TextUtils.isEmpty(user.getAvatar())) {
            SPUtils.setString(context, Constant.wechat_headimg_url, user.getAvatar() + "");
        }
        //保存用户的姓名
        if (!TextUtils.isEmpty(user.getRealname())) {
            SPUtils.setString(context, Constant.real_name, user.getRealname() + "");
        }
        //保存用户的手机号
        if (!TextUtils.isEmpty(user.getMobile())) {
            SPUtils.setString(context, Constant.mobile, user.getMobile() + "");
        }
        //保存用户的职业
        if (!TextUtils.isEmpty(user.getJob())) {
            SPUtils.setString(context, Constant.job, user.getJob());
        }
        //保存用户的身份证号
        if (!TextUtils.isEmpty(user.getId_card_no())) {
            SPUtils.setString(context, Constant.identify_card_num, user.getId_card_no());
        }
        //保存用户的家庭id
        if (!TextUtils.isEmpty(user.getFamily_id() + "")) {
            SPUtils.setString(context, Constant.family_id, user.getFamily_id() + "");
        }
        //保存用户的性别
        if (!TextUtils.isEmpty(user.getGender() + "")) {
            SPUtils.setString(context, Constant.gender, user.getGender() + "");
        }
    }
}
