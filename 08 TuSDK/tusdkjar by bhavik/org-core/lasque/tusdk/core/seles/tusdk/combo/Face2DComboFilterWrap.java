package org.lasque.tusdk.core.seles.tusdk.combo;

import android.graphics.RectF;
import java.util.List;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterStickerInterface;
import org.lasque.tusdk.core.seles.filters.SelesPointDrawFilter;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKMap2DFilter;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSdkPlasticFace;
import org.lasque.tusdk.core.utils.TLog;

public class Face2DComboFilterWrap
  extends FilterWrap
  implements SelesParameters.FilterStickerInterface
{
  private SelesParameters a;
  private boolean b;
  private TuSdkPlasticFace c = new TuSdkPlasticFace();
  private TuSDKMap2DFilter d = new TuSDKMap2DFilter();
  private SelesPointDrawFilter e;
  protected SelesOutInput mFirstFilter;
  private boolean f = false;
  
  public static Face2DComboFilterWrap creat()
  {
    FilterOption localFilterOption = FilterLocalPackage.shared().option(null);
    return creat(localFilterOption);
  }
  
  public static Face2DComboFilterWrap creat(FilterOption paramFilterOption)
  {
    if (paramFilterOption == null)
    {
      TLog.e("Can not found FilterOption", new Object[0]);
      return null;
    }
    return new Face2DComboFilterWrap(paramFilterOption);
  }
  
  public boolean isEnablePlastic()
  {
    return this.b;
  }
  
  public void setIsEnablePlastic(boolean paramBoolean)
  {
    if (this.b == paramBoolean) {
      return;
    }
    this.b = paramBoolean;
    a();
  }
  
  protected Face2DComboFilterWrap(FilterOption paramFilterOption)
  {
    if (this.f) {
      this.e = new SelesPointDrawFilter();
    }
    changeOption(paramFilterOption);
  }
  
  public Face2DComboFilterWrap clone()
  {
    Face2DComboFilterWrap localFace2DComboFilterWrap = creat(getOption());
    if (localFace2DComboFilterWrap != null)
    {
      localFace2DComboFilterWrap.setFilterParameter(getFilterParameter());
      localFace2DComboFilterWrap.setIsEnablePlastic(isEnablePlastic());
      localFace2DComboFilterWrap.setStickerVisibility(isStickerVisibility());
    }
    return localFace2DComboFilterWrap;
  }
  
  protected void changeOption(FilterOption paramFilterOption)
  {
    super.changeOption(paramFilterOption);
    a();
  }
  
  public void addOrgin(SelesOutput paramSelesOutput)
  {
    if ((paramSelesOutput == null) || (this.mFirstFilter == null)) {
      return;
    }
    paramSelesOutput.addTarget(this.mFirstFilter, 0);
  }
  
  public void removeOrgin(SelesOutput paramSelesOutput)
  {
    if ((paramSelesOutput == null) || (this.mFirstFilter == null)) {
      return;
    }
    paramSelesOutput.removeTarget(this.mFirstFilter);
  }
  
  private void a()
  {
    if (this.mFilter == null) {
      return;
    }
    this.a = new SelesParameters();
    this.a.merge(super.getFilterParameter());
    this.a.merge(this.c.getParameter());
    this.mFilter.removeAllTargets();
    this.c.removeAllTargets();
    this.d.removeAllTargets();
    if (this.e != null) {
      this.e.removeAllTargets();
    }
    if (FilterManager.shared().isConmicEffectFilter(getOption().code))
    {
      if (isEnablePlastic())
      {
        this.c.addTarget(this.d, 0);
        this.d.addTarget(this.mFilter, 0);
        this.mFirstFilter = this.c;
      }
      else
      {
        this.d.addTarget(this.mFilter, 0);
        this.mFirstFilter = this.d;
      }
      this.mLastFilter = this.mFilter;
    }
    else
    {
      if (isEnablePlastic())
      {
        this.mFilter.addTarget(this.c, 0);
        this.c.addTarget(this.d, 0);
      }
      else
      {
        this.mFilter.addTarget(this.d, 0);
      }
      this.mFirstFilter = this.mFilter;
      this.mLastFilter = this.d;
    }
    if (this.f)
    {
      this.mLastFilter.addTarget(this.e, 0);
      this.mLastFilter = this.e;
    }
  }
  
  public void setFilterParameter(SelesParameters paramSelesParameters)
  {
    if (paramSelesParameters == null) {
      return;
    }
    SelesParameters localSelesParameters = super.getFilterParameter();
    if (localSelesParameters != null)
    {
      localSelesParameters.syncArgs(paramSelesParameters);
      super.setFilterParameter(localSelesParameters);
    }
    localSelesParameters = this.c.getParameter();
    localSelesParameters.syncArgs(paramSelesParameters);
    this.c.setParameter(localSelesParameters);
  }
  
  public SelesParameters getFilterParameter()
  {
    return this.a;
  }
  
  public void submitFilterParameter()
  {
    super.submitFilterParameter();
    this.c.submitParameter();
  }
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    this.c.updateFaceFeatures(paramArrayOfFaceAligment, paramFloat);
    this.d.updateFaceFeatures(paramArrayOfFaceAligment, paramFloat);
    if (this.f) {
      this.e.updateFaceFeatures(paramArrayOfFaceAligment, paramFloat);
    }
  }
  
  public void updateStickers(List<TuSDKLiveStickerImage> paramList)
  {
    this.d.updateStickers(paramList);
  }
  
  public int[] getMap2DCurrentStickerIndexs()
  {
    return this.d.getCurrentStickerIndexs();
  }
  
  public void setMap2DCurrentStickerIndex(int[] paramArrayOfInt)
  {
    this.d.setCurrentStickerIndexs(paramArrayOfInt);
  }
  
  public void setDisplayRect(RectF paramRectF, float paramFloat)
  {
    this.d.setDisplayRect(paramRectF, paramFloat);
  }
  
  public void setEnableAutoplayMode(boolean paramBoolean)
  {
    this.d.setEnableAutoplayMode(paramBoolean);
  }
  
  public void seekStickerToFrameTime(long paramLong)
  {
    this.d.seekStickerToFrameTime(paramLong);
  }
  
  public void setBenchmarkTime(long paramLong)
  {
    this.d.setBenchmarkTime(paramLong);
  }
  
  public void setStickerVisibility(boolean paramBoolean)
  {
    this.d.setStickerVisibility(paramBoolean);
  }
  
  public boolean isStickerVisibility()
  {
    return this.d.isStickerVisibility();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\combo\Face2DComboFilterWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */