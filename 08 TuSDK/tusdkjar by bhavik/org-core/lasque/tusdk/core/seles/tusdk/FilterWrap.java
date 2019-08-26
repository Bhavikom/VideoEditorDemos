package org.lasque.tusdk.core.seles.tusdk;

import android.graphics.Bitmap;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterTexturesInterface;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.particle.TuSDKParticleFilterInterface;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class FilterWrap
{
  private FilterOption a;
  protected SelesOutInput mFilter;
  protected SelesOutInput mLastFilter;
  private List<SelesPicture> b;
  private boolean c;
  
  public FilterOption getOption()
  {
    return this.a;
  }
  
  public String getCode()
  {
    if (this.a == null) {
      return null;
    }
    return this.a.code;
  }
  
  public SelesOutInput getFilter()
  {
    return this.mFilter;
  }
  
  public SelesOutInput getLastFilter()
  {
    return this.mLastFilter;
  }
  
  public static FilterWrap creat(FilterOption paramFilterOption)
  {
    if (paramFilterOption == null)
    {
      TLog.e("Can not found FilterOption", new Object[0]);
      return null;
    }
    return new FilterWrap(paramFilterOption);
  }
  
  protected FilterWrap() {}
  
  protected FilterWrap(FilterOption paramFilterOption)
  {
    changeOption(paramFilterOption);
  }
  
  public boolean equals(Object paramObject)
  {
    if (this.a == null) {
      return super.equals(paramObject);
    }
    if ((paramObject == null) || (!(paramObject instanceof FilterWrap))) {
      return false;
    }
    return this.a.equals(((FilterWrap)paramObject).getOption());
  }
  
  public boolean equalsCode(String paramString)
  {
    if ((paramString == null) || (getCode() == null)) {
      return false;
    }
    return paramString.equalsIgnoreCase(getCode());
  }
  
  public void changeFilter(String paramString)
  {
    if ((paramString == null) || (equalsCode(paramString)))
    {
      TLog.e("The filterCode exist or empty: %s", new Object[] { paramString });
      return;
    }
    FilterOption localFilterOption = FilterLocalPackage.shared().option(paramString);
    changeOption(localFilterOption);
  }
  
  protected void changeOption(FilterOption paramFilterOption)
  {
    if (paramFilterOption == null)
    {
      TLog.e("Can not found FilterOption", new Object[0]);
      return;
    }
    a();
    this.a = paramFilterOption;
    b();
  }
  
  public void destroy()
  {
    a();
    this.c = true;
    if (this.mLastFilter != null) {
      this.mLastFilter.removeAllTargets();
    }
  }
  
  private void a()
  {
    this.c = false;
    if (this.b != null)
    {
      Iterator localIterator = this.b.iterator();
      while (localIterator.hasNext())
      {
        SelesPicture localSelesPicture = (SelesPicture)localIterator.next();
        localSelesPicture.destroy();
      }
      this.b.clear();
    }
    this.b = null;
    if (this.mFilter != null) {
      this.mFilter.removeAllTargets();
    }
    if (this.a != null) {
      this.a.destroy();
    }
  }
  
  private void b()
  {
    this.mLastFilter = (this.mFilter = this.a.getFilter());
    if (this.mFilter == null)
    {
      TLog.e("Can not found Filter class: %s", new Object[] { ReflectUtils.trace(this.a) });
      return;
    }
    if ((this.a.args != null) && (!this.a.args.isEmpty()))
    {
      localObject = new SelesParameters(this.a.args);
      setFilterParameter((SelesParameters)localObject);
    }
    Object localObject = new ArrayList();
    List localList1 = FilterLocalPackage.shared().loadTextures(this.a.code);
    if (localList1 != null) {
      ((List)localObject).addAll(localList1);
    }
    List localList2 = FilterLocalPackage.shared().loadInternalTextures(this.a.internalTextures);
    if (localList2 != null) {
      ((List)localObject).addAll(localList2);
    }
    if (this.a.getRuntimeTextureDelegate() != null)
    {
      List localList3 = this.a.getRuntimeTextureDelegate().getRunTimeTextures();
      if ((localList3 != null) && (localList3.size() > 0)) {
        ((List)localObject).addAll(localList3);
      }
    }
    if (!((List)localObject).isEmpty()) {
      this.b = ((List)localObject);
    }
  }
  
  public void processImage()
  {
    if (this.c) {
      return;
    }
    this.c = true;
    if (this.b == null) {
      return;
    }
    if ((this.mFilter instanceof SelesParameters.FilterTexturesInterface))
    {
      ((SelesParameters.FilterTexturesInterface)this.mFilter).appendTextures(this.b);
      return;
    }
    int i = 1;
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      SelesPicture localSelesPicture = (SelesPicture)localIterator.next();
      localSelesPicture.processImage();
      localSelesPicture.addTarget(this.mFilter, i);
      i++;
    }
  }
  
  public void rotationTextures(InterfaceOrientation paramInterfaceOrientation)
  {
    if ((this.b == null) || (!this.a.texturesKeepInput) || (paramInterfaceOrientation == null)) {
      return;
    }
    ImageOrientation localImageOrientation = ImageOrientation.Up;
    switch (1.a[paramInterfaceOrientation.ordinal()])
    {
    case 1: 
      localImageOrientation = ImageOrientation.Down;
      break;
    case 2: 
      localImageOrientation = ImageOrientation.Left;
      break;
    case 3: 
      localImageOrientation = ImageOrientation.Right;
      break;
    }
    int i = 1;
    int j = this.b.size() + 1;
    while (i < j)
    {
      this.mFilter.setInputRotation(localImageOrientation, i);
      i++;
    }
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    if (this.mLastFilter == null) {
      return;
    }
    this.mLastFilter.addTarget(paramSelesInput, paramInt);
  }
  
  public void removeTarget(SelesContext.SelesInput paramSelesInput)
  {
    if (this.mLastFilter == null) {
      return;
    }
    this.mLastFilter.removeTarget(paramSelesInput);
  }
  
  public void bindWithCameraView(SelesContext.SelesInput paramSelesInput)
  {
    if (paramSelesInput == null) {
      return;
    }
    addTarget(paramSelesInput, 0);
  }
  
  public void addOrgin(SelesOutput paramSelesOutput)
  {
    if ((paramSelesOutput == null) || (this.mFilter == null)) {
      return;
    }
    paramSelesOutput.addTarget(this.mFilter, 0);
  }
  
  public void removeOrgin(SelesOutput paramSelesOutput)
  {
    if ((paramSelesOutput == null) || (this.mFilter == null)) {
      return;
    }
    paramSelesOutput.removeTarget(this.mFilter);
  }
  
  public boolean hasFilterParameter()
  {
    return (this.mFilter != null) && ((this.mFilter instanceof SelesParameters.FilterParameterInterface));
  }
  
  public void setFilterParameter(SelesParameters paramSelesParameters)
  {
    if (!hasFilterParameter()) {
      return;
    }
    ((SelesParameters.FilterParameterInterface)this.mFilter).setParameter(paramSelesParameters);
  }
  
  public SelesParameters getFilterParameter()
  {
    if (!hasFilterParameter()) {
      return null;
    }
    return ((SelesParameters.FilterParameterInterface)this.mFilter).getParameter();
  }
  
  public void submitFilterParameter()
  {
    if (!hasFilterParameter()) {
      return;
    }
    ((SelesParameters.FilterParameterInterface)this.mFilter).submitParameter();
  }
  
  public boolean hasParticleFilter()
  {
    return (this.mFilter != null) && ((this.mFilter instanceof TuSDKParticleFilterInterface));
  }
  
  public void updateParticleEmitPosition(PointF paramPointF)
  {
    if (!hasParticleFilter()) {
      return;
    }
    ((TuSDKParticleFilterInterface)this.mFilter).updateParticleEmitPosition(paramPointF);
  }
  
  public void setParticleSize(float paramFloat)
  {
    if (!hasParticleFilter()) {
      return;
    }
    ((TuSDKParticleFilterInterface)this.mFilter).setParticleSize(paramFloat);
  }
  
  public void setParticleColor(int paramInt)
  {
    if (!hasParticleFilter()) {
      return;
    }
    ((TuSDKParticleFilterInterface)this.mFilter).setParticleColor(paramInt);
  }
  
  public FilterWrap clone()
  {
    FilterWrap localFilterWrap = creat(getOption());
    if (localFilterWrap != null) {
      localFilterWrap.setFilterParameter(getFilterParameter());
    }
    return localFilterWrap;
  }
  
  public Bitmap process(Bitmap paramBitmap)
  {
    return process(paramBitmap, ImageOrientation.Up);
  }
  
  public Bitmap process(Bitmap paramBitmap, ImageOrientation paramImageOrientation)
  {
    return process(paramBitmap, paramImageOrientation, 0.0F);
  }
  
  public Bitmap process(Bitmap paramBitmap, ImageOrientation paramImageOrientation, float paramFloat)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled())) {
      return null;
    }
    paramBitmap = BitmapHelper.imageRotaing(paramBitmap, paramImageOrientation);
    float f = 1.0F;
    Bitmap localBitmap = a(paramBitmap);
    while ((localBitmap == null) && (f > 0.0F))
    {
      FilterWrap localFilterWrap = clone();
      localBitmap = localFilterWrap.a(BitmapHelper.imageScale(paramBitmap, f));
      f -= paramFloat;
    }
    return localBitmap;
  }
  
  private Bitmap a(Bitmap paramBitmap)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled()) || (this.c)) {
      return null;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramBitmap);
    processImage();
    Bitmap localBitmap = null;
    try
    {
      paramBitmap = BitmapHelper.imageScale(paramBitmap, localTuSdkSize.limitScale());
      processImage();
      localBitmap = this.mFilter.imageByFilteringImage(paramBitmap);
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      TLog.e(localOutOfMemoryError, "appliedFilter OutOfMemoryError: %s ", new Object[] { localTuSdkSize });
    }
    catch (Exception localException)
    {
      TLog.e(localException, "appliedFilter Exception: %s ", new Object[] { localTuSdkSize });
    }
    return localBitmap;
  }
  
  public Bitmap captureVideoImage()
  {
    SelesOutInput localSelesOutInput = getLastFilter();
    if (localSelesOutInput == null) {
      return null;
    }
    localSelesOutInput.useNextFrameForImageCapture();
    Bitmap localBitmap = localSelesOutInput.imageFromCurrentlyProcessedOutput();
    return localBitmap;
  }
  
  private static class TempResult {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\FilterWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */