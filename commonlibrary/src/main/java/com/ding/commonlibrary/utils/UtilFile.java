package com.ding.commonlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UtilFile {
    public static final String TAG = "UtilFile";
    private static final String PATH_CACHE_IMAGES = "/images";

    public static String getSaveImagePath(Context context) {
        String path = getCachePath(context) + PATH_CACHE_IMAGES;
        createFileDir(path);
        return path;
    }

    /**
     * 缓存目录
     */
    public static String getCachePath(Context context) {
        String path = getCanUseFileRootPath() + "/" + context.getPackageName();
        createFileDir(path);
        return path;
    }

    /**
     * 获取可用的文件根路径
     *
     * @return 如果存在SDCard，则返回SDCard根路径，否则返回手机内部存储根路径
     */
    private static String getCanUseFileRootPath() {
        String path;
        if (isExistSDCard()) {
            path = Environment.getExternalStorageDirectory().getPath();
        } else {
            path = Environment.getRootDirectory().getPath();
        }
        return path;
    }

    /**
     * 是否存在SDCard
     *
     * @return true 存在
     */
    public static boolean isExistSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return true 创建成果
     */
    public static boolean createFileDir(String path) {
        File file = new File(path);
        return file.exists() || file.mkdirs();
    }

    /**
     * 创建空文件
     *
     * @param path     路径
     * @param fileName 文件名
     * @return true 创建成功
     */
    public static boolean createFile(String path, String fileName) {
        if (createFileDir(path)) {
            File file = new File(path + "/" + fileName);
            try {
                return file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 保存Bitmap到指定文件
     *
     * @param b              Bitmap
     * @param targetFilePath 文件路径
     */
    public static void saveBitmapByPath(Bitmap b, String targetFilePath) {
        if (b != null) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(targetFilePath);
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (IOException ex) {
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }
    }

    //Delete File
    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return true;
    }

    /**
     * Read bytes from InputStream
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @link http://stackoverflow.com/questions/2436385/android-getting-from-a-uri-to-an-inputstream-to-a-byte-array
     */
    public static byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

//    public static File getCacheFolder(Context context) {
//        File cacheDir = null;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            cacheDir = new File(Environment.getExternalStorageDirectory(), "cache");
//            if (!cacheDir.isDirectory()) {
//                cacheDir.mkdirs();
//            }
//        }
//
//        if (!cacheDir.isDirectory()) {
//            cacheDir = context.getCacheDir(); //get system cache folder
//        }
//
//        return cacheDir;
//    }

    public static File downloadAndCacheFile(Context context, String url) {
        URL fileURL = null;
        try {
            fileURL = new URL(url);

            Log.d(TAG, String.format("Start downloading file at %s.", url));

            HttpURLConnection connection = (HttpURLConnection) fileURL.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, String.format("Failed to download file from %s, response code: %d.", url, connection.getResponseCode()));
                return null;
            }

            InputStream inputStream = connection.getInputStream();

            File cacheDir = new File(getCachePath(context));
            File cacheFile = new File(cacheDir, url.substring(url.lastIndexOf("/") + 1));
            FileOutputStream outputStream = new FileOutputStream(cacheFile);

            byte buffer[] = new byte[4096];
            int dataSize;
            while ((dataSize = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, dataSize);
            }
            outputStream.close();

            Log.d(TAG, String.format("File was downloaded and saved at %s.", cacheFile.getAbsolutePath()));

            return cacheFile;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
