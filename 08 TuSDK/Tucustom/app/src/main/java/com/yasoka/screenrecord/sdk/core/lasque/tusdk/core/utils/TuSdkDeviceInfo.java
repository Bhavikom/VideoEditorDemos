// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.os.IInterface;
import android.os.Looper;
import android.content.ComponentName;
import java.util.concurrent.LinkedBlockingQueue;
import android.content.ServiceConnection;
import android.os.Parcel;
import android.os.IBinder;

import java.util.ArrayList;
//import org.lasque.tusdk.core.TuSdk;
import android.content.pm.PackageInfo;
import java.util.List;
import android.app.ActivityManager;
import android.location.Location;
import android.provider.Settings;
import java.util.Iterator;
import java.util.Enumeration;
import java.net.InterfaceAddress;
import android.text.TextUtils;
import java.net.SocketException;
import java.net.NetworkInterface;
import java.net.InetAddress;
import android.telephony.TelephonyManager;
//import org.lasque.tusdk.core.TuSdkContext;
import android.annotation.TargetApi;
import android.os.Build;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;

public class TuSdkDeviceInfo {
    public static final String VENDER_HUAWEI = "HUAWEI";
    public static final String MODEL_HUAWEI_NXTAL10 = "HUAWEI NXT-AL10";
    public static final String VENDER_OPPO = "OPPO";
    public static final String MODEL_OPPO_A3 = "PADM00";
    public static final String VENDER_MEITU = "Meitu";
    public static final String VENDER_XIAOMI = "XiaoMi";
    public static final String MODEL_XIAOMI_MI_NOTE_LTE = "MI NOTE LTE";

    public TuSdkDeviceInfo() {
    }

    @TargetApi(23)
    public static boolean hasRequiredPermissions(Context var0, String[] var1) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        } else {
            if (var1 != null && var1.length > 0) {
                String[] var2 = var1;
                int var3 = var1.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    String var5 = var2[var4];
                    if (var0.checkSelfPermission(var5) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static String getIMEI() {
        String var0 = "";
        Context var1 = TuSdkContext.context();
        if (var1 == null) {
            return var0;
        } else {
            boolean var2 = hasRequiredPermissions(var1, getRequiredPermissions());
            if (var2) {
                var0 = readPhoneInfo(var1, 10);
                return var0;
            } else {
                return var0;
            }
        }
    }

    @SuppressLint("MissingPermission")
    protected static String readPhoneInfo(Context var0, int var1) {
        String var2 = "";

        try {
            TelephonyManager var3 = (TelephonyManager)var0.getSystemService(Context.TELEPHONY_SERVICE);
            if (var3 == null) {
                return var2;
            }

            switch(var1) {
                case 10:
                    var2 = var3.getDeviceId();
                    break;
                case 20:
                    var2 = var3.getSubscriberId();
            }
        } catch (Exception var4) {
        }

        return var2;
    }

    public static String getIMSI() {
        String var0 = "";
        Context var1 = TuSdkContext.context();
        if (var1 == null) {
            return var0;
        } else {
            boolean var2 = hasRequiredPermissions(var1, getRequiredPermissions());
            if (var2) {
                var0 = readPhoneInfo(var1, 20);
                return var0;
            } else {
                return var0;
            }
        }
    }

    @TargetApi(23)
    protected static String[] getRequiredPermissions() {
        String[] var0 = new String[]{"android.permission.READ_PHONE_STATE"};
        return var0;
    }

    public static String getMac() {
        String var0 = "";
        Context var1 = TuSdkContext.context();
        if (var1 == null) {
            return var0;
        } else {
            try {
                NetworkInterface var3 = NetworkInterface.getByInetAddress(InetAddress.getByName(getIP()));
                byte[] var2 = var3.getHardwareAddress();
                var0 = byte2hex(var2);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return var0;
        }
    }

    public static String getMac2() {
        Enumeration var0 = null;

        try {
            var0 = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException var6) {
        }

        String var1 = null;
        NetworkInterface var2 = null;
        if (var0 == null) {
            return null;
        } else {
            String var3 = getIP();

            while(var0.hasMoreElements()) {
                var2 = (NetworkInterface)var0.nextElement();

                try {
                    if (!TextUtils.isEmpty(var3)) {
                        Iterator var4 = var2.getInterfaceAddresses().iterator();

                        while(var4.hasNext()) {
                            InterfaceAddress var5 = (InterfaceAddress)var4.next();
                            if (var5.getAddress().getHostAddress().equals(var3) && var2.getHardwareAddress() != null) {
                                var1 = byte2hex(var2.getHardwareAddress());
                                break;
                            }
                        }

                        if (var1 != null) {
                            break;
                        }
                    }
                } catch (Exception var7) {
                }
            }

            return var1 == null ? "" : var1;
        }
    }

    public static String byte2hex(byte[] var0) {
        StringBuffer var1 = new StringBuffer(var0.length);
        String var2 = "";
        int var3 = var0.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            var2 = Integer.toHexString(var0[var4] & 255);
            if (var2.length() == 1) {
                var1 = var1.append("0").append(var2);
            } else {
                var1 = var1.append(var2);
            }
        }

        return String.valueOf(var1);
    }

    public static String getIP() {
        String var0 = "";

        try {
            Enumeration var1 = NetworkInterface.getNetworkInterfaces();

            while(var1.hasMoreElements()) {
                NetworkInterface var2 = (NetworkInterface)var1.nextElement();
                Enumeration var3 = var2.getInetAddresses();

                while(var3.hasMoreElements()) {
                    InetAddress var4 = (InetAddress)var3.nextElement();
                    if (!var4.isLoopbackAddress()) {
                        var0 = var4.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception var5) {
        }

        return var0;
    }

    public static String getOSVersion() {
        String var0 = Build.VERSION.RELEASE;
        return var0;
    }

    public static String getModel() {
        String var0 = Build.MODEL;
        return var0;
    }

    public static String getVender() {
        String var0 = Build.MANUFACTURER;
        return var0;
    }

    public static String getAndroidID() {
        String var0 = "";
        Context var1 = TuSdkContext.context();
        if (var1 == null) {
            return var0;
        } else {
            var0 = Settings.Secure.getString(var1.getContentResolver(), "android_id");
            return var0;
        }
    }

    public static String getLocation() {
        Location var0 = TuSdkLocation.getLastLocation();
        return var0 == null ? "" : var0.getLongitude() + "," + var0.getLatitude();
    }

    public static boolean isSupportPbo() {
        Context var0 = TuSdkContext.context();
        ActivityManager var1 = (ActivityManager)var0.getSystemService("activity");
        ConfigurationInfo var2 = var1.getDeviceConfigurationInfo();
        int var3 = (var2.reqGlEsVersion & -65536) >> 16;
        return var3 >= 3 && Build.VERSION.SDK_INT >= 18;
    }

    public static List<PackageInfo> getInstallAppInfoList() {
        List var0 = TuSdk.appContext().getContext().getPackageManager().getInstalledPackages(0);
        ArrayList var1 = new ArrayList();
        Iterator var2 = var0.iterator();

        while(var2.hasNext()) {
            PackageInfo var3 = (PackageInfo)var2.next();
            if ((var3.applicationInfo.flags & 1) == 0) {
                var1.add(var3);
            }
        }

        return var1;
    }

    public static String getAdvertisingIdInfo(Context var0) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot be called from the main thread");
        } else {
            try {
                PackageManager var1 = var0.getPackageManager();
                var1.getPackageInfo("com.android.vending", 0);
            } catch (Exception var11) {
                try {
                    throw var11;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            Intent var12 = new Intent("com.google.android.gms.ads.identifier.service.START");
            var12.setPackage("com.google.android.gms");

            class AdvertisingConnection implements ServiceConnection {
                private boolean a = false;
                private final LinkedBlockingQueue<IBinder> b = new LinkedBlockingQueue(1);

                AdvertisingConnection() {
                }

                public void onServiceConnected(ComponentName var1, IBinder var2) {
                    try {
                        this.b.put(var2);
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }

                }

                public void onServiceDisconnected(ComponentName var1) {
                }

                public IBinder getBinder() {
                    if (this.a) {
                        throw new IllegalStateException();
                    } else {
                        this.a = true;
                        try {
                            return (IBinder)this.b.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

            }

            AdvertisingConnection var2 = new AdvertisingConnection();
            if (var0.bindService(var12, var2, 1)) {
                String var4;
                try {
                    class AdvertisingInterface implements IInterface {
                        private IBinder a;

                        public AdvertisingInterface/* $FF was: 1AdvertisingInterface*/(IBinder var1) {
                            this.a = var1;
                        }

                        public IBinder asBinder() {
                            return this.a;
                        }

                        public String getId() {
                            Parcel var1 = Parcel.obtain();
                            Parcel var2 = Parcel.obtain();
                            String var3 = null;

                            try {
                                var1.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                                this.a.transact(1, var1, var2, 0);
                                var2.readException();
                                var3 = var2.readString();
                            } catch (Exception var8) {
                                var8.printStackTrace();
                            } finally {
                                var2.recycle();
                                var1.recycle();
                            }

                            return var3;
                        }
                    }

                    AdvertisingInterface var3 = new AdvertisingInterface(var2.getBinder());
                    var4 = var3.getId();
                } catch (Exception var9) {
                    throw var9;
                } finally {
                    var0.unbindService(var2);
                }

                return var4;
            } else {
                try {
                    throw new IOException("Google Play connection failed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
