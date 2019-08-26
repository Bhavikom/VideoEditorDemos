// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
import android.content.Intent;
import android.content.IntentFilter;
import java.io.UnsupportedEncodingException;
import java.io.File;
//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.TuSdkContext;
import java.util.Calendar;
//import org.lasque.tusdk.core.http.RequestParams;
//import org.lasque.tusdk.core.http.RequestHandle;
//import org.lasque.tusdk.core.http.ResponseHandlerInterface;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.utils.TuSdkDeviceInfo;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdkConfigs;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.RequestHandle;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.ResponseHandlerInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkDeviceInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.HardwareHelper;

public class TuSdkHttpEngine
{
    public static final String SDK_TYPE_IMAGE = "1";
    public static final int ENVIRONMENT_LOCAL = 0;
    public static final int ENVIRONMENT_TEST = 1;
    public static final int ENVIRONMENT_PRODUCTION = 2;
    public static final int ENVIRONMENT = 2;
    public static final String NETWORK_PATH = "api";
    public static final String WEB_PATH = "web";
    public static final String SRV_PATH = "srv";
    public static final String NETWORK_DOMAIN;
    public static final String NETWORK_WEB_DOMAIN;
    public static final String SERVICE_DOMAIN;
    public static final String NETWORK_AUTH_DOMAIN;
    public static final int NETWORK_PORT;
    public static final int SERVICE_PORT;
    public static String API_DOMAIN;
    public static String WEB_DOMAIN;
    public static String WEB_API_DOMAIN;
    public static String AUTH_DOMAIN;
    public static String WEB_PIC_DOMAIN;
    public static String SRV_DOMAIN;
    public static final boolean DEBUG;
    private boolean a;
    private static TuSdkHttpEngine b;
    private static TuSdkHttpEngine c;
    private static TuSdkHttpEngine d;
    private static TuSdkHttpEngine e;
    public static boolean useSSL;
    private Context f;
    private TuSdkHttp g;
    private String h;
    private String i;
    private String j;
    private String k;
    private String l;
    private int m;
    private BroadcastReceiver n;
    
    public static TuSdkHttpEngine shared() {
        return TuSdkHttpEngine.b;
    }
    
    public static TuSdkHttpEngine service() {
        return TuSdkHttpEngine.c;
    }
    
    public static TuSdkHttpEngine webAPIEngine() {
        return TuSdkHttpEngine.d;
    }
    
    public static TuSdkHttpEngine auth() {
        return TuSdkHttpEngine.e;
    }
    
    public static TuSdkHttpEngine init(final TuSdkConfigs tuSdkConfigs, final String s, final Context context) {
        if (TuSdkHttpEngine.b == null && TuSdkHttpEngine.c == null && TuSdkHttpEngine.d == null && TuSdkHttpEngine.e == null && tuSdkConfigs != null) {
            final String s2 = TuSdkHttpEngine.useSSL ? "https://%s/%s" : "http://%s/%s";
            TuSdkHttpEngine.API_DOMAIN = String.format("http://%s/%s", TuSdkHttpEngine.NETWORK_DOMAIN, "api");
            TuSdkHttpEngine.WEB_DOMAIN = String.format(s2, TuSdkHttpEngine.NETWORK_WEB_DOMAIN, "web");
            TuSdkHttpEngine.SRV_DOMAIN = String.format(s2, TuSdkHttpEngine.SERVICE_DOMAIN, "srv");
            TuSdkHttpEngine.WEB_API_DOMAIN = String.format(s2, TuSdkHttpEngine.NETWORK_WEB_DOMAIN, "api");
            TuSdkHttpEngine.WEB_PIC_DOMAIN = String.format(s2, "img.tusdk.com", "api");
            TuSdkHttpEngine.AUTH_DOMAIN = String.format("https://%s/%s", TuSdkHttpEngine.NETWORK_AUTH_DOMAIN, "api");
            TuSdkHttpEngine.b = new TuSdkHttpEngine(tuSdkConfigs, s, context, TuSdkHttpEngine.API_DOMAIN, TuSdkHttpEngine.NETWORK_PORT);
            TuSdkHttpEngine.c = new TuSdkHttpEngine(tuSdkConfigs, s, context, TuSdkHttpEngine.SRV_DOMAIN, TuSdkHttpEngine.SERVICE_PORT);
            TuSdkHttpEngine.d = new TuSdkHttpEngine(tuSdkConfigs, s, context, TuSdkHttpEngine.WEB_API_DOMAIN, TuSdkHttpEngine.NETWORK_PORT);
            TuSdkHttpEngine.e = new TuSdkHttpEngine(tuSdkConfigs, s, context, TuSdkHttpEngine.AUTH_DOMAIN, TuSdkHttpEngine.NETWORK_PORT);
        }
        return TuSdkHttpEngine.b;
    }
    
    private TuSdkHttp a() {
        return this.g;
    }
    
    public String userIdentify() {
        return this.k;
    }
    
    public void setUserIdentify(final Object o) {
        if (this.k == null) {
            this.k = null;
            this.g.removeHeader("x-client-user");
        }
        else {
            this.k = o.toString();
            this.g.addHeader("x-client-user", this.k);
        }
    }
    
    private TuSdkHttpEngine(final TuSdkConfigs tuSdkConfigs, final String j, final Context f, final String l, final int m) {
        this.a = false;
        this.f = f;
        this.j = j;
        this.l = l;
        this.m = m;
        this.a(j);
    }
    
    private void a(final String s) {
        (this.g = new TuSdkHttp(this.m)).setEnableRedirct(true);
        this.g.setMaxConnections(2);
        this.g.addHeader("x-client-identifier", "android");
        this.g.addHeader("uuid", this.uniqueDeviceID());
        if (s != null) {
            this.g.addHeader("x-client-dev", s);
        }
        this.b();
    }
    
    private void b() {
        final StringBuilder sb = new StringBuilder(String.format("%s:%s", 24, StringHelper.Base64Encode("3.1.1")));
        sb.append(String.format("|%s:%s", 40, StringHelper.Base64Encode(TuSdkDeviceInfo.getModel())));
        sb.append(String.format("|%s:%s", 56, StringHelper.Base64Encode(TuSdkDeviceInfo.getOSVersion())));
        sb.append(String.format("|%s:%s", 72, StringHelper.Base64Encode(this.f.getPackageName())));
        sb.append(String.format("|%s:%s", 88, StringHelper.Base64Encode(ContextUtils.getVersionName(this.f))));
        sb.append(String.format("|%s:%s", 120, StringHelper.Base64Encode(this.c())));
        sb.append(String.format("|%s:%s", 136, StringHelper.Base64Encode(TuSdkDeviceInfo.getIMSI())));
        sb.append(String.format("|%s:%s", 152, StringHelper.Base64Encode(TuSdkDeviceInfo.getIMEI())));
        sb.append(String.format("|%s:%s", 264, StringHelper.Base64Encode(TuSdkDeviceInfo.getMac())));
        sb.append(String.format("|%s:%s", 280, StringHelper.Base64Encode(TuSdkDeviceInfo.getVender())));
        sb.append(String.format("|%s:%s", 296, StringHelper.Base64Encode(TuSdkDeviceInfo.getIP())));
        sb.append(String.format("|%s:%s", 312, StringHelper.Base64Encode(TuSdkDeviceInfo.getAndroidID())));
        this.a = (TuSdkDeviceInfo.getLocation() != null && !TuSdkDeviceInfo.getLocation().isEmpty());
        sb.append(String.format("|%s:%s", 328, StringHelper.Base64Encode(TuSdkDeviceInfo.getLocation())));
        if (SdkValid.shared.appType() > 0) {
            sb.append(String.format("|%s:%s", 376, StringHelper.Base64Encode(String.format("%s", SdkValid.shared.appType()))));
        }
        this.g.addHeader("x-client-bundle", StringHelper.Base64Encode(sb.toString()));
    }
    
    public RequestHandle get(final String s, final boolean b, final ResponseHandlerInterface responseHandlerInterface) {
        if (!this.a) {
            this.b();
        }
        return this.get(s, null, b, responseHandlerInterface);
    }
    
    public RequestHandle get(String urlBuild, final TuSdkHttpParams tuSdkHttpParams, final boolean b, final ResponseHandlerInterface responseHandlerInterface) {
        if (!this.a) {
            this.b();
        }
        urlBuild = this.urlBuild(urlBuild, b);
        return this.a().get(urlBuild, tuSdkHttpParams, responseHandlerInterface);
    }
    
    public RequestHandle post(final String s, final boolean b, final ResponseHandlerInterface responseHandlerInterface) {
        if (!this.a) {
            this.b();
        }
        return this.post(s, null, b, responseHandlerInterface);
    }
    
    public RequestHandle post(String urlBuild, final TuSdkHttpParams tuSdkHttpParams, final boolean b, final ResponseHandlerInterface responseHandlerInterface) {
        if (!this.a) {
            this.b();
        }
        urlBuild = this.urlBuild(urlBuild, b);
        return this.a().post(urlBuild, tuSdkHttpParams, responseHandlerInterface);
    }
    
    public RequestHandle postService(String serviceUrlBuild, final TuSdkHttpParams tuSdkHttpParams, final ResponseHandlerInterface responseHandlerInterface) {
        if (!this.a) {
            this.b();
        }
        serviceUrlBuild = this.serviceUrlBuild(serviceUrlBuild, tuSdkHttpParams, true);
        return this.a().post(serviceUrlBuild, tuSdkHttpParams, responseHandlerInterface);
    }
    
    protected String urlBuild(final String str, final boolean b) {
        if (str == null) {
            return null;
        }
        final StringBuilder append = new StringBuilder(this.l).append(str);
        if (b) {
            this.a(append, str);
        }
        this.b(append, str);
        return append.toString();
    }
    
    private StringBuilder a(final StringBuilder sb, final String str) {
        if (sb == null) {
            return null;
        }
        final long n = Calendar.getInstance().getTimeInMillis() / 1000000L * 1000L;
        final String md5 = StringHelper.md5(str + "_" + this.uniqueDeviceID() + "_" + n);
        if (sb.indexOf("?") == -1) {
            sb.append("?");
        }
        else {
            sb.append("&");
        }
        sb.append("hash=").append(md5);
        sb.append("&t=").append(n);
        return sb;
    }
    
    protected String serviceUrlBuild(final String str, final TuSdkHttpParams tuSdkHttpParams, final boolean b) {
        if (str == null) {
            return null;
        }
        final StringBuilder append = new StringBuilder(this.l).append(str);
        if (b && append != null) {
            final long l = Calendar.getInstance().getTimeInMillis() / 1000000L * 1000L;
            tuSdkHttpParams.add("pid", StringHelper.md5(this.j));
            tuSdkHttpParams.add("t", String.valueOf(l));
            tuSdkHttpParams.add("sign", StringHelper.md5(tuSdkHttpParams.toPairString() + this.j));
        }
        return append.toString();
    }
    
    public RequestHandle postService(String serviceUrlBuild, final boolean b, final TuSdkHttpParams tuSdkHttpParams, final ResponseHandlerInterface responseHandlerInterface) {
        if (b) {
            serviceUrlBuild = this.serviceUrlBuild(serviceUrlBuild, tuSdkHttpParams, true);
        }
        return this.a().post(serviceUrlBuild, tuSdkHttpParams, responseHandlerInterface);
    }
    
    private StringBuilder b(final StringBuilder sb, final String s) {
        if (sb == null) {
            return null;
        }
        final String packageName = this.f.getPackageName();
        if (sb.indexOf("?") == -1) {
            sb.append("?");
        }
        else {
            sb.append("&");
        }
        sb.append("app=").append(packageName);
        return sb;
    }
    
    public String getWebUrl(final String str, final boolean b) {
        if (str == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder(TuSdkHttpEngine.WEB_DOMAIN);
        sb.append(str);
        this.b(sb, str);
        if (!b) {
            return sb.toString();
        }
        if (sb.indexOf("?") == -1) {
            sb.append("?");
        }
        else {
            sb.append("&");
        }
        sb.append("uuid=").append(this.uniqueDeviceID());
        if (this.j != null) {
            sb.append("&devid=").append(this.j);
        }
        if (this.k != null) {
            sb.append("&uid=").append(this.k);
        }
        sb.append("&v=").append(11);
        this.a(sb, str);
        return sb.toString();
    }
    
    public String uniqueDeviceID() {
        if (this.h != null) {
            return this.h;
        }
        this.h = TuSdkContext.sharedPreferences().loadSharedCache("TUSDK_DeviceUUID");
        if (this.h != null) {
            return this.h;
        }
        this.h = StringHelper.md5(StringHelper.uuid() + "_" + Calendar.getInstance().getTimeInMillis() / 1000000L * 1000L);
        TuSdkContext.sharedPreferences().saveSharedCache("TUSDK_DeviceUUID", this.h);
        return this.h;
    }
    
    public String getDevId() {
        return this.j;
    }
    
    private String c() {
        if (this.i != null) {
            return this.i;
        }
        this.i = this.d();
        if (this.i == null) {
            this.b(this.i = this.uniqueDeviceID());
        }
        return this.i;
    }
    
    private String d() {
        final File externalStoragePublicDirectory = FileHelper.getExternalStoragePublicDirectory(null);
        if (externalStoragePublicDirectory == null) {
            return null;
        }
        final byte[] file = FileHelper.readFile(new File(externalStoragePublicDirectory, "TuSDK/global.keystore"));
        if (file == null) {
            return null;
        }
        String s;
        try {
            s = new String(file, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            return null;
        }
        return s.trim();
    }
    
    private void b(final String s) {
        final File externalStoragePublicDirectory = FileHelper.getExternalStoragePublicDirectory(null);
        if (externalStoragePublicDirectory == null) {
            return;
        }
        new File(externalStoragePublicDirectory, "TuSDK").mkdirs();
        final File file = new File(externalStoragePublicDirectory, "TuSDK/global.keystore");
        try {
            FileHelper.saveFile(file, s.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException ex) {}
    }
    
    protected void overseeNetwork() {
        if (this.n != null) {
            return;
        }
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.n = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                TuSdkHttpEngine.this.onNetworkStateChanged(HardwareHelper.isNetworkAvailable(context));
            }
        };
        this.f.registerReceiver(this.n, intentFilter);
    }
    
    protected void cancelOverseeNetwork() {
        if (this.n != null) {
            this.f.unregisterReceiver(this.n);
        }
        this.n = null;
    }
    
    protected void onNetworkStateChanged(final boolean b) {
        TLog.d("connected: %s", b);
    }
    
    static {
        switch (2) {
            case 2: {
                NETWORK_DOMAIN = "api.tusdk.com";
                NETWORK_WEB_DOMAIN = "m.tusdk.com";
                NETWORK_AUTH_DOMAIN = "auth.tusdk.com";
                NETWORK_PORT = 80;
                SERVICE_DOMAIN = "srv2.tusdk.com";
                SERVICE_PORT = 80;
                DEBUG = false;
                break;
            }
            case 1: {
                NETWORK_DOMAIN = "10.10.10.25";
                NETWORK_WEB_DOMAIN = "m.tusdk.com";
                NETWORK_AUTH_DOMAIN = "auth.tusdk.com";
                NETWORK_PORT = 80;
                SERVICE_DOMAIN = "srv2.tusdk.com";
                SERVICE_PORT = 80;
                DEBUG = false;
                break;
            }
            default: {
                NETWORK_DOMAIN = "192.168.199.152:80";
                NETWORK_WEB_DOMAIN = "192.168.199.152:80";
                NETWORK_AUTH_DOMAIN = "auth.tusdk.com";
                NETWORK_PORT = 80;
                SERVICE_DOMAIN = "192.168.199.152:80";
                SERVICE_PORT = 80;
                DEBUG = true;
                break;
            }
        }
        TuSdkHttpEngine.useSSL = true;
    }
}
