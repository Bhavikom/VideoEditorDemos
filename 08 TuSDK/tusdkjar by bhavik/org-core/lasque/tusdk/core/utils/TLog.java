package org.lasque.tusdk.core.utils;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU.GpuType;

public class TLog
{
  private static volatile boolean a = false;
  private static volatile String b;
  public static boolean LOG_AUDIO_DECODEC_INFO = false;
  public static boolean LOG_AUDIO_ENCODEC_INFO = false;
  public static boolean LOG_VIDEO_DECODEC_INFO = false;
  public static boolean LOG_VIDEO_ENCODEC_INFO = false;
  public static boolean LOG_MEDIA_MUXER_INFO = false;
  public static boolean LOG_CURRENT_FPS = false;
  public static boolean LOG_TO_GLOBAL_FILE = false;
  private static FileOutputStream c;
  private static Object d = new Object();
  private static String e = Environment.getExternalStorageDirectory().getPath() + "/TuSdk/LOG_" + StringHelper.timeStampString() + ".log";
  private static Map<String, LinkedList<Long>> f = new HashMap();
  
  public static void enableLogging(String paramString)
  {
    b = paramString;
    a = true;
  }
  
  public static void disableLogging()
  {
    a = false;
  }
  
  public static void enableLog2File(boolean paramBoolean)
  {
    LOG_TO_GLOBAL_FILE = paramBoolean;
  }
  
  public static void d(String paramString, Object... paramVarArgs)
  {
    a(3, null, paramString, paramVarArgs);
  }
  
  public static void i(String paramString, Object... paramVarArgs)
  {
    a(4, null, paramString, paramVarArgs);
  }
  
  public static void w(String paramString, Object... paramVarArgs)
  {
    a(5, null, paramString, paramVarArgs);
  }
  
  public static void e(Throwable paramThrowable)
  {
    a(6, paramThrowable, null, new Object[0]);
  }
  
  public static void e(String paramString, Object... paramVarArgs)
  {
    a(6, null, paramString, paramVarArgs);
  }
  
  public static void e(Throwable paramThrowable, String paramString, Object... paramVarArgs)
  {
    a(6, paramThrowable, paramString, paramVarArgs);
  }
  
  public static void dump(int paramInt, String paramString, Object... paramVarArgs)
  {
    if (!LOG_TO_GLOBAL_FILE) {
      return;
    }
    synchronized (d)
    {
      if (!LOG_TO_GLOBAL_FILE) {
        return;
      }
      if (paramVarArgs.length > 0) {
        paramString = String.format(paramString, paramVarArgs);
      }
      String str = " " + paramInt + " :  " + paramString + "\n";
      try
      {
        a().write(StringHelper.timeStampString().getBytes());
        a().write(str.getBytes());
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        e("write log filed !!!", new Object[0]);
      }
    }
  }
  
  public static void dump(String paramString, Object... paramVarArgs)
  {
    dump(3, paramString, paramVarArgs);
  }
  
  private static void a(int paramInt, Throwable paramThrowable, String paramString, Object... paramVarArgs)
  {
    if (!a) {
      return;
    }
    if (paramVarArgs.length > 0) {
      paramString = String.format(paramString, paramVarArgs);
    }
    String str1;
    if (paramThrowable == null)
    {
      str1 = paramString;
    }
    else
    {
      String str2 = paramString == null ? paramThrowable.getMessage() : paramString;
      String str3 = Log.getStackTraceString(paramThrowable);
      str1 = String.format("%1$s\n%2$s", new Object[] { str2, str3 });
    }
    dump(paramInt, paramString, paramVarArgs);
    Log.println(paramInt, b, str1);
  }
  
  private static FileOutputStream a()
  {
    if (!LOG_TO_GLOBAL_FILE) {
      return null;
    }
    if (c == null) {
      try
      {
        c = new FileOutputStream(e);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("\n");
        localStringBuilder.append("Vender : " + TuSdkDeviceInfo.getVender() + "\n");
        localStringBuilder.append("Model : " + TuSdkDeviceInfo.getModel() + "\n");
        localStringBuilder.append("OSVersion : " + TuSdkDeviceInfo.getOSVersion() + "\n");
        localStringBuilder.append("Core :3.1.1\n");
        localStringBuilder.append("GPUInfo : " + SelesContext.getGpuInfo() + "\n");
        localStringBuilder.append("CPUInfo : " + SelesContext.getCpuType() + "\n");
        localStringBuilder.append("isSupportGL2 : " + SelesContext.isSupportGL2() + "\n");
        localStringBuilder.append("MaxTextureSize : " + SelesContext.getMaxTextureSize() + "\n");
        localStringBuilder.append("GPURank:" + TuSdkGPU.getGpuType().toString() + "\n\n\n");
        c.write(localStringBuilder.toString().getBytes());
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        e("open out put file failed!", new Object[0]);
      }
    }
    return c;
  }
  
  public static void fps(String paramString)
  {
    if (LOG_CURRENT_FPS) {
      d("[debug]" + paramString + "FPS:" + a(a(paramString)), new Object[0]);
    }
  }
  
  private static double a(LinkedList<Long> paramLinkedList)
  {
    long l = System.nanoTime();
    double d1 = (l - ((Long)paramLinkedList.getFirst()).longValue()) / 1.0E9D;
    paramLinkedList.addLast(Long.valueOf(l));
    int i = paramLinkedList.size();
    if (i > 100) {
      paramLinkedList.removeFirst();
    }
    return d1 > 0.0D ? paramLinkedList.size() / d1 : 0.0D;
  }
  
  private static LinkedList<Long> a(String paramString)
  {
    Object localObject = (LinkedList)f.get(paramString);
    if (localObject == null)
    {
      localObject = new LinkedList() {};
      f.put(paramString, localObject);
    }
    return (LinkedList<Long>)localObject;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\TLog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */