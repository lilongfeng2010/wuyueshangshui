package com.wuyueshangshui.yuanxinkangfu.bean.SendCancelOrder;

import com.wuyueshangshui.yuanxinkangfu.bean.Get.GetBody;
import com.wuyueshangshui.yuanxinkangfu.bean.GetOrder.GetOrderBody;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendBodyBean;
import com.wuyueshangshui.yuanxinkangfu.bean.SendBean.SendHeadBean;

/**
 * Created by lilfi on 2017/8/31.
 */

public class SendCancelOrderRoot {
    private SendHeadBean head;
    private SendBodyBean body;

    public SendHeadBean getHead() {
        return head;
    }

    public void setHead(SendHeadBean head) {
        this.head = head;
    }

    public SendBodyBean getBody() {
        return body;
    }

    public void setBody(SendBodyBean body) {
        this.body = body;
    }
}
