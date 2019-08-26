package org.lasque.tusdk.core.utils.hardware;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkGPU
{
  private static int a;
  private static int b = 2000;
  private static String c;
  private static GpuType d = new GpuType(0, 2000, 0, 2);
  private static boolean e;
  private static boolean f;
  private static Hashtable<String, ArrayList<GpuType>> g = new Hashtable();
  private static int[] h = { 505, 506, 530, 540 };
  
  public static int getMaxTextureSize()
  {
    return a;
  }
  
  public static int getMaxTextureOptimizedSize()
  {
    return b;
  }
  
  public static String getGpuInfo()
  {
    return c;
  }
  
  public static GpuType getGpuType()
  {
    if (d == null) {
      d = new GpuType(0, 2000, 0, 2);
    }
    return d;
  }
  
  public static boolean isSupporTurbo()
  {
    return f;
  }
  
  public static boolean isLiveStickerSupported()
  {
    return getGpuType().getPerformance() >= 3;
  }
  
  public static boolean isFaceBeautySupported()
  {
    return getGpuType().getPerformance() >= 3;
  }
  
  public static void init(int paramInt, String paramString)
  {
    if ((e) || (paramString == null)) {
      return;
    }
    e = true;
    a();
    a = paramInt;
    c = paramString;
    a(c);
  }
  
  private static void a()
  {
    ArrayList localArrayList = null;
    g.put("Mali", localArrayList = new ArrayList());
    localArrayList.add(new GpuTypeMali(300, 2000, 0, 2));
    localArrayList.add(new GpuTypeMali(400, 2000, 0, 2));
    localArrayList.add(new GpuTypeMali(400, 3000, 4, 3));
    localArrayList.add(new GpuTypeMali(450, 4000, 4, 5));
    localArrayList.add(new GpuTypeMali(604, 3000, 4, 3));
    localArrayList.add(new GpuTypeMali(622, 2800, 4, 2));
    localArrayList.add(new GpuTypeMali(624, 4000, 4, 4));
    localArrayList.add(new GpuTypeMali(628, 4000, 6, 4));
    localArrayList.add(new GpuTypeMali(760, 4000, 6, 4));
    localArrayList.add(new GpuTypeMali(880, 4000, 6, 5));
    localArrayList.add(new GpuTypeMali(7100, 4000, 6, 5));
    localArrayList.add(new GpuTypeMali(7200, 4000, 6, 5));
    g.put("Adreno", localArrayList = new ArrayList());
    localArrayList.add(new GpuTypeAdreno(130, 2000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(200, 2000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(203, 2000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(205, 2000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(220, 2000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(225, 2000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(302, 2000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(304, 2000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(305, 2000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(306, 2000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(320, 4000, 0, 2));
    localArrayList.add(new GpuTypeAdreno(330, 4000, 0, 4));
    localArrayList.add(new GpuTypeAdreno(405, 4000, 0, 3));
    localArrayList.add(new GpuTypeAdreno(418, 4000, 0, 4));
    localArrayList.add(new GpuTypeAdreno(420, 4000, 0, 4));
    localArrayList.add(new GpuTypeAdreno(430, 4000, 0, 5));
    localArrayList.add(new GpuTypeAdreno(505, 4000, 0, 3));
    localArrayList.add(new GpuTypeAdreno(506, 4000, 0, 3));
    localArrayList.add(new GpuTypeAdreno(510, 4000, 0, 4));
    localArrayList.add(new GpuTypeAdreno(512, 4000, 0, 5));
    localArrayList.add(new GpuTypeAdreno(530, 4000, 0, 5));
    localArrayList.add(new GpuTypeAdreno(540, 4000, 0, 5));
    localArrayList.add(new GpuTypeAdreno(630, 4000, 0, 5));
    g.put("PowerVR", localArrayList = new ArrayList());
    localArrayList.add(new GpuTypePowerVR(530, 2000, 0, 2));
    localArrayList.add(new GpuTypePowerVR(531, 2000, 0, 2));
    localArrayList.add(new GpuTypePowerVR(535, 2000, 0, 2));
    localArrayList.add(new GpuTypePowerVR(540, 2000, 0, 2));
    localArrayList.add(new GpuTypePowerVR(543, 2000, 4, 3));
    localArrayList.add(new GpuTypePowerVR(544, 3000, 0, 3));
    localArrayList.add(new GpuTypePowerVR(544, 3000, 3, 4));
    localArrayList.add(new GpuTypePowerVR(6200, 4000, 0, 5));
    localArrayList.add(new GpuTypePowerVR(7400, 4000, 4, 5));
    localArrayList.add(new GpuTypePowerVR(8100, 3500, 1, 5));
    localArrayList.add(new GpuTypePowerVR(8200, 4000, 1, 5));
    g.put("Nvidia", localArrayList = new ArrayList());
    localArrayList.add(new GpuTypeNvidia(3, 2500, 0, 4));
    g.put("Immersion", localArrayList = new ArrayList());
    localArrayList.add(new GpuTypeImmersion(16, 3000, 0, 2));
    g.put("Vivante", localArrayList = new ArrayList());
    localArrayList.add(new GpuTypeVivante(1000, 2000, 0, 2));
    localArrayList.add(new GpuTypeVivante(2000, 2000, 0, 2));
    localArrayList.add(new GpuTypeVivante(4000, 4000, 0, 2));
  }
  
  @SuppressLint({"DefaultLocale"})
  private static void a(String paramString)
  {
    if (paramString == null) {
      return;
    }
    paramString = paramString.toLowerCase();
    int i = 0;
    Iterator localIterator = g.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (paramString.contains(((String)localEntry.getKey()).toLowerCase()))
      {
        a((String)localEntry.getKey(), (ArrayList)localEntry.getValue(), paramString);
        i = 1;
        break;
      }
    }
    if (i == 0) {
      b(paramString);
    }
    b();
  }
  
  private static void a(String paramString1, ArrayList<GpuType> paramArrayList, String paramString2)
  {
    GpuType localGpuType1 = (GpuType)ReflectUtils.classInstance(((GpuType)paramArrayList.get(0)).getClass());
    localGpuType1.matchInfo(paramString2);
    Object localObject = null;
    for (int i = paramArrayList.size() - 1; i > -1; i--)
    {
      GpuType localGpuType2 = (GpuType)paramArrayList.get(i);
      if (localGpuType1.getCode() >= localGpuType2.getCode())
      {
        localObject = localGpuType2;
        break;
      }
    }
    if (localObject == null) {
      localObject = (GpuType)paramArrayList.get(0);
    }
    d = (GpuType)localObject;
  }
  
  private static void b(String paramString)
  {
    if (paramString == null) {
      return;
    }
    if (paramString.contains("gc1000")) {
      d = new GpuTypeVivante(1000, 2000, 0, 2);
    }
  }
  
  private static void b()
  {
    if (d == null) {
      return;
    }
    if (a > 0) {
      d.setSize(Math.min(d.getSize(), a));
    }
    b = GpuType.a(d);
    if ((!(d instanceof GpuTypeNvidia)) || (d.getCode() > 2)) {
      f = true;
    }
    TLog.d("GPU info: %s %s", new Object[] { c, d });
  }
  
  public static boolean lowPerformance()
  {
    return ((getGpuType() instanceof GpuTypeNvidia)) || ((getGpuType() instanceof GpuTypeImmersion)) || ((getGpuType() instanceof GpuTypeVivante));
  }
  
  public static class GpuTypeVivante
    extends TuSdkGPU.GpuType
  {
    public GpuTypeVivante() {}
    
    public GpuTypeVivante(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(paramInt2, paramInt3, paramInt4);
    }
    
    public void matchInfo(String paramString)
    {
      super.matchInfo(paramString);
      String str = StringHelper.matchString(paramString, "([0-9]+)");
      setCode(StringHelper.parserInt(str));
    }
  }
  
  public static class GpuTypeImmersion
    extends TuSdkGPU.GpuType
  {
    public GpuTypeImmersion() {}
    
    public GpuTypeImmersion(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(paramInt2, paramInt3, paramInt4);
    }
    
    public void matchInfo(String paramString)
    {
      super.matchInfo(paramString);
      String str = StringHelper.matchString(paramString, "\\.?([0-9]+)");
      setCode(StringHelper.parserInt(str));
    }
  }
  
  public static class GpuTypeNvidia
    extends TuSdkGPU.GpuType
  {
    public GpuTypeNvidia() {}
    
    public GpuTypeNvidia(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(paramInt2, paramInt3, paramInt4);
    }
    
    public void matchInfo(String paramString)
    {
      if (paramString == null) {
        return;
      }
      if (paramString.contains("Tegra"))
      {
        setCode(1);
        setSize(2000);
        setPerformance(2);
      }
    }
  }
  
  public static class GpuTypeAdreno
    extends TuSdkGPU.GpuType
  {
    public GpuTypeAdreno() {}
    
    public GpuTypeAdreno(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(paramInt2, paramInt3, paramInt4);
    }
    
    public void matchInfo(String paramString)
    {
      super.matchInfo(paramString);
      String str = StringHelper.matchString(paramString, "[ ]([0-9]+)");
      setCode(StringHelper.parserInt(str));
    }
  }
  
  public static class GpuTypePowerVR
    extends TuSdkGPU.GpuType
  {
    public GpuTypePowerVR() {}
    
    public GpuTypePowerVR(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(paramInt2, paramInt3, paramInt4);
    }
    
    public void matchInfo(String paramString)
    {
      super.matchInfo(paramString);
      String str = "";
      if (paramString.indexOf("sgx") != -1) {
        str = StringHelper.matchString(paramString, "sgx[ ]*([0-9]+)");
      }
      if (paramString.indexOf("marlowe") != -1)
      {
        str = "7400";
        setMp(4);
      }
      else if (paramString.indexOf("rogue") != -1)
      {
        str = StringHelper.matchString(paramString, "rogue[ ]*g[a-z]*([0-9]+)");
      }
      else
      {
        str = StringHelper.matchString(paramString, "[ ]*g[a-z]*([0-9]+)");
      }
      setCode(StringHelper.parserInt(str));
    }
  }
  
  public static class GpuTypeMali
    extends TuSdkGPU.GpuType
  {
    public GpuTypeMali() {}
    
    public GpuTypeMali(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(paramInt2, paramInt3, paramInt4);
    }
    
    public void matchInfo(String paramString)
    {
      super.matchInfo(paramString);
      String str = StringHelper.matchString(paramString, "-[a-z]*([0-9]+)");
      int i = StringHelper.parserInt(str);
      if (StringHelper.matchString(paramString, "-g([0-9]+)") != null) {
        i *= 100;
      }
      setCode(i);
    }
  }
  
  public static class GpuType
  {
    private int a;
    private int b;
    private int c;
    private int d;
    
    public GpuType() {}
    
    public GpuType(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      this.a = paramInt1;
      this.b = paramInt2;
      this.c = paramInt3;
      this.d = paramInt4;
    }
    
    public int getCode()
    {
      return this.a;
    }
    
    public void setCode(int paramInt)
    {
      this.a = paramInt;
    }
    
    public int getSize()
    {
      return this.b;
    }
    
    public void setSize(int paramInt)
    {
      this.b = paramInt;
    }
    
    public int getMp()
    {
      return this.c;
    }
    
    public void setMp(int paramInt)
    {
      this.c = paramInt;
    }
    
    public int getPerformance()
    {
      return this.d;
    }
    
    public void setPerformance(int paramInt)
    {
      this.d = paramInt;
    }
    
    public void matchInfo(String paramString)
    {
      String str = StringHelper.matchString(paramString, "mp([0-9]+)");
      this.c = StringHelper.parserInt(str);
    }
    
    public String toString()
    {
      String str = String.format("%s - {code: %s, mp: %s, size: %s, pf: %s}", new Object[] { getClass().getSimpleName(), Integer.valueOf(this.a), Integer.valueOf(this.c), Integer.valueOf(this.b), Integer.valueOf(this.d) });
      return str;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkGPU.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */