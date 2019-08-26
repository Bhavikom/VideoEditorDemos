package org.lasque.tusdk.impl.activity;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import java.io.File;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.modules.components.ComponentErrorType;

public abstract class TuImageResultFragment
  extends TuResultFragment
{
  protected float mScreenSizeScale = 0.75F;
  private File a;
  private ImageSqlInfo b;
  private Bitmap c;
  private boolean d;
  private boolean e;
  private ImageView f;
  private View.OnClickListener g = new TuSdkViewHelper.OnSafeClickListener()
  {
    public void onSafeClick(View paramAnonymousView)
    {
      if (TuImageResultFragment.this.equalViewIds(paramAnonymousView, TuImageResultFragment.this.getPreview())) {
        TuImageResultFragment.this.handleTest();
      }
    }
  };
  
  public File getTempFilePath()
  {
    return this.a;
  }
  
  public void setTempFilePath(File paramFile)
  {
    this.a = paramFile;
  }
  
  public ImageSqlInfo getImageSqlInfo()
  {
    return this.b;
  }
  
  public void setImageSqlInfo(ImageSqlInfo paramImageSqlInfo)
  {
    this.b = paramImageSqlInfo;
  }
  
  public Bitmap getImage()
  {
    if (this.c == null)
    {
      TuSdkSize localTuSdkSize = getImageDisplaySize();
      this.c = BitmapHelper.getBitmap(getTempFilePath(), localTuSdkSize, true);
      if (this.c == null) {
        this.c = BitmapHelper.getBitmap(getImageSqlInfo(), true, localTuSdkSize);
      }
    }
    return this.c;
  }
  
  public TuSdkSize getImageDisplaySize()
  {
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(getActivity());
    if (localTuSdkSize != null)
    {
      localTuSdkSize.width = ((int)Math.floor(localTuSdkSize.width * this.mScreenSizeScale));
      localTuSdkSize.height = ((int)Math.floor(localTuSdkSize.height * this.mScreenSizeScale));
    }
    return localTuSdkSize;
  }
  
  public void setImage(Bitmap paramBitmap)
  {
    this.c = paramBitmap;
  }
  
  public boolean isShowResultPreview()
  {
    return this.d;
  }
  
  public void setShowResultPreview(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }
  
  public boolean isAutoRemoveTemp()
  {
    return this.e;
  }
  
  public void setAutoRemoveTemp(boolean paramBoolean)
  {
    this.e = paramBoolean;
  }
  
  public void onDestroyView()
  {
    if (isAutoRemoveTemp()) {
      FileHelper.delete(getTempFilePath());
    }
    super.onDestroyView();
  }
  
  public void loadOrginImage(TuSdkResult paramTuSdkResult)
  {
    if (paramTuSdkResult == null) {
      return;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(TuSdkGPU.getMaxTextureOptimizedSize());
    localTuSdkSize = localTuSdkSize.limitSize();
    paramTuSdkResult.image = BitmapHelper.getBitmap(getTempFilePath(), localTuSdkSize, true);
    if (paramTuSdkResult.image == null) {
      paramTuSdkResult.image = BitmapHelper.getBitmap(getImageSqlInfo(), true, localTuSdkSize);
    }
    if (paramTuSdkResult.image == null) {
      paramTuSdkResult.image = getImage();
    }
  }
  
  public void processingImage() {}
  
  protected void loadImageWithThread()
  {
    hubStatus(TuSdkContext.getString("lsq_edit_loading"));
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        final Bitmap localBitmap = TuImageResultFragment.this.asyncLoadImage();
        final boolean bool = TuImageResultFragment.this.preProcessWithImage(localBitmap);
        TuImageResultFragment.this.runOnUiThread(new Runnable()
        {
          public void run()
          {
            if (!bool) {
              TuImageResultFragment.this.hubDismiss();
            }
            TuImageResultFragment.this.asyncLoadImageCompleted(localBitmap);
            if (bool) {
              TuImageResultFragment.this.postProcessWithImage(localBitmap);
            }
          }
        });
      }
    });
  }
  
  protected boolean preProcessWithImage(Bitmap paramBitmap)
  {
    return false;
  }
  
  protected Bitmap asyncLoadImage()
  {
    return getImage();
  }
  
  protected void asyncLoadImageCompleted(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
    {
      notifyError(null, ComponentErrorType.TypeInputImageEmpty);
      TLog.e("Can not find input image.", new Object[0]);
      return;
    }
  }
  
  protected void postProcessWithImage(Bitmap paramBitmap)
  {
    hubDismiss();
  }
  
  public Bitmap getCuterImage(Bitmap paramBitmap, TuSdkResult paramTuSdkResult)
  {
    if (paramTuSdkResult == null) {
      return paramBitmap;
    }
    return getCuterImage(paramBitmap, paramTuSdkResult.cutRect, paramTuSdkResult.imageOrientation, 0.0F);
  }
  
  public Bitmap getCuterImage(Bitmap paramBitmap, RectF paramRectF, ImageOrientation paramImageOrientation, float paramFloat)
  {
    if (paramBitmap == null) {
      return paramBitmap;
    }
    if (paramRectF != null) {
      paramBitmap = BitmapHelper.imageCorp(paramBitmap, paramRectF, paramImageOrientation);
    } else if (paramImageOrientation != null) {
      paramBitmap = BitmapHelper.imageRotaing(paramBitmap, paramImageOrientation);
    } else if (paramFloat > 0.0F) {
      paramBitmap = BitmapHelper.imageCorp(paramBitmap, paramFloat);
    }
    return paramBitmap;
  }
  
  public ImageView getPreview()
  {
    if ((this.f == null) && (getRootView() != null) && (isShowResultPreview()))
    {
      this.f = new ImageView(getActivity());
      this.f.setScaleType(ImageView.ScaleType.FIT_CENTER);
      this.f.setBackgroundColor(-7829368);
      getRootView().addView(this.f, new RelativeLayout.LayoutParams(-1, -1));
      this.f.setOnClickListener(this.g);
      showViewIn(this.f, false);
    }
    return this.f;
  }
  
  protected boolean showResultPreview(TuSdkResult paramTuSdkResult)
  {
    if (!isShowResultPreview()) {
      return false;
    }
    ImageView localImageView = getPreview();
    if (isSaveToAlbum()) {
      paramTuSdkResult.image = BitmapHelper.getBitmap(paramTuSdkResult.imageSqlInfo);
    } else if (isSaveToTemp()) {
      paramTuSdkResult.image = BitmapHelper.getBitmap(paramTuSdkResult.imageFile);
    } else {
      hubDismiss();
    }
    TLog.d("showResultPreview: %s", new Object[] { paramTuSdkResult });
    localImageView.setImageBitmap(paramTuSdkResult.image);
    showViewIn(localImageView, true);
    return true;
  }
  
  protected void handleTest()
  {
    showViewIn(getPreview(), false);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\activity\TuImageResultFragment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */