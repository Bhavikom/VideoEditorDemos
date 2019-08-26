package org.lasque.tusdk.core.task;

import android.graphics.Bitmap;
import android.widget.ImageView;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;

public class AlbumTaskManager
  extends ImageViewTask<AlbumTaskImageWare>
{
  public static final AlbumTaskManager shared = new AlbumTaskManager();
  public static int TypeMiniThumbSize = 100;
  public static int TypeThumbSize = 200;
  
  protected void finalize()
  {
    resetQueues();
    super.finalize();
  }
  
  public void loadThumbMiniImage(ImageView paramImageView, ImageSqlInfo paramImageSqlInfo)
  {
    loadImage(paramImageView, paramImageSqlInfo, AlbumTaskType.TypeMiniThumb);
  }
  
  public void loadThumbImage(ImageView paramImageView, ImageSqlInfo paramImageSqlInfo)
  {
    loadImage(paramImageView, paramImageSqlInfo, AlbumTaskType.TypeThumb);
  }
  
  public void loadFullScreenImage(ImageView paramImageView, ImageSqlInfo paramImageSqlInfo)
  {
    loadImage(paramImageView, paramImageSqlInfo, AlbumTaskType.TypeFullScreen);
  }
  
  public void loadResolution(ImageView paramImageView, ImageSqlInfo paramImageSqlInfo)
  {
    loadImage(paramImageView, paramImageSqlInfo, AlbumTaskType.TypeResolution);
  }
  
  public void loadImage(ImageView paramImageView, ImageSqlInfo paramImageSqlInfo, AlbumTaskType paramAlbumTaskType)
  {
    if ((paramImageView == null) || (paramAlbumTaskType == null) || (paramImageSqlInfo == null) || (paramImageSqlInfo.id == 0L) || (paramImageSqlInfo.path == null)) {
      return;
    }
    AlbumTaskImageWare localAlbumTaskImageWare = new AlbumTaskImageWare(paramImageView, paramImageSqlInfo, paramAlbumTaskType);
    loadImage(localAlbumTaskImageWare);
  }
  
  protected String getCacheKey(AlbumTaskImageWare paramAlbumTaskImageWare)
  {
    String str = StringHelper.md5(String.format("%s_%s", new Object[] { paramAlbumTaskImageWare.imageSqlInfo.identify(), paramAlbumTaskImageWare.taskType }));
    return str;
  }
  
  protected Bitmap asyncTaskLoadImage(AlbumTaskImageWare paramAlbumTaskImageWare)
  {
    Bitmap localBitmap = null;
    switch (1.a[paramAlbumTaskImageWare.taskType.ordinal()])
    {
    case 1: 
      localBitmap = BitmapHelper.getBitmap(paramAlbumTaskImageWare.imageSqlInfo, TuSdkContext.getScreenSize());
      break;
    case 2: 
      localBitmap = BitmapHelper.getBitmap(paramAlbumTaskImageWare.imageSqlInfo);
      break;
    case 3: 
    case 4: 
    default: 
      localBitmap = a(paramAlbumTaskImageWare);
    }
    return localBitmap;
  }
  
  private Bitmap a(AlbumTaskImageWare paramAlbumTaskImageWare)
  {
    if (paramAlbumTaskImageWare == null) {
      return null;
    }
    if (!TuSdkContext.hasAvailableExternal()) {
      return ImageSqlHelper.getThumbnail(TuSdkContext.ins().getContext(), paramAlbumTaskImageWare.imageSqlInfo, 1);
    }
    if (paramAlbumTaskImageWare.taskType == AlbumTaskType.TypeThumb) {
      return BitmapHelper.getBitmap(paramAlbumTaskImageWare.imageSqlInfo, TypeThumbSize, false);
    }
    return BitmapHelper.getBitmap(paramAlbumTaskImageWare.imageSqlInfo, TypeMiniThumbSize, false);
  }
  
  public static class AlbumTaskImageWare
    extends ImageViewTaskWare
  {
    public ImageSqlInfo imageSqlInfo;
    public AlbumTaskManager.AlbumTaskType taskType;
    
    public AlbumTaskImageWare(ImageView paramImageView, ImageSqlInfo paramImageSqlInfo, AlbumTaskManager.AlbumTaskType paramAlbumTaskType)
    {
      setImageView(paramImageView);
      this.imageSqlInfo = paramImageSqlInfo;
      this.taskType = paramAlbumTaskType;
      if (paramAlbumTaskType == null) {
        this.taskType = AlbumTaskManager.AlbumTaskType.TypeMiniThumb;
      }
    }
    
    public void imageLoaded(Bitmap paramBitmap, ImageViewTaskWare.LoadType paramLoadType)
    {
      if ((paramBitmap == null) && (!isCancel()))
      {
        ImageView localImageView = getImageView();
        if (localImageView == null) {
          return;
        }
        localImageView.setImageResource(TuSdkContext.getDrawableResId("lsq_style_default_image_none"));
        return;
      }
      super.imageLoaded(paramBitmap, paramLoadType);
    }
  }
  
  public static enum AlbumTaskType
  {
    private AlbumTaskType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\task\AlbumTaskManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */