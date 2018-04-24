package com.ding.commonlibrary.net;


import com.ding.commonlibrary.net.callback.UploadHeadImgListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ding on 18/08/2017.
 */

public class UpLoadProgressInterceptor implements Interceptor {
    private UploadHeadImgListener progressListener;

    public UpLoadProgressInterceptor(UploadHeadImgListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (originalRequest.body() == null) {
            return chain.proceed(originalRequest);
        }

        Request progressRequest = originalRequest.newBuilder()
                .method(originalRequest.method(),
                        new CountingRequestBody(originalRequest.body(), progressListener))
                .build();

        return chain.proceed(progressRequest);
    }
}
