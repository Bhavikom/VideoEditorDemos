package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import android.graphics.RectF;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKLiveFaultFilter
  extends SelesFilter
{
  private TuSDKLiveSignalVertexBuild d = new TuSDKLiveSignalVertexBuild();
  private int e;
  private float f;
  private float g;
  private float h = 1.0F;
  private float[] i = { 0.0F, 0.0F, 0.0F, 0.0F };
  private float j;
  int a = 0;
  long b = -50L;
  long c = -1L;
  private int k;
  
  public TuSDKLiveFaultFilter()
  {
    super("-slive14f");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.e = this.mFilterProgram.uniformIndex("flutter");
    setFlutter(this.i);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    runPendingOnDrawTasks();
    if (isPreventRendering())
    {
      inputFramebufferUnlock();
      return;
    }
    SelesContext.setActiveShaderProgram(this.mFilterProgram);
    TuSdkSize localTuSdkSize = sizeOfFBO();
    this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, localTuSdkSize, getOutputTextureOptions());
    this.mOutputFramebuffer.activateFramebuffer();
    if (this.mUsingNextFrameForImageCapture) {
      this.mOutputFramebuffer.lock();
    }
    checkGLError(getClass().getSimpleName() + " activateFramebuffer");
    setUniformsForProgramAtIndex(0);
    a();
    GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
    GLES20.glClear(16384);
    inputFramebufferBindTexture();
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, this.d.getPositionSize(), 5126, false, 0, this.d.getPositions());
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, this.d.getTextureCoordinateSize(), 5126, false, 0, this.d.getTextureCoordinates());
    checkGLError(getClass().getSimpleName() + " bindFramebuffer");
    GLES20.glEnable(3042);
    GLES20.glBlendFunc(1, 771);
    GLES20.glDrawArrays(4, 0, this.d.getDrawTotal());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    GLES20.glDisable(3042);
    inputFramebufferUnlock();
    cacaptureImageBuffer();
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    makeAnimationWithTime(paramLong);
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  private void a()
  {
    this.d.setBarTotal((int)getBarTotal());
    this.d.setBlockTotal(0);
    this.d.setTextureSize(this.mInputTextureSize);
    this.d.setRotation(this.mInputRotation);
    this.d.setType((int)getType());
    this.d.calculate();
  }
  
  public float[] getFlutter()
  {
    return this.i;
  }
  
  public void setFlutter(float[] paramArrayOfFloat)
  {
    this.i = paramArrayOfFloat;
    setVec4(this.i, this.e, this.mFilterProgram);
  }
  
  public void setFlutterR(float paramFloat)
  {
    float[] arrayOfFloat = getFlutter();
    arrayOfFloat[0] = paramFloat;
    setFlutter(arrayOfFloat);
  }
  
  public void setFlutterG(float paramFloat)
  {
    float[] arrayOfFloat = getFlutter();
    arrayOfFloat[1] = paramFloat;
    setFlutter(arrayOfFloat);
  }
  
  public void setFlutterB(float paramFloat)
  {
    float[] arrayOfFloat = getFlutter();
    arrayOfFloat[2] = paramFloat;
    setFlutter(arrayOfFloat);
  }
  
  public void setFlutterMixed(float paramFloat)
  {
    float[] arrayOfFloat = getFlutter();
    arrayOfFloat[3] = paramFloat;
    setFlutter(arrayOfFloat);
  }
  
  public float getType()
  {
    return this.j;
  }
  
  public void setType(float paramFloat)
  {
    this.j = paramFloat;
  }
  
  public float getBarTotal()
  {
    return this.f;
  }
  
  public void setBarTotal(float paramFloat)
  {
    this.f = paramFloat;
  }
  
  public float getBlockTotal()
  {
    return this.g;
  }
  
  public void setBlockTotal(float paramFloat)
  {
    this.g = paramFloat;
  }
  
  public float getAnimation()
  {
    return this.h;
  }
  
  public void setAnimation(float paramFloat)
  {
    this.h = paramFloat;
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("flutterR", getFlutter()[0], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterG", getFlutter()[1], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterB", getFlutter()[2], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterMixed", getFlutter()[3], 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("Type", getType(), 0.0F, 10.0F);
    paramSelesParameters.appendFloatArg("barTotal", getBarTotal(), 0.0F, 10.0F);
    paramSelesParameters.appendFloatArg("blockTotal", getBlockTotal(), 0.0F, 100.0F);
    paramSelesParameters.appendFloatArg("animation", getAnimation(), 0.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("flutterR")) {
      setFlutterR(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterG")) {
      setFlutterG(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterB")) {
      setFlutterB(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterMixed")) {
      setFlutterMixed(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("Type")) {
      setType(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("barTotal")) {
      setBarTotal(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("blockTotal")) {
      setBlockTotal(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("animation")) {
      setAnimation(paramFilterArg.getValue());
    }
  }
  
  public void makeAnimationWithTime(long paramLong)
  {
    if (getAnimation() < 0.5D) {
      return;
    }
    long l = paramLong % 1000000000L;
    long[] arrayOfLong = { 0L, 50000000L, 100000000L, 150000000L, 200000000L, 250000000L, 300000000L, 340000000L, 380000000L, 420000000L };
    float[] arrayOfFloat1 = { 0.0F, 0.1F, 0.3F, 0.3F, 0.2F, 0.2F, 0.3F };
    float[] arrayOfFloat2 = { 0.0F, 0.1F, 0.2F, 0.2F, 0.3F, 0.3F, 0.4F };
    float[] arrayOfFloat3 = { 0.5F, 0.52F, 0.52F };
    float[] arrayOfFloat4 = { 0.51F, 0.53F, 0.54F };
    float[] arrayOfFloat5 = { 0.49F, 0.5F, 0.49F };
    if (l > arrayOfLong[(arrayOfLong.length - 1)])
    {
      getParameter().setFilterArg("Type", 0.0F);
      getParameter().setFilterArg("barTotal", 0.0F);
      getParameter().setFilterArg("flutterR", 0.5F);
      getParameter().setFilterArg("flutterG", 0.5F);
      getParameter().setFilterArg("flutterB", 0.5F);
      getParameter().setFilterArg("flutterMixed", 0.0F);
    }
    for (int m = 0; m < 6; m++) {
      if ((arrayOfLong[m] < l) && (arrayOfLong[(m + 1)] >= l))
      {
        getParameter().setFilterArg("Type", arrayOfFloat2[(m + 1)]);
        getParameter().setFilterArg("barTotal", arrayOfFloat1[(m + 1)]);
      }
    }
    for (m = 6; m < arrayOfLong.length - 1; m++) {
      if ((arrayOfLong[m] < l) && (arrayOfLong[(m + 1)] >= l))
      {
        getParameter().setFilterArg("Type", 0.0F);
        getParameter().setFilterArg("barTotal", 0.0F);
        getParameter().setFilterArg("flutterR", arrayOfFloat3[(m - 6)]);
        getParameter().setFilterArg("flutterG", arrayOfFloat4[(m - 6)] + (this.k == m ? 0.01F : 0.0F));
        getParameter().setFilterArg("flutterB", arrayOfFloat5[(m - 6)]);
        getParameter().setFilterArg("flutterMixed", 1.0F);
        this.k = m;
      }
    }
    submitParameter();
  }
  
  private class TuSDKLiveSignalVertexBuild
  {
    public final int VERTEX_POSITION_SIZE = 2;
    public final int VERTEX_TEXTURECOORDINATE_SIZE = 2;
    public final int VERTEX_ELEMENT_POINTS = 6;
    private FloatBuffer b;
    private FloatBuffer c;
    private boolean d;
    private int e = 1;
    private int f;
    private int g;
    private TuSdkSize h;
    private int i;
    private ImageOrientation j = ImageOrientation.Up;
    
    public int getDrawTotal()
    {
      return 6 * this.e;
    }
    
    public FloatBuffer getPositions()
    {
      if (this.b != null) {
        this.b.position(0);
      }
      return this.b;
    }
    
    public FloatBuffer getTextureCoordinates()
    {
      if (this.c != null) {
        this.c.position(0);
      }
      return this.c;
    }
    
    public int getPositionSize()
    {
      return 2;
    }
    
    public int getTextureCoordinateSize()
    {
      return 2;
    }
    
    public int getElementPoints()
    {
      return 6;
    }
    
    public int getElementTotal()
    {
      return this.e;
    }
    
    public void setType(int paramInt)
    {
      this.i = paramInt;
    }
    
    public void setBarTotal(int paramInt)
    {
      if (this.f == paramInt) {
        return;
      }
      this.d = true;
      this.f = paramInt;
    }
    
    public void setBlockTotal(int paramInt)
    {
      if (this.g == paramInt) {
        return;
      }
      this.d = true;
      this.g = paramInt;
    }
    
    public void setTextureSize(TuSdkSize paramTuSdkSize)
    {
      if ((paramTuSdkSize == null) || (paramTuSdkSize.equals(this.h))) {
        return;
      }
      this.d = true;
      this.h = paramTuSdkSize;
    }
    
    public void setRotation(ImageOrientation paramImageOrientation)
    {
      if ((paramImageOrientation == null) || (paramImageOrientation == this.j)) {
        return;
      }
      this.d = true;
      this.j = paramImageOrientation;
    }
    
    public TuSDKLiveSignalVertexBuild()
    {
      a();
    }
    
    public void calculate()
    {
      if (this.d) {
        a();
      }
    }
    
    private int a(ArrayList<RectF> paramArrayList1, ArrayList<RectF> paramArrayList2)
    {
      if ((this.f <= 0) || (this.h == null) || (!this.h.isSize())) {
        return 0;
      }
      float[][] arrayOfFloat1 = { { 0.0F, 0.8F, 1.0F, 0.7F } };
      float[][] arrayOfFloat2 = { { 0.0F, 0.98F, 1.0F, 0.88F }, { 0.0F, 0.65F, 0.5F, 0.55F }, { 0.5F, 0.65F, 0.75F, 0.2F } };
      float[][] arrayOfFloat3 = { { 0.5F, 0.95F, 1.0F, 0.85F }, { 0.0F, 0.06F, 1.0F, 0.0F } };
      float[][] arrayOfFloat4 = { { 0.0F, 1.0F, 0.1F, 0.2F }, { 0.5F, 0.7F, 1.0F, 0.6F }, { 0.15F, 0.2F, 1.0F, 0.15F } };
      float[][] arrayOfFloat5;
      switch (this.i)
      {
      case 0: 
        arrayOfFloat5 = new float[0][];
        break;
      case 1: 
        arrayOfFloat5 = arrayOfFloat1;
        break;
      case 2: 
        arrayOfFloat5 = arrayOfFloat2;
        break;
      case 3: 
        arrayOfFloat5 = arrayOfFloat3;
        break;
      case 4: 
        arrayOfFloat5 = arrayOfFloat4;
        break;
      default: 
        arrayOfFloat5 = new float[0][];
      }
      int k = 0;
      for (int m = 0; m < arrayOfFloat5.length; m++)
      {
        RectF localRectF = new RectF(arrayOfFloat5[m][0], arrayOfFloat5[m][1], arrayOfFloat5[m][2], arrayOfFloat5[m][3]);
        paramArrayList1.add(localRectF);
        localRectF = new RectF(arrayOfFloat5[m][0], 1.0F - arrayOfFloat5[m][3], arrayOfFloat5[m][2], 1.0F - arrayOfFloat5[m][1]);
        paramArrayList2.add(localRectF);
        k++;
      }
      return k;
    }
    
    private void a()
    {
      this.d = false;
      this.e = (this.f + this.g + 1);
      this.b = ByteBuffer.allocateDirect(getDrawTotal() * getPositionSize() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      this.c = ByteBuffer.allocateDirect(getDrawTotal() * getTextureCoordinateSize() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      float[] arrayOfFloat1 = { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
      float[] arrayOfFloat2 = SelesFilter.textureCoordinates(this.j);
      float[] arrayOfFloat3 = { arrayOfFloat2[0], arrayOfFloat2[1], arrayOfFloat2[2], arrayOfFloat2[3], arrayOfFloat2[4], arrayOfFloat2[5], arrayOfFloat2[2], arrayOfFloat2[3], arrayOfFloat2[4], arrayOfFloat2[5], arrayOfFloat2[6], arrayOfFloat2[7] };
      this.b.put(arrayOfFloat1);
      this.c.put(arrayOfFloat3);
      ArrayList localArrayList1 = new ArrayList(this.f + this.g);
      ArrayList localArrayList2 = new ArrayList(this.f + this.g);
      int k = a(localArrayList1, localArrayList2);
      if (k > 0)
      {
        Iterator localIterator = localArrayList1.iterator();
        RectF localRectF;
        while (localIterator.hasNext())
        {
          localRectF = (RectF)localIterator.next();
          a(this.b, localRectF);
        }
        localIterator = localArrayList2.iterator();
        while (localIterator.hasNext())
        {
          localRectF = (RectF)localIterator.next();
          b(this.c, localRectF);
        }
      }
    }
    
    private void a(FloatBuffer paramFloatBuffer, RectF paramRectF)
    {
      float[] arrayOfFloat = new float[12];
      arrayOfFloat[0] = (paramRectF.left * 2.0F - 1.0F);
      arrayOfFloat[1] = (1.0F - paramRectF.bottom * 2.0F);
      arrayOfFloat[2] = (paramRectF.right * 2.0F - 1.0F);
      arrayOfFloat[3] = arrayOfFloat[1];
      arrayOfFloat[4] = arrayOfFloat[0];
      arrayOfFloat[5] = (1.0F - paramRectF.top * 2.0F);
      arrayOfFloat[6] = arrayOfFloat[2];
      arrayOfFloat[7] = arrayOfFloat[3];
      arrayOfFloat[8] = arrayOfFloat[4];
      arrayOfFloat[9] = arrayOfFloat[5];
      arrayOfFloat[10] = arrayOfFloat[2];
      arrayOfFloat[11] = arrayOfFloat[5];
      paramFloatBuffer.put(arrayOfFloat);
    }
    
    private void b(FloatBuffer paramFloatBuffer, RectF paramRectF)
    {
      float[] arrayOfFloat1 = RectHelper.textureCoordinates(this.j, paramRectF);
      float[] arrayOfFloat2 = { arrayOfFloat1[0] * 0.8F, arrayOfFloat1[1] * 0.8F, arrayOfFloat1[2] * 0.8F, arrayOfFloat1[3] * 0.8F, arrayOfFloat1[4] * 0.8F, arrayOfFloat1[5] * 0.8F, arrayOfFloat1[2] * 0.8F, arrayOfFloat1[3] * 0.8F, arrayOfFloat1[4] * 0.8F, arrayOfFloat1[5] * 0.8F, arrayOfFloat1[6] * 0.8F, arrayOfFloat1[7] * 0.8F };
      paramFloatBuffer.put(arrayOfFloat2);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveFaultFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */