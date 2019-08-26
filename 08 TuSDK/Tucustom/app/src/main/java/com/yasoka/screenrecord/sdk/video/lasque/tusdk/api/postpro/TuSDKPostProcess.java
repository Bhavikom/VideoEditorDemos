// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.postpro;

//import org.lasque.tusdk.api.TuSDKPostProcessJNI;
import android.text.TextUtils;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
//import org.lasque.tusdk.core.TuSdkContext;
import android.net.Uri;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.TuSDKPostProcessJNI;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;

//import org.lasque.tusdk.core.TuSdk;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.List;
import java.io.File;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;

public abstract class TuSDKPostProcess
{
    protected final boolean process(final TuSDKMediaDataSource tuSDKMediaDataSource, final File file, final List<PostProcessArg> list) {
        if (list == null || list.size() == 0) {
            TLog.e("%s : Please set input parameters.", new Object[] { this });
            return false;
        }
        if (tuSDKMediaDataSource == null || !tuSDKMediaDataSource.isValid()) {
            TLog.e("%s : Please set valid data source.", new Object[] { this });
            return false;
        }
        if (file == null) {
            TLog.e("%s :Please set a file output path.", new Object[] { this });
            return false;
        }
        if (tuSDKMediaDataSource.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
            TLog.e("%s :Please set a valid output.", new Object[] { this });
            return false;
        }
        final ArrayList<PostProcessArg> list2 = new ArrayList<PostProcessArg>();
        File file2 = null;
        if (tuSDKMediaDataSource.getFilePath() != null) {
            list2.add(new PostProcessArg("-i", tuSDKMediaDataSource.getFilePath()));
        }
        else {
            file2 = new File(TuSdk.getAppTempPath() + "/" + System.currentTimeMillis());
            this.a(tuSDKMediaDataSource.getFileUri(), file2);
            if (!file2.exists()) {
                return false;
            }
            list2.add(new PostProcessArg("-i", file2.getAbsolutePath()));
        }
        list2.addAll(list);
        list2.add(new PostProcessArg(null, file.getAbsolutePath()));
        final boolean process = process(list2);
        if (file2 != null) {
            file2.delete();
        }
        return process;
    }
    
    private void a(final Uri uri, final File file) {
        InputStream openInputStream = null;
        FilterOutputStream filterOutputStream = null;
        try {
            openInputStream = TuSdkContext.context().getContentResolver().openInputStream(uri);
            filterOutputStream = new BufferedOutputStream(new FileOutputStream(file, false));
            final byte[] b = new byte[1024];
            openInputStream.read(b);
            do {
                filterOutputStream.write(b);
            } while (openInputStream.read(b) != -1);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            try {
                if (openInputStream != null) {
                    openInputStream.close();
                }
                if (filterOutputStream != null) {
                    filterOutputStream.close();
                }
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }
        finally {
            try {
                if (openInputStream != null) {
                    openInputStream.close();
                }
                if (filterOutputStream != null) {
                    filterOutputStream.close();
                }
            }
            catch (IOException ex3) {
                ex3.printStackTrace();
            }
        }
    }
    
    protected boolean process(final ArrayList<PostProcessArg> list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        final ArrayList<String> list2 = new ArrayList<String>();
        list2.add("ffmpeg");
        for (int i = 0; i < list.size(); ++i) {
            final PostProcessArg postProcessArg = list.get(i);
            if (!TextUtils.isEmpty((CharSequence)postProcessArg.getKey())) {
                list2.add(list.get(i).getKey());
            }
            if (!TextUtils.isEmpty((CharSequence)postProcessArg.getValue())) {
                list2.add(list.get(i).getValue());
            }
        }
        return TuSDKPostProcessJNI.runVideoCommands(list2.toArray(new String[list2.size()]));
    }
    
    public static class PostProcessArg
    {
        private String a;
        private String b;
        
        public PostProcessArg(final String a, final String b) {
            this.a = a;
            this.b = b;
        }
        
        public String getKey() {
            return this.a;
        }
        
        public String getValue() {
            return this.b;
        }
    }
}
