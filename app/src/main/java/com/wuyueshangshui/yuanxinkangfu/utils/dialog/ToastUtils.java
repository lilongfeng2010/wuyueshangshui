package com.wuyueshangshui.yuanxinkangfu.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wuyueshangshui.yuanxinkangfu.R;

/**
 * Created by lilfi on 2017/8/21.
 */

public class ToastUtils {

    Context mContext;
    Toast mToast;

    public static void Toast(Context context,String toastContent){
        Toast.makeText(context,toastContent, Toast.LENGTH_SHORT).show();
    }

    public ToastUtils(Context mContext){
        this.mContext=mContext;
        mToast=new Toast(mContext);
        mToast=Toast.makeText(mContext,"",Toast.LENGTH_SHORT);
    }



    public static void showMiddleToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    /**
     * 航仔的代码
     */
    private static Dialog mDialog;
    private static View view;
    public static void set(Context context,String value){
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
    }


    public static void showLoadingToast(Context context ) {
        mDialog = new Dialog( context , R.style.Them_dialog);
        LayoutInflater inflater = LayoutInflater.from( context );
        view = inflater.inflate(R.layout.toast_loading,null);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public static void dismissLoadingToast(){
        if( mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }


}
