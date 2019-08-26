// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Locale;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;

public class RequestParams implements Serializable {
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String APPLICATION_JSON = "application/json";
    protected final ConcurrentHashMap<String, String> mUrlParams;
    protected final ConcurrentHashMap<String, RequestParams.StreamWrapper> mStreamParams;
    protected final ConcurrentHashMap<String, RequestParams.FileWrapper> mFileParams;
    protected final ConcurrentHashMap<String, List<RequestParams.FileWrapper>> mFileArrayParams;
    protected final ConcurrentHashMap<String, Object> mUrlParamsWithObjects;
    protected boolean mIsRepeatable;
    protected boolean mForceMultipartEntity;
    protected boolean mUseJsonStreamer;
    protected String mElapsedFieldInJsonStreamer;
    protected boolean mAutoCloseInputStreams;
    protected String mContentEncoding;

    public RequestParams() {
        this((Map)null);
    }

    public RequestParams(Map<String, String> var1) {
        this.mUrlParams = new ConcurrentHashMap();
        this.mStreamParams = new ConcurrentHashMap();
        this.mFileParams = new ConcurrentHashMap();
        this.mFileArrayParams = new ConcurrentHashMap();
        this.mUrlParamsWithObjects = new ConcurrentHashMap();
        this.mForceMultipartEntity = false;
        this.mElapsedFieldInJsonStreamer = "_elapsed";
        this.mContentEncoding = "UTF-8";
        if (var1 != null) {
            Iterator var2 = var1.entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry var3 = (Map.Entry)var2.next();
                this.put((String)var3.getKey(), (String)var3.getValue());
            }
        }

    }

    public RequestParams(final String var1, final String var2) {
        this((Map)(new HashMap<String, String>() {
            {
                this.put(var1, var2);
            }
        }));
    }

    public RequestParams(Object... var1) {
        this.mUrlParams = new ConcurrentHashMap();
        this.mStreamParams = new ConcurrentHashMap();
        this.mFileParams = new ConcurrentHashMap();
        this.mFileArrayParams = new ConcurrentHashMap();
        this.mUrlParamsWithObjects = new ConcurrentHashMap();
        this.mForceMultipartEntity = false;
        this.mElapsedFieldInJsonStreamer = "_elapsed";
        this.mContentEncoding = "UTF-8";
        int var2 = var1.length;
        if (var2 % 2 != 0) {
            throw new IllegalArgumentException("Supplied arguments must be even");
        } else {
            for(int var3 = 0; var3 < var2; var3 += 2) {
                String var4 = String.valueOf(var1[var3]);
                String var5 = String.valueOf(var1[var3 + 1]);
                this.put(var4, var5);
            }

        }
    }

    public void setContentEncoding(String var1) {
        if (var1 != null) {
            this.mContentEncoding = var1;
        } else {
            TLog.d("setContentEncoding called with null attribute", new Object[0]);
        }

    }

    public void setForceMultipartEntityContentType(boolean var1) {
        this.mForceMultipartEntity = var1;
    }

    public void put(String var1, String var2) {
        if (var1 != null && var2 != null) {
            this.mUrlParams.put(var1, var2);
        }

    }

    public void put(String var1, File[] var2) {
        this.put(var1, (File[])var2, (String)null, (String)null);
    }

    public void put(String var1, File[] var2, String var3, String var4) {
        if (var1 != null) {
            ArrayList var5 = new ArrayList();
            File[] var6 = var2;
            int var7 = var2.length;
            int var8 = 0;

            while(true) {
                if (var8 >= var7) {
                    this.mFileArrayParams.put(var1, var5);
                    break;
                }

                File var9 = var6[var8];
                if (var9 == null || !var9.exists()) {
                    try {
                        throw new FileNotFoundException();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                var5.add(new RequestParams.FileWrapper(var9, var3, var4));
                ++var8;
            }
        }

    }

    public void put(String var1, File var2) {
        this.put(var1, (File)var2, (String)null, (String)null);
    }

    public void put(String var1, String var2, File var3) {
        this.put(var1, (File)var3, (String)null, var2);
    }

    public void put(String var1, File var2, String var3) {
        this.put(var1, (File)var2, var3, (String)null);
    }

    public void put(String var1, File var2, String var3, String var4) {
        if (var2 != null && var2.exists()) {
            if (var1 != null) {
                this.mFileParams.put(var1, new RequestParams.FileWrapper(var2, var3, var4));
            }

        } else {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void put(String var1, InputStream var2) {
        this.put(var1, (InputStream)var2, (String)null);
    }

    public void put(String var1, InputStream var2, String var3) {
        this.put(var1, (InputStream)var2, var3, (String)null);
    }

    public void put(String var1, InputStream var2, String var3, String var4) {
        this.put(var1, var2, var3, var4, this.mAutoCloseInputStreams);
    }

    public void put(String var1, InputStream var2, String var3, String var4, boolean var5) {
        if (var1 != null && var2 != null) {
            this.mStreamParams.put(var1, RequestParams.StreamWrapper.a(var2, var3, var4, var5));
        }

    }

    public void put(String var1, Object var2) {
        if (var1 != null && var2 != null) {
            this.mUrlParamsWithObjects.put(var1, var2);
        }

    }

    public void put(String var1, int var2) {
        if (var1 != null) {
            this.mUrlParams.put(var1, String.valueOf(var2));
        }

    }

    public void put(String var1, long var2) {
        if (var1 != null) {
            this.mUrlParams.put(var1, String.valueOf(var2));
        }

    }

    public void add(String var1, String var2) {
        if (var1 != null && var2 != null) {
            Object var3 = this.mUrlParamsWithObjects.get(var1);
            if (var3 == null) {
                var3 = new HashSet();
                this.put(var1, var3);
            }

            if (var3 instanceof List) {
                ((List)var3).add(var2);
            } else if (var3 instanceof Set) {
                ((Set)var3).add(var2);
            }
        }

    }

    public void remove(String var1) {
        this.mUrlParams.remove(var1);
        this.mStreamParams.remove(var1);
        this.mFileParams.remove(var1);
        this.mUrlParamsWithObjects.remove(var1);
        this.mFileArrayParams.remove(var1);
    }

    public boolean has(String var1) {
        return this.mUrlParams.get(var1) != null || this.mStreamParams.get(var1) != null || this.mFileParams.get(var1) != null || this.mUrlParamsWithObjects.get(var1) != null || this.mFileArrayParams.get(var1) != null;
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        Iterator var2 = this.mUrlParams.entrySet().iterator();

        Map.Entry var3;
        while(var2.hasNext()) {
            var3 = (Map.Entry)var2.next();
            if (var1.length() > 0) {
                var1.append("&");
            }

            var1.append((String)var3.getKey());
            var1.append("=");
            var1.append((String)var3.getValue());
        }

        var2 = this.mStreamParams.entrySet().iterator();

        while(var2.hasNext()) {
            var3 = (Map.Entry)var2.next();
            if (var1.length() > 0) {
                var1.append("&");
            }

            var1.append((String)var3.getKey());
            var1.append("=");
            var1.append("STREAM");
        }

        var2 = this.mFileParams.entrySet().iterator();

        while(var2.hasNext()) {
            var3 = (Map.Entry)var2.next();
            if (var1.length() > 0) {
                var1.append("&");
            }

            var1.append((String)var3.getKey());
            var1.append("=");
            var1.append("FILE");
        }

        var2 = this.mFileArrayParams.entrySet().iterator();

        while(var2.hasNext()) {
            var3 = (Map.Entry)var2.next();
            if (var1.length() > 0) {
                var1.append("&");
            }

            var1.append((String)var3.getKey());
            var1.append("=");
            var1.append("FILES(SIZE=").append(((List)var3.getValue()).size()).append(")");
        }

        List var5 = this.a((String)null, this.mUrlParamsWithObjects);
        Iterator var6 = var5.iterator();

        while(var6.hasNext()) {
            URLEncodedUtils.BasicNameValuePair var4 = (URLEncodedUtils.BasicNameValuePair)var6.next();
            if (var1.length() > 0) {
                var1.append("&");
            }

            var1.append(var4.getName());
            var1.append("=");
            var1.append(var4.getValue());
        }

        return var1.toString();
    }

    public String toPairString() {
        ConcurrentHashMap var1 = new ConcurrentHashMap();
        Iterator var2 = this.mUrlParams.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            var1.put(var3.getKey(), var3.getValue());
        }

        List var7 = this.a((String)null, this.mUrlParamsWithObjects);
        Iterator var8 = var7.iterator();

        while(var8.hasNext()) {
            URLEncodedUtils.BasicNameValuePair var4 = (URLEncodedUtils.BasicNameValuePair)var8.next();
            var1.put(var4.getName(), var4.getValue());
        }

        ArrayList var9 = new ArrayList(var1.entrySet());
        Collections.sort(var9, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> var1, Map.Entry<String, Object> var2) {
                return ((String)var1.getKey()).toString().compareTo((String)var2.getKey());
            }
        });
        StringBuilder var10 = new StringBuilder();

        for(int var5 = 0; var5 < var9.size(); ++var5) {
            Map.Entry var6 = (Map.Entry)var9.get(var5);
            var10.append((String)var6.getKey()).append(var6.getValue());
        }

        return var10.toString();
    }

    public void setHttpEntityIsRepeatable(boolean var1) {
        this.mIsRepeatable = var1;
    }

    public void setUseJsonStreamer(boolean var1) {
        this.mUseJsonStreamer = var1;
    }

    public void setElapsedFieldInJsonStreamer(String var1) {
        this.mElapsedFieldInJsonStreamer = var1;
    }

    public void setAutoCloseInputStreams(boolean var1) {
        this.mAutoCloseInputStreams = var1;
    }

    public HttpEntity getEntity(ResponseHandlerInterface var1) {
        if (this.mUseJsonStreamer) {
            return this.a(var1);
        } else {
            return !this.mForceMultipartEntity && this.mStreamParams.isEmpty() && this.mFileParams.isEmpty() && this.mFileArrayParams.isEmpty() ? this.a() : this.b(var1);
        }
    }

    private HttpEntity a(ResponseHandlerInterface var1) {
        JsonStreamerEntity var2 = new JsonStreamerEntity(var1, !this.mFileParams.isEmpty() || !this.mStreamParams.isEmpty(), this.mElapsedFieldInJsonStreamer);
        Iterator var3 = this.mUrlParams.entrySet().iterator();

        Map.Entry var4;
        while(var3.hasNext()) {
            var4 = (Map.Entry)var3.next();
            var2.addPart((String)var4.getKey(), var4.getValue());
        }

        var3 = this.mUrlParamsWithObjects.entrySet().iterator();

        while(var3.hasNext()) {
            var4 = (Map.Entry)var3.next();
            var2.addPart((String)var4.getKey(), var4.getValue());
        }

        var3 = this.mFileParams.entrySet().iterator();

        while(var3.hasNext()) {
            var4 = (Map.Entry)var3.next();
            var2.addPart((String)var4.getKey(), var4.getValue());
        }

        var3 = this.mStreamParams.entrySet().iterator();

        while(var3.hasNext()) {
            var4 = (Map.Entry)var3.next();
            RequestParams.StreamWrapper var5 = (RequestParams.StreamWrapper)var4.getValue();
            if (var5.inputStream != null) {
                var2.addPart((String)var4.getKey(), RequestParams.StreamWrapper.a(var5.inputStream, var5.name, var5.contentType, var5.autoClose));
            }
        }

        return var2;
    }

    private HttpEntity a() {
        return new UrlEncodedFormEntity(this.getParamsList(), this.mContentEncoding);
    }

    private HttpEntity b(ResponseHandlerInterface var1) {
        SimpleMultipartEntity var2 = new SimpleMultipartEntity(var1);
        var2.setIsRepeatable(this.mIsRepeatable);
        Iterator var3 = this.mUrlParams.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            var2.addPartWithCharset((String)var4.getKey(), (String)var4.getValue(), this.mContentEncoding);
        }

        List var9 = this.a((String)null, this.mUrlParamsWithObjects);
        Iterator var10 = var9.iterator();

        while(var10.hasNext()) {
            URLEncodedUtils.BasicNameValuePair var5 = (URLEncodedUtils.BasicNameValuePair)var10.next();
            var2.addPartWithCharset(var5.getName(), var5.getValue(), this.mContentEncoding);
        }

        var10 = this.mStreamParams.entrySet().iterator();

        Map.Entry var11;
        while(var10.hasNext()) {
            var11 = (Map.Entry)var10.next();
            RequestParams.StreamWrapper var6 = (RequestParams.StreamWrapper)var11.getValue();
            if (var6.inputStream != null) {
                var2.addPart((String)var11.getKey(), var6.name, var6.inputStream, var6.contentType);
            }
        }

        var10 = this.mFileParams.entrySet().iterator();

        while(var10.hasNext()) {
            var11 = (Map.Entry)var10.next();
            RequestParams.FileWrapper var12 = (RequestParams.FileWrapper)var11.getValue();
            var2.addPart((String)var11.getKey(), var12.file, var12.contentType, var12.customFileName);
        }

        var10 = this.mFileArrayParams.entrySet().iterator();

        while(var10.hasNext()) {
            var11 = (Map.Entry)var10.next();
            List var13 = (List)var11.getValue();
            Iterator var7 = var13.iterator();

            while(var7.hasNext()) {
                RequestParams.FileWrapper var8 = (RequestParams.FileWrapper)var7.next();
                var2.addPart((String)var11.getKey(), var8.file, var8.contentType, var8.customFileName);
            }
        }

        return var2;
    }

    protected List<URLEncodedUtils.BasicNameValuePair> getParamsList() {
        LinkedList var1 = new LinkedList();
        Iterator var2 = this.mUrlParams.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            var1.add(new URLEncodedUtils.BasicNameValuePair((String)var3.getKey(), (String)var3.getValue()));
        }

        var1.addAll(this.a((String)null, this.mUrlParamsWithObjects));
        return var1;
    }

    private List<URLEncodedUtils.BasicNameValuePair> a(String var1, Object var2) {
        LinkedList var3 = new LinkedList();
        if (var2 instanceof Map) {
            Map var4 = (Map)var2;
            ArrayList var5 = new ArrayList(var4.keySet());
            if (var5.size() > 0 && var5.get(0) instanceof Comparable) {
                Collections.sort(var5);
            }

            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
                Object var7 = var6.next();
                if (var7 instanceof String) {
                    Object var8 = var4.get(var7);
                    if (var8 != null) {
                        var3.addAll(this.a(var1 == null ? (String)var7 : String.format(Locale.US, "%s[%s]", var1, var7), var8));
                    }
                }
            }
        } else {
            int var12;
            int var14;
            if (var2 instanceof List) {
                List var9 = (List)var2;
                var12 = var9.size();

                for(var14 = 0; var14 < var12; ++var14) {
                    var3.addAll(this.a(String.format(Locale.US, "%s[%d]", var1, var14), var9.get(var14)));
                }
            } else if (var2 instanceof Object[]) {
                Object[] var10 = (Object[])((Object[])var2);
                var12 = var10.length;

                for(var14 = 0; var14 < var12; ++var14) {
                    var3.addAll(this.a(String.format(Locale.US, "%s[%d]", var1, var14), var10[var14]));
                }
            } else if (var2 instanceof Set) {
                Set var11 = (Set)var2;
                Iterator var13 = var11.iterator();

                while(var13.hasNext()) {
                    Object var15 = var13.next();
                    var3.addAll(this.a(var1, var15));
                }
            } else {
                var3.add(new URLEncodedUtils.BasicNameValuePair(var1, var2.toString()));
            }
        }

        return var3;
    }

    protected String getParamString() {
        return URLEncodedUtils.format(this.getParamsList(), this.mContentEncoding);
    }

    public static class StreamWrapper {
        public final InputStream inputStream;
        public final String name;
        public final String contentType;
        public final boolean autoClose;

        public StreamWrapper(InputStream var1, String var2, String var3, boolean var4) {
            this.inputStream = var1;
            this.name = var2;
            this.contentType = var3;
            this.autoClose = var4;
        }

        static RequestParams.StreamWrapper a(InputStream var0, String var1, String var2, boolean var3) {
            return new RequestParams.StreamWrapper(var0, var1, var2 == null ? "application/octet-stream" : var2, var3);
        }
    }

    public static class FileWrapper implements Serializable {
        public final File file;
        public final String contentType;
        public final String customFileName;

        public FileWrapper(File var1, String var2, String var3) {
            this.file = var1;
            this.contentType = var2;
            this.customFileName = var3;
        }
    }
}
