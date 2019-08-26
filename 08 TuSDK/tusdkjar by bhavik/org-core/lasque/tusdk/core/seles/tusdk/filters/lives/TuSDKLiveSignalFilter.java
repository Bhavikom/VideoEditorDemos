package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKLiveSignalFilter
  extends SelesTwoInputFilter
{
  private int f;
  private int g;
  private int h;
  private int i;
  private TuSDKLiveSignalVertexBuild j;
  private PointF k = new PointF(0.0F, 0.0F);
  private float[] l = { 0.1F, 0.2F, 1.0F, 0.5F };
  private float[] m = { 0.6F, 0.4F, 0.7F, 1.0F };
  private float n;
  private float o;
  private float p;
  private float q;
  private float r;
  private float s = 25.0F;
  private float t = 1.0F;
  int a;
  long b;
  long c;
  int d;
  int e;
  
  public TuSDKLiveSignalFilter()
  {
    super("-slive06v", "-slive06f");
    disableSecondFrameCheck();
    this.j = new TuSDKLiveSignalVertexBuild();
  }
  
  public TuSDKLiveSignalFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f1 = 0.0F;
      float f2 = 0.0F;
      float f3 = 0.1F;
      float f4 = 0.2F;
      float f5 = 0.6F;
      float f6 = 0.4F;
      float f7 = 0.7F;
      float f8;
      if (paramFilterOption.args.containsKey("splitX"))
      {
        f8 = Float.parseFloat((String)paramFilterOption.args.get("splitX"));
        if (f8 > 0.0F) {
          f1 = f8;
        }
      }
      if (paramFilterOption.args.containsKey("splitY"))
      {
        f8 = Float.parseFloat((String)paramFilterOption.args.get("splitY"));
        if (f8 > 0.0F) {
          f2 = f8;
        }
      }
      setSplit(new PointF(f1, f2));
      if (paramFilterOption.args.containsKey("flutterX"))
      {
        f8 = Float.parseFloat((String)paramFilterOption.args.get("flutterX"));
        if (f8 > 0.0F) {
          f3 = f8;
        }
      }
      if (paramFilterOption.args.containsKey("flutterY"))
      {
        f8 = Float.parseFloat((String)paramFilterOption.args.get("flutterY"));
        if (f8 > 0.0F) {
          f4 = f8;
        }
      }
      setFlutter(new float[] { f3, f4, 1.0F, 0.5F });
      if (paramFilterOption.args.containsKey("partialRed"))
      {
        f8 = Float.parseFloat((String)paramFilterOption.args.get("partialRed"));
        if (f8 > 0.0F) {
          f5 = f8;
        }
      }
      if (paramFilterOption.args.containsKey("partialGreen"))
      {
        f8 = Float.parseFloat((String)paramFilterOption.args.get("partialGreen"));
        if (f8 > 0.0F) {
          f6 = f8;
        }
      }
      if (paramFilterOption.args.containsKey("partialBlue"))
      {
        f8 = Float.parseFloat((String)paramFilterOption.args.get("partialBlue"));
        if (f8 > 0.0F) {
          f7 = f8;
        }
      }
      setPartialColor(new float[] { f5, f6, f7, 1.0F });
      if (paramFilterOption.args.containsKey("division"))
      {
        f8 = Float.parseFloat((String)paramFilterOption.args.get("division"));
        if (f8 > 0.0F) {
          setDivision(f8);
        }
      }
      if (paramFilterOption.args.containsKey("animation"))
      {
        f8 = Float.parseFloat((String)paramFilterOption.args.get("animation"));
        if (f8 > 0.0F) {
          setAnimation(f8);
        }
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.f = this.mFilterProgram.attributeIndex("inputTextureType");
    GLES20.glEnableVertexAttribArray(this.f);
    this.g = this.mFilterProgram.uniformIndex("split");
    this.h = this.mFilterProgram.uniformIndex("flutter");
    this.i = this.mFilterProgram.uniformIndex("partialColor");
    setSplit(this.k);
    setFlutter(this.l);
    setPartialColor(this.m);
  }
  
  protected void initializeAttributes()
  {
    super.initializeAttributes();
    this.mFilterProgram.addAttribute("inputTextureType");
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
    setUniformsForProgramAtIndex(0);
    a();
    GLES20.glClearColor(this.mBackgroundColorRed, this.mBackgroundColorGreen, this.mBackgroundColorBlue, this.mBackgroundColorAlpha);
    GLES20.glClear(16384);
    inputFramebufferBindTexture();
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, this.j.getPositionSize(), 5126, false, 0, this.j.getPositions());
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, this.j.getTextureCoordinateSize(), 5126, false, 0, this.j.getTextureCoordinates());
    GLES20.glVertexAttribPointer(this.mFilterSecondTextureCoordinateAttribute, this.j.getTextureCoordinateSize2(), 5126, false, 0, this.j.getTextureCoordinates2());
    GLES20.glVertexAttribPointer(this.f, this.j.getParamsSize(), 5126, false, 0, this.j.getParams());
    GLES20.glEnable(3042);
    GLES20.glBlendFunc(1, 771);
    GLES20.glDrawArrays(4, 0, this.j.getDrawTotal());
    GLES20.glDisable(3042);
    inputFramebufferUnlock();
    cacaptureImageBuffer();
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    makeAnimotionWithTime(System.currentTimeMillis());
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  private void a()
  {
    this.j.setMainType(getMainType());
    this.j.setBarType(getBarType());
    this.j.setBarTotal((int)Math.floor(getBarTotal()));
    this.j.setBlockType(getBlockType());
    this.j.setBlockTotal((int)Math.floor(getBlockTotal()));
    this.j.setDivision((int)Math.floor(getDivision()));
    this.j.setTextureSize(this.mInputTextureSize);
    this.j.setRotation(this.mInputRotation);
    this.j.setRotation2(this.mInputRotation2);
    this.j.calculate();
  }
  
  public PointF getSplit()
  {
    return this.k;
  }
  
  public void setSplit(PointF paramPointF)
  {
    this.k = paramPointF;
    setPoint(this.k, this.g, this.mFilterProgram);
  }
  
  public void setSplitX(float paramFloat)
  {
    PointF localPointF = getSplit();
    localPointF.x = paramFloat;
    setSplit(localPointF);
  }
  
  public void setSplitY(float paramFloat)
  {
    PointF localPointF = getSplit();
    localPointF.y = paramFloat;
    setSplit(localPointF);
  }
  
  public float[] getFlutter()
  {
    return this.l;
  }
  
  public void setFlutter(float[] paramArrayOfFloat)
  {
    this.l = paramArrayOfFloat;
    setVec4(this.l, this.h, this.mFilterProgram);
  }
  
  public void setFlutterX(float paramFloat)
  {
    float[] arrayOfFloat = getFlutter();
    arrayOfFloat[0] = paramFloat;
    setFlutter(arrayOfFloat);
  }
  
  public void setFlutterY(float paramFloat)
  {
    float[] arrayOfFloat = getFlutter();
    arrayOfFloat[1] = paramFloat;
    setFlutter(arrayOfFloat);
  }
  
  public void setFlutterStrength(float paramFloat)
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
  
  public float[] getPartialColor()
  {
    return this.m;
  }
  
  public void setPartialColor(float[] paramArrayOfFloat)
  {
    this.m = paramArrayOfFloat;
    setVec4(this.m, this.i, this.mFilterProgram);
  }
  
  public void setPartialRed(float paramFloat)
  {
    float[] arrayOfFloat = getPartialColor();
    arrayOfFloat[0] = paramFloat;
    setPartialColor(arrayOfFloat);
  }
  
  public void setPartialGreen(float paramFloat)
  {
    float[] arrayOfFloat = getPartialColor();
    arrayOfFloat[1] = paramFloat;
    setPartialColor(arrayOfFloat);
  }
  
  public void setPartialBlue(float paramFloat)
  {
    float[] arrayOfFloat = getPartialColor();
    arrayOfFloat[2] = paramFloat;
    setPartialColor(arrayOfFloat);
  }
  
  public void setPartialInvers(float paramFloat)
  {
    float[] arrayOfFloat = getPartialColor();
    arrayOfFloat[3] = paramFloat;
    setPartialColor(arrayOfFloat);
  }
  
  public float getMainType()
  {
    return this.n;
  }
  
  public void setMainType(float paramFloat)
  {
    this.n = paramFloat;
  }
  
  public float getBarType()
  {
    return this.o;
  }
  
  public void setBarType(float paramFloat)
  {
    this.o = paramFloat;
  }
  
  public float getBarTotal()
  {
    return this.p;
  }
  
  public void setBarTotal(float paramFloat)
  {
    this.p = paramFloat;
  }
  
  public float getBlockType()
  {
    return this.q;
  }
  
  public void setBlockType(float paramFloat)
  {
    this.q = paramFloat;
  }
  
  public float getBlockTotal()
  {
    return this.r;
  }
  
  public void setBlockTotal(float paramFloat)
  {
    this.r = paramFloat;
  }
  
  public float getDivision()
  {
    return this.s;
  }
  
  public void setDivision(float paramFloat)
  {
    this.s = paramFloat;
  }
  
  public float getAnimation()
  {
    return this.t;
  }
  
  public void setAnimation(float paramFloat)
  {
    this.t = paramFloat;
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("splitX", getSplit().x, -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("splitY", getSplit().y, -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterX", getFlutter()[0], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterY", getFlutter()[1], -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterStrength", getFlutter()[2], 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("flutterMixed", getFlutter()[3], 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("partialRed", getPartialColor()[0], 0.0F, 2.0F);
    paramSelesParameters.appendFloatArg("partialGreen", getPartialColor()[1], 0.0F, 2.0F);
    paramSelesParameters.appendFloatArg("partialBlue", getPartialColor()[2], 0.0F, 2.0F);
    paramSelesParameters.appendFloatArg("partialInvers", getPartialColor()[3], 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("mainType", getMainType(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("barType", getBarType(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("barTotal", getBarTotal(), 0.0F, 10.0F);
    paramSelesParameters.appendFloatArg("blockType", getBlockType(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("blockTotal", getBlockTotal(), 0.0F, 150.0F);
    paramSelesParameters.appendFloatArg("division", getDivision(), 0.0F, 40.0F);
    paramSelesParameters.appendFloatArg("animation", getAnimation(), 0.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("splitX")) {
      setSplitX(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("splitY")) {
      setSplitY(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterX")) {
      setFlutterX(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterY")) {
      setFlutterY(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterStrength")) {
      setFlutterStrength(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("flutterMixed")) {
      setFlutterMixed(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("partialRed")) {
      setPartialRed(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("partialGreen")) {
      setPartialGreen(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("partialBlue")) {
      setPartialBlue(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("partialInvers")) {
      setPartialInvers(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("mainType")) {
      setMainType(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("barType")) {
      setBarType(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("barTotal")) {
      setBarTotal(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("blockType")) {
      setBlockType(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("blockTotal")) {
      setBlockTotal(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("division")) {
      setDivision(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("animation")) {
      setAnimation(paramFilterArg.getValue());
    }
  }
  
  public void makeAnimotionWithTime(long paramLong)
  {
    if (getAnimation() < 0.5D) {
      return;
    }
    long l1 = paramLong / 50L;
    if (this.c == -1L) {
      this.c = l1;
    } else {
      l1 -= this.c;
    }
    if (l1 == this.b) {
      return;
    }
    this.b = l1;
    int[] arrayOfInt = { 0, 2, 4, 44, 60, 110, 112, 114, 164, 166, 168, 208, 224 };
    float[] arrayOfFloat1 = { 0.15F, 0.0F, 0.0F, 0.35F, 0.0F, 0.0F, 0.15F, 0.0F, 0.15F, 0.0F, 0.0F, 0.0F };
    float[] arrayOfFloat2 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.15F, 0.0F, 0.0F, 0.25F, 0.0F, 0.0F, 0.4F, 0.0F };
    float[] arrayOfFloat3 = { 0.0F, 0.0F, -0.005F, 0.0F, 0.004F, 0.0F, 0.0F, 0.004F, 0.0F, 0.0F, 0.004F, 0.0F };
    float[] arrayOfFloat4 = { 0.0F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
    float[] arrayOfFloat5 = { 0.0F, 0.4F, 0.12F, 0.0F, 0.4F, 0.4F, 0.0F, 0.4F, 0.0F, 0.4F, 0.4F, 0.0F };
    float[] arrayOfFloat6 = { 0.0F, 0.5F, 0.68F, 0.0F, 0.6F, 0.68F, 0.0F, 0.5F, 0.0F, 0.68F, 0.68F, 0.0F };
    float[] arrayOfFloat7 = { 0.0F, 0.0F, 0.002F, 0.0F, 0.0023F, 0.0F, 0.0F, 0.0023F, 0.0F, 0.0F, 0.0023F, 0.0F };
    float[] arrayOfFloat8 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4F, 0.0F, 0.0F, 0.0F, 0.4F, 0.0F, 0.0F };
    float[] arrayOfFloat9 = { 0.0F, 0.0F, 0.0F, 0.01F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.01F };
    float[] arrayOfFloat10 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
    if ((this.d == 0) || (this.d >= arrayOfInt[(arrayOfInt.length - 1)] - 1))
    {
      this.d = 0;
      this.a = 0;
      this.b = -1L;
      getParameter().setFilterArg("splitX", 0.5F);
      getParameter().setFilterArg("splitX", 0.5F);
      getParameter().setFilterArg("splitY", 0.5F);
      getParameter().setFilterArg("partialRed", 0.0F);
      getParameter().setFilterArg("partialGreen", 0.0F);
      getParameter().setFilterArg("partialBlue", 0.0F);
      getParameter().setFilterArg("partialInvers", 0.0F);
      getParameter().setFilterArg("flutterX", 0.3F);
      getParameter().setFilterArg("flutterY", 0.3F);
      getParameter().setFilterArg("flutterMixed", 0.6F);
    }
    this.d += 1;
    long l2 = arrayOfInt[(this.a + 1)];
    if ((this.d >= l2) || (this.a == 0))
    {
      this.e = 0;
      if (this.d != 1) {
        this.a += 1;
      }
      getParameter().setFilterArg("mainType", arrayOfFloat1[this.a]);
      getParameter().setFilterArg("barType", arrayOfFloat2[this.a]);
      getParameter().setFilterArg("blockType", arrayOfFloat5[this.a]);
      getParameter().setFilterArg("division", arrayOfFloat6[this.a]);
      getParameter().setFilterArg("barTotal", arrayOfFloat4[this.a]);
      getParameter().setFilterArg("blockTotal", arrayOfFloat8[this.a]);
      getParameter().setFilterArg("flutterStrength", arrayOfFloat10[this.a]);
    }
    else
    {
      this.e += 1;
      getParameter().setFilterArg("barTotal", arrayOfFloat3[this.a] * this.e + arrayOfFloat4[this.a]);
      getParameter().setFilterArg("blockTotal", arrayOfFloat7[this.a] * this.e + arrayOfFloat8[this.a]);
      getParameter().setFilterArg("flutterStrength", arrayOfFloat9[this.a] * this.e + arrayOfFloat10[this.a]);
    }
    submitParameter();
  }
  
  private class TuSDKLiveSignalVertexBuild
  {
    public final int VERTEX_POSITION_SIZE = 2;
    public final int VERTEX_TEXTURECOORDINATE_SIZE = 2;
    public final int VERTEX_TEXTURECOORDINATE_SIZE2 = 2;
    public final int VERTEX_PARAMS_SIZE = 1;
    public final int VERTEX_ELEMENT_POINTS = 6;
    private FloatBuffer b;
    private FloatBuffer c;
    private FloatBuffer d;
    private FloatBuffer e;
    private boolean f;
    private boolean g;
    private int h = 1;
    private float i;
    private float j;
    private int k;
    private float l;
    private int m;
    private int n;
    private TuSdkSize o;
    private ImageOrientation p = ImageOrientation.Up;
    private ImageOrientation q = ImageOrientation.Up;
    
    public int getDrawTotal()
    {
      return 6 * this.h;
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
    
    public FloatBuffer getTextureCoordinates2()
    {
      if (this.d != null) {
        this.d.position(0);
      }
      return this.d;
    }
    
    public FloatBuffer getParams()
    {
      if (this.e != null) {
        this.e.position(0);
      }
      return this.e;
    }
    
    public int getPositionSize()
    {
      return 2;
    }
    
    public int getTextureCoordinateSize()
    {
      return 2;
    }
    
    public int getTextureCoordinateSize2()
    {
      return 2;
    }
    
    public int getParamsSize()
    {
      return 1;
    }
    
    public int getElementPoints()
    {
      return 6;
    }
    
    public int getElementTotal()
    {
      return this.h;
    }
    
    public void setMainType(float paramFloat)
    {
      if (this.i == paramFloat) {
        return;
      }
      this.g = true;
      this.i = paramFloat;
    }
    
    public void setBarType(float paramFloat)
    {
      if (this.j == paramFloat) {
        return;
      }
      this.g = true;
      this.j = paramFloat;
    }
    
    public void setBarTotal(int paramInt)
    {
      if (this.k == paramInt) {
        return;
      }
      this.f = true;
      this.k = paramInt;
    }
    
    public void setBlockType(float paramFloat)
    {
      if (this.l == paramFloat) {
        return;
      }
      this.g = true;
      this.l = paramFloat;
    }
    
    public void setBlockTotal(int paramInt)
    {
      if (this.m == paramInt) {
        return;
      }
      this.f = true;
      this.m = paramInt;
    }
    
    public void setDivision(int paramInt)
    {
      if (paramInt < 1) {
        paramInt = 1;
      }
      if (this.n == paramInt) {
        return;
      }
      this.f = true;
      this.n = paramInt;
    }
    
    public void setTextureSize(TuSdkSize paramTuSdkSize)
    {
      if ((paramTuSdkSize == null) || (paramTuSdkSize.equals(this.o))) {
        return;
      }
      this.f = true;
      this.o = paramTuSdkSize;
    }
    
    public void setRotation(ImageOrientation paramImageOrientation)
    {
      if ((paramImageOrientation == null) || (paramImageOrientation == this.p)) {
        return;
      }
      this.f = true;
      this.p = paramImageOrientation;
    }
    
    public void setRotation2(ImageOrientation paramImageOrientation)
    {
      if ((paramImageOrientation == null) || (paramImageOrientation == this.q)) {
        return;
      }
      this.f = true;
      this.q = paramImageOrientation;
    }
    
    public TuSDKLiveSignalVertexBuild()
    {
      a();
    }
    
    public void calculate()
    {
      if (this.f) {
        a();
      } else if (this.g) {
        b();
      }
    }
    
    private int a(ArrayList<RectF> paramArrayList1, ArrayList<RectF> paramArrayList2)
    {
      if ((this.n < 1) || (this.o == null) || (!this.o.isSize())) {
        return 0;
      }
      float f1 = this.o.minSide() / this.n;
      int i1 = (int)Math.floor(this.o.width / f1);
      int i2 = (int)Math.floor(this.o.height / f1);
      float f2 = f1 / this.o.width;
      float f3 = f1 / this.o.height;
      int i3 = 0;
      Random localRandom = new Random();
      float f4;
      for (int i4 = 0; i4 < this.k; i4++)
      {
        f4 = localRandom.nextInt(i2) * f3;
        RectF localRectF1 = new RectF(0.0F, f4, 1.0F, f4 + f3);
        paramArrayList1.add(localRectF1);
        f4 = localRandom.nextInt(i2) * f3;
        localRectF1 = new RectF(0.0F, f4, 1.0F, f4 + f3);
        paramArrayList2.add(localRectF1);
        i3++;
      }
      for (i4 = 0; i4 < this.m; i4++)
      {
        f4 = localRandom.nextInt(i1) * f2;
        float f5 = localRandom.nextInt(i2) * f3;
        RectF localRectF2 = new RectF(f4, f5, f4 + f2, f5 + f3);
        paramArrayList1.add(localRectF2);
        f4 = localRandom.nextInt(i1) * f2;
        f5 = localRandom.nextInt(i2) * f3;
        localRectF2 = new RectF(f4, f5, f4 + f2, f5 + f3);
        paramArrayList2.add(localRectF2);
        i3++;
      }
      return i3;
    }
    
    private void a()
    {
      this.f = false;
      this.h = (this.k + this.m + 1);
      this.b = ByteBuffer.allocateDirect(getDrawTotal() * getPositionSize() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      this.c = ByteBuffer.allocateDirect(getDrawTotal() * getTextureCoordinateSize() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      this.d = ByteBuffer.allocateDirect(getDrawTotal() * getTextureCoordinateSize2() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      float[] arrayOfFloat1 = { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
      float[] arrayOfFloat2 = SelesFilter.textureCoordinates(this.p);
      float[] arrayOfFloat3 = { arrayOfFloat2[0], arrayOfFloat2[1], arrayOfFloat2[2], arrayOfFloat2[3], arrayOfFloat2[4], arrayOfFloat2[5], arrayOfFloat2[2], arrayOfFloat2[3], arrayOfFloat2[4], arrayOfFloat2[5], arrayOfFloat2[6], arrayOfFloat2[7] };
      arrayOfFloat2 = SelesFilter.textureCoordinates(this.q);
      float[] arrayOfFloat4 = { arrayOfFloat2[0], arrayOfFloat2[1], arrayOfFloat2[2], arrayOfFloat2[3], arrayOfFloat2[4], arrayOfFloat2[5], arrayOfFloat2[2], arrayOfFloat2[3], arrayOfFloat2[4], arrayOfFloat2[5], arrayOfFloat2[6], arrayOfFloat2[7] };
      this.b.put(arrayOfFloat1);
      this.c.put(arrayOfFloat3);
      this.d.put(arrayOfFloat4);
      b();
      ArrayList localArrayList1 = new ArrayList(this.k + this.m);
      ArrayList localArrayList2 = new ArrayList(this.k + this.m);
      int i1 = a(localArrayList1, localArrayList2);
      if (i1 > 0)
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
          this.d.put(arrayOfFloat4);
          b(this.c, localRectF);
        }
      }
    }
    
    private void b()
    {
      this.g = false;
      this.e = ByteBuffer.allocateDirect(getDrawTotal() * getParamsSize() * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
      float[] arrayOfFloat1 = { this.i, this.i, this.i, this.i, this.i, this.i };
      this.e.put(arrayOfFloat1);
      float[] arrayOfFloat2;
      int i1;
      if (this.k > 0)
      {
        arrayOfFloat2 = new float[] { this.j, this.j, this.j, this.j, this.j, this.j };
        for (i1 = 0; i1 < this.k; i1++) {
          this.e.put(arrayOfFloat2);
        }
      }
      if (this.m > 0)
      {
        arrayOfFloat2 = new float[] { this.l, this.l, this.l, this.l, this.l, this.l };
        for (i1 = 0; i1 < this.m; i1++) {
          this.e.put(arrayOfFloat2);
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
      float[] arrayOfFloat1 = RectHelper.textureCoordinates(this.p, paramRectF);
      float[] arrayOfFloat2 = { arrayOfFloat1[0], arrayOfFloat1[1], arrayOfFloat1[2], arrayOfFloat1[3], arrayOfFloat1[4], arrayOfFloat1[5], arrayOfFloat1[2], arrayOfFloat1[3], arrayOfFloat1[4], arrayOfFloat1[5], arrayOfFloat1[6], arrayOfFloat1[7] };
      paramFloatBuffer.put(arrayOfFloat2);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveSignalFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */