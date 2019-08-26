package org.lasque.tusdk.core;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import java.io.File;
import java.util.List;
import org.lasque.tusdk.core.exif.ExifInterface;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ExifHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.modules.view.widget.sticker.StickerResult;

public class TuSdkResult
{
  public byte[] imageData;
  public File imageFile;
  public Bitmap image;
  public String filterCode;
  public FilterWrap filterWrap;
  public ExifInterface metadata;
  public Uri uri;
  public ImageSqlInfo imageSqlInfo;
  public List<ImageSqlInfo> images;
  public TuSdkSize outputSize;
  public float imageSizeRatio;
  public Object extendData;
  public ImageOrientation imageOrientation;
  public RectF cutRect;
  public int cutRatioType;
  public float cutScale;
  public SelesParameters filterParams;
  public List<StickerResult> stickers;
  
  public void fixedMetadata()
  {
    if ((this.metadata == null) || (this.image == null)) {
      return;
    }
    this.metadata.setTagValue(ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(this.image.getWidth()));
    this.metadata.setTagValue(ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(this.image.getHeight()));
    this.metadata.setTagValue(ExifInterface.TAG_ORIENTATION, Integer.valueOf(ImageOrientation.Up.getExifOrientation()));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (this.image != null) {
      localStringBuilder.append(String.format("image (%s): %s\n", new Object[] { this.imageOrientation, TuSdkSize.create(this.image) }));
    }
    if (this.imageFile != null) {
      localStringBuilder.append(String.format("imageFile: %s\n", new Object[] { this.imageFile }));
    }
    if (this.filterCode != null) {
      localStringBuilder.append(String.format("filterCode: %s\n", new Object[] { this.filterCode }));
    }
    if (this.uri != null) {
      localStringBuilder.append(String.format("uri: %s\n", new Object[] { this.uri }));
    }
    if (this.imageSqlInfo != null) {
      localStringBuilder.append(String.format("sqlInfo: %s\n", new Object[] { this.imageSqlInfo }));
    }
    if (this.outputSize != null) {
      localStringBuilder.append(String.format("outputSize: %s\n", new Object[] { this.outputSize }));
    }
    if (this.cutRect != null) {
      localStringBuilder.append(String.format("cutRect: %s\n", new Object[] { this.cutRect }));
    }
    if (this.images != null) {
      localStringBuilder.append(String.format("images: %s\n", new Object[] { this.images }));
    }
    return localStringBuilder.toString();
  }
  
  public void destroy()
  {
    this.imageData = null;
    this.imageFile = null;
    BitmapHelper.recycled(this.image);
    this.filterWrap = null;
    this.metadata = null;
    this.uri = null;
    this.imageSqlInfo = null;
    this.images = null;
    this.outputSize = null;
    this.extendData = null;
    this.imageOrientation = null;
    this.cutRect = null;
    this.filterParams = null;
    this.stickers = null;
  }
  
  public void logInfo()
  {
    logInfo(false);
  }
  
  public void logInfo(boolean paramBoolean)
  {
    TLog.d("TuSdkResult:\r%s", new Object[] { toString() });
    if ((paramBoolean) && (this.metadata != null)) {
      ExifHelper.log(this.metadata.getAllTags());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\TuSdkResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */