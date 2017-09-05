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

    public void showButtonToast(String msg){
        mToast.setText(msg);
        mToast.setGravity(Gravity.BOTTOM,0,mContext.getResources().getDimensionPixelOffset(R.dimen.toast_y));
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    public void showMiddleToast(int id) {
        mToast.setText(id);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    public void showMiddleToast(String msg) {
        mToast.setText(msg);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    public void showMiddleToast(String msg, int duration) {
        mToast.setText(msg);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(duration);
        mToast.show();
    }
    public void showMiddleToastLong(String msg) {
        mToast.setText(msg);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }
    public static void showMiddleToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void showMiddleToast(Context context, int message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    /**
     * 航仔的代码
     */
    static Toast toast;
    private static Dialog mDialog;
    private static AnimationDrawable mAnimationDrawable;
    private static View view;
    public static void set(Context context,String value){
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
    }
    public static void setCenter(Context context,String value){
        Toast toast =  Toast.makeText(context,value ,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    public static void showToast(Context context, String text, boolean isLongLength) {
        int length;
        if (isLongLength) {
            length = Toast.LENGTH_LONG;
        } else {
            length = Toast.LENGTH_SHORT;
        }
        if (toast == null) {
            toast = Toast.makeText(context, text, length);
        } else {
            toast.setText(text);
            toast.setDuration(length);
        }
        toast.show();
    }
    public static void showToastNet(Context context){
        set(context,"网络没有连接，请连接网络");
    }
    public static void showDialogToast(Context context, int resId ) {
        View view = InflateUtils.inflate(R.layout.control_waiting_indicator, null , false);
        TextView txt = (TextView) view.findViewById(R.id.waitingindicator_txt);
        txt.setText( resId );
        if( toast == null){
            toast = new Toast( context );
        }
        toast.setView(view);
        toast.setDuration( Toast.LENGTH_SHORT );
        toast.setGravity( Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void showDialogToast(Context context, String resId ) {
        View view = InflateUtils.inflate(R.layout.control_waiting_indicator, null , false);
        TextView txt = (TextView) view.findViewById(R.id.waitingindicator_txt);
        txt.setText( resId );
        if( toast == null){
            toast = new Toast( context );
        }
        toast.setView(view);
        toast.setDuration( Toast.LENGTH_SHORT );
        toast.setGravity( Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void showLoadingToast(Context context ) {
        mDialog = new Dialog( context , R.style.Them_dialog);
        LayoutInflater inflater = LayoutInflater.from( context );
        view = inflater.inflate(R.layout.toast_loading,null);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.show();
    }
    public static void changeshowLoadingToastText(String text){
        TextView textView = (TextView) view.findViewById(R.id.toast_loading_tv);
        textView.setText(text);
    }
    public static void dismissLoadingToast(){
        if( mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }


}
