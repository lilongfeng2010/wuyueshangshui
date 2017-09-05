package com.wuyueshangshui.yuanxinkangfu.EventBus;

/**
 * Created by yebidaxiong on 2017/9/4.
 */

public class MineHeadImageBus {
    private String headImagePath;
    public MineHeadImageBus(String headImagePath){
        this.headImagePath=headImagePath;
    }
    public String getHeadImagePath(){
        return headImagePath;
    }
}
