package com.wuyueshangshui.yuanxinkangfu.utils.Images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by yebidaxiong on 2017/9/6.
 */

public class ImageManager {
    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath){
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(filePath,options);
        // Calculate inSampleSize
        options.inSampleSize=calculateInSampleSize(options,480,800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(filePath,options);

    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int height=options.outHeight;
        int width=options.outWidth;
        int inSampleSize=1;
        if (height>reqHeight || width>reqWidth){
            int heightRatio=Math.round((float) height/(float)reqHeight);
            int widthRatio=Math.round((float) width/(float)reqWidth);
            inSampleSize=heightRatio<widthRatio?heightRatio:widthRatio;
        }
        return inSampleSize;
    }

}
