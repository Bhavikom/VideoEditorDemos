// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import android.annotation.SuppressLint;
import java.util.UUID;
import android.util.Base64;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class StringHelper
{
    public static final String EMPTY = "";
    
    public static boolean isEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    public static boolean isNotEmpty(final String s) {
        return !isEmpty(s);
    }
    
    public static boolean isBlank(final String s) {
        final int length;
        if (s == null || (length = s.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNotBlank(final String s) {
        return !isBlank(s);
    }
    
    public static String trim(final String s) {
        return (s == null) ? null : s.trim();
    }
    
    public static String trimToNull(final String s) {
        final String trim = trim(s);
        return isEmpty(trim) ? null : trim;
    }
    
    public static String trimToEmpty(final String s) {
        return (s == null) ? "" : s.trim();
    }
    
    public static String md5(final String s) {
        return md5(s.getBytes());
    }
    
    public static String md5(final byte[] input) {
        MessageDigest instance;
        try {
            instance = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException cause) {
            throw new RuntimeException(cause);
        }
        instance.update(input);
        return String.format("%032x", new BigInteger(1, instance.digest()));
    }
    
    public static String encryptMd5(final String s) {
        final char[] charArray = s.toCharArray();
        for (int i = 0; i < charArray.length; ++i) {
            charArray[i] ^= 'l';
        }
        return new String(charArray);
    }
    
    public static String Base64Encode(final String s) {
        if (s == null) {
            return "";
        }
        return Base64.encodeToString(ByteUtils.getBytes(s), 2);
    }
    
    @SuppressLint({ "DefaultLocale" })
    public static String uuid() {
        return UUID.randomUUID().toString().toLowerCase();
    }
    
    public static HashMap<String, String> urlQuery(final String s) {
        if (s == null) {
            return null;
        }
        final String[] split = s.split("&");
        if (split == null || split.length == 0) {
            return null;
        }
        final HashMap hashMap = new HashMap<String, String>(split.length);
        final String[] array = split;
        for (int length = array.length, i = 0; i < length; ++i) {
            final String[] split2 = array[i].split("=");
            if (split2 != null) {
                if (split2.length == 2) {
                    hashMap.put(split2[0], split2[1]);
                }
            }
        }
        return (HashMap<String, String>)hashMap;
    }
    
    @SuppressLint({ "SimpleDateFormat" })
    public static String timeStampString() {
        return new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
    }
    
    public static String formatByte(final long l) {
        if (l < 1024L) {
            return String.format("%sB", l);
        }
        final long i = l / 1024L;
        if (i < 1024L) {
            return String.format("%sK", i);
        }
        final float f = l / 1048576.0f;
        if (f < 1024.0f) {
            return String.format(Locale.getDefault(), "%.1fM", f);
        }
        return String.format(Locale.getDefault(), "%.1fG", f / 1024.0f);
    }
    
    public static boolean isLetter(String substring, final int endIndex) {
        if (isEmpty(substring) || endIndex > substring.length()) {
            return false;
        }
        if (endIndex > 0) {
            substring = substring.substring(0, endIndex);
        }
        return substring.matches("[a-zA-z]");
    }
    
    public static int parserInt(final String s) {
        int int1 = 0;
        if (s == null) {
            return int1;
        }
        try {
            int1 = Integer.parseInt(s);
        }
        catch (Exception ex) {}
        return int1;
    }
    
    public static int parserInt(final String s, final int radix) {
        int int1 = 0;
        if (s == null) {
            return int1;
        }
        try {
            int1 = Integer.parseInt(s, radix);
        }
        catch (Exception ex) {}
        return int1;
    }
    
    public static float parseFloat(final String s) {
        float float1 = 0.0f;
        if (s == null) {
            return float1;
        }
        try {
            float1 = Float.parseFloat(s);
        }
        catch (Exception ex) {}
        return float1;
    }
    
    public static long parserLong(final String s) {
        long long1 = 0L;
        if (s == null) {
            return long1;
        }
        try {
            long1 = Long.parseLong(s);
        }
        catch (Exception ex) {}
        return long1;
    }
    
    @SuppressLint({ "DefaultLocale" })
    public static String bytesToHexString(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        if (array == null || array.length <= 0) {
            return null;
        }
        final char[] str = new char[2];
        for (int i = 0; i < array.length; ++i) {
            str[0] = Character.forDigit(array[i] >>> 4 & 0xF, 16);
            str[1] = Character.forDigit(array[i] & 0xF, 16);
            sb.append(str);
        }
        return sb.toString().toUpperCase();
    }
    
    public static int matchInt(final String s, final String s2) {
        return parserInt(matchString(s, s2));
    }
    
    public static String matchString(final String input, final String regex) {
        if (input == null || regex == null) {
            return null;
        }
        final Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    public static ArrayList<String> matchStrings(final String input, final String regex) {
        if (input == null || regex == null) {
            return null;
        }
        final ArrayList<String> list = new ArrayList<String>();
        final Matcher matcher = Pattern.compile(regex).matcher(input);
        while (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); ++i) {
                list.add(matcher.group(i));
            }
        }
        return list;
    }
    
    public static String removeSuffix(final String s) {
        if (s == null) {
            return null;
        }
        final int lastIndex = s.lastIndexOf(".");
        if (lastIndex > -1) {
            return s.substring(0, lastIndex);
        }
        return s;
    }
}
