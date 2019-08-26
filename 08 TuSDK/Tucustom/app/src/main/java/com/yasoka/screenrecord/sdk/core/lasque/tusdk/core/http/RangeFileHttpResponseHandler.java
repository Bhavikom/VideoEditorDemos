// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
//import org.lasque.tusdk.core.utils.TLog;
import java.io.File;

public abstract class RangeFileHttpResponseHandler extends FileHttpResponseHandler
{
    public static final int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    private long a;
    private boolean b;
    
    public RangeFileHttpResponseHandler(final File file) {
        super(file);
        this.a = 0L;
        this.b = false;
    }
    
    @Override
    public void sendResponseMessage(final HttpResponse httpResponse) {
        if (!Thread.currentThread().isInterrupted()) {
            final int responseCode = httpResponse.getResponseCode();
            if (responseCode == 416) {
                if (!Thread.currentThread().isInterrupted()) {
                    this.sendSuccessMessage(responseCode, httpResponse.getAllHeaders(), null);
                }
            }
            else if (responseCode >= 300) {
                if (!Thread.currentThread().isInterrupted()) {
                    this.sendFailureMessage(responseCode, httpResponse.getAllHeaders(), null, new HttpResponseException(responseCode, httpResponse.getResponseMessage()));
                }
            }
            else if (!Thread.currentThread().isInterrupted()) {
                final HttpHeader firstHeader = httpResponse.getFirstHeader("Content-Range");
                if (firstHeader == null) {
                    this.b = false;
                    this.a = 0L;
                }
                else {
                    TLog.w("%s : %s", "Content-Range", firstHeader.getValue());
                }
                this.sendSuccessMessage(responseCode, httpResponse.getAllHeaders(), this.getResponseData(httpResponse.getEntity()));
            }
        }
    }
    
    @Override
    protected byte[] getResponseData(final HttpEntity httpEntity) {
        if (httpEntity != null) {
            final InputStream content = httpEntity.getContent();
            final long n = httpEntity.getContentLength() + this.a;
             FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(this.getTargetFile(), this.b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (content != null) {
                try {
                    final byte[] array = new byte[4096];
                    int read;
                    while (this.a < n && (read = content.read(array)) != -1 && !Thread.currentThread().isInterrupted()) {
                        this.a += read;
                        fileOutputStream.write(array, 0, read);
                        this.sendProgressMessage(this.a, n);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        content.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return null;
    }
    
    public void updateRequestHeaders(final HttpUriRequest httpUriRequest) {
        if (this.file.exists() && this.file.canWrite()) {
            this.a = this.file.length();
        }
        if (this.a > 0L) {
            this.b = true;
            httpUriRequest.setHeader("Range", "bytes=" + this.a + "-");
        }
    }
}
