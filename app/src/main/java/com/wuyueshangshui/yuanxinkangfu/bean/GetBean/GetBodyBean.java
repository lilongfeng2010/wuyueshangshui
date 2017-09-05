package com.wuyueshangshui.yuanxinkangfu.bean.GetBean;

import com.wuyueshangshui.yuanxinkangfu.bean.UserBean;
import com.wuyueshangshui.yuanxinkangfu.bean.WXUserBean;

/**
 * Created by lilfi on 2017/8/23.
 */

public class GetBodyBean {

    private WXUserBean wx_user;
    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public WXUserBean getWx_user() {
        return wx_user;
    }

    public void setWx_user(WXUserBean wx_user) {
        this.wx_user = wx_user;
    }
}
