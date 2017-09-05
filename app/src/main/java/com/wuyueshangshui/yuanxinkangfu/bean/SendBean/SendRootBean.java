package com.wuyueshangshui.yuanxinkangfu.bean.SendBean;

/**
 * Created by lilfi on 2017/8/21.
 */

public class SendRootBean {
    private SendHeadBean head;
    private SendBodyBean body;

    public SendBodyBean getBody() {
        return body;
    }

    public void setBody(SendBodyBean body) {
        this.body = body;
    }

    public SendHeadBean getHead() {
        return head;
    }
    public void setHead(SendHeadBean head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "SendRootBean{" +
                "head=" + head +
                ", body=" + body +
                '}';
    }
}
