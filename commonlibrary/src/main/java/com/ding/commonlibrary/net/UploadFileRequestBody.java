package com.ding.commonlibrary.net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by ding on 18/08/2017.
 */

public class UploadFileRequestBody extends RequestBody {
    private RequestBody mRequestBody;
    private UploadSubscriberWrapper fileUploadObserver;

    public UploadFileRequestBody(String json, UploadSubscriberWrapper fileUploadObserver) {
        this.mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        this.fileUploadObserver = fileUploadObserver;
    }


    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        //写入
        mRequestBody.writeTo(bufferedSink);
        //刷新
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();

    }

    protected final class CountingSink extends ForwardingSink {

        private long bytesWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            bytesWritten += byteCount;
            if (fileUploadObserver != null) {
                fileUploadObserver.callBackListener.onProgress((int) (bytesWritten*100 / contentLength()));
            }

        }

    }
}