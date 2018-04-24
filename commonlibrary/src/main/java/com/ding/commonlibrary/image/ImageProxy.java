package com.ding.commonlibrary.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.ding.commonlibrary.R;


import java.io.File;

/**
 * Created by cuieney on 10/08/2017.
 */

final class ImageProxy implements IImageLoader {

    private static final int HEAD_PLACEHOLDER = 0;//R.mipmap.default_head;
    private static final int IMAGE_PLACEHOLDER = 0;// R.mipmap.holder_img;
    private static final int ERROR = 0;// R.mipmap.error_img;

    @Override
    public void displayHeadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, File file, ImageView imageView) {
        Glide.with(context).load(file)
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, int resId, ImageView imageView) {
        Glide.with(context).load(resId)
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, Bitmap bitmap, ImageView imageView) {
        Glide.with(context).load(bitmap)
                .into(imageView);
    }

    @Override
    public void displayCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .into(imageView);
    }

    @Override
    public void displayCircleImage(Context context, File file, ImageView imageView) {
        Glide.with(context).load(file)
                .into(imageView);
    }

    @Override
    public void displayCircleImage(Context context, int resId, ImageView imageView) {
        Glide.with(context).load(resId)
                .into(imageView);
    }

    @Override
    public void displayCircleImage(Context context, Bitmap bitmap, ImageView imageView) {
        Glide.with(context).load(bitmap)
                .into(imageView);
    }
}
