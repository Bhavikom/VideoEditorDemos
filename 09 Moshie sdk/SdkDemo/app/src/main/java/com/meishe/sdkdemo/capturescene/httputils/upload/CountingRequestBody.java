package com.meishe.sdkdemo.capturescene.httputils.upload;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by CaoZhiChao on 2018/11/30 19:07
 */
public class CountingRequestBody extends RequestBody {
    protected RequestBody delegate;//请求体的代理
    private Listener mListener;//进度监听

    public CountingRequestBody(RequestBody delegate, Listener listener) {
        this.delegate = delegate;
        mListener = listener;
    }

    protected final class CountingSink extends ForwardingSink {
        private long byteWritten;//已经写入的大小

        private CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            byteWritten += byteCount;
            mListener.onReqProgress(byteWritten, contentLength());//每次写入触发回调函数
        }
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
//            return file.length();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink buffer = Okio.buffer(countingSink);
        delegate.writeTo(buffer);
        buffer.flush();
    }

    /////////////----------进度监听接口
    public interface Listener {
        void onReqProgress(long byteWritten, long contentLength);
    }
}
