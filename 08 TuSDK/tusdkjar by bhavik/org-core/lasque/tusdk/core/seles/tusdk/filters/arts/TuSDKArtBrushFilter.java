package org.lasque.tusdk.core.seles.tusdk.filters.arts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterTexturesInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurSevenRadiusFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.TuSdkSizeF;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKArtBrushFilter
  extends SelesFilterGroup
  implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
  private float a = 0.0F;
  private final TuSDKGaussianBlurFiveRadiusFilter b = TuSDKGaussianBlurSevenRadiusFilter.hardware(true);
  private _TuSDKArtBrushFilter c;
  
  public TuSDKArtBrushFilter()
  {
    addFilter(this.b);
    this.c = new _TuSDKArtBrushFilter();
    addFilter(this.c);
    this.b.addTarget(this.c, 0);
    setInitialFilters(new SelesOutInput[] { this.b });
    setTerminalFilter(this.c);
    setMix(this.a);
  }
  
  public TuSDKArtBrushFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null) && (paramFilterOption.args.containsKey("mixied")))
    {
      float f = Float.parseFloat((String)paramFilterOption.args.get("mixied"));
      if (f > 0.0F) {
        setMix(f);
      }
    }
  }
  
  public float getMix()
  {
    return this.a;
  }
  
  public void setMix(float paramFloat)
  {
    this.a = paramFloat;
    this.b.setBlurSize(paramFloat);
  }
  
  public void appendTextures(List<SelesPicture> paramList)
  {
    if (paramList == null) {
      return;
    }
    int i = 1;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SelesPicture localSelesPicture = (SelesPicture)localIterator.next();
      localSelesPicture.processImage();
      localSelesPicture.addTarget(this.c, i);
      i++;
    }
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("mixied", this.a, 0.0F, 3.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("mixied")) {
      setMix(paramFilterArg.getValue());
    }
  }
  
  private static class _TuSDKArtBrushFilter
    extends SelesTwoInputFilter
  {
    private int a;
    
    public _TuSDKArtBrushFilter()
    {
      super();
      disableSecondFrameCheck();
    }
    
    protected void onInitOnGLThread()
    {
      super.onInitOnGLThread();
      this.a = this.mFilterProgram.uniformIndex("cropAspectRatio");
      a();
    }
    
    private void a()
    {
      if (!this.mInputTextureSize.isSize()) {
        return;
      }
      TuSdkSizeF localTuSdkSizeF = TuSdkSizeF.create(1.0F, 1.0F);
      if (this.mInputTextureSize.width > this.mInputTextureSize.height) {
        localTuSdkSizeF.height = (this.mInputTextureSize.height / this.mInputTextureSize.width);
      } else {
        localTuSdkSizeF.width = (this.mInputTextureSize.width / this.mInputTextureSize.height);
      }
      setSize(localTuSdkSizeF, this.a, this.mFilterProgram);
    }
    
    public void forceProcessingAtSize(TuSdkSize paramTuSdkSize)
    {
      super.forceProcessingAtSize(paramTuSdkSize);
      a();
    }
    
    public void setInputSize(TuSdkSize paramTuSdkSize, int paramInt)
    {
      TuSdkSize localTuSdkSize = this.mInputTextureSize.copy();
      super.setInputSize(paramTuSdkSize, paramInt);
      if ((paramInt == 0) && (!localTuSdkSize.equals(this.mInputTextureSize))) {
        a();
      }
    }
    
    public void setInputRotation(ImageOrientation paramImageOrientation, int paramInt)
    {
      super.setInputRotation(paramImageOrientation, paramInt);
      a();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\arts\TuSDKArtBrushFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */