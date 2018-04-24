package com.ding.commonlibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageHelper {

    public static void compressBmpToFile(Bitmap bmp, File file) {
        if (bmp == null) return;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = 80;//个人喜欢从80开始,
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
            while (baos.toByteArray().length / 1024 > 100 && options != 10) {
                baos.reset();
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 10;
            }
            bmp.recycle();
            byte[] result = baos.toByteArray();
            baos.close();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(result);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmap(String url) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        options.inSampleSize = calculateInSampleSize(options, 200, 200);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(url, options);
    }

    /**
     * 计算出压缩比
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //通过参数options来获取真实图片的宽、高
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;//初始值是没有压缩的
        if (width > reqWidth || height > reqHeight) {
            //计算出原始宽与现有宽，原始高与现有高的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            //选出两个比率中的较小值，这样的话能够保证图片显示完全
            inSampleSize = widthRatio < heightRatio ? widthRatio : heightRatio;
        }
        System.out.println("压缩比:  " + inSampleSize);
        return inSampleSize;
    }

    //将View转为Bitmap
    public static Bitmap getBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
