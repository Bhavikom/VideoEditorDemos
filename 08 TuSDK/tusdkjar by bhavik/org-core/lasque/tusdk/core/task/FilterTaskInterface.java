package org.lasque.tusdk.core.task;

import android.graphics.Bitmap;
import android.widget.ImageView;
import java.util.List;

public abstract interface FilterTaskInterface
{
  public abstract void setFilerNames(List<String> paramList);
  
  public abstract void setInputImage(Bitmap paramBitmap);
  
  public abstract void start();
  
  public abstract void resetQueues();
  
  public abstract void appendFilterCode(String paramString);
  
  public abstract boolean isRenderFilterThumb();
  
  public abstract void setRenderFilterThumb(boolean paramBoolean);
  
  public abstract void loadImage(ImageView paramImageView, String paramString);
  
  public abstract void cancelLoadImage(ImageView paramImageView);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\task\FilterTaskInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */