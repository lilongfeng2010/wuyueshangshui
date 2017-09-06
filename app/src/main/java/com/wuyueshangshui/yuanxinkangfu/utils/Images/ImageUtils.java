package com.wuyueshangshui.yuanxinkangfu.utils.Images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.wuyueshangshui.yuanxinkangfu.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yebidaxiong on 2017/9/4.
 */

public class ImageUtils {
    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(filePath,options);
        // Calculate inSampleSize
        options.inSampleSize=calculateInSampleSize(options,480,800);
        //Decode bitmap with inSampleSize set
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(filePath,options);
    }


    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int height=options.outHeight;
        int width=options.outWidth;
        int inSampleSize=1;
        if (height>reqHeight||width>reqWidth){
            int heightRatio=Math.round((float) height/(float)reqHeight);
            int widthRatio=Math.round((float)width/(float)reqWidth);
            inSampleSize=heightRatio<widthRatio?heightRatio:widthRatio;
        }
        return inSampleSize;

    }
    //把bitmap转换成String
    public static String bitmapToString(String filePath){
        Bitmap bm=getSmallBitmap(filePath);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG,40,baos);
        byte[] b=baos.toByteArray();
        LogUtils.showe("======压缩后的大小=" + b.length);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    //把bitmap转换成String
    public static String bitmapToString2(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bitmap.compress(Bitmap.CompressFormat.JPEG,40,baos);
        byte[] b=baos.toByteArray();
        LogUtils.showe("======压缩后的大小=" + b.length);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }



    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

}
