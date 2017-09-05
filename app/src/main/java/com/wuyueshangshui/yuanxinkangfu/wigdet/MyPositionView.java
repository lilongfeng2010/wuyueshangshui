package com.wuyueshangshui.yuanxinkangfu.wigdet;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by lilfi on 2017/8/30.
 */

public class MyPositionView extends TextView {

    public MyPositionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }

    public interface OnMyClickListener{
        void onMyClick(AlertDialog dialog);
    }

    public void setOnMyClickListener(OnMyClickListener listener,AlertDialog dialog){
        listener.onMyClick(dialog);
    }

}
