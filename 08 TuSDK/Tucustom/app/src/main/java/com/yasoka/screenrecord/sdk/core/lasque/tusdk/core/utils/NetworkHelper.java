// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.ScanResult;
import java.util.ArrayList;
import java.util.List;
//import org.lasque.tusdk.core.TuSdkContext;
import java.util.Enumeration;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import java.net.NetworkInterface;
import android.os.Build;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.net.NetworkInfo;
//import org.lasque.tusdk.core.TuSdk;
import android.net.ConnectivityManager;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class NetworkHelper
{
    public static InetAddress getLocalHost() {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        }
        catch (UnknownHostException ex) {
            TLog.e(ex, "getLocalHost", new Object[0]);
        }
        return localHost;
    }
    
    public static InetAddress getInetAddress(final String host) {
        if (StringHelper.isEmpty(host)) {
            return null;
        }
        InetAddress byName = null;
        try {
            byName = InetAddress.getByName(host);
        }
        catch (UnknownHostException ex) {
            TLog.e(ex, "getInetAddress: %s", host);
        }
        return byName;
    }
    
    public static Socket buildSocket(final InetAddress address, final int n) {
        if (address == null || n < 1 || n > 65535) {
            return null;
        }
        Socket socket = null;
        try {
            socket = new Socket(address, n);
        }
        catch (IOException ex) {
            TLog.e(ex, "buildSocket: %s | %s", address, n);
        }
        return socket;
    }
    
    public static long ipToLong(final String s) {
        final long[] array = new long[4];
        final int index = s.indexOf(".");
        final int index2 = s.indexOf(".", index + 1);
        final int index3 = s.indexOf(".", index2 + 1);
        array[0] = Long.parseLong(s.substring(0, index));
        array[1] = Long.parseLong(s.substring(index + 1, index2));
        array[2] = Long.parseLong(s.substring(index2 + 1, index3));
        array[3] = Long.parseLong(s.substring(index3 + 1));
        return (array[0] << 24) + (array[1] << 16) + (array[2] << 8) + array[3];
    }
    
    public static String longToIP(final long n) {
        return String.format("%d.%d.%d.%d", n & 0xFFL, n >> 8 & 0xFFL, n >> 16 & 0xFFL, n >> 24 & 0xFFL);
    }
    
    public static boolean isPhoneNetwork() {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)TuSdk.appContext().getContext().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
    }
    
    public static boolean isWifiNetwork() {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager) TuSdk.appContext().getContext().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
    }
    
    public static String getNetworkState() {
        if (isPhoneNetwork()) {
            return "Cellular";
        }
        if (isWifiNetwork()) {
            return "wifi";
        }
        return "No Connection";
    }
    
    public static WIFIInfoBean getLocalMacAddress(final Context context) {
        final WIFIInfoBean wifiInfoBean = new WIFIInfoBean();
        if (!a() || isPhoneNetwork()) {
            return wifiInfoBean;
        }
        try {
            final WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            wifiInfoBean.setMacAddress(connectionInfo.getMacAddress());
            wifiInfoBean.setSSID(connectionInfo.getSSID());
            wifiInfoBean.setBSSID(connectionInfo.getBSSID());
            final DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            wifiInfoBean.setGateway(longToIP(dhcpInfo.gateway));
            wifiInfoBean.setIp(longToIP(dhcpInfo.ipAddress));
            wifiInfoBean.setMacAddress(TuSdkDeviceInfo.getMac());
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    final NetworkInterface networkInterface = networkInterfaces.nextElement();
                    if (!"wlan0".equalsIgnoreCase(networkInterface.getName())) {
                        continue;
                    }
                    final byte[] array = new byte[0];
                    final byte[] hardwareAddress = networkInterface.getHardwareAddress();
                    if (hardwareAddress == null) {
                        continue;
                    }
                    if (hardwareAddress.length == 0) {
                        continue;
                    }
                    final StringBuilder sb = new StringBuilder();
                    final byte[] array2 = hardwareAddress;
                    for (int length = array2.length, i = 0; i < length; ++i) {
                        sb.append(String.format("%02X:", array2[i]));
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    wifiInfoBean.setMacAddress(sb.toString());
                }
            }
        }
        catch (Throwable t2) {
            t2.printStackTrace();
        }
        return wifiInfoBean;
    }
    
    private static boolean a() {
        if (Build.VERSION.SDK_INT < 23) {
            return 0 == TuSdkContext.context().getPackageManager().checkPermission("android.permission.ACCESS_WIFI_STATE", TuSdkContext.getPackageName());
        }
        return TuSdkContext.context().checkSelfPermission("android.permission.ACCESS_WIFI_STATE") != -1;
    }
    
    public static List<ScanResultBean> getScanResultList() {
        final ArrayList<ScanResultBean> list = new ArrayList<ScanResultBean>();
        if (!a()) {
            return list;
        }
        final List<ScanResult> wifiScanResult = getWifiScanResult(TuSdk.appContext().getContext());
        if (wifiScanResult == null || wifiScanResult.size() == 0) {
            return list;
        }
        for (final ScanResult scanResult : wifiScanResult) {
            final ScanResultBean scanResultBean = new ScanResultBean();
            scanResultBean.b = scanResult.BSSID;
            scanResultBean.c = scanResult.level + "";
            scanResultBean.a = scanResult.SSID;
            scanResultBean.d = scanResult.capabilities;
            list.add(scanResultBean);
        }
        return list;
    }
    
    public static List<ScanResult> getWifiScanResult(final Context context) {
        final boolean b = context == null;
        return (List<ScanResult>)((WifiManager)context.getSystemService("wifi")).getScanResults();
    }
    
    public static class ScanResultBean
    {
        private String a;
        private String b;
        private String c;
        private String d;
        
        public String getSSID() {
            return this.a;
        }
        
        public void setSSID(final String a) {
            this.a = a;
        }
        
        public String getBSSID() {
            return this.b;
        }
        
        public void setBSSID(final String b) {
            this.b = b;
        }
        
        public String getLevel() {
            return this.c;
        }
        
        public void setLevel(final String c) {
            this.c = c;
        }
        
        public String getCapabilities() {
            return this.d;
        }
        
        public void setCapabilities(final String d) {
            this.d = d;
        }
        
        @Override
        public String toString() {
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("0x2001", (Object)StringHelper.Base64Encode(this.getSSID()));
                jsonObject.put("0x2002", (Object)StringHelper.Base64Encode(this.getBSSID()));
                jsonObject.put("0x2003", (Object)StringHelper.Base64Encode(this.getLevel()));
                jsonObject.put("0x2004", (Object)StringHelper.Base64Encode(this.getCapabilities()));
            }
            catch (JSONException ex) {}
            return jsonObject.toString();
        }
    }
    
    public static class WIFIInfoBean
    {
        private String a;
        private String b;
        private String c;
        private String d;
        private String e;
        
        public String getMacAddress() {
            return this.a;
        }
        
        public void setMacAddress(final String a) {
            this.a = a;
        }
        
        public String getSSID() {
            return this.b;
        }
        
        public void setSSID(final String b) {
            this.b = b;
        }
        
        public String getBSSID() {
            return this.c;
        }
        
        public void setBSSID(final String c) {
            this.c = c;
        }
        
        public String getGateway() {
            return this.d;
        }
        
        public void setGateway(final String d) {
            this.d = d;
        }
        
        public String getIp() {
            return this.e;
        }
        
        public void setIp(final String e) {
            this.e = e;
        }
        
        @Override
        public String toString() {
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("0x5001", (Object)StringHelper.Base64Encode(this.a));
                jsonObject.put("0x5002", (Object)StringHelper.Base64Encode(this.b));
                jsonObject.put("0x5003", (Object)StringHelper.Base64Encode(this.c));
                jsonObject.put("0x5004", (Object)StringHelper.Base64Encode(this.d));
                jsonObject.put("0x5005", (Object)StringHelper.Base64Encode(this.e));
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
            return jsonObject.toString();
        }
    }
}
