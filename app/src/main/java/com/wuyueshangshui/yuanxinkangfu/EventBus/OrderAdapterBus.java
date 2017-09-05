package com.wuyueshangshui.yuanxinkangfu.EventBus;

/**
 * Created by lilfi on 2017/8/31.
 * //取消预约的eventbus，这个position就是取消的条目在整个recyclerview中的位置
 */

public class OrderAdapterBus {
    private int mPostion;
    public OrderAdapterBus(int position){
        mPostion=position;
    }
    public int getMPosition(){
        return mPostion;
    }
}
