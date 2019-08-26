// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import java.io.FileNotFoundException;
import java.io.InputStream;
//import org.lasque.tusdk.core.utils.FileHelper;
import java.io.FileOutputStream;
import java.util.List;
import java.io.IOException;
import android.content.Context;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.ReflectUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.io.File;

public abstract class FileHttpResponseHandler extends ClearHttpResponseHandler
{
    protected final File file;
    protected final boolean append;
    protected final boolean renameIfExists;
    protected File frontendFile;
    
    public FileHttpResponseHandler(final File file) {
        this(file, false);
    }
    
    public FileHttpResponseHandler(final File file, final boolean b) {
        this(file, b, false);
    }
    
    public FileHttpResponseHandler(final File file, final boolean b, final boolean b2) {
        this(file, b, b2, false);
    }
    
    public FileHttpResponseHandler(final File file, final boolean append, final boolean renameIfExists, final boolean b) {
        super(b);
        ReflectUtils.asserts(file != null, "File passed into FileHttpResponseHandler constructor must not be null");
        if (!file.isDirectory() && !file.getParentFile().isDirectory()) {
            ReflectUtils.asserts(file.getParentFile().mkdirs(), "Cannot create parent directories for requested File location");
        }
        if (file.isDirectory() && !file.mkdirs()) {
            TLog.d("Cannot create directories for requested Directory location, might not be a problem", new Object[0]);
        }
        this.file = file;
        this.append = append;
        this.renameIfExists = renameIfExists;
    }
    
    public FileHttpResponseHandler(final Context context) {
        this.file = this.getTemporaryFile(context);
        this.append = false;
        this.renameIfExists = false;
    }
    
    public boolean deleteTargetFile() {
        return this.getTargetFile() != null && this.getTargetFile().delete();
    }
    
    protected File getTemporaryFile(final Context context) {
        ReflectUtils.asserts(context != null, "Tried creating temporary file without having Context");
        try {
            return File.createTempFile("temp_", "_handled", context.getCacheDir());
        }
        catch (IOException ex) {
            TLog.e("Cannot create temporary file: %s", ex);
            return null;
        }
    }
    
    protected File getOriginalFile() {
        ReflectUtils.asserts(this.file != null, "Target file is null, fatal!");
        return this.file;
    }
    
    public File getTargetFile() {
        if (this.frontendFile == null) {
            this.frontendFile = (this.getOriginalFile().isDirectory() ? this.getTargetFileByParsingURL() : this.getOriginalFile());
        }
        return this.frontendFile;
    }
    
    protected File getTargetFileByParsingURL() {
        ReflectUtils.asserts(this.getOriginalFile().isDirectory(), "Target file is not a directory, cannot proceed");
        ReflectUtils.asserts(this.getRequestURL() != null, "RequestURL is null, cannot proceed");
        final String string = this.getRequestURL().toString();
        final String substring = string.substring(string.lastIndexOf(47) + 1, string.length());
        final File file = new File(this.getOriginalFile(), substring);
        if (file.exists() && this.renameIfExists) {
            String format;
            if (!substring.contains(".")) {
                format = substring + " (%d)";
            }
            else {
                format = substring.substring(0, substring.lastIndexOf(46)) + " (%d)" + substring.substring(substring.lastIndexOf(46), substring.length());
            }
            int i = 0;
            File file2;
            while (true) {
                file2 = new File(this.getOriginalFile(), String.format(format, i));
                if (!file2.exists()) {
                    break;
                }
                ++i;
            }
            return file2;
        }
        return file;
    }
    
    @Override
    public final void onFailure(final int n, final List<HttpHeader> list, final byte[] array, final Throwable t) {
        this.onFailure(n, list, t, this.getTargetFile());
    }
    
    public abstract void onFailure(final int p0, final List<HttpHeader> p1, final Throwable p2, final File p3);
    
    @Override
    public final void onSuccess(final int n, final List<HttpHeader> list, final byte[] array) {
        this.onSuccess(n, list, this.getTargetFile());
    }
    
    public abstract void onSuccess(final int p0, final List<HttpHeader> p1, final File p2);
    
    protected byte[] getResponseData(final HttpEntity httpEntity) {
        if (httpEntity != null) {
            final InputStream content = httpEntity.getContent();
            final long contentLength = httpEntity.getContentLength();
             FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(this.getTargetFile(), this.append);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (content != null) {
                try {
                    final byte[] array = new byte[4096];
                    int n = 0;
                    int read;
                    while ((read = content.read(array)) != -1 && !Thread.currentThread().isInterrupted()) {
                        n += read;
                        fileOutputStream.write(array, 0, read);
                        this.sendProgressMessage(n, contentLength);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    FileHelper.safeClose(content);
                    try {
                        fileOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileHelper.safeClose(fileOutputStream);
                }
            }
        }
        return null;
    }
}
