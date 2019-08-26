package org.lasque.tusdk.core.utils;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCoder
{
  public static String encodeCBC256PKCS7PaddingToString(String paramString1, String paramString2)
  {
    byte[] arrayOfByte = encodeCBC256PKCS7Padding(paramString1, paramString2);
    if (arrayOfByte == null) {
      return null;
    }
    String str = Base64.encodeToString(arrayOfByte, 0);
    return str;
  }
  
  public static byte[] encodeCBC256PKCS7Padding(String paramString1, String paramString2)
  {
    if (paramString1 == null) {
      return null;
    }
    byte[] arrayOfByte = null;
    try
    {
      arrayOfByte = paramString1.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      TLog.e(localUnsupportedEncodingException, "%s encodeCBC256PKCS7Padding: %s | %s", new Object[] { "AESCoder", paramString1, paramString2 });
    }
    return encodeCBC256PKCS7Padding(arrayOfByte, paramString2);
  }
  
  public static byte[] encodeCBC256PKCS7Padding(byte[] paramArrayOfByte, String paramString)
  {
    return a(1, paramArrayOfByte, paramString);
  }
  
  public static String decodeCBC256PKCS7PaddingToString(String paramString1, String paramString2)
  {
    byte[] arrayOfByte = decodeCBC256PKCS7Padding(paramString1, paramString2);
    if (arrayOfByte == null) {
      return null;
    }
    String str = new String(arrayOfByte);
    return str;
  }
  
  public static byte[] decodeCBC256PKCS7Padding(String paramString1, String paramString2)
  {
    return decodeCBC256PKCS7Padding(Base64.decode(paramString1, 0), paramString2);
  }
  
  public static String decodeCBC256PKCS7PaddingToString(byte[] paramArrayOfByte, String paramString)
  {
    byte[] arrayOfByte = decodeCBC256PKCS7Padding(paramArrayOfByte, paramString);
    if (arrayOfByte == null) {
      return null;
    }
    String str = new String(arrayOfByte);
    return str;
  }
  
  public static byte[] decodeCBC256PKCS7Padding(byte[] paramArrayOfByte, String paramString)
  {
    return a(2, paramArrayOfByte, paramString);
  }
  
  private static byte[] a(int paramInt, byte[] paramArrayOfByte, String paramString)
  {
    if ((paramArrayOfByte == null) || (paramString == null)) {
      return null;
    }
    try
    {
      SecretKeySpec localSecretKeySpec = a(paramString);
      byte[] arrayOfByte1 = new byte[16];
      Arrays.fill(arrayOfByte1, (byte)0);
      IvParameterSpec localIvParameterSpec = new IvParameterSpec(arrayOfByte1);
      Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
      localCipher.init(paramInt, localSecretKeySpec, localIvParameterSpec);
      byte[] arrayOfByte2 = localCipher.doFinal(paramArrayOfByte);
      return arrayOfByte2;
    }
    catch (Exception localException)
    {
      TLog.e(localException, "%s aesCBC256PKCS7Padding", new Object[] { "AESCoder" });
    }
    return null;
  }
  
  private static SecretKeySpec a(String paramString)
  {
    int i = 256;
    byte[] arrayOfByte1 = new byte[i / 8];
    Arrays.fill(arrayOfByte1, (byte)0);
    byte[] arrayOfByte2 = paramString.getBytes("UTF-8");
    int j = arrayOfByte2.length < arrayOfByte1.length ? arrayOfByte2.length : arrayOfByte1.length;
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, j);
    SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte1, "AES");
    return localSecretKeySpec;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\AESCoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */