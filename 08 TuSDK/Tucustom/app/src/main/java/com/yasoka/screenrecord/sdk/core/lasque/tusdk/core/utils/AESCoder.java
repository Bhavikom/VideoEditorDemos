// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.util.Arrays;
import java.io.UnsupportedEncodingException;
import android.util.Base64;

public class AESCoder
{
    public static String encodeCBC256PKCS7PaddingToString(final String s, final String s2) {
        final byte[] encodeCBC256PKCS7Padding = encodeCBC256PKCS7Padding(s, s2);
        if (encodeCBC256PKCS7Padding == null) {
            return null;
        }
        return Base64.encodeToString(encodeCBC256PKCS7Padding, 0);
    }
    
    public static byte[] encodeCBC256PKCS7Padding(final String s, final String s2) {
        if (s == null) {
            return null;
        }
        byte[] bytes = null;
        try {
            bytes = s.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            TLog.e(ex, "%s encodeCBC256PKCS7Padding: %s | %s", "AESCoder", s, s2);
        }
        return encodeCBC256PKCS7Padding(bytes, s2);
    }
    
    public static byte[] encodeCBC256PKCS7Padding(final byte[] array, final String s) {
        return a(1, array, s);
    }
    
    public static String decodeCBC256PKCS7PaddingToString(final String s, final String s2) {
        final byte[] decodeCBC256PKCS7Padding = decodeCBC256PKCS7Padding(s, s2);
        if (decodeCBC256PKCS7Padding == null) {
            return null;
        }
        return new String(decodeCBC256PKCS7Padding);
    }
    
    public static byte[] decodeCBC256PKCS7Padding(final String s, final String s2) {
        return decodeCBC256PKCS7Padding(Base64.decode(s, 0), s2);
    }
    
    public static String decodeCBC256PKCS7PaddingToString(final byte[] array, final String s) {
        final byte[] decodeCBC256PKCS7Padding = decodeCBC256PKCS7Padding(array, s);
        if (decodeCBC256PKCS7Padding == null) {
            return null;
        }
        return new String(decodeCBC256PKCS7Padding);
    }
    
    public static byte[] decodeCBC256PKCS7Padding(final byte[] array, final String s) {
        return a(2, array, s);
    }
    
    private static byte[] a(final int opmode, final byte[] input, final String s) {
        if (input == null || s == null) {
            return null;
        }
        try {
            final SecretKeySpec a = a(s);
            final byte[] array = new byte[16];
            Arrays.fill(array, (byte)0);
            final IvParameterSpec params = new IvParameterSpec(array);
            final Cipher instance = Cipher.getInstance("AES/CBC/PKCS7Padding");
            instance.init(opmode, a, params);
            return instance.doFinal(input);
        }
        catch (Exception ex) {
            TLog.e(ex, "%s aesCBC256PKCS7Padding", "AESCoder");
            return null;
        }
    }
    
    private static SecretKeySpec a(final String s) {
        final byte[] array = new byte[256 / 8];
        Arrays.fill(array, (byte)0);
         byte[] bytes = new byte[0];
        try {
            bytes = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.arraycopy(bytes, 0, array, 0, (bytes.length < array.length) ? bytes.length : array.length);
        return new SecretKeySpec(array, "AES");
    }
}
