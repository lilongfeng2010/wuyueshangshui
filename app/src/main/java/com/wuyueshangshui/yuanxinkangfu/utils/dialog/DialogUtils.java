package com.wuyueshangshui.yuanxinkangfu.utils.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tencent.mm.opensdk.constants.Build;
import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.wigdet.MyPositionView;

/**
 * Created by lilfi on 2017/8/22.
 */

public class DialogUtils {
    //只有一个“确定”按钮的dialog
    public static void onePosition(Context context,String content){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view=View.inflate(context, R.layout.oneposition,null);
        ((TextView)view.findViewById(R.id.onePositionTV)).setText(content);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog=builder.create();
        dialog.show();
        ((TextView)view.findViewById(R.id.onePositionButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    //有“确定”和“取消”的dialog
    public static void twoButton(Context context, String content, View.OnClickListener negativeListener,View.OnClickListener positionListener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view=View.inflate(context, R.layout.twobutton,null);
        ((TextView)view.findViewById(R.id.twoButtonTV)).setText(content);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog mDialog=builder.create();
        mDialog.show();
        view.findViewById(R.id.TwoButtonNegative).setOnClickListener(negativeListener);
        view.findViewById(R.id.TwoButtonPosition).setOnClickListener(positionListener);
    }
}
