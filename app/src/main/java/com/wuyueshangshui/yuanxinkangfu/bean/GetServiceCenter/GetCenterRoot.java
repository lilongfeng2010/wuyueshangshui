package com.wuyueshangshui.yuanxinkangfu.bean.GetServiceCenter;

import com.wuyueshangshui.yuanxinkangfu.bean.GetBean.GetHeadBean;

/**
 * Created by lilfi on 2017/8/29.
 */

public class GetCenterRoot {
    private GetHeadBean head;
    private GetCenterBody body;

    public GetHeadBean getHead() {
        return head;
    }

    public void setHead(GetHeadBean head) {
        this.head = head;
    }

    public GetCenterBody getBody() {
        return body;
    }

    public void setBody(GetCenterBody body) {
        this.body = body;
    }
}
