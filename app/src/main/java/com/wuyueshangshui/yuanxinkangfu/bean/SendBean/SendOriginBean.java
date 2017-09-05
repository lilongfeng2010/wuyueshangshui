package com.wuyueshangshui.yuanxinkangfu.bean.SendBean;

/**
 * Created by lilfi on 2017/8/21.
 */

public class SendOriginBean {
    private SendRootBean root;

    public SendRootBean getRoot() {
        return root;
    }

    public void setRoot(SendRootBean root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "SendOriginBean{" +
                "root=" + root +
                '}';
    }
}
