package com.ding.commonlibrary.net;


import android.app.Application;

import com.ding.commonlibrary.BuildConfig;
import com.ding.commonlibrary.MainApp;
import com.ding.commonlibrary.utils.SpUtils;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ding
 * @date 2017/3/27
 */
public class RetrofitHelper {

    private final OkHttpClient mClient;
    private Retrofit mRetrofit;

    private OkHttpClient provideClient(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);//使用自定义的Log拦截器
        }

//        File cacheFile = new File(Constant.NETWORK_CACHE_PATH);
        File cacheFile = new File("cache_path");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 1);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetWorkUtil.isNetworkConnected((Application) MainApp.getInstance())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetWorkUtil.isNetworkConnected((Application) MainApp.getInstance())) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };

        //https
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = (hostname, sslSession) -> true;


        Interceptor apikey = chain -> {
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("token", SpUtils.getInstance(MainApp.getInstance()).getString("token", ""))
                    .build();
            return chain.proceed(request);
        };
        //设置统一的请求头部参数
        builder.addInterceptor(apikey);

        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //设置https
//        builder.sslSocketFactory(sslContext.getSocketFactory());
        builder.sslSocketFactory(sslContext.getSocketFactory(), xtm);
        builder.hostnameVerifier(DO_NOT_VERIFY);

        return builder.build();

    }

    private Retrofit createRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                //base url
                .baseUrl("")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Api getApi() {
        if (mRetrofit == null) {
            mRetrofit = createRetrofit(mClient);
        }
        return mRetrofit.create(Api.class);
    }


    private static class RetrofitHelperHolder {
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }

    private RetrofitHelper() {
        OkHttpClient client = new OkHttpClient();
        mClient = provideClient(client.newBuilder());
    }

    public static final RetrofitHelper getInstance() {
        return RetrofitHelper.RetrofitHelperHolder.INSTANCE;
    }

}
