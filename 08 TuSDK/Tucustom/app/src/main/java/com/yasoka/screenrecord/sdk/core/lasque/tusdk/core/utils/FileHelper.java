// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StatFs;
import java.lang.reflect.Method;
import android.os.storage.StorageManager;
import android.content.Context;
import android.os.Environment;
import java.io.FilterOutputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.io.InputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.File;

public class FileHelper
{
    private static final char[] a;
    public static final long MIN_AVAILABLE_SPACE_BYTES = 52428800L;
    
    public static String toHexString(final byte[] array) {
        final StringBuilder sb = new StringBuilder(array.length * 2);
        for (int i = 0; i < array.length; ++i) {
            sb.append(FileHelper.a[(array[i] & 0xF0) >>> 4]);
            sb.append(FileHelper.a[array[i] & 0xF]);
        }
        return sb.toString();
    }
    
    public static String md5sum(final File file) {
        return md5sum(file.getAbsolutePath());
    }
    
    public static String md5sum(final String name) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(name);
            return md5sum(inputStream);
        }
        catch (Exception ex) {
            System.out.println("error");
            return null;
        }
        finally {
            safeClose(inputStream);
        }
    }
    
    public static String md5sum(final InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        final byte[] array = new byte[1024];
        try {
            final MessageDigest instance = MessageDigest.getInstance("MD5");
            int read;
            while ((read = inputStream.read(array)) > 0) {
                instance.update(array, 0, read);
            }
            return toHexString(instance.digest());
        }
        catch (Exception ex) {
            TLog.e(ex, "md5sum", new Object[0]);
            return null;
        }
        finally {
            safeClose(inputStream);
        }
    }
    
    public static String md5sum(final byte[] input) {
        if (input == null) {
            return null;
        }
        try {
            final MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(input);
            return toHexString(instance.digest());
        }
        catch (NoSuchAlgorithmException ex) {
            TLog.e(ex, "md5sum", new Object[0]);
            return null;
        }
    }
    
    public static void deleteSubs(final File file) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return;
        }
        final File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return;
        }
        for (int i = 0; i < listFiles.length; ++i) {
            delete(listFiles[i]);
        }
    }
    
    public static void delete(final File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < listFiles.length; ++i) {
                delete(listFiles[i]);
            }
            file.delete();
        }
    }
    
    public static byte[] readFile(final File file) {
        return readFile(file, 0L);
    }
    
    public static byte[] readFile(final File file, final long n) {
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            TLog.d("readFile: %s", file);
            return null;
        }
        final long length = file.length();
        if (n == 0L || n < length) {
            return readFile(file, n, 0L);
        }
        return null;
    }
    
    public static byte[] readFile(final File file, final long n, long n2) {
        if (!file.exists() || !file.isFile() || !file.canRead() || (n2 > 0L && n2 <= n)) {
            TLog.e("readFile: %s", file);
            return null;
        }
        final long length = file.length();
        if (n >= length) {
            return null;
        }
        if (n2 == 0L || n2 > length) {
            n2 = length;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;
        long n3 = n;
        final byte[] b = new byte[1024];
        try {
            inputStream = new FileInputStream(file);
            inputStream.skip(n3);
            int read;
            while ((read = inputStream.read(b)) != -1) {
                n3 += read;
                if (n2 < n3) {
                    byteArrayOutputStream.write(b, 0, read - (int)(n3 - n2));
                    break;
                }
                byteArrayOutputStream.write(b, 0, read);
            }
        }
        catch (FileNotFoundException ex) {
            TLog.e(ex, "readFile: %s", file);
        }
        catch (IOException ex2) {
            TLog.e(ex2, "readFile: %s", file);
        }
        finally {
            safeClose(byteArrayOutputStream);
            safeClose(inputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public static boolean safeClose(final Closeable closeable) {
        if (closeable == null) {
            return true;
        }
        try {
            closeable.close();
            return true;
        }
        catch (IOException ex) {
            TLog.e(ex, "safeClose close InputStream", new Object[0]);
            return false;
        }
    }
    
    public static int copy(final InputStream inputStream, final OutputStream outputStream) {
        final long copyLarge = copyLarge(inputStream, outputStream, new byte[4096]);
        if (copyLarge > 2147483647L) {
            return -1;
        }
        return (int)copyLarge;
    }
    
    public static long copyLarge(final InputStream inputStream, final OutputStream outputStream, final byte[] array) {
        try {
            long n = 0L;
            int read;
            while (-1 != (read = inputStream.read(array))) {
                outputStream.write(array, 0, read);
                n += read;
            }
            return n;
        }catch (Exception e){

        }
        return 0;

    }
    
    public static FileInputStream getFileInputStream(final File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        }
        catch (FileNotFoundException ex) {
            TLog.e(ex, "getFileInputStream: %s", file);
        }
        return fileInputStream;
    }
    
    public static boolean copyFile(final File file, final File file2) {
        boolean b = true;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            outputStream = new FileOutputStream(file2);
            final byte[] array = new byte[1024];
            int read;
            while ((read = inputStream.read(array)) != -1) {
                outputStream.write(array, 0, read);
            }
        }
        catch (Exception ex) {
            b = false;
            TLog.e(ex, "copyFile: %s | %s", file, file2);
        }
        finally {
            safeClose(inputStream);
            safeClose(outputStream);
        }
        return b;
    }
    
    public static byte[] getBytesFromFile(final File file) {
        return getBytesFromFile(file, 0);
    }
    
    public static byte[] getBytesFromFile(final File file, int size) {
        final FileInputStream fileInputStream = getFileInputStream(file);
        if (fileInputStream == null) {
            return null;
        }
        if (size == 0 || size > file.length()) {
            size = (int)file.length();
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream(size);
            final byte[] array = new byte[size];
            final int read;
            if ((read = fileInputStream.read(array)) != -1) {
                byteArrayOutputStream.write(array, 0, read);
            }
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException ex) {
            TLog.e(ex, "getBytesFromFile(File file, int length): %s", file.getPath());
        }
        finally {
            safeClose(fileInputStream);
            safeClose(byteArrayOutputStream);
        }
        return null;
    }

    public static Object getObjectFromBytes(byte[] var0) throws IOException, ClassNotFoundException {
        if (var0 != null && var0.length != 0) {
            ByteArrayInputStream var1 = new ByteArrayInputStream(var0);
            ObjectInputStream var2 = new ObjectInputStream(var1);
            return var2.readObject();
        } else {
            return null;
        }
    }
    
    public static byte[] getBytesFromObject(final Serializable obj) throws IOException {
        if (obj == null) {
            return null;
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectOutputStream(out).writeObject(obj);
        return out.toByteArray();
    }
    
    public static boolean rename(final File file, final File dest) {
        return file != null && file.isFile() && file.exists() && dest != null && file.renameTo(dest);
    }
    
    public static File saveFile(final String pathname, final byte[] array) {
        return saveFile(new File(pathname), array);
    }
    
    public static File saveFile(final File file, final byte[] b) {
        FilterOutputStream filterOutputStream = null;
        try {
            filterOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            filterOutputStream.write(b);
        }
        catch (Exception ex) {
            TLog.d("File not found [saveFile(File file, byte[] b)]: %s", file.getPath());
        }
        finally {
            safeClose(filterOutputStream);
        }
        return file;
    }
    
    public static boolean saveFile(final File file, final InputStream inputStream) {
        if (file == null || inputStream == null) {
            return false;
        }
        if (file.exists()) {
            file.delete();
        }
        boolean b = false;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            final byte[] array = new byte[1024];
            int read;
            while ((read = inputStream.read(array)) != -1) {
                fileOutputStream.write(array, 0, read);
            }
            fileOutputStream.flush();
            b = true;
        }
        catch (FileNotFoundException ex) {
            TLog.e(ex, "File not found: %s", file.getPath());
        }
        catch (IOException ex2) {
            TLog.e(ex2, "Error accessing file: %s", file.getPath());
        }
        finally {
            safeClose(fileOutputStream);
            safeClose(inputStream);
        }
        return b;
    }
    
    public static boolean mountedExternalStorage() {
        return Environment.getExternalStorageState().equals("mounted");
    }
    
    public static File getExternalStoragePublicDirectory(final String s) {
        if (!mountedExternalStorage()) {
            return null;
        }
        if (s == null) {
            return Environment.getExternalStorageDirectory();
        }
        return Environment.getExternalStoragePublicDirectory(s);
    }
    
    public static String[] getExternalStorages(final Context context) {
        final StorageManager storageManager = ContextUtils.getSystemService(context, "storage");
        if (storageManager == null) {
            return null;
        }
        final Method method = ReflectUtils.getMethod(StorageManager.class, "getVolumePaths", (Class<?>[])new Class[0]);
        if (method == null) {
            return null;
        }
        return (String[])ReflectUtils.reflectMethod(method, storageManager, new Object[0]);
    }
    
    public static File getAppCacheDir(final Context context, final boolean b) {
        if (context == null) {
            return null;
        }
        File file = context.getCacheDir();
        if (!b && mountedExternalStorage()) {
            try {
                file = context.getExternalCacheDir();
            }
            catch (Exception ex) {
                TLog.e("create externalCacheDir failed", new Object[0]);
            }
        }
        return file;
    }
    
    public static File getAppCacheDir(final Context context, final String child, final boolean b) {
        final File appCacheDir = getAppCacheDir(context, b);
        if (appCacheDir == null || child == null) {
            return appCacheDir;
        }
        final File file = new File(appCacheDir.getPath(), child);
        file.mkdirs();
        return file;
    }
    
    public static boolean hasAvailableExternal(final Context context) {
        return context != null && mountedExternalStorage() && 52428800L < getAvailableStore(context.getCacheDir().getAbsolutePath());
    }
    
    public static long getAvailableStore(final String s) {
        final StatFs statFs = new StatFs(s);
        if (Build.VERSION.SDK_INT < 18) {
            return a(statFs);
        }
        return b(statFs);
    }
    
    private static long a(final StatFs statFs) {
        return statFs.getAvailableBlocks() * (long)statFs.getBlockSize();
    }
    
    @TargetApi(18)
    private static long b(final StatFs statFs) {
        return statFs.getAvailableBytes();
    }
    
    static {
        a = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }
}
