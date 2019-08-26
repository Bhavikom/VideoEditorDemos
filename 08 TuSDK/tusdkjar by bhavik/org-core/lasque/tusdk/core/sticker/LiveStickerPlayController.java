package org.lasque.tusdk.core.sticker;

import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGLContext;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

public class LiveStickerPlayController
{
  public static final int LIVE_STICKER_MAX_NUM = 5;
  private LiveStickerLoader a;
  private List<TuSDKLiveStickerImage> b;
  private List<TuSDKLiveStickerImage> c;
  private List<StickerData> d;
  private boolean e;
  
  public LiveStickerPlayController(EGLContext paramEGLContext)
  {
    if (paramEGLContext == null) {
      return;
    }
    this.d = new ArrayList();
    this.c = new ArrayList();
    this.b = new ArrayList();
    this.a = new LiveStickerLoader(paramEGLContext);
  }
  
  public LiveStickerLoader getLiveStickerLoader()
  {
    return this.a;
  }
  
  public void destroy()
  {
    if (this.a != null)
    {
      this.a.destroy();
      this.a = null;
    }
    removeAllStickers();
    if (this.c != null)
    {
      this.c.clear();
      this.c = null;
    }
    this.b = null;
    this.d = null;
  }
  
  private boolean a(StickerData paramStickerData)
  {
    if (this.a == null) {
      return false;
    }
    if (this.e)
    {
      removeAllStickers();
      this.e = false;
    }
    if (b(paramStickerData)) {
      return false;
    }
    this.d.add(paramStickerData);
    TuSDKLiveStickerImage localTuSDKLiveStickerImage = a();
    if (localTuSDKLiveStickerImage == null) {
      localTuSDKLiveStickerImage = new TuSDKLiveStickerImage(this.a);
    }
    localTuSDKLiveStickerImage.updateSticker(paramStickerData);
    this.b.add(localTuSDKLiveStickerImage);
    return true;
  }
  
  private boolean b(StickerData paramStickerData)
  {
    if ((this.d != null) && (this.d.size() > 0)) {
      return this.d.contains(paramStickerData);
    }
    return false;
  }
  
  public void removeSticker(StickerData paramStickerData)
  {
    if ((paramStickerData == null) || (!b(paramStickerData))) {
      return;
    }
    this.d.remove(paramStickerData);
    TuSDKLiveStickerImage localTuSDKLiveStickerImage = c(paramStickerData);
    if (localTuSDKLiveStickerImage == null) {
      return;
    }
    localTuSDKLiveStickerImage.removeSticker();
    this.b.remove(localTuSDKLiveStickerImage);
    this.c.add(localTuSDKLiveStickerImage);
  }
  
  private TuSDKLiveStickerImage c(StickerData paramStickerData)
  {
    if ((this.b == null) || (this.b.size() <= 0)) {
      return null;
    }
    int i = 0;
    int j = this.b.size();
    while (i < j)
    {
      TuSDKLiveStickerImage localTuSDKLiveStickerImage = (TuSDKLiveStickerImage)this.b.get(i);
      if (localTuSDKLiveStickerImage.equals(localTuSDKLiveStickerImage)) {
        return localTuSDKLiveStickerImage;
      }
      i++;
    }
    return null;
  }
  
  private TuSDKLiveStickerImage a()
  {
    if ((this.c == null) || (this.c.size() == 0)) {
      return null;
    }
    int i = 0;
    int j = this.c.size();
    while (i < j)
    {
      TuSDKLiveStickerImage localTuSDKLiveStickerImage = (TuSDKLiveStickerImage)this.c.get(i);
      if ((!localTuSDKLiveStickerImage.isActived()) && (!localTuSDKLiveStickerImage.isEnabled())) {
        return (TuSDKLiveStickerImage)this.c.remove(i);
      }
      i++;
    }
    return null;
  }
  
  public boolean showGroupSticker(StickerGroup paramStickerGroup)
  {
    if (isGroupStickerUsed(paramStickerGroup))
    {
      TLog.e("The sticker group has already been used", new Object[0]);
      return false;
    }
    if ((paramStickerGroup.stickers == null) || (paramStickerGroup.stickers.size() <= 0))
    {
      TLog.e("invalid sticker group", new Object[0]);
      return false;
    }
    removeAllStickers();
    this.e = false;
    int i = 0;
    int j = paramStickerGroup.stickers.size();
    while (i < j)
    {
      a((StickerData)paramStickerGroup.stickers.get(i));
      i++;
    }
    this.e = true;
    return true;
  }
  
  public boolean isGroupStickerUsed(StickerGroup paramStickerGroup)
  {
    if ((this.e) && (this.d != null) && (this.d.size() > 0)) {
      return ((StickerData)this.d.get(0)).groupId == paramStickerGroup.groupId;
    }
    return false;
  }
  
  public long getCurrentGroupId()
  {
    if ((this.e) && (this.d != null) && (this.d.size() > 0)) {
      return ((StickerData)this.d.get(0)).groupId;
    }
    return -1L;
  }
  
  public void removeAllStickers()
  {
    if ((this.b == null) || (this.b.size() == 0)) {
      return;
    }
    int i = 0;
    int j = this.b.size();
    while (i < j)
    {
      TuSDKLiveStickerImage localTuSDKLiveStickerImage = (TuSDKLiveStickerImage)this.b.get(i);
      localTuSDKLiveStickerImage.reset();
      this.c.add(localTuSDKLiveStickerImage);
      i++;
    }
    this.b.clear();
    this.d.clear();
    this.e = false;
  }
  
  public void pauseAllStickers()
  {
    a(true);
  }
  
  public void resumeAllStickers()
  {
    a(false);
  }
  
  private void a(boolean paramBoolean)
  {
    if ((this.b == null) || (this.b.size() == 0)) {
      return;
    }
    TuSDKLiveStickerImage localTuSDKLiveStickerImage = null;
    int i = 0;
    int j = this.b.size();
    while (i < j)
    {
      localTuSDKLiveStickerImage = (TuSDKLiveStickerImage)this.b.get(i);
      localTuSDKLiveStickerImage.setEnableAutoplayMode(!paramBoolean);
      i++;
    }
  }
  
  public List<TuSDKLiveStickerImage> getStickers()
  {
    return this.b;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\sticker\LiveStickerPlayController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */