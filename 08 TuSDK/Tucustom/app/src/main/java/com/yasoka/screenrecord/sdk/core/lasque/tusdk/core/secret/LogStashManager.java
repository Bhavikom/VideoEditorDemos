// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret;

import android.util.Base64;
import java.io.ByteArrayInputStream;
import org.json.JSONException;
//import org.lasque.tusdk.core.utils.FileHelper;
import org.json.JSONArray;
//import org.lasque.tusdk.core.utils.StringHelper;
import org.json.JSONObject;
import java.util.Iterator;
import java.util.List;
import android.content.pm.PackageInfo;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.NetworkHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkDeviceInfo;

import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.NetworkHelper;
//import org.lasque.tusdk.core.utils.TuSdkDeviceInfo;
//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.core.utils.TLog;
import java.io.File;

public class LogStashManager
{
    private static String a;
    private static LogStashManager b;
    private final File c;
    private Object d;
    
    public static void init(final File file) {
        if (LogStashManager.b == null) {
            LogStashManager.b = new LogStashManager(file);
        }
    }
    
    public static LogStashManager getInstance() {
        if (LogStashManager.b == null) {
            TLog.w("LogStashManager is not Initialization !!!", new Object[0]);
        }
        return LogStashManager.b;
    }
    
    public LogStashManager(final File c) {
        this.d = new Object();
        this.c = c;
        this.stashLog();
    }
    
    public void stashLog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200L);
                    LogStashManager.this.a();
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
    
    private void a() {
        try {
            String advertisingIdInfo = "";
            try {
                advertisingIdInfo = TuSdkDeviceInfo.getAdvertisingIdInfo(TuSdk.appContext().getContext());
            }
            catch (Exception ex2) {
                TLog.w("Google id has exception!!!", new Object[0]);
            }
            final List<NetworkHelper.ScanResultBean> scanResultList = NetworkHelper.getScanResultList();
            final List<PackageInfo> installAppInfoList = TuSdkDeviceInfo.getInstallAppInfoList();
            final ArrayList<PacketInfoBean> list = new ArrayList<PacketInfoBean>();
            if (installAppInfoList != null && installAppInfoList.size() > 0) {
                for (final PackageInfo packageInfo : installAppInfoList) {
                    final PacketInfoBean packetInfoBean = new PacketInfoBean();
                    packetInfoBean.b = packageInfo.versionName;
                    packetInfoBean.c = packageInfo.versionCode + "";
                    packetInfoBean.e = packageInfo.firstInstallTime + "";
                    packetInfoBean.d = packageInfo.packageName;
                    list.add(packetInfoBean);
                }
            }
            this.a(this.a(advertisingIdInfo, scanResultList, list).toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private JSONObject a(final String s, final List<NetworkHelper.ScanResultBean> list, final List<PacketInfoBean> list2) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ttp", (Object)String.valueOf(System.currentTimeMillis()));
            jsonObject.put("0x1000", (Object) StringHelper.Base64Encode(s));
            final JSONArray jsonArray = new JSONArray();
            if (list != null && list.size() > 0) {
                final Iterator<NetworkHelper.ScanResultBean> iterator = list.iterator();
                while (iterator.hasNext()) {
                    jsonArray.put((Object)new JSONObject(iterator.next().toString()));
                }
            }
            jsonObject.put("0x2000", (Object)jsonArray);
            final JSONArray jsonArray2 = new JSONArray();
            if (list2 != null && list2.size() > 0) {
                final Iterator<PacketInfoBean> iterator2 = list2.iterator();
                while (iterator2.hasNext()) {
                    jsonArray2.put((Object)new JSONObject(iterator2.next().toString()));
                }
            }
            jsonObject.put("0x3000", (Object)jsonArray2);
            jsonObject.put("0x4000", (Object)StringHelper.Base64Encode(NetworkHelper.getNetworkState()));
            jsonObject.put("0x5000", (Object)new JSONObject(NetworkHelper.getLocalMacAddress(TuSdk.appContext().getContext()).toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
    
    private void a(final String s) {
        synchronized (this.d) {
            SdkValid.shared.saveLogStash(s, this.c.getPath() + "/" + LogStashManager.a);
        }
    }
    
    public LogBean getUpLoadData() {
        if (!new File(this.c + "/" + LogStashManager.a).exists()) {
            return null;
        }
        final LogBean logBean = new LogBean();
        synchronized (this.d) {
            final byte[] bytesFromFile = FileHelper.getBytesFromFile(new File(this.c + "/" + LogStashManager.a));
            if (bytesFromFile == null) {
                return logBean;
            }
            final String s = new String(bytesFromFile);
            if (s.isEmpty()) {
                return logBean;
            }
            final String substring = s.substring(0, s.length() - 2);
            logBean.b = s.substring(s.length() - 2, s.length());
            logBean.c = substring;
        }
        return logBean;
    }
    
    public void deleteTempFile() {
        try {
            if (!new File(this.c + "/" + LogStashManager.a).exists()) {
                return;
            }
            FileHelper.delete(new File(this.c + "/" + LogStashManager.a));
            if (getInstance() != null) {
                getInstance().stashLog();
            }
        }
        catch (Exception ex) {
            TLog.w("delete log temp file error", new Object[0]);
        }
    }
    
    static {
        LogStashManager.a = "logstash.statistics";
    }
    
    private class PacketInfoBean
    {
        private String b;
        private String c;
        private String d;
        private String e;
        
        public String getVersion() {
            return this.b;
        }
        
        public void setVersion(final String b) {
            this.b = b;
        }
        
        public String getCode() {
            return this.c;
        }
        
        public void setCode(final String c) {
            this.c = c;
        }
        
        public String getAppName() {
            return this.d;
        }
        
        public void setAppName(final String d) {
            this.d = d;
        }
        
        public String getInstallTime() {
            return this.e;
        }
        
        public void setInstallTime(final String e) {
            this.e = e;
        }
        
        @Override
        public String toString() {
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("0x3001", (Object)StringHelper.Base64Encode(this.getVersion()));
                jsonObject.put("0x3002", (Object)StringHelper.Base64Encode(this.getCode()));
                jsonObject.put("0x3003", (Object)StringHelper.Base64Encode(this.getAppName()));
                jsonObject.put("0x3004", (Object)StringHelper.Base64Encode(this.getInstallTime()));
            }
            catch (JSONException ex) {}
            return jsonObject.toString();
        }
    }
    
    public class LogBean
    {
        private String b;
        private String c;
        
        public String getIndex() {
            return this.b;
        }
        
        public void setIndex(final String b) {
            this.b = b;
        }
        
        public String getData() {
            return this.c;
        }
        
        public void setData(final String c) {
            this.c = c;
        }
        
        public ByteArrayInputStream getByteArrayInputStream() {
            return new ByteArrayInputStream(Base64.decode(this.getData(), 0));
        }
        
        public boolean isValid() {
            try {
                return !StringHelper.isEmpty(this.b) && !StringHelper.isEmpty(this.c);
            }
            catch (Exception ex) {
                return false;
            }
        }
    }
}
