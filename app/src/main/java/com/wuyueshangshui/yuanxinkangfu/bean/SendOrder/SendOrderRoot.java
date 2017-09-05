package com.wuyueshangshui.yuanxinkangfu.bean.SendOrder;

import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;

/**
 * Created by lilfi on 2017/8/30.
 */

public class SendOrderRoot {
    private SendHeadBean head;
    private SendOrderBody body;

    public SendHeadBean getHead() {
        return head;
    }

    public void setHead(SendHeadBean head) {
        this.head = head;
    }

    public SendOrderBody getBody() {
        return body;
    }

    public void setBody(SendOrderBody body) {
        this.body = body;
    }
}
