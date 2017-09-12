package com.wuyueshangshui.yuanxinkangfu.utils.NiceDialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.utils.ScreenUtils;

/**
 * Created by yebidaxiong on 2017/9/7.
 */

public abstract class BaseNiceDialog extends DialogFragment{
    private int margin;//左右边距
    private int width;//宽度
    private int height;//高度
    private int animStyle;
    protected int layoutId;
    protected float dimAmount=0.5f;//灰度深浅
    protected boolean showBottom;//是否底部显示
    protected boolean outCancel=true;//是否点击外部取消

    public abstract int intLayoutId();

    public abstract void convertView(ViewHolder holder,BaseNiceDialog dialog);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.NiceDialog);
        layoutId=intLayoutId();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(layoutId,container,false);
        convertView(ViewHolder.create(view),this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    private void initParams() {
        Window window=getDialog().getWindow();
        if (window!=null){
            WindowManager.LayoutParams lp=window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount=dimAmount;
            //是否在底部显示
            if (showBottom){
                lp.gravity= Gravity.BOTTOM;
                if (animStyle==0){
                    //如果没有设置动画，使用默认动画
                    animStyle= R.style.DefaultNiceDialogAnimation;
                }
            }
            //设置dialog宽度
            if (width==0){
                lp.width=ScreenUtils.getScreenWidth(getContext())-2*ScreenUtils.dp2px(getContext(),margin);
            }else{
                lp.width=ScreenUtils.dp2px(getContext(),width);
            }

            //设置dialog高度
            if (height==0){
                lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
            }else{
                lp.height= ScreenUtils.dp2px(getContext(),height);
            }
            //设置dialog进入、退出的动画
            window.setWindowAnimations(animStyle);
            window.setAttributes(lp);
        }
        setCancelable(outCancel);

    }
    public BaseNiceDialog setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public BaseNiceDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    public BaseNiceDialog setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    public BaseNiceDialog setHeight(int height) {
        this.height = height;
        return this;
    }

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
