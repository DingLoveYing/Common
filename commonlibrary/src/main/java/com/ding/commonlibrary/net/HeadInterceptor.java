package com.ding.commonlibrary.net;

import android.content.Context;


import com.ding.commonlibrary.utils.SpUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author ding
 * @date 14/08/2017
 */
public class HeadInterceptor implements Interceptor {
    private Context context;

    public HeadInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        request = request.newBuilder()
                .addHeader("token", SpUtils.getInstance(context).getString("token", ""))
                .build();
        return chain.proceed(request);
    }
}
