// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct;

import android.os.Build;
import android.media.MediaMetadataRetriever;
import java.io.File;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.FileDescriptor;
import android.content.Context;
import android.net.Uri;
import android.media.MediaDataSource;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaDataSource
{
    private MediaDataSource a;
    private Uri b;
    private Context c;
    private String d;
    private FileDescriptor e;
    private long f;
    private long g;
    private Map<String, String> h;
    private TuSdkMediaDataSourceType i;
    
    public static List<TuSdkMediaDataSource> create(final String... array) {
        final ArrayList<TuSdkMediaDataSource> list = new ArrayList<TuSdkMediaDataSource>();
        if (array == null || array.length == 0) {
            TLog.w("%s create List<TuSdkMediaDataSource> path is null", "TuSdkMediaDataSource");
            return list;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(new TuSdkMediaDataSource(array[i]));
        }
        return list;
    }
    
    public TuSdkMediaDataSource() {
        this.f = 0L;
        this.g = 0L;
    }
    
    public TuSdkMediaDataSource(final String path) {
        this.f = 0L;
        this.g = 0L;
        this.setPath(path);
    }
    
    public TuSdkMediaDataSource(final String s, final Map<String, String> map) {
        this.f = 0L;
        this.g = 0L;
        this.setPath(s, map);
    }
    
    public TuSdkMediaDataSource(final Context context, final Uri uri) {
        this.f = 0L;
        this.g = 0L;
        this.setUri(context, uri);
    }
    
    public TuSdkMediaDataSource(final Context context, final Uri uri, final Map<String, String> map) {
        this.f = 0L;
        this.g = 0L;
        this.setUri(context, uri, map);
    }
    
    public TuSdkMediaDataSource(final MediaDataSource mediaDataSource) {
        this.f = 0L;
        this.g = 0L;
        this.setMediaDataSource(mediaDataSource);
    }
    
    public TuSdkMediaDataSource(final FileDescriptor fileDescriptor, final long n, final long n2) {
        this.f = 0L;
        this.g = 0L;
        this.setFileDescriptor(fileDescriptor, n, n2);
    }
    
    public boolean isValid() {
        switch (this.getMediaDataType().ordinal()) {
            case 1: {
                if (this.getMediaDataSource() != null) {
                    return true;
                }
            }
            case 2: {
                if (this.getFileDescriptor() != null) {
                    return true;
                }
            }
            case 3: {
                if (new File(this.getPath()).exists()) {
                    return true;
                }
            }
            case 4: {
                if (this.getUri() != null) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
    
    public MediaMetadataRetriever getMediaMetadataRetriever() {
        final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        switch (this.getMediaDataType().ordinal()) {
            case 4: {
                mediaMetadataRetriever.setDataSource(this.getContext(), this.getUri());
                return mediaMetadataRetriever;
            }
            case 3: {
                if (this.getRequestHeaders() == null || Build.VERSION.SDK_INT <= 13) {
                    mediaMetadataRetriever.setDataSource(this.getPath());
                }
                else {
                    mediaMetadataRetriever.setDataSource(this.getPath(), (Map)this.getRequestHeaders());
                }
                return mediaMetadataRetriever;
            }
            case 2: {
                if (this.getFileDescriptorLength() == 0L) {
                    mediaMetadataRetriever.setDataSource(this.getFileDescriptor());
                }
                else {
                    mediaMetadataRetriever.setDataSource(this.getFileDescriptor(), this.getFileDescriptorOffset(), this.getFileDescriptorLength());
                }
                return mediaMetadataRetriever;
            }
            case 1: {
                if (Build.VERSION.SDK_INT >= 23) {
                    mediaMetadataRetriever.setDataSource(this.getMediaDataSource());
                    return mediaMetadataRetriever;
                }
                TLog.e("%s if use MediaDataSource,current SDK VERSION must >= 23 ", "TuSdkMediaDataSource");
                return null;
            }
            default: {
                TLog.e("%s unkwon MediaDataType", "TuSdkMediaDataSource");
                return null;
            }
        }
    }
    
    public MediaDataSource getMediaDataSource() {
        return this.a;
    }
    
    public void setMediaDataSource(final MediaDataSource a) {
        this.i = TuSdkMediaDataSourceType.MEDIA_DATA_SOURCE;
        this.a = a;
    }
    
    public Uri getUri() {
        return this.b;
    }
    
    public void setUri(final Context context, final Uri uri) {
        this.setUri(context, uri, null);
    }
    
    public void setUri(final Context c, final Uri b, final Map<String, String> h) {
        this.i = TuSdkMediaDataSourceType.URI;
        this.c = c;
        this.b = b;
        this.h = h;
    }
    
    public Context getContext() {
        return this.c;
    }
    
    public String getPath() {
        return this.d;
    }
    
    public void setPath(final String s) {
        this.setPath(s, null);
    }
    
    public void setPath(final String d, final Map<String, String> h) {
        this.i = TuSdkMediaDataSourceType.PATH;
        this.d = d;
        this.h = h;
    }
    
    public FileDescriptor getFileDescriptor() {
        return this.e;
    }
    
    public void setFileDescriptor(final FileDescriptor e, final long f, final long g) {
        this.i = TuSdkMediaDataSourceType.FILE_DESCRIPTOR;
        this.e = e;
        this.f = f;
        this.g = g;
    }
    
    public long getFileDescriptorOffset() {
        return this.f;
    }
    
    public void setFileDescriptorOffset(final long f) {
        this.f = f;
    }
    
    public long getFileDescriptorLength() {
        return this.g;
    }
    
    public void setFileDescriptorLength(final long g) {
        this.g = g;
    }
    
    public Map<String, String> getRequestHeaders() {
        return this.h;
    }
    
    public void setRequestHeaders(final Map<String, String> h) {
        this.h = h;
    }
    
    public TuSdkMediaDataSourceType getMediaDataType() {
        return this.i;
    }
    
    public void setMediaDataType(final TuSdkMediaDataSourceType i) {
        this.i = i;
    }
    
    public void deleted() {
        if (this.isValid()) {
            return;
        }
        File file = null;
        switch (this.i.ordinal()) {
            case 3: {
                file = new File(this.d);
                break;
            }
        }
        if (file == null || !file.isFile()) {
            return;
        }
        file.delete();
    }
    
    @Override
    public String toString() {
        final StringBuffer append = new StringBuffer("TuSdkMediaDataSource").append("{ \n");
        if (this.a != null) {
            append.append("MediaDataSource: ").append(this.a).append(", \n");
        }
        if (this.b != null) {
            append.append("Uri: ").append(this.b).append(", \n");
        }
        if (this.c != null) {
            append.append("Context: ").append(this.c).append(", \n");
        }
        if (this.d != null) {
            append.append("Path: ").append(this.d).append(", \n");
        }
        if (this.e != null) {
            append.append("FileDescriptor: ").append(this.e).append(", \n");
            append.append("FileDescriptorOffset: ").append(this.f).append(", \n");
            append.append("FileDescriptorLength: ").append(this.g).append(", \n");
        }
        if (this.h != null) {
            append.append("RequestHeaders: ").append(this.h).append(", \n");
        }
        append.append("MediaDataType: ").append(this.i).append(", \n");
        append.append("}");
        return append.toString();
    }
    
    public enum TuSdkMediaDataSourceType
    {
        URI, 
        PATH, 
        MEDIA_DATA_SOURCE, 
        FILE_DESCRIPTOR;
    }
}
