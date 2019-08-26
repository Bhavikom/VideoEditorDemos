package org.lasque.tusdk.core.api;

import android.graphics.Bitmap;
import java.util.List;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract class TuSDKImageFilterAPI
{
  protected abstract FilterWrap getFilterWrap();
  
  public List<String> getArgKeys()
  {
    if (getFilterWrap() == null) {
      return null;
    }
    return getFilterWrap().getFilterParameter().getArgKeys();
  }
  
  protected void submitFilterParameter()
  {
    if (getFilterWrap() == null) {
      return;
    }
    getFilterWrap().submitFilterParameter();
  }
  
  public boolean setFilterArgPrecentValue(String paramString, float paramFloat)
  {
    if (getFilterWrap() == null) {
      return false;
    }
    SelesParameters.FilterArg localFilterArg = getFilterWrap().getFilterParameter().getFilterArg(paramString);
    if (localFilterArg == null)
    {
      TLog.e("setFilterArgPrecentValue Key : %s  does not exist", new Object[] { paramString });
      return false;
    }
    localFilterArg.setPrecentValue(paramFloat);
    return true;
  }
  
  public float getFilterArgPrecentValue(String paramString)
  {
    if (getFilterWrap() == null) {
      return 0.0F;
    }
    SelesParameters.FilterArg localFilterArg = getFilterWrap().getFilterParameter().getFilterArg(paramString);
    if (localFilterArg == null)
    {
      TLog.e("setFilterArg Invalid key : %s", new Object[] { paramString });
      return 0.0F;
    }
    return localFilterArg.getPrecentValue();
  }
  
  public void reset()
  {
    if (getFilterWrap() == null) {
      return;
    }
    getFilterWrap().getFilterParameter().reset();
  }
  
  public Bitmap process(Bitmap paramBitmap)
  {
    return process(paramBitmap, ImageOrientation.Up);
  }
  
  public Bitmap process(Bitmap paramBitmap, ImageOrientation paramImageOrientation)
  {
    return process(paramBitmap, paramImageOrientation, 0.0F);
  }
  
  public final Bitmap process(Bitmap paramBitmap, ImageOrientation paramImageOrientation, float paramFloat)
  {
    if (!a()) {
      return paramBitmap;
    }
    if ((getFilterWrap() == null) || (paramBitmap == null)) {
      return paramBitmap;
    }
    float f = TuSdkSize.create(paramBitmap).limitScale();
    Bitmap localBitmap = BitmapHelper.imageScale(paramBitmap, f);
    submitFilterParameter();
    return getFilterWrap().process(localBitmap, paramImageOrientation, paramFloat);
  }
  
  private boolean a()
  {
    if (!SdkValid.shared.sdkValid())
    {
      TLog.e("Configuration not found! Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
      return false;
    }
    if (SdkValid.shared.isExpired())
    {
      TLog.e("Your account has expired Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
      return false;
    }
    return true;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\TuSDKImageFilterAPI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */