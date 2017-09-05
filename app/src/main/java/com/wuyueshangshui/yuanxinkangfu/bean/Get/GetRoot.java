package com.wuyueshangshui.yuanxinkangfu.bean.Get;

import java.util.List;

/**
 * Created by lilfi on 2017/8/30.
 */

public class GetRoot {
    private GetHead head;
    private List<GetBody> body;

    public GetHead getHead() {
        return head;
    }

    public void setHead(GetHead head) {
        this.head = head;
    }

    public List<GetBody> getBody() {
        return body;
    }

    public void setBody(List<GetBody> body) {
        this.body = body;
    }
}
