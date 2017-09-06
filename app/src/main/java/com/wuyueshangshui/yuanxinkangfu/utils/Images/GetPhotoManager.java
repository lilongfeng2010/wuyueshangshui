package com.wuyueshangshui.yuanxinkangfu.utils.Images;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wuyueshangshui.yuanxinkangfu.R;
import com.wuyueshangshui.yuanxinkangfu.mainUI.fragment.mine.PersonInformationActivity;
import com.wuyueshangshui.yuanxinkangfu.utils.dialog.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yebidaxiong on 2017/9/6.
 * 获取照片的工具类
 */

public class GetPhotoManager {

    private static PopupWindow mPopupWindow;
    private static String mAppTag = "yuanxinkangfu";
    private static String strImgPath;
    private static Context mContext;
    private static Activity mActivity;
    public static void getPhoto(Context context,Activity activity){
        mContext=context;
        mActivity=activity;
        View contentView= LayoutInflater.from(context).inflate(R.layout.popupwindow,null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setContentView(contentView);
        LinearLayout photo= (LinearLayout) contentView.findViewById(R.id.popup_takephoto);
        LinearLayout album= (LinearLayout) contentView.findViewById(R.id.popup_album);
        TextView cancel= (TextView) contentView.findViewById(R.id.popup_cancel);
        setStrImgPath(makeImgFullPath());
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                takePhoto();
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
//                StartAblum();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        View rootView=LayoutInflater.from(context).inflate(R.layout.activity_person_information,null);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.popup_animation);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);


    }

    private static String makeImgFullPath() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        Date dt=new Date();
        String strDateTime=simpleDateFormat.format(dt);
        String img_full_path=getProductImagesDir()+"/"+strDateTime+".jpg";
        return img_full_path;
    }

    /**
     *获取SD卡上产品的图片目录
     */
    private static String getProductImagesDir() {
        String strDir=getProductDir()+"/images";
        File dirSDCard=new File(strDir);
        if (!dirSDCard.exists()){
            dirSDCard.mkdir();
        }
        return strDir;
    }

    /**
     *获取SD卡上产品的目录
     */
    private static String getProductDir() {
        String strDir=Environment.getExternalStorageDirectory()+"/"+mAppTag;
        File dirSDCard=new File(strDir);
        if (!dirSDCard.exists()){
            dirSDCard.mkdir();
        }
        if (!dirSDCard.exists()){
            strDir="/mnt/emmc/wanxiaoju";
            File dirInternal=new File(strDir);
            if (!dirInternal.exists()){
                dirInternal.mkdir();
            }
        }
        return strDir;
    }

    private static void setStrImgPath(String strImgPath) {
        GetPhotoManager.strImgPath=strImgPath;
    }

    public static String getStrImgPath() {
        return strImgPath;
    }

    private static void takePhoto() {
        String state= Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            File file=new File(getStrImgPath());
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            mActivity.startActivityForResult(intent,100);
        }else{
            ToastUtils.Toast(mContext,"请确认已经插入SD卡");
        }
    }
}
