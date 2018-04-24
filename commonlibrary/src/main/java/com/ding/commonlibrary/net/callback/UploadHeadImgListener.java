package com.ding.commonlibrary.net.callback;

/**
 * Created by ding on 18/08/2017.
 */

public interface UploadHeadImgListener {
    void onRequestProgress(long bytesWritten, long contentLength);
}
