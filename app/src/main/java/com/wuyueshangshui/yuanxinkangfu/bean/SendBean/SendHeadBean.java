package com.wuyueshangshui.yuanxinkangfu.bean.SendBean;

/**
 * Created by lilfi on 2017/8/21.
 */

public class SendHeadBean {
    private String clientid;
    private int action;
    private String timestamp;

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SendHeadBean{" +
                "clientid='" + clientid + '\'' +
                ", action=" + action +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
