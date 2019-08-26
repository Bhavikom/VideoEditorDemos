package org.lasque.tusdk.core.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHelper
{
  private static final char[] a = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
  public static final long MIN_AVAILABLE_SPACE_BYTES = 52428800L;
  
  public static String toHexString(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramArrayOfByte.length * 2);
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      localStringBuilder.append(a[((paramArrayOfByte[i] & 0xF0) >>> 4)]);
      localStringBuilder.append(a[(paramArrayOfByte[i] & 0xF)]);
    }
    return localStringBuilder.toString();
  }
  
  public static String md5sum(File paramFile)
  {
    return md5sum(paramFile.getAbsolutePath());
  }
  
  public static String md5sum(String paramString)
  {
    FileInputStream localFileInputStream = null;
    try
    {
      localFileInputStream = new FileInputStream(paramString);
      String str1 = md5sum(localFileInputStream);
      return str1;
    }
    catch (Exception localException)
    {
      System.out.println("error");
      String str2 = null;
      return str2;
    }
    finally
    {
      safeClose(localFileInputStream);
    }
  }
  
  public static String md5sum(InputStream paramInputStream)
  {
    if (paramInputStream == null) {
      return null;
    }
    InputStream localInputStream = paramInputStream;
    byte[] arrayOfByte = new byte['Ѐ'];
    int i = 0;
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      while ((i = localInputStream.read(arrayOfByte)) > 0) {
        localMessageDigest.update(arrayOfByte, 0, i);
      }
      String str1 = toHexString(localMessageDigest.digest());
      return str1;
    }
    catch (Exception localException)
    {
      TLog.e(localException, "md5sum", new Object[0]);
      String str2 = null;
      return str2;
    }
    finally
    {
      safeClose(localInputStream);
    }
  }
  
  public static String md5sum(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramArrayOfByte);
      return toHexString(localMessageDigest.digest());
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      TLog.e(localNoSuchAlgorithmException, "md5sum", new Object[0]);
    }
    return null;
  }
  
  public static void deleteSubs(File paramFile)
  {
    if ((paramFile == null) || (!paramFile.exists()) || (!paramFile.isDirectory())) {
      return;
    }
    File[] arrayOfFile = paramFile.listFiles();
    if ((arrayOfFile == null) || (arrayOfFile.length == 0)) {
      return;
    }
    for (int i = 0; i < arrayOfFile.length; i++) {
      delete(arrayOfFile[i]);
    }
  }
  
  public static void delete(File paramFile)
  {
    if ((paramFile == null) || (!paramFile.exists())) {
      return;
    }
    if (paramFile.isFile())
    {
      paramFile.delete();
      return;
    }
    if (paramFile.isDirectory())
    {
      File[] arrayOfFile = paramFile.listFiles();
      if ((arrayOfFile == null) || (arrayOfFile.length == 0))
      {
        paramFile.delete();
        return;
      }
      for (int i = 0; i < arrayOfFile.length; i++) {
        delete(arrayOfFile[i]);
      }
      paramFile.delete();
    }
  }
  
  public static byte[] readFile(File paramFile)
  {
    return readFile(paramFile, 0L);
  }
  
  public static byte[] readFile(File paramFile, long paramLong)
  {
    if ((!paramFile.exists()) || (!paramFile.isFile()) || (!paramFile.canRead()))
    {
      TLog.d("readFile: %s", new Object[] { paramFile });
      return null;
    }
    long l = paramFile.length();
    if ((paramLong == 0L) || (paramLong < l)) {
      return readFile(paramFile, paramLong, 0L);
    }
    return null;
  }
  
  public static byte[] readFile(File paramFile, long paramLong1, long paramLong2)
  {
    if ((!paramFile.exists()) || (!paramFile.isFile()) || (!paramFile.canRead()) || ((paramLong2 > 0L) && (paramLong2 <= paramLong1)))
    {
      TLog.e("readFile: %s", new Object[] { paramFile });
      return null;
    }
    long l1 = paramFile.length();
    if (paramLong1 >= l1) {
      return null;
    }
    if ((paramLong2 == 0L) || (paramLong2 > l1)) {
      paramLong2 = l1;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    FileInputStream localFileInputStream = null;
    int i = 0;
    long l2 = paramLong1;
    byte[] arrayOfByte = new byte['Ѐ'];
    try
    {
      localFileInputStream = new FileInputStream(paramFile);
      localFileInputStream.skip(l2);
      while ((i = localFileInputStream.read(arrayOfByte)) != -1)
      {
        l2 += i;
        if (paramLong2 >= l2)
        {
          localByteArrayOutputStream.write(arrayOfByte, 0, i);
        }
        else
        {
          int j = i - (int)(l2 - paramLong2);
          localByteArrayOutputStream.write(arrayOfByte, 0, j);
        }
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException, "readFile: %s", new Object[] { paramFile });
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "readFile: %s", new Object[] { paramFile });
    }
    finally
    {
      safeClose(localByteArrayOutputStream);
      safeClose(localFileInputStream);
    }
    return localByteArrayOutputStream.toByteArray();
  }
  
  public static boolean safeClose(Closeable paramCloseable)
  {
    if (paramCloseable == null) {
      return true;
    }
    try
    {
      paramCloseable.close();
      return true;
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "safeClose close InputStream", new Object[0]);
    }
    return false;
  }
  
  public static int copy(InputStream paramInputStream, OutputStream paramOutputStream)
  {
    long l = copyLarge(paramInputStream, paramOutputStream, new byte['က']);
    if (l > 2147483647L) {
      return -1;
    }
    return (int)l;
  }
  
  public static long copyLarge(InputStream paramInputStream, OutputStream paramOutputStream, byte[] paramArrayOfByte)
  {
    long l = 0L;
    int i = 0;
    while (-1 != (i = paramInputStream.read(paramArrayOfByte)))
    {
      paramOutputStream.write(paramArrayOfByte, 0, i);
      l += i;
    }
    return l;
  }
  
  public static FileInputStream getFileInputStream(File paramFile)
  {
    if ((paramFile == null) || (!paramFile.exists()) || (!paramFile.isFile())) {
      return null;
    }
    FileInputStream localFileInputStream = null;
    try
    {
      localFileInputStream = new FileInputStream(paramFile);
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException, "getFileInputStream: %s", new Object[] { paramFile });
    }
    return localFileInputStream;
  }
  
  public static boolean copyFile(File paramFile1, File paramFile2)
  {
    boolean bool = true;
    FileInputStream localFileInputStream = null;
    FileOutputStream localFileOutputStream = null;
    try
    {
      localFileInputStream = new FileInputStream(paramFile1);
      localFileOutputStream = new FileOutputStream(paramFile2);
      byte[] arrayOfByte = new byte['Ѐ'];
      int i;
      while ((i = localFileInputStream.read(arrayOfByte)) != -1) {
        localFileOutputStream.write(arrayOfByte, 0, i);
      }
    }
    catch (Exception localException)
    {
      bool = false;
      TLog.e(localException, "copyFile: %s | %s", new Object[] { paramFile1, paramFile2 });
    }
    finally
    {
      safeClose(localFileInputStream);
      safeClose(localFileOutputStream);
    }
    return bool;
  }
  
  public static byte[] getBytesFromFile(File paramFile)
  {
    return getBytesFromFile(paramFile, 0);
  }
  
  public static byte[] getBytesFromFile(File paramFile, int paramInt)
  {
    FileInputStream localFileInputStream = getFileInputStream(paramFile);
    if (localFileInputStream == null) {
      return null;
    }
    if ((paramInt == 0) || (paramInt > paramFile.length())) {
      paramInt = (int)paramFile.length();
    }
    ByteArrayOutputStream localByteArrayOutputStream = null;
    try
    {
      localByteArrayOutputStream = new ByteArrayOutputStream(paramInt);
      byte[] arrayOfByte1 = new byte[paramInt];
      int i;
      if ((i = localFileInputStream.read(arrayOfByte1)) != -1) {
        localByteArrayOutputStream.write(arrayOfByte1, 0, i);
      }
      byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
      return arrayOfByte2;
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "getBytesFromFile(File file, int length): %s", new Object[] { paramFile.getPath() });
    }
    finally
    {
      safeClose(localFileInputStream);
      safeClose(localByteArrayOutputStream);
    }
    return null;
  }
  
  public static Object getObjectFromBytes(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
      return null;
    }
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    ObjectInputStream localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
    return localObjectInputStream.readObject();
  }
  
  public static byte[] getBytesFromObject(Serializable paramSerializable)
  {
    if (paramSerializable == null) {
      return null;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
    localObjectOutputStream.writeObject(paramSerializable);
    return localByteArrayOutputStream.toByteArray();
  }
  
  public static boolean rename(File paramFile1, File paramFile2)
  {
    if ((paramFile1 == null) || (!paramFile1.isFile()) || (!paramFile1.exists()) || (paramFile2 == null)) {
      return false;
    }
    return paramFile1.renameTo(paramFile2);
  }
  
  public static File saveFile(String paramString, byte[] paramArrayOfByte)
  {
    return saveFile(new File(paramString), paramArrayOfByte);
  }
  
  public static File saveFile(File paramFile, byte[] paramArrayOfByte)
  {
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
      localBufferedOutputStream = new BufferedOutputStream(localFileOutputStream);
      localBufferedOutputStream.write(paramArrayOfByte);
    }
    catch (Exception localException)
    {
      TLog.d("File not found [saveFile(File file, byte[] b)]: %s", new Object[] { paramFile.getPath() });
    }
    finally
    {
      safeClose(localBufferedOutputStream);
    }
    return paramFile;
  }
  
  public static boolean saveFile(File paramFile, InputStream paramInputStream)
  {
    if ((paramFile == null) || (paramInputStream == null)) {
      return false;
    }
    if (paramFile.exists()) {
      paramFile.delete();
    }
    boolean bool = false;
    FileOutputStream localFileOutputStream = null;
    try
    {
      localFileOutputStream = new FileOutputStream(paramFile);
      byte[] arrayOfByte = new byte['Ѐ'];
      int i;
      while ((i = paramInputStream.read(arrayOfByte)) != -1) {
        localFileOutputStream.write(arrayOfByte, 0, i);
      }
      localFileOutputStream.flush();
      bool = true;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException, "File not found: %s", new Object[] { paramFile.getPath() });
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "Error accessing file: %s", new Object[] { paramFile.getPath() });
    }
    finally
    {
      safeClose(localFileOutputStream);
      safeClose(paramInputStream);
    }
    return bool;
  }
  
  public static boolean mountedExternalStorage()
  {
    return Environment.getExternalStorageState().equals("mounted");
  }
  
  public static File getExternalStoragePublicDirectory(String paramString)
  {
    if (!mountedExternalStorage()) {
      return null;
    }
    if (paramString == null) {
      return Environment.getExternalStorageDirectory();
    }
    return Environment.getExternalStoragePublicDirectory(paramString);
  }
  
  public static String[] getExternalStorages(Context paramContext)
  {
    StorageManager localStorageManager = (StorageManager)ContextUtils.getSystemService(paramContext, "storage");
    if (localStorageManager == null) {
      return null;
    }
    Method localMethod = ReflectUtils.getMethod(StorageManager.class, "getVolumePaths", new Class[0]);
    if (localMethod == null) {
      return null;
    }
    Object localObject = ReflectUtils.reflectMethod(localMethod, localStorageManager, new Object[0]);
    return (String[])localObject;
  }
  
  public static File getAppCacheDir(Context paramContext, boolean paramBoolean)
  {
    if (paramContext == null) {
      return null;
    }
    File localFile = paramContext.getCacheDir();
    if ((!paramBoolean) && (mountedExternalStorage())) {
      try
      {
        localFile = paramContext.getExternalCacheDir();
      }
      catch (Exception localException)
      {
        TLog.e("create externalCacheDir failed", new Object[0]);
      }
    }
    return localFile;
  }
  
  public static File getAppCacheDir(Context paramContext, String paramString, boolean paramBoolean)
  {
    File localFile = getAppCacheDir(paramContext, paramBoolean);
    if ((localFile == null) || (paramString == null)) {
      return localFile;
    }
    localFile = new File(localFile.getPath(), paramString);
    localFile.mkdirs();
    return localFile;
  }
  
  public static boolean hasAvailableExternal(Context paramContext)
  {
    if ((paramContext == null) || (!mountedExternalStorage())) {
      return false;
    }
    boolean bool = 52428800L < getAvailableStore(paramContext.getCacheDir().getAbsolutePath());
    return bool;
  }
  
  public static long getAvailableStore(String paramString)
  {
    StatFs localStatFs = new StatFs(paramString);
    if (Build.VERSION.SDK_INT < 18) {
      return a(localStatFs);
    }
    return b(localStatFs);
  }
  
  private static long a(StatFs paramStatFs)
  {
    long l1 = paramStatFs.getBlockSize();
    long l2 = paramStatFs.getAvailableBlocks();
    long l3 = l2 * l1;
    return l3;
  }
  
  @TargetApi(18)
  private static long b(StatFs paramStatFs)
  {
    long l = paramStatFs.getAvailableBytes();
    return l;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\FileHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */