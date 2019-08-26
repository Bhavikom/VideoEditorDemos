package org.lasque.tusdk.core.seles.tusdk.liveSticker;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.sticker.StickerPositionInfo;
import org.lasque.tusdk.core.sticker.StickerPositionInfo.StickerPositionType;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;

public class TuSDKMap2DFilter
  extends SelesFilter
{
  public static final String TUSDK_MAP_2D_VERTEX_SHADER = "attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}";
  protected static final float[] stickerVertices = { -0.5F, -0.5F, 0.0F, 1.0F, 0.5F, -0.5F, 0.0F, 1.0F, -0.5F, 0.5F, 0.0F, 1.0F, 0.5F, 0.5F, 0.0F, 1.0F };
  private final FloatBuffer a = buildBuffer(stickerVertices);
  private final FloatBuffer b = buildBuffer(noRotationTextureCoordinates);
  private int c;
  private int d;
  private final float[] e = new float[16];
  private final float[] f = new float[16];
  private List<TuSDKLiveStickerImage> g;
  private RectF h;
  private float i;
  protected FaceAligment[] mFaces;
  protected float mDeviceRadian = 0.0F;
  private boolean j;
  
  public boolean isStickerVisibility()
  {
    return this.j;
  }
  
  public void setStickerVisibility(boolean paramBoolean)
  {
    this.j = paramBoolean;
  }
  
  public TuSDKMap2DFilter()
  {
    this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
  }
  
  public TuSDKMap2DFilter(String paramString)
  {
    this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}", paramString);
  }
  
  public TuSDKMap2DFilter(String paramString1, String paramString2)
  {
    super(paramString1, paramString2);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.c = this.mFilterProgram.uniformIndex("uTexMatrix");
    this.d = this.mFilterProgram.uniformIndex("uMVPMatrix");
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    if (this.mFirstInputFramebuffer == null) {
      return;
    }
    this.b.clear();
    this.b.put(textureCoordinates(this.mInputRotation)).position(0);
    renderToTexture(this.a, this.b);
    informTargetsAboutNewFrame(paramLong);
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
    this.mOutputFramebuffer = this.mFirstInputFramebuffer;
    this.mOutputFramebuffer.activateFramebuffer();
    if (this.mUsingNextFrameForImageCapture) {
      this.mOutputFramebuffer.lock();
    }
    checkGLError(getClass().getSimpleName() + " activateFramebuffer");
    setUniformsForProgramAtIndex(0);
    if ((isStickerVisibility()) && (getStickerCount() > 0))
    {
      GLES20.glEnable(3042);
      a(paramFloatBuffer1, paramFloatBuffer2);
      GLES20.glDisable(3042);
    }
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    cacaptureImageBuffer();
  }
  
  private void a(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    int k = 0;
    int m = getStickerCount();
    while ((k < m) && (k < this.g.size()))
    {
      TuSDKLiveStickerImage localTuSDKLiveStickerImage = (TuSDKLiveStickerImage)this.g.get(k);
      if (localTuSDKLiveStickerImage != null)
      {
        StickerPositionInfo localStickerPositionInfo = localTuSDKLiveStickerImage.getSticker().positionInfo;
        if (localStickerPositionInfo != null)
        {
          switch (1.a[localStickerPositionInfo.getRenderType().ordinal()])
          {
          case 1: 
            GLES20.glBlendFunc(774, 771);
            break;
          case 2: 
            GLES20.glBlendFunc(775, 1);
            break;
          case 3: 
          default: 
            GLES20.glBlendFunc(1, 771);
          }
          if (localTuSDKLiveStickerImage.getSticker().requireFaceFeature())
          {
            int n = a();
            for (int i1 = 0; i1 < n; i1++) {
              a(localTuSDKLiveStickerImage, paramFloatBuffer1, paramFloatBuffer2, i1);
            }
          }
          else
          {
            a(localTuSDKLiveStickerImage, paramFloatBuffer1, paramFloatBuffer2, -1);
          }
          k++;
        }
      }
    }
  }
  
  private void a(TuSDKLiveStickerImage paramTuSDKLiveStickerImage, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2, int paramInt)
  {
    if ((paramTuSDKLiveStickerImage == null) || (!paramTuSDKLiveStickerImage.isEnabled()) || (paramTuSDKLiveStickerImage.getCurrentTextureID() < 1)) {
      return;
    }
    TuSdkSize localTuSdkSize = sizeOfFBO();
    StickerPositionInfo localStickerPositionInfo = paramTuSDKLiveStickerImage.getSticker().positionInfo;
    float[] arrayOfFloat = { -0.5F, -0.5F, 0.0F, 1.0F, 0.5F, -0.5F, 0.0F, 1.0F, -0.5F, 0.5F, 0.0F, 1.0F, 0.5F, 0.5F, 0.0F, 1.0F };
    if (paramInt < 0)
    {
      a(localStickerPositionInfo, localTuSdkSize, paramTuSDKLiveStickerImage.getTextureSize());
    }
    else if (paramInt >= 0)
    {
      if (!a(localStickerPositionInfo, localTuSdkSize, paramInt, paramFloatBuffer1)) {
        return;
      }
      FaceAligment localFaceAligment = a(paramInt);
      arrayOfFloat = a(localFaceAligment, arrayOfFloat);
    }
    paramFloatBuffer1.clear();
    paramFloatBuffer1.put(arrayOfFloat).position(0);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(3553, paramTuSDKLiveStickerImage.getCurrentTextureID());
    GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
    GLES20.glUniformMatrix4fv(this.d, 1, false, this.f, 0);
    GLES20.glUniformMatrix4fv(this.c, 1, false, this.e, 0);
    GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, paramFloatBuffer1);
    GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, paramFloatBuffer2);
    GLES20.glDrawArrays(5, 0, 4);
  }
  
  private float[] a(FaceAligment paramFaceAligment, float[] paramArrayOfFloat)
  {
    if ((paramFaceAligment == null) || (paramArrayOfFloat == null)) {
      return paramArrayOfFloat;
    }
    float f1 = 0.0F;
    if (paramFaceAligment.yaw < 0.0F) {
      f1 = (paramFaceAligment.yaw < -30.0F ? -30.0F : paramFaceAligment.yaw) / 80.0F;
    } else {
      f1 = (paramFaceAligment.yaw > 30.0F ? 30.0F : paramFaceAligment.yaw) / 80.0F;
    }
    f1 = -f1;
    paramArrayOfFloat[3] += -f1;
    paramArrayOfFloat[7] += f1;
    paramArrayOfFloat[11] += -f1;
    paramArrayOfFloat[15] += f1;
    float f2 = 0.0F;
    if (paramFaceAligment.pitch < 0.0F) {
      f2 = (paramFaceAligment.pitch < -30.0F ? -30.0F : paramFaceAligment.pitch) / 120.0F;
    } else {
      f2 = (paramFaceAligment.pitch > 30.0F ? 30.0F : paramFaceAligment.pitch) / 120.0F;
    }
    paramArrayOfFloat[3] -= f2;
    paramArrayOfFloat[7] -= f2;
    return paramArrayOfFloat;
  }
  
  public void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat)
  {
    this.mFaces = paramArrayOfFaceAligment;
    this.mDeviceRadian = ((float)-Math.toRadians(paramFloat));
  }
  
  public void updateStickers(List<TuSDKLiveStickerImage> paramList)
  {
    this.g = paramList;
  }
  
  public void setDisplayRect(RectF paramRectF, float paramFloat)
  {
    this.h = paramRectF;
    this.i = paramFloat;
  }
  
  public void seekStickerToFrameTime(long paramLong)
  {
    if (this.g == null) {
      return;
    }
    Iterator localIterator = this.g.iterator();
    while (localIterator.hasNext())
    {
      TuSDKLiveStickerImage localTuSDKLiveStickerImage = (TuSDKLiveStickerImage)localIterator.next();
      localTuSDKLiveStickerImage.seekStickerToFrameTime(paramLong);
    }
  }
  
  public void setBenchmarkTime(long paramLong)
  {
    if (this.g == null) {
      return;
    }
    Iterator localIterator = this.g.iterator();
    while (localIterator.hasNext())
    {
      TuSDKLiveStickerImage localTuSDKLiveStickerImage = (TuSDKLiveStickerImage)localIterator.next();
      localTuSDKLiveStickerImage.setBenchmarkTime(paramLong);
    }
  }
  
  public void setEnableAutoplayMode(boolean paramBoolean)
  {
    if (this.g == null) {
      return;
    }
    Iterator localIterator = this.g.iterator();
    while (localIterator.hasNext())
    {
      TuSDKLiveStickerImage localTuSDKLiveStickerImage = (TuSDKLiveStickerImage)localIterator.next();
      localTuSDKLiveStickerImage.setEnableAutoplayMode(paramBoolean);
    }
  }
  
  private int a()
  {
    if (this.mFaces == null) {
      return 0;
    }
    return this.mFaces.length;
  }
  
  private FaceAligment a(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= a()) || (this.mFaces == null) || (this.mFaces.length < 1)) {
      return null;
    }
    return this.mFaces[paramInt];
  }
  
  protected int getStickerCount()
  {
    if (this.g == null) {
      return 0;
    }
    return this.g.size();
  }
  
  public TuSDKLiveStickerImage getStickerImageByData(StickerData paramStickerData)
  {
    if ((this.g == null) || (this.g.size() < 1)) {
      return null;
    }
    int k = 0;
    int m = this.g.size();
    while (k < m)
    {
      if (((TuSDKLiveStickerImage)this.g.get(k)).equals(paramStickerData)) {
        return (TuSDKLiveStickerImage)this.g.get(k);
      }
      k++;
    }
    return null;
  }
  
  public int[] getCurrentStickerIndexs()
  {
    if ((this.g == null) || (this.g.size() < 1)) {
      return new int[] { 0 };
    }
    int[] arrayOfInt = new int[this.g.size()];
    for (int k = 0; k < this.g.size(); k++) {
      arrayOfInt[k] = ((TuSDKLiveStickerImage)this.g.get(k)).getCurrentFrameIndex();
    }
    return arrayOfInt;
  }
  
  public void setCurrentStickerIndexs(int[] paramArrayOfInt)
  {
    if ((this.g == null) || (this.g.size() != paramArrayOfInt.length)) {
      return;
    }
    for (int k = 0; k < this.g.size(); k++) {
      ((TuSDKLiveStickerImage)this.g.get(k)).setCurrentFrameIndex(paramArrayOfInt[k]);
    }
  }
  
  private void a(StickerPositionInfo paramStickerPositionInfo, TuSdkSize paramTuSdkSize1, TuSdkSize paramTuSdkSize2)
  {
    RectF localRectF = this.h;
    float f1 = 0.0F;
    PointF localPointF1 = new PointF(0.0F, 0.0F);
    PointF localPointF2 = new PointF(0.0F, 0.0F);
    PointF localPointF3 = new PointF(0.0F, 0.0F);
    PointF localPointF4 = new PointF(paramTuSdkSize1.width, paramTuSdkSize1.height);
    float f4;
    if ((localRectF == null) || (localRectF.isEmpty())) {
      if (this.i > 0.0F)
      {
        TuSdkSize localTuSdkSize = TuSdkSize.create(paramTuSdkSize1);
        localTuSdkSize.width = ((int)(paramTuSdkSize1.height * this.i));
        Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(localTuSdkSize, new Rect(0, 0, paramTuSdkSize1.width, paramTuSdkSize1.height));
        f4 = localRect.left / paramTuSdkSize1.width;
        float f5 = localRect.top / paramTuSdkSize1.height;
        float f6 = localRect.right / paramTuSdkSize1.width;
        float f7 = localRect.bottom / paramTuSdkSize1.height;
        localRectF = new RectF(f4, f5, f6, f7);
      }
      else
      {
        localRectF = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
      }
    }
    if (this.i > 0.0F)
    {
      f2 = paramTuSdkSize1.width / paramTuSdkSize1.height;
      if (f2 > this.i)
      {
        localPointF4.x = (paramTuSdkSize1.height * this.i);
        localPointF4.y = paramTuSdkSize1.height;
      }
      else
      {
        localPointF4.x = paramTuSdkSize1.width;
        localPointF4.y = (paramTuSdkSize1.width / this.i);
      }
    }
    else
    {
      localPointF4.x = (paramTuSdkSize1.width * (localRectF.left + localRectF.right));
      localPointF4.y = (paramTuSdkSize1.height * (localRectF.top + localRectF.bottom));
    }
    localPointF3.x = (paramTuSdkSize1.width * localRectF.left);
    localPointF3.y = (paramTuSdkSize1.height * localRectF.top);
    f1 = paramStickerPositionInfo.rotation;
    localPointF1.x = (paramTuSdkSize2.width * paramStickerPositionInfo.scale);
    localPointF1.y = (paramTuSdkSize2.width * paramStickerPositionInfo.scale / paramStickerPositionInfo.ratio);
    float f2 = localPointF4.x / paramStickerPositionInfo.getDesignScreenSize().width;
    float f3 = localPointF4.y / paramStickerPositionInfo.getDesignScreenSize().height;
    if ((f2 != 1.0F) && (f3 != 1.0F))
    {
      f4 = Math.max(f2, f3);
      localPointF1.x *= f4;
      localPointF1.y *= f4;
    }
    switch (1.b[paramStickerPositionInfo.getPosType().ordinal()])
    {
    case 1: 
      f1 = 0.0F;
      localPointF1.x = localPointF4.x;
      localPointF1.y = localPointF4.y;
      localPointF2.x = (localPointF4.x / 2.0F + localPointF3.x);
      localPointF2.y = (localPointF4.y / 2.0F + localPointF3.y);
      break;
    case 2: 
      localPointF2.x = (localPointF1.x / 2.0F + localPointF4.x * paramStickerPositionInfo.offsetX + localPointF3.x);
      localPointF2.y = (localPointF1.y / 2.0F + localPointF4.y * paramStickerPositionInfo.offsetY + localPointF3.y);
      break;
    case 3: 
      localPointF2.x = (localPointF4.x - localPointF1.x / 2.0F + localPointF4.x * paramStickerPositionInfo.offsetX + localPointF3.x);
      localPointF2.y = (localPointF1.y / 2.0F + localPointF4.y * paramStickerPositionInfo.offsetY + localPointF3.y);
      break;
    case 4: 
      localPointF2.x = (localPointF1.x / 2.0F + localPointF4.x * paramStickerPositionInfo.offsetX + localPointF3.x);
      localPointF2.y = (localPointF4.y - localPointF1.y / 2.0F + localPointF4.y * paramStickerPositionInfo.offsetY + localPointF3.y);
      break;
    case 5: 
      localPointF2.x = (localPointF4.x - localPointF1.x / 2.0F + localPointF4.x * paramStickerPositionInfo.offsetX + localPointF3.x);
      localPointF2.y = (localPointF4.y - localPointF1.y / 2.0F + localPointF4.y * paramStickerPositionInfo.offsetY + localPointF3.y);
      break;
    case 6: 
      localPointF2.x = (localPointF4.x / 2.0F + localPointF4.x * paramStickerPositionInfo.offsetX + localPointF3.x);
      localPointF2.y = (localPointF4.y / 2.0F + localPointF4.y * paramStickerPositionInfo.offsetY + localPointF3.y);
      break;
    case 7: 
      localPointF2.x = (localPointF4.x / 2.0F + localPointF4.x * paramStickerPositionInfo.offsetX + localPointF3.x);
      localPointF2.y = (localPointF1.y / 2.0F + localPointF4.y * paramStickerPositionInfo.offsetY + localPointF3.y);
      break;
    case 8: 
      localPointF2.x = (localPointF1.x / 2.0F + localPointF4.x * paramStickerPositionInfo.offsetX + localPointF3.x);
      localPointF2.y = (localPointF4.y / 2.0F + localPointF4.y * paramStickerPositionInfo.offsetY + localPointF3.y);
      break;
    case 9: 
      localPointF2.x = (localPointF4.x - localPointF1.x / 2.0F + localPointF4.x * paramStickerPositionInfo.offsetX + localPointF3.x);
      localPointF2.y = (localPointF4.y / 2.0F + localPointF4.y * paramStickerPositionInfo.offsetY + localPointF3.y);
      break;
    case 10: 
      localPointF2.x = (localPointF4.x / 2.0F + localPointF4.x * paramStickerPositionInfo.offsetX + localPointF3.x);
      localPointF2.y = (localPointF4.y - localPointF1.y / 2.0F + localPointF4.y * paramStickerPositionInfo.offsetY + localPointF3.y);
      break;
    }
    a(paramTuSdkSize1, localPointF1, localPointF2, f1);
  }
  
  private boolean a(StickerPositionInfo paramStickerPositionInfo, TuSdkSize paramTuSdkSize, int paramInt, FloatBuffer paramFloatBuffer)
  {
    FaceAligment localFaceAligment = a(paramInt);
    if (localFaceAligment == null) {
      return false;
    }
    PointF[] arrayOfPointF = localFaceAligment.getMarks();
    PointF localPointF1 = a(a(paramStickerPositionInfo.getPosType(), true), arrayOfPointF);
    PointF localPointF2 = a(a(paramStickerPositionInfo.getPosType(), false), arrayOfPointF);
    localPointF1.x *= paramTuSdkSize.width;
    localPointF1.y *= paramTuSdkSize.height;
    localPointF2.x *= paramTuSdkSize.width;
    localPointF2.y *= paramTuSdkSize.height;
    PointF localPointF3 = arrayOfPointF[a(paramStickerPositionInfo.getPosType())];
    PointF localPointF4 = new PointF(localPointF3.x * paramTuSdkSize.width, localPointF3.y * paramTuSdkSize.height);
    float f1 = Math.abs(RectHelper.getDistanceOfTwoPoints(localPointF1, localPointF2));
    float f2 = f1 * paramStickerPositionInfo.offsetX;
    float f3 = f1 * paramStickerPositionInfo.offsetY;
    float f4 = (float)-(this.mDeviceRadian + Math.toRadians(localFaceAligment.roll));
    PointF localPointF5 = new PointF(0.0F, 0.0F);
    localPointF5.x = ((float)(localPointF4.x + f2 * Math.cos(f4) + f3 * Math.sin(f4)));
    localPointF5.y = ((float)(localPointF4.y - f2 * Math.sin(f4) + f3 * Math.cos(f4)));
    PointF localPointF6 = new PointF(f1 * paramStickerPositionInfo.scale, f1 * paramStickerPositionInfo.scale / paramStickerPositionInfo.ratio);
    float f5 = (float)Math.atan2(localPointF2.y - localPointF1.y, localPointF2.x - localPointF1.x);
    float f6 = (float)Math.toDegrees(f5);
    a(paramTuSdkSize, localPointF6, localPointF5, f6);
    return true;
  }
  
  private void a(TuSdkSize paramTuSdkSize, PointF paramPointF1, PointF paramPointF2, float paramFloat)
  {
    float[] arrayOfFloat1 = new float[16];
    Matrix.setIdentityM(arrayOfFloat1, 0);
    float[] arrayOfFloat2 = new float[16];
    Matrix.setIdentityM(arrayOfFloat2, 0);
    Matrix.setIdentityM(this.e, 0);
    Matrix.orthoM(arrayOfFloat1, 0, 0.0F, paramTuSdkSize.width, 0.0F, paramTuSdkSize.height, -1.0F, 1.0F);
    Matrix.translateM(arrayOfFloat2, 0, paramPointF2.x, paramPointF2.y, 0.0F);
    if (paramFloat != 0.0F) {
      Matrix.rotateM(arrayOfFloat2, 0, paramFloat, 0.0F, 0.0F, 1.0F);
    }
    Matrix.scaleM(arrayOfFloat2, 0, paramPointF1.x, paramPointF1.y, 1.0F);
    Matrix.multiplyMM(this.f, 0, arrayOfFloat1, 0, arrayOfFloat2, 0);
  }
  
  private int a(StickerPositionInfo.StickerPositionType paramStickerPositionType)
  {
    switch (1.b[paramStickerPositionType.ordinal()])
    {
    case 11: 
      return 27;
    case 12: 
      return 66;
    case 13: 
    case 14: 
      return 30;
    case 15: 
      return 27;
    }
    return 0;
  }
  
  private int[] a(StickerPositionInfo.StickerPositionType paramStickerPositionType, boolean paramBoolean)
  {
    int[] arrayOfInt = null;
    switch (1.b[paramStickerPositionType.ordinal()])
    {
    case 11: 
      if (paramBoolean) {
        arrayOfInt = new int[] { 36, 37, 38, 39, 40, 41 };
      } else {
        arrayOfInt = new int[] { 42, 43, 44, 45, 46, 47 };
      }
      break;
    case 12: 
      if (paramBoolean) {
        arrayOfInt = new int[] { 48, 49, 59 };
      } else {
        arrayOfInt = new int[] { 53, 54, 55 };
      }
      break;
    case 13: 
    case 14: 
      if (paramBoolean) {
        arrayOfInt = new int[] { 2, 29, 30, 31, 32 };
      } else {
        arrayOfInt = new int[] { 14, 29, 30, 34, 35 };
      }
      break;
    case 15: 
      if (paramBoolean) {
        arrayOfInt = new int[] { 36, 37, 38, 39, 40, 41 };
      } else {
        arrayOfInt = new int[] { 42, 43, 44, 45, 46, 47 };
      }
      break;
    }
    return arrayOfInt;
  }
  
  private PointF a(int[] paramArrayOfInt, PointF[] paramArrayOfPointF)
  {
    PointF localPointF1 = new PointF();
    int k = paramArrayOfInt.length;
    for (int n = 0; n < k; n++)
    {
      int m = paramArrayOfInt[n];
      PointF localPointF2 = paramArrayOfPointF[m];
      localPointF1.x += localPointF2.x;
      localPointF1.y += localPointF2.y;
    }
    localPointF1.x /= k;
    localPointF1.y /= k;
    return localPointF1;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\liveSticker\TuSDKMap2DFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */