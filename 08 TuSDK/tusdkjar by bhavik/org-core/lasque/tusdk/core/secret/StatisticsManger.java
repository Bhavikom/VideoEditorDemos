package org.lasque.tusdk.core.secret;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import org.lasque.tusdk.core.network.TuSdkHttpHandler;
import org.lasque.tusdk.core.network.TuSdkHttpParams;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.utils.ByteUtils;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkDate;
import org.lasque.tusdk.core.utils.TuSdkLocation;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;

public class StatisticsManger
{
  private static String a = "tusdk.statistics";
  private static long b = 256L;
  private static StatisticsManger c;
  private final Context d;
  private final File e;
  private final List<StatisticData> f;
  private boolean g;
  private boolean h;
  
  public static void init(Context paramContext, File paramFile)
  {
    if ((c == null) && (paramContext != null)) {
      c = new StatisticsManger(paramContext, paramFile);
    }
  }
  
  public static void appendComponent(long paramLong)
  {
    if (c == null) {
      return;
    }
    StatisticData localStatisticData = new StatisticData(StatisticsType.Component, paramLong, null);
    c.a(localStatisticData);
  }
  
  public static void appendFilter(FilterOption paramFilterOption)
  {
    if ((c == null) || (paramFilterOption == null) || (paramFilterOption.id == 0L)) {
      return;
    }
    StatisticData localStatisticData = new StatisticData(StatisticsType.Filter, paramFilterOption.id, null);
    c.a(localStatisticData);
  }
  
  public static void appendSticker(StickerData paramStickerData)
  {
    if ((c == null) || (paramStickerData == null)) {
      return;
    }
    StatisticData localStatisticData = new StatisticData(StatisticsType.Sticker, paramStickerData.stickerId, null);
    c.a(localStatisticData);
  }
  
  public static void appendBrush(BrushData paramBrushData)
  {
    if ((c == null) || (paramBrushData == null)) {
      return;
    }
    StatisticData localStatisticData = new StatisticData(StatisticsType.Brush, paramBrushData.brushId, null);
    c.a(localStatisticData);
  }
  
  private synchronized boolean a()
  {
    return this.g;
  }
  
  private synchronized void a(boolean paramBoolean)
  {
    this.g = paramBoolean;
  }
  
  private StatisticsManger(Context paramContext, File paramFile)
  {
    this.d = paramContext;
    this.f = new ArrayList();
    this.e = paramFile;
    this.h = (!TuSdkHttpEngine.DEBUG);
    TuSdkLocation.init(paramContext);
    new Thread(new Runnable()
    {
      public void run()
      {
        StatisticsManger.a(StatisticsManger.this);
      }
    }).start();
  }
  
  private void b()
  {
    if ((this.e == null) || (!this.e.exists()) || (!this.h)) {
      return;
    }
    File localFile = new File(this.e, a);
    byte[] arrayOfByte = FileHelper.getBytesFromFile(localFile);
    StatisticData localStatisticData = new StatisticData(StatisticsType.Component, ComponentActType.sdkLoadedComponent, null);
    b(localStatisticData);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      if (arrayOfByte != null) {
        localByteArrayOutputStream.write(arrayOfByte);
      }
      localByteArrayOutputStream.write(StatisticData.a(localStatisticData));
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "asyncLoadCacheData", new Object[0]);
    }
    final ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(localByteArrayOutputStream.toByteArray());
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      public void run()
      {
        StatisticsManger.a(StatisticsManger.this, localByteArrayInputStream);
      }
    });
  }
  
  private void c()
  {
    File localFile1 = new File(this.e, a);
    File localFile2 = new File(this.e, String.format("%s.%s", new Object[] { a, Long.valueOf(TuSdkDate.create().getTimeInMillis()) }));
    FileHelper.rename(localFile1, localFile2);
    FileHelper.delete(localFile1);
    FileHelper.delete(localFile2);
    if (LogStashManager.getInstance() == null) {
      return;
    }
    LogStashManager.getInstance().deleteTempFile();
  }
  
  private void a(InputStream paramInputStream)
  {
    a(true);
    TuSdkHttpHandler local3 = new TuSdkHttpHandler()
    {
      protected void onRequestedSucceed(TuSdkHttpHandler paramAnonymousTuSdkHttpHandler) {}
      
      protected void onRequestedFinish(TuSdkHttpHandler paramAnonymousTuSdkHttpHandler)
      {
        StatisticsManger.a(StatisticsManger.this, false);
        StatisticsManger.b(StatisticsManger.this);
      }
    };
    TuSdkHttpParams localTuSdkHttpParams = new TuSdkHttpParams();
    localTuSdkHttpParams.put("statistics", paramInputStream);
    if (LogStashManager.getInstance() != null)
    {
      LogStashManager.LogBean localLogBean = LogStashManager.getInstance().getUpLoadData();
      if ((localLogBean != null) && (localLogBean.isValid())) {
        try
        {
          int i = Integer.valueOf(localLogBean.getIndex()).intValue();
          localTuSdkHttpParams.put("key_index", i + "");
          localTuSdkHttpParams.put("loginfo", localLogBean.getByteArrayInputStream());
        }
        catch (Exception localException) {}
      }
    }
    TuSdkHttpEngine.shared().post("/put", localTuSdkHttpParams, true, local3);
  }
  
  private void a(StatisticData paramStatisticData)
  {
    if ((paramStatisticData == null) || (!this.h)) {
      return;
    }
    b(paramStatisticData);
    synchronized (this.f)
    {
      this.f.add(paramStatisticData);
      c(paramStatisticData);
      d();
    }
  }
  
  private void b(StatisticData paramStatisticData)
  {
    if (paramStatisticData == null) {
      return;
    }
    StatisticData.a(paramStatisticData, TuSdkDate.create().getTimeInSeconds());
    Location localLocation = TuSdkLocation.getLastLocation();
    if (localLocation != null)
    {
      StatisticData.a(paramStatisticData, localLocation.getLongitude());
      StatisticData.b(paramStatisticData, localLocation.getLatitude());
    }
  }
  
  private void c(StatisticData paramStatisticData)
  {
    RandomAccessFile localRandomAccessFile = null;
    try
    {
      File localFile = new File(this.e, a);
      localRandomAccessFile = new RandomAccessFile(localFile, "rw");
      localRandomAccessFile.seek(localRandomAccessFile.length());
      localRandomAccessFile.write(StatisticData.a(paramStatisticData));
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException, "asyncFlushData: %s", new Object[] { Long.valueOf(StatisticData.b(paramStatisticData)) });
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "asyncFlushData: %s", new Object[] { Long.valueOf(StatisticData.b(paramStatisticData)) });
    }
    finally
    {
      FileHelper.safeClose(localRandomAccessFile);
    }
  }
  
  private void d()
  {
    if ((a()) || (this.f.size() < b)) {
      return;
    }
    final ArrayList localArrayList = new ArrayList(this.f);
    this.f.clear();
    c();
    new Thread(new Runnable()
    {
      public void run()
      {
        StatisticsManger.this.asyncConvertData(localArrayList);
      }
    }).start();
  }
  
  protected void asyncConvertData(List<StatisticData> paramList)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    final Object localObject = paramList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      StatisticData localStatisticData = (StatisticData)((Iterator)localObject).next();
      try
      {
        localByteArrayOutputStream.write(StatisticData.a(localStatisticData));
      }
      catch (IOException localIOException)
      {
        TLog.e(localIOException, "asyncConvertData", new Object[0]);
      }
    }
    localObject = new ByteArrayInputStream(localByteArrayOutputStream.toByteArray());
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      public void run()
      {
        StatisticsManger.a(StatisticsManger.this, localObject);
      }
    });
  }
  
  private static class StatisticData
  {
    private byte a;
    private long b;
    private long c;
    private double d;
    private double e;
    
    private StatisticData(byte paramByte, long paramLong)
    {
      this.a = paramByte;
      this.b = paramLong;
    }
    
    private byte[] a()
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(33);
      localByteArrayOutputStream.write(this.a);
      localByteArrayOutputStream.write(ByteUtils.getBytes(this.b));
      localByteArrayOutputStream.write(ByteUtils.getBytes(this.c));
      localByteArrayOutputStream.write(ByteUtils.getBytes(this.d));
      localByteArrayOutputStream.write(ByteUtils.getBytes(this.e));
      return localByteArrayOutputStream.toByteArray();
    }
  }
  
  public static class StatisticsType
  {
    public static byte Component = 16;
    public static byte Filter = 32;
    public static byte Sticker = 48;
    public static byte Brush = 64;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\StatisticsManger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */