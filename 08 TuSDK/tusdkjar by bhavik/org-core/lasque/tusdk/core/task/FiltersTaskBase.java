package org.lasque.tusdk.core.task;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract class FiltersTaskBase
  implements FilterTaskInterface
{
  public static final String TAG = "FiltersTaskBase";
  private File a;
  private ArrayList<FiltersTaskImageWare> b = new ArrayList();
  private List<String> c = new ArrayList();
  private Hashtable<String, SoftReference<Bitmap>> d = new Hashtable();
  private Bitmap e;
  private Handler f = new Handler(Looper.getMainLooper());
  private ExecutorService g;
  private boolean h;
  
  public File getSampleRootPath()
  {
    return this.a;
  }
  
  public void setSampleRootPath(File paramFile)
  {
    this.a = paramFile;
  }
  
  private ArrayList<FiltersTaskImageWare> a()
  {
    if (this.b == null) {
      this.b = new ArrayList();
    }
    return new ArrayList(this.b);
  }
  
  public List<String> getFilerNames()
  {
    return this.c;
  }
  
  public void setFilerNames(List<String> paramList)
  {
    List localList = FilterLocalPackage.shared().verifyCodes(paramList);
    if (localList == null) {
      return;
    }
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      appendFilterCode(str);
    }
  }
  
  public void appendFilterCode(String paramString)
  {
    if ((paramString != null) && (!this.c.contains(paramString))) {
      this.c.add(paramString);
    }
  }
  
  public boolean isRenderFilterThumb()
  {
    return b();
  }
  
  private boolean b()
  {
    if (!SdkValid.shared.renderFilterThumb()) {
      return false;
    }
    return this.h;
  }
  
  public void setRenderFilterThumb(boolean paramBoolean)
  {
    this.h = paramBoolean;
  }
  
  public Bitmap getInputImage()
  {
    return this.e;
  }
  
  public void setInputImage(Bitmap paramBitmap)
  {
    this.e = paramBitmap;
  }
  
  public Bitmap getCache(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    SoftReference localSoftReference = (SoftReference)this.d.get(paramString);
    if (localSoftReference != null) {
      return (Bitmap)localSoftReference.get();
    }
    return null;
  }
  
  public void addCache(String paramString, Bitmap paramBitmap)
  {
    if ((paramString == null) || (paramBitmap == null)) {
      return;
    }
    SoftReference localSoftReference = new SoftReference(paramBitmap);
    this.d.put(paramString, localSoftReference);
  }
  
  public void start()
  {
    c();
  }
  
  private void c()
  {
    if (!b()) {
      return;
    }
    if (getInputImage() == null)
    {
      TLog.w("%s You need set inputImage.", new Object[] { "FiltersTaskBase" });
      return;
    }
    if (this.c == null)
    {
      TLog.w("%s You need set filerNames.", new Object[] { "FiltersTaskBase" });
      return;
    }
    if (getSampleRootPath() == null)
    {
      TLog.w("%s Can not found SampleRootPath: %s", new Object[] { "FiltersTaskBase", this.a });
      return;
    }
    ExecutorService localExecutorService = d();
    ArrayList localArrayList = new ArrayList(this.c);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      final String str = (String)localIterator.next();
      localExecutorService.execute(new Runnable()
      {
        public void run()
        {
          FiltersTaskBase.this.asyncBuildWithFilterName(str);
        }
      });
    }
  }
  
  protected void asyncBuildWithFilterName(String paramString)
  {
    File localFile = b(paramString);
    if (localFile.exists()) {
      return;
    }
    final FiltersTaskImageResult localFiltersTaskImageResult = new FiltersTaskImageResult();
    localFiltersTaskImageResult.setFilterName(paramString);
    Bitmap localBitmap = a(this.e, paramString);
    if (localBitmap != null)
    {
      BitmapHelper.saveBitmap(localFile, localBitmap, 90);
      localFiltersTaskImageResult.setImage(localBitmap);
    }
    this.f.post(new Runnable()
    {
      public void run()
      {
        FiltersTaskBase.a(FiltersTaskBase.this, localFiltersTaskImageResult);
      }
    });
  }
  
  private Bitmap a(Bitmap paramBitmap, String paramString)
  {
    FilterWrap localFilterWrap = FilterLocalPackage.shared().getFilterWrap(paramString);
    if (localFilterWrap == null) {
      return paramBitmap;
    }
    localFilterWrap.setFilterParameter(null);
    return localFilterWrap.process(paramBitmap, ImageOrientation.Up, 0.0F);
  }
  
  private void a(FiltersTaskImageResult paramFiltersTaskImageResult)
  {
    addCache(paramFiltersTaskImageResult.getFilterName(), paramFiltersTaskImageResult.getImage());
    ArrayList localArrayList = a();
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      FiltersTaskImageWare localFiltersTaskImageWare = (FiltersTaskImageWare)localIterator.next();
      if (localFiltersTaskImageWare.setImageResult(paramFiltersTaskImageResult)) {
        this.b.remove(localFiltersTaskImageWare);
      }
    }
  }
  
  private ExecutorService d()
  {
    if (this.g == null) {
      this.g = Executors.newFixedThreadPool(1);
    }
    return this.g;
  }
  
  public void resetQueues()
  {
    if (this.g != null) {
      this.g.shutdownNow();
    }
    this.b.clear();
    this.d.clear();
  }
  
  private Bitmap a(String paramString)
  {
    Bitmap localBitmap = getCache(paramString);
    if (localBitmap != null) {
      return localBitmap;
    }
    File localFile = b(paramString);
    localBitmap = BitmapHelper.getBitmap(localFile);
    addCache(paramString, localBitmap);
    return localBitmap;
  }
  
  private File b(String paramString)
  {
    File localFile = new File(getSampleRootPath(), String.format("%s.%s", new Object[] { paramString, "lfs" }));
    return localFile;
  }
  
  protected void finalize()
  {
    resetQueues();
    super.finalize();
  }
  
  public void loadImage(ImageView paramImageView, String paramString)
  {
    if (!b())
    {
      FilterLocalPackage.shared().loadFilterThumb(paramImageView, FilterLocalPackage.shared().option(paramString));
      return;
    }
    if ((paramImageView == null) || (paramString == null)) {
      return;
    }
    cancelLoadImage(paramImageView);
    Bitmap localBitmap = a(paramString);
    if (localBitmap != null)
    {
      paramImageView.setImageBitmap(localBitmap);
      return;
    }
    FiltersTaskImageWare localFiltersTaskImageWare = new FiltersTaskImageWare(paramImageView, paramString);
    this.b.add(localFiltersTaskImageWare);
  }
  
  public void cancelLoadImage(ImageView paramImageView)
  {
    if (!b())
    {
      FilterLocalPackage.shared().cancelLoadImage(paramImageView);
      return;
    }
    if (paramImageView == null) {
      return;
    }
    ArrayList localArrayList = a();
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      FiltersTaskImageWare localFiltersTaskImageWare = (FiltersTaskImageWare)localIterator.next();
      if (localFiltersTaskImageWare.isEqualView(paramImageView))
      {
        this.b.remove(localFiltersTaskImageWare);
        break;
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\task\FiltersTaskBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */