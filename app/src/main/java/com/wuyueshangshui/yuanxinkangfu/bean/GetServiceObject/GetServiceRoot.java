package com.wuyueshangshui.yuanxinkangfu.bean.GetServiceObject;

import com.wuyueshangshui.yuanxinkangfu.bean.GetBean.GetHeadBean;

/**
 * Created by lilfi on 2017/8/29.
 */

public class GetServiceRoot {
    private GetHeadBean head;
    private GetServiceBody body;

    public GetHeadBean getHead() {
        return head;
    }

    public void setHead(GetHeadBean head) {
        this.head = head;
    }

    public GetServiceBody getBody() {
        return body;
    }

    public void setBody(GetServiceBody body) {
        this.body = body;
    }
}
