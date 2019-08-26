package org.lasque.tusdk.core.task;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Iterator;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.ImageLoaderHelper;

public abstract class ImageViewTask<T extends ImageViewTaskWare>
{
  private final ArrayList<T> a = new ArrayList();
  private Handler b = new Handler(Looper.getMainLooper());
  private boolean c;
  private T d;
  
  public void resetQueues()
  {
    this.a.clear();
    TLog.d("%s resetQueues", new Object[] { getClass().getName() });
  }
  
  protected void finalize()
  {
    resetQueues();
    super.finalize();
  }
  
  protected abstract String getCacheKey(T paramT);
  
  protected abstract Bitmap asyncTaskLoadImage(T paramT);
  
  private ArrayList<T> a()
  {
    return new ArrayList(this.a);
  }
  
  public void cancelLoadImage(ImageView paramImageView)
  {
    if (paramImageView == null) {
      return;
    }
    if ((this.d != null) && (this.d.isEqualView(paramImageView))) {
      this.d.cancel();
    }
    ArrayList localArrayList = a();
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      ImageViewTaskWare localImageViewTaskWare = (ImageViewTaskWare)localIterator.next();
      if (localImageViewTaskWare.isEqualView(paramImageView))
      {
        localImageViewTaskWare.cancel();
        this.a.remove(localImageViewTaskWare);
      }
    }
  }
  
  public void loadImage(T paramT)
  {
    if (paramT == null) {
      return;
    }
    cancelLoadImage(paramT.getImageView());
    String str = getCacheKey(paramT);
    Bitmap localBitmap = ImageLoaderHelper.loadMemoryBitmap(str, null);
    if (localBitmap != null)
    {
      paramT.imageLoaded(localBitmap, ImageViewTaskWare.LoadType.TypeMomery);
      return;
    }
    submitTask(paramT);
  }
  
  public void submitTask(T paramT)
  {
    if (paramT != null) {
      this.a.add(paramT);
    }
    if (this.c) {
      return;
    }
    this.d = b();
    if (this.d == null) {
      return;
    }
    this.c = true;
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        try
        {
          ImageViewTask.a(ImageViewTask.this, ImageViewTask.a(ImageViewTask.this));
        }
        catch (Exception localException)
        {
          TLog.e(localException, "ImageViewTask: %s", new Object[] { getClass() });
        }
      }
    });
  }
  
  private void a(T paramT)
  {
    String str = getCacheKey(paramT);
    if (TextUtils.isEmpty(str)) {
      return;
    }
    Bitmap localBitmap = ImageLoaderHelper.loadDiscBitmap(str);
    ImageViewTaskWare.LoadType localLoadType = ImageViewTaskWare.LoadType.TypeDisk;
    if (localBitmap == null)
    {
      localBitmap = asyncTaskLoadImage(paramT);
      localLoadType = ImageViewTaskWare.LoadType.TypeBuild;
    }
    a(paramT, localBitmap, localLoadType);
    if (localLoadType == ImageViewTaskWare.LoadType.TypeDisk) {
      ImageLoaderHelper.saveToMemoryCache(str, localBitmap, null);
    } else if (paramT.isSaveToDisk()) {
      ImageLoaderHelper.save(str, localBitmap, paramT.getImageCompress());
    }
  }
  
  private void a(final T paramT, final Bitmap paramBitmap, final ImageViewTaskWare.LoadType paramLoadType)
  {
    this.b.post(new Runnable()
    {
      public void run()
      {
        ImageViewTask.a(ImageViewTask.this, paramT, paramBitmap, paramLoadType);
      }
    });
  }
  
  private void b(T paramT, Bitmap paramBitmap, ImageViewTaskWare.LoadType paramLoadType)
  {
    paramT.imageLoaded(paramBitmap, paramLoadType);
    this.c = false;
    submitTask(null);
  }
  
  private T b()
  {
    if (this.a.size() == 0) {
      return null;
    }
    ImageViewTaskWare localImageViewTaskWare = null;
    do
    {
      localImageViewTaskWare = (ImageViewTaskWare)this.a.remove(0);
      if ((localImageViewTaskWare == null) || (localImageViewTaskWare.getImageView() == null) || (localImageViewTaskWare.isCancel())) {
        localImageViewTaskWare = null;
      } else {
        return localImageViewTaskWare;
      }
    } while (this.a.size() > 0);
    return null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\task\ImageViewTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */