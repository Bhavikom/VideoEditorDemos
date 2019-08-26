// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.io.InputStream;
import java.io.IOException;
//import org.lasque.tusdk.core.utils.FileHelper;
import java.io.ByteArrayOutputStream;
//import org.lasque.tusdk.core.utils.ReflectUtils;
import android.os.Message;
//import org.lasque.tusdk.core.utils.TLog;
import java.lang.ref.WeakReference;
import android.os.Looper;
import java.util.List;
import java.net.URL;
import android.os.Handler;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public abstract class ClearHttpResponseHandler implements ResponseHandlerInterface
{
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String UTF8_BOM = "\ufeff";
    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int START_MESSAGE = 2;
    protected static final int FINISH_MESSAGE = 3;
    protected static final int PROGRESS_MESSAGE = 4;
    protected static final int RETRY_MESSAGE = 5;
    protected static final int CANCEL_MESSAGE = 6;
    protected static final int BUFFER_SIZE = 4096;
    private String a;
    private Handler b;
    private boolean c;
    private boolean d;
    private URL e;
    private List<HttpHeader> f;
    private Looper g;
    private WeakReference<Object> h;
    
    public ClearHttpResponseHandler() {
        this(null);
    }
    
    public ClearHttpResponseHandler(final Looper looper) {
        this.a = "UTF-8";
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = new WeakReference<Object>(null);
        this.g = ((looper == null) ? Looper.myLooper() : looper);
        this.setUseSynchronousMode(false);
        this.setUsePoolThread(false);
    }
    
    public ClearHttpResponseHandler(final boolean usePoolThread) {
        this.a = "UTF-8";
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = new WeakReference<Object>(null);
        this.setUsePoolThread(usePoolThread);
        if (!this.getUsePoolThread()) {
            this.g = Looper.myLooper();
            this.setUseSynchronousMode(false);
        }
    }
    
    @Override
    public Object getTag() {
        return this.h.get();
    }
    
    @Override
    public void setTag(final Object referent) {
        this.h = new WeakReference<Object>(referent);
    }
    
    @Override
    public URL getRequestURL() {
        return this.e;
    }
    
    @Override
    public void setRequestURL(final URL e) {
        this.e = e;
    }
    
    @Override
    public List<HttpHeader> getRequestHeaders() {
        return this.f;
    }
    
    @Override
    public void setRequestHeaders(final List<HttpHeader> f) {
        this.f = f;
    }
    
    @Override
    public boolean getUseSynchronousMode() {
        return this.c;
    }
    
    @Override
    public void setUseSynchronousMode(boolean c) {
        if (!c && this.g == null) {
            c = true;
            TLog.w("Current thread has not called Looper.prepare(). Forcing synchronous mode.", new Object[0]);
        }
        if (!c && this.b == null) {
            this.b = new ResponderHandler(this, this.g);
        }
        else if (c && this.b != null) {
            this.b = null;
        }
        this.c = c;
    }
    
    @Override
    public boolean getUsePoolThread() {
        return this.d;
    }
    
    @Override
    public void setUsePoolThread(final boolean d) {
        if (d) {
            this.g = null;
            this.b = null;
        }
        this.d = d;
    }
    
    public String getCharset() {
        return (this.a == null) ? "UTF-8" : this.a;
    }
    
    public void setCharset(final String a) {
        this.a = a;
    }
    
    public void onProgress(final long l, final long i) {
        TLog.i("Progress %d from %d (%2.0f%%)", l, i, (i > 0L) ? (l * 1.0 / i * 100.0) : -1.0);
    }
    
    public void onStart() {
    }
    
    public void onFinish() {
    }
    
    @Override
    public void onPreProcessResponse(final ResponseHandlerInterface responseHandlerInterface, final HttpResponse httpResponse) {
    }
    
    @Override
    public void onPostProcessResponse(final ResponseHandlerInterface responseHandlerInterface, final HttpResponse httpResponse) {
    }
    
    public abstract void onSuccess(final int p0, final List<HttpHeader> p1, final byte[] p2);
    
    public abstract void onFailure(final int p0, final List<HttpHeader> p1, final byte[] p2, final Throwable p3);
    
    public void onRetry(final int i) {
        TLog.d("Request retry no. %d", i);
    }
    
    public void onCancel() {
        TLog.d("Request got cancelled", new Object[0]);
    }
    
    public void onUserException(final Throwable cause) {
        TLog.e("User-space exception detected! : %s", cause);
        throw new RuntimeException(cause);
    }
    
    @Override
    public final void sendProgressMessage(final long l, final long i) {
        this.sendMessage(this.obtainMessage(4, new Object[] { l, i }));
    }
    
    @Override
    public final void sendSuccessMessage(final int i, final List<HttpHeader> list, final byte[] array) {
        this.sendMessage(this.obtainMessage(0, new Object[] { i, list, array }));
    }
    
    @Override
    public final void sendFailureMessage(final int i, final List<HttpHeader> list, final byte[] array, final Throwable t) {
        this.sendMessage(this.obtainMessage(1, new Object[] { i, list, array, t }));
    }
    
    @Override
    public final void sendStartMessage() {
        this.sendMessage(this.obtainMessage(2, null));
    }
    
    @Override
    public final void sendFinishMessage() {
        this.sendMessage(this.obtainMessage(3, null));
    }
    
    @Override
    public final void sendRetryMessage(final int i) {
        this.sendMessage(this.obtainMessage(5, new Object[] { i }));
    }
    
    @Override
    public final void sendCancelMessage() {
        this.sendMessage(this.obtainMessage(6, null));
    }
    
    protected void handleMessage(final Message message) {
        try {
            switch (message.what) {
                case 0: {
                    final Object[] array = (Object[])message.obj;
                    if (array != null && array.length >= 3) {
                        this.onSuccess((int)array[0], (List<HttpHeader>)array[1], (byte[])array[2]);
                        break;
                    }
                    TLog.e("SUCCESS_MESSAGE didn't got enough params", new Object[0]);
                    break;
                }
                case 1: {
                    final Object[] array2 = (Object[])message.obj;
                    if (array2 != null && array2.length >= 4) {
                        this.onFailure((int)array2[0], (List<HttpHeader>)array2[1], (byte[])array2[2], (Throwable)array2[3]);
                        break;
                    }
                    TLog.e("FAILURE_MESSAGE didn't got enough params", new Object[0]);
                    break;
                }
                case 2: {
                    this.onStart();
                    break;
                }
                case 3: {
                    this.onFinish();
                    break;
                }
                case 4: {
                    final Object[] array3 = (Object[])message.obj;
                    if (array3 != null && array3.length >= 2) {
                        try {
                            this.onProgress((long)array3[0], (long)array3[1]);
                        }
                        catch (Throwable t) {
                            TLog.e("custom onProgress contains an error: %s", t);
                        }
                        break;
                    }
                    TLog.e("PROGRESS_MESSAGE didn't got enough params", new Object[0]);
                    break;
                }
                case 5: {
                    final Object[] array4 = (Object[])message.obj;
                    if (array4 != null && array4.length == 1) {
                        this.onRetry((int)array4[0]);
                        break;
                    }
                    TLog.e("RETRY_MESSAGE didn't get enough params", new Object[0]);
                    break;
                }
                case 6: {
                    this.onCancel();
                    break;
                }
            }
        }
        catch (Throwable t2) {
            this.onUserException(t2);
        }
    }
    
    protected void sendMessage(final Message message) {
        if (this.getUseSynchronousMode() || this.b == null) {
            this.handleMessage(message);
        }
        else if (!Thread.currentThread().isInterrupted()) {
            ReflectUtils.asserts(this.b != null, "handler should not be null!");
            this.b.sendMessage(message);
        }
    }
    
    protected void postRunnable(final Runnable runnable) {
        if (runnable != null) {
            if (this.getUseSynchronousMode() || this.b == null) {
                runnable.run();
            }
            else {
                this.b.post(runnable);
            }
        }
    }
    
    protected Message obtainMessage(final int n, final Object o) {
        return Message.obtain(this.b, n, o);
    }
    
    @Override
    public void sendResponseMessage(final HttpResponse httpResponse) {
        if (!Thread.currentThread().isInterrupted()) {
            final int responseCode = httpResponse.getResponseCode();
            final byte[] responseData = this.getResponseData(httpResponse.getEntity());
            if (!Thread.currentThread().isInterrupted()) {
                if (responseCode >= 300) {
                    this.sendFailureMessage(responseCode, httpResponse.getAllHeaders(), responseData, new HttpResponseException(responseCode, httpResponse.getResponseMessage()));
                }
                else {
                    this.sendSuccessMessage(responseCode, httpResponse.getAllHeaders(), responseData);
                }
            }
        }
    }
    
    byte[] getResponseData(final HttpEntity httpEntity) {
        byte[] byteArray = null;
        if (httpEntity != null) {
            final InputStream content = httpEntity.getContent();
            if (content != null) {
                final long contentLength = httpEntity.getContentLength();
                if (contentLength > 2147483647L) {
                    throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
                }
                try {
                    ByteArrayOutputStream byteArrayOutputStream = null;
                    try {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        final byte[] array = new byte[4096];
                        long n = 0L;
                        int read;
                        while ((read = content.read(array)) != -1 && !Thread.currentThread().isInterrupted()) {
                            n += read;
                            byteArrayOutputStream.write(array, 0, read);
                            this.sendProgressMessage(n, (contentLength <= 0L) ? 1L : contentLength);
                        }
                        byteArray = byteArrayOutputStream.toByteArray();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        FileHelper.safeClose(byteArrayOutputStream);
                        FileHelper.safeClose(content);
                        ClearHttpClient.endEntityViaReflection(httpEntity);
                    }
                }
                catch (OutOfMemoryError outOfMemoryError) {
                    System.gc();
                    try {
                        throw new IOException("File too large to fit into available memory");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return byteArray;
    }
    
    private static class ResponderHandler extends Handler
    {
        private final ClearHttpResponseHandler a;
        
        ResponderHandler(final ClearHttpResponseHandler a, final Looper looper) {
            super(looper);
            this.a = a;
        }
        
        public void handleMessage(final Message message) {
            this.a.handleMessage(message);
        }
    }
}
