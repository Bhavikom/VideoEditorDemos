package org.lasque.tusdk.core.utils;

import java.util.HashMap;

public final class NativeLibraryHelper
{
  private static NativeLibraryHelper a;
  private HashMap<String, String> b = new HashMap();
  
  public static NativeLibraryHelper shared()
  {
    if (a == null) {
      a = new NativeLibraryHelper();
    }
    return a;
  }
  
  public void mapLibrary(NativeLibType paramNativeLibType, String paramString)
  {
    this.b.put(paramNativeLibType.libName(), paramString);
  }
  
  public void loadLibrary(NativeLibType paramNativeLibType)
  {
    if (this.b.containsKey(paramNativeLibType.libName()))
    {
      String str = (String)this.b.get(paramNativeLibType.libName());
      System.load(str);
    }
    else
    {
      System.loadLibrary(paramNativeLibType.libName());
    }
  }
  
  public static enum NativeLibType
  {
    private String a;
    
    private NativeLibType(String paramString)
    {
      this.a = paramString;
    }
    
    public String libName()
    {
      return this.a;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\NativeLibraryHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */