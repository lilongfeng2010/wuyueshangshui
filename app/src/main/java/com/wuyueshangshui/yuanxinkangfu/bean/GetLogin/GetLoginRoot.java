package com.wuyueshangshui.yuanxinkangfu.bean.GetLogin;

import java.util.List;

/**
 * Created by lilfi on 2017/8/31.
 */

public class GetLoginRoot {
    private GetLoginHead head;
    private List<GetLoginBody> body;

    public GetLoginHead getHead() {
        return head;
    }

    public void setHead(GetLoginHead head) {
        this.head = head;
    }

    public List<GetLoginBody> getBody() {
        return body;
    }

    public void setBody(List<GetLoginBody> body) {
        this.body = body;
    }
}
