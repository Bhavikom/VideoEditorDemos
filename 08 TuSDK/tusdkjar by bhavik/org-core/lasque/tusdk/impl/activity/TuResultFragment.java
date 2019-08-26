package org.lasque.tusdk.impl.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.FontUtils;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.TextPosition;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;
import org.lasque.tusdk.core.utils.image.AlbumHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ExifHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.components.ComponentErrorType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerFactory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerResult;
import org.lasque.tusdk.modules.view.widget.sticker.StickerText;

public abstract class TuResultFragment
  extends TuComponentFragment
{
  private boolean a;
  private boolean b;
  private String c;
  private int d = 95;
  private TuSdkWaterMarkOption e;
  
  public boolean isSaveToTemp()
  {
    return this.a;
  }
  
  public void setSaveToTemp(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public boolean isSaveToAlbum()
  {
    return this.b;
  }
  
  public void setSaveToAlbum(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public String getSaveToAlbumName()
  {
    return this.c;
  }
  
  public void setSaveToAlbumName(String paramString)
  {
    this.c = paramString;
  }
  
  public int getOutputCompress()
  {
    if (this.d < 0) {
      this.d = 0;
    } else if (this.d > 100) {
      this.d = 100;
    }
    return this.d;
  }
  
  public void setOutputCompress(int paramInt)
  {
    this.d = paramInt;
  }
  
  public void setWaterMarkOption(TuSdkWaterMarkOption paramTuSdkWaterMarkOption)
  {
    if (this.e != null) {
      this.e.destroy();
    }
    this.e = paramTuSdkWaterMarkOption;
  }
  
  public TuSdkWaterMarkOption getWaterMarkOption()
  {
    return this.e;
  }
  
  protected abstract void notifyProcessing(TuSdkResult paramTuSdkResult);
  
  protected abstract boolean asyncNotifyProcessing(TuSdkResult paramTuSdkResult);
  
  protected void asyncProcessingIfNeedSave(TuSdkResult paramTuSdkResult)
  {
    if (ThreadHelper.isMainThread())
    {
      notifyProcessing(paramTuSdkResult);
      return;
    }
    if (!asyncNotifyProcessing(paramTuSdkResult)) {
      if (isSaveToTemp()) {
        saveToTemp(paramTuSdkResult);
      } else if (isSaveToAlbum()) {
        saveToAlbum(paramTuSdkResult);
      }
    }
    backUIThreadNotifyProcessing(paramTuSdkResult);
    StatisticsManger.appendComponent(ComponentActType.editPhotoAction);
  }
  
  protected void backUIThreadNotifyProcessing(final TuSdkResult paramTuSdkResult)
  {
    runOnUiThread(new Runnable()
    {
      public void run()
      {
        TuResultFragment.this.notifyProcessing(paramTuSdkResult);
      }
    });
  }
  
  protected Bitmap addWaterMarkToImage(Bitmap paramBitmap)
  {
    if ((paramBitmap == null) || (this.e == null) || (!this.e.isValid())) {
      return paramBitmap;
    }
    Context localContext = TuSdkContext.ins().getContext();
    StickerData localStickerData1 = null;
    StickerData localStickerData2 = null;
    Rect localRect1 = new Rect(0, 0, 0, 0);
    Bitmap localBitmap = this.e.getMarkImage();
    if (localBitmap != null)
    {
      localStickerData1 = new StickerData();
      localStickerData1.stickerType = 1;
      localStickerData1.stickerId = 0L;
      localStickerData1.groupId = 0L;
      localStickerData1.setImage(localBitmap);
      localStickerData1.width = localBitmap.getWidth();
      localStickerData1.height = localBitmap.getHeight();
      localRect1.right = localStickerData1.width;
      localRect1.bottom = localStickerData1.height;
    }
    String str = this.e.getMarkText();
    if ((StringHelper.isNotEmpty(str)) && (StringHelper.isNotBlank(str)))
    {
      localStickerData2 = new StickerData();
      localStickerData2.stickerType = 2;
      localStickerData2.stickerId = 0L;
      localStickerData2.groupId = 0L;
      localObject = new StickerText();
      ((StickerText)localObject).content = str;
      ((StickerText)localObject).color = this.e.getMarkTextColor();
      ((StickerText)localObject).textSize = this.e.getMarkTextSize();
      ((StickerText)localObject).shadowColor = this.e.getMarkTextShadowColor();
      ((StickerText)localObject).alignment = 0;
      ((StickerText)localObject).rectTop = 0.0F;
      ((StickerText)localObject).rectLeft = 0.0F;
      ((StickerText)localObject).rectWidth = 1.0F;
      ((StickerText)localObject).rectHeight = 1.0F;
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(localObject);
      localStickerData2.texts = localArrayList;
      Rect localRect2 = FontUtils.getTextBounds(((StickerText)localObject).content, ((StickerText)localObject).textSize);
      localStickerData2.width = localRect2.width();
      localStickerData2.height = localRect2.height();
      if (localRect1.right > 0)
      {
        localRect1.right = (localRect1.right + localStickerData2.width + ContextUtils.dip2px(localContext, this.e.getMarkTextPadding()));
        localRect1.bottom = Math.max(localRect1.bottom, localStickerData2.height);
      }
      else
      {
        localRect1.right = localStickerData2.width;
        localRect1.bottom = localStickerData2.height;
      }
    }
    Object localObject = new ArrayList();
    if (localStickerData1 != null) {
      ((ArrayList)localObject).add(a(localStickerData1, paramBitmap.getWidth(), paramBitmap.getHeight(), localRect1, TuSdkWaterMarkOption.TextPosition.Right));
    }
    if (localStickerData2 != null) {
      ((ArrayList)localObject).add(a(localStickerData2, paramBitmap.getWidth(), paramBitmap.getHeight(), localRect1, TuSdkWaterMarkOption.TextPosition.Right));
    }
    return StickerFactory.megerStickers(paramBitmap, (List)localObject);
  }
  
  private StickerResult a(StickerData paramStickerData, int paramInt1, int paramInt2, Rect paramRect, TuSdkWaterMarkOption.TextPosition paramTextPosition)
  {
    Context localContext = TuSdkContext.ins().getContext();
    int i = ContextUtils.dip2px(localContext, this.e.getMarkMargin());
    RectF localRectF = new RectF((paramInt1 - paramStickerData.width / 2.0F - i) / paramInt1, (paramInt2 - paramRect.bottom / 2.0F - i) / paramInt2, paramStickerData.width * 1.0F / paramInt1, paramStickerData.height * 1.0F / paramInt2);
    TuSdkWaterMarkOption.WaterMarkPosition localWaterMarkPosition = this.e.getMarkPosition();
    if (paramStickerData.stickerType == 1)
    {
      if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomRight)
      {
        localRectF.left = ((paramInt1 - paramRect.right - i + paramStickerData.width / 2.0F) / paramInt1);
      }
      else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft)
      {
        localRectF.left = ((paramStickerData.width / 2.0F + i) / paramInt1);
      }
      else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopLeft)
      {
        localRectF.left = ((paramStickerData.width / 2.0F + i) / paramInt1);
        localRectF.top = ((paramRect.bottom / 2.0F + i) / paramInt2);
      }
      else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopRight)
      {
        localRectF.left = ((paramInt1 - paramRect.right - i + paramStickerData.width / 2.0F) / paramInt1);
        localRectF.top = ((paramRect.bottom / 2.0F + i) / paramInt2);
      }
      else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.Center)
      {
        localRectF.left = ((paramInt1 / 2.0F - paramRect.right / 2.0F + paramStickerData.width / 2.0F) / paramInt1);
        localRectF.top = 0.5F;
      }
    }
    else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft)
    {
      localRectF.left = ((paramRect.right - paramStickerData.width / 2.0F + i) / paramInt1);
    }
    else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopLeft)
    {
      localRectF.left = ((paramRect.right - paramStickerData.width / 2.0F + i) / paramInt1);
      localRectF.top = ((paramRect.bottom / 2.0F + i) / paramInt2);
    }
    else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopRight)
    {
      localRectF.top = ((paramRect.bottom / 2.0F + i) / paramInt2);
    }
    else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.Center)
    {
      localRectF.left = ((paramInt1 / 2.0F + paramRect.right / 2.0F - paramStickerData.width / 2.0F) / paramInt1);
      localRectF.top = 0.5F;
    }
    if ((paramRect.right > paramStickerData.width) && (this.e.getMarkTextPosition() != paramTextPosition))
    {
      localRectF.left = ((paramInt1 - paramRect.right + paramStickerData.width / 2.0F - i) / paramInt1);
      if (paramStickerData.stickerType == 1)
      {
        if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomRight)
        {
          localRectF.left = ((paramInt1 - paramStickerData.width / 2.0F - i) / paramInt1);
        }
        else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft)
        {
          localRectF.left = ((paramRect.right - paramStickerData.width / 2.0F + i) / paramInt1);
        }
        else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopLeft)
        {
          localRectF.left = ((paramRect.right - paramStickerData.width / 2.0F + i) / paramInt1);
          localRectF.top = ((paramRect.bottom / 2.0F + i) / paramInt2);
        }
        else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopRight)
        {
          localRectF.left = ((paramInt1 - paramStickerData.width / 2.0F - i) / paramInt1);
          localRectF.top = ((paramRect.bottom / 2.0F + i) / paramInt2);
        }
        else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.Center)
        {
          localRectF.left = ((paramInt1 / 2.0F + paramRect.right / 2.0F - paramStickerData.width / 2.0F) / paramInt1);
          localRectF.top = 0.5F;
        }
      }
      else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.BottomLeft)
      {
        localRectF.left = ((paramStickerData.width / 2.0F + i) / paramInt1);
      }
      else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopLeft)
      {
        localRectF.left = ((paramStickerData.width / 2.0F + i) / paramInt1);
        localRectF.top = ((paramRect.bottom / 2.0F + i) / paramInt2);
      }
      else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.TopRight)
      {
        localRectF.top = ((paramRect.bottom / 2.0F + i) / paramInt2);
      }
      else if (localWaterMarkPosition == TuSdkWaterMarkOption.WaterMarkPosition.Center)
      {
        localRectF.left = ((paramInt1 / 2.0F - paramRect.right / 2.0F + paramStickerData.width / 2.0F) / paramInt1);
        localRectF.top = 0.5F;
      }
    }
    localRectF.right += localRectF.left;
    localRectF.bottom += localRectF.top;
    StickerResult localStickerResult = new StickerResult();
    localStickerResult.item = paramStickerData.copy();
    localStickerResult.item.setImage(paramStickerData.getImage());
    localStickerResult.degree = 0.0F;
    localStickerResult.center = new RectF(localRectF);
    return localStickerResult;
  }
  
  protected void saveToTemp(TuSdkResult paramTuSdkResult)
  {
    if ((paramTuSdkResult == null) || (paramTuSdkResult.image == null) || (!a(paramTuSdkResult))) {
      return;
    }
    hubStatus(TuSdkContext.getString("lsq_save_saveToTemp"));
    paramTuSdkResult.imageFile = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", new Object[] { StringHelper.timeStampString() }));
    BitmapHelper.saveBitmap(paramTuSdkResult.imageFile, paramTuSdkResult.image, getOutputCompress());
    paramTuSdkResult.image = null;
    if (paramTuSdkResult.imageFile.exists())
    {
      ExifHelper.writeExifInterface(paramTuSdkResult.metadata, paramTuSdkResult.imageFile);
      hubSuccess(TuSdkContext.getString("lsq_save_saveToTemp_completed"));
    }
    else
    {
      hubError(TuSdkContext.getString("lsq_save_saveToTemp_failed"));
    }
  }
  
  protected void saveToAlbum(TuSdkResult paramTuSdkResult)
  {
    if ((paramTuSdkResult == null) || (paramTuSdkResult.image == null) || (!a(paramTuSdkResult))) {
      return;
    }
    hubStatus(TuSdkContext.getString("lsq_save_saveToAlbum"));
    File localFile = null;
    if (StringHelper.isNotBlank(getSaveToAlbumName())) {
      localFile = AlbumHelper.getAlbumFile(getSaveToAlbumName());
    }
    paramTuSdkResult.imageSqlInfo = ImageSqlHelper.saveJpgToAblum(getActivity(), paramTuSdkResult.image, getOutputCompress(), localFile);
    if (paramTuSdkResult.imageSqlInfo != null) {
      localFile = new File(paramTuSdkResult.imageSqlInfo.path);
    }
    paramTuSdkResult.image = null;
    if ((paramTuSdkResult.imageSqlInfo != null) && (localFile != null) && (localFile.exists()))
    {
      ExifHelper.writeExifInterface(paramTuSdkResult.metadata, paramTuSdkResult.imageSqlInfo.path);
      if (paramTuSdkResult.imageSqlInfo.length == 0L) {
        paramTuSdkResult.imageSqlInfo.length = localFile.length();
      }
      ImageSqlHelper.notifyRefreshAblum(getActivity(), paramTuSdkResult.imageSqlInfo);
      hubSuccess(TuSdkContext.getString("lsq_save_saveToAlbum_succeed"));
    }
    else
    {
      hubError(TuSdkContext.getString("lsq_save_saveToAlbum_failed"));
    }
  }
  
  private boolean a(TuSdkResult paramTuSdkResult)
  {
    ComponentErrorType localComponentErrorType = canSaveFile();
    if (localComponentErrorType == null) {
      return true;
    }
    notifyError(paramTuSdkResult, localComponentErrorType);
    return false;
  }
  
  public ComponentErrorType canSaveFile()
  {
    ComponentErrorType localComponentErrorType = null;
    if (!FileHelper.mountedExternalStorage())
    {
      localComponentErrorType = ComponentErrorType.TypeNotFoundSDCard;
      hubError(TuSdkContext.getString("lsq_save_not_found_sdcard"));
    }
    else if (!FileHelper.hasAvailableExternal(getActivity()))
    {
      localComponentErrorType = ComponentErrorType.TypeStorageSpace;
      hubError(TuSdkContext.getString("lsq_save_insufficient_storage_space"));
    }
    return localComponentErrorType;
  }
  
  public void onDestroyView()
  {
    if (this.e != null)
    {
      this.e.destroy();
      this.e = null;
    }
    super.onDestroyView();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\activity\TuResultFragment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */