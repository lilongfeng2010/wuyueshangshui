package com.wuyueshangshui.yuanxinkangfu.utils.NiceDialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wuyueshangshui.yuanxinkangfu.R;

/**
 * Created by yebidaxiong on 2017/9/7.
 */

public class ConfirmDialog extends BaseNiceDialog {
    private String mMessage;
    public static ConfirmDialog newInstance(){
        ConfirmDialog dialog=new ConfirmDialog();
        return dialog;
    }


    @Override
    public int intLayoutId() {
        return R.layout.confirm_layout;
    }

    public ConfirmDialog setMessage(String message){
        mMessage=message;
        return this;
    }

    @Override
    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
        holder.setText(R.id.confirm_layout_title,"提示");
        holder.setText(R.id.confirm_layout_message,mMessage);
        holder.setOnClickListener(R.id.confirm_layout_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
