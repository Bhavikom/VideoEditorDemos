package com.meishe.sdkdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by admin on 2018-5-28.
 */

public class SpUtil {
    private static SharedPreferences sp;
    private static SpUtil spUtil; ///1)private static  当前类的类型 变量

    private SpUtil(Context context) { //2)给当前类写个私有的构造方法
        if (sp == null) {
            sp = context.getSharedPreferences("default", Context.MODE_PRIVATE);
        }
    }

    public static SpUtil getInstance(Context context) {//3)public static 当前类类型 getInstance
        if (spUtil == null) {
            spUtil = new SpUtil(context);
        }
        return spUtil;
    }

    public void putString(String key,
                          String value) {
        if (!sp.contains(key) || !sp.getString(key, "None").equals(value)) {
            SharedPreferences.Editor e = sp.edit();
            e.putString(key, value);
            e.apply();
        }
    }

    public void putBoolean(String key,
                           boolean value) {
        if (!sp.contains(key) || sp.getBoolean(key, false) != value) {
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean(key, value);
            e.apply();
        }
    }

    public void putInt(String key, int value) {
        if (!sp.contains(key) || sp.getInt(key, Integer.MIN_VALUE) != value) {
            SharedPreferences.Editor e = sp.edit();
            e.putInt(key, value);
            e.apply();
        }
    }

    public void putLong(String key,
                        long value) {
        if (!sp.contains(key) || sp.getLong(key, Long.MIN_VALUE) != value) {
            SharedPreferences.Editor e = sp.edit();
            e.putLong(key, value);
            e.apply();
        }
    }

    public void putFloat(String key,
                         float value) {
        if (!sp.contains(key) || sp.getFloat(key, Float.MIN_VALUE) != value) {
            SharedPreferences.Editor e = sp.edit();
            e.putFloat(key, value);
            e.apply();
        }
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public String getString(String key) {
        return sp.getString(key, "None");
    }

    //摄像头
    public int getInt(String key) {
        return sp.getInt(key, 1);
    }

    public void removeKey(String... key) {
        for (String s : key) {
            SharedPreferences.Editor e = sp.edit();
            e.remove(s);
            e.apply();
        }

    }

    public boolean containKeys(String... key) {
        for (String s : key) {
            if (sp.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean setObjectToShare(Context context, Object object, String key) {
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(context);
        if (object == null) {
            SharedPreferences.Editor editor = share.edit().remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
// 将对象放到OutputStream中
// 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = share.edit();
        // 将编码后的字符串写到base64.xml文件中
        editor.putString(key, objectStr);
        return editor.commit();
    }

    public static Object getObjectFromShare(Context context, String key) {
        SharedPreferences sharePre = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            String wordBase64 = sharePre.getString(key, "");
            // 将base64格式字符串还原成byte数组
            if (wordBase64 == null || wordBase64.equals("")) { // 不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
