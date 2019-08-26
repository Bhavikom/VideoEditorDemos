// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.annotation.SuppressLint;

import java.io.File;
import android.content.res.AssetFileDescriptor;
import java.io.InputStream;
import java.io.IOException;
import android.content.Context;
import java.util.Hashtable;

public class AssetsHelper
{
    private static final Hashtable<String, Hashtable<String, String>> a;
    
    public static boolean hasAssets(final Context context, final String s) {
        return getAssetPath(context, s) != null;
    }
    
    public static String getAssetsText(final Context context, final String s) {
        final InputStream assetsStream = getAssetsStream(context, s);
        if (assetsStream == null) {
            return null;
        }
        try {
            final byte[] array = new byte[assetsStream.available()];
            assetsStream.read(array);
            return new String(array, "UTF-8");
        }
        catch (IOException ex) {
            TLog.e(ex, "getAssetsText: %s | %s", s, s);
        }
        finally {
            FileHelper.safeClose(assetsStream);
        }
        return null;
    }
    
    public static InputStream getAssetsStream(final Context context, final String s) {
        final String assetPath = getAssetPath(context, s);
        if (assetPath == null) {
            return null;
        }
        try {
            return context.getAssets().open(assetPath, 1);
        }
        catch (IOException ex) {
            TLog.e(ex, "getAssetsStream: %s | %s", s, assetPath);
            return null;
        }
    }
    
    public static AssetFileDescriptor getAssetFileDescriptor(final Context context, final String s) {
        final String assetPath = getAssetPath(context, s);
        if (assetPath == null) {
            return null;
        }
        try {
            return context.getAssets().openFd(assetPath);
        }
        catch (Exception ex) {
            TLog.e(ex, "getAssetFileDescriptor: %s | %s", s, assetPath);
            return null;
        }
    }
    
    @SuppressLint({ "DefaultLocale" })
    public static String getAssetPath(final Context context, final String s) {
        if (context == null || s == null) {
            return null;
        }
        final File file = new File(s);
        final Hashtable<String, String> assetsFiles = getAssetsFiles(context, (file.getParent() != null) ? file.getParent() : "");
        if (assetsFiles == null) {
            return null;
        }
        for (final String s2 : assetsFiles.values()) {
            if (s2.equalsIgnoreCase(s)) {
                return s2;
            }
        }
        return assetsFiles.get(file.getName().toLowerCase());
    }
    
    public static Hashtable<String, String> getAssetsFiles(final Context context, final String key) {
        if (key == null || context == null) {
            return null;
        }
        final Hashtable<String, String> hashtable = AssetsHelper.a.get(key);
        if (hashtable != null) {
            return hashtable;
        }
        try {
            return a(key, context.getAssets().list(key));
        }
        catch (IOException ex) {
            TLog.e(ex, "getAssetsFiles: %s", key);
            return null;
        }
    }
    
    @SuppressLint({ "DefaultLocale" })
    private static Hashtable<String, String> a(final String s, final String[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        final Hashtable<String, String> value = new Hashtable<String, String>(array.length);
        final String s2 = (s.length() > 0) ? (s + File.separator) : s;
        for (final String s3 : array) {
            value.put(StringHelper.removeSuffix(s3).toLowerCase(), String.format("%s%s", s2, s3));
        }
        AssetsHelper.a.put(s, value);
        return value;
    }
    
    static {
        a = new Hashtable<String, Hashtable<String, String>>();
    }
}
