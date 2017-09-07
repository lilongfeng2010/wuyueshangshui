package com.wuyueshangshui.yuanxinkangfu.utils.NiceDialog;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by yebidaxiong on 2017/9/7.
 */

public class BaseNiceDialog extends DialogFragment{

    protected int layoutId;
    protected float dimAmount;
    protected boolean showBottom;
    public BaseNiceDialog setDimAmount(float dimAmount){
        this.dimAmount=dimAmount;
        return this;
    }
    public BaseNiceDialog setShowBottom(boolean showBottom){
        this.showBottom=showBottom;
        return this;
    }
    public BaseNiceDialog show(FragmentManager manager){
        super.show(manager,String.valueOf(System.currentTimeMillis()));
        return this;
    }
}
