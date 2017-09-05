package com.wuyueshangshui.yuanxinkangfu.bean.GetOrder;

import com.wuyueshangshui.yuanxinkangfu.bean.GetBean.GetHeadBean;

/**
 * Created by lilfi on 2017/8/30.
 */

public class GetOrderRoot {
    private GetHeadBean head;
    private GetOrderBody body;

    public GetHeadBean getHead() {
        return head;
    }

    public void setHead(GetHeadBean head) {
        this.head = head;
    }

    public GetOrderBody getBody() {
        return body;
    }

    public void setBody(GetOrderBody body) {
        this.body = body;
    }
}
