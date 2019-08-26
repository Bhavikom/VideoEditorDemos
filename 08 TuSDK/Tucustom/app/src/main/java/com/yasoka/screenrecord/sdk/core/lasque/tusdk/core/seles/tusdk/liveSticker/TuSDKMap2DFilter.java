// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker;

import android.opengl.Matrix;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
import android.graphics.PointF;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import java.util.Iterator;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.nio.Buffer;
//import org.lasque.tusdk.core.sticker.StickerPositionInfo;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.face.FaceAligment;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker.StickerPositionInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;

import java.util.List;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKMap2DFilter extends SelesFilter
{
    public static final String TUSDK_MAP_2D_VERTEX_SHADER = "attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}";
    protected static final float[] stickerVertices;
    private final FloatBuffer a;
    private final FloatBuffer b;
    private int c;
    private int d;
    private final float[] e;
    private final float[] f;
    private List<TuSDKLiveStickerImage> g;
    private RectF h;
    private float i;
    protected FaceAligment[] mFaces;
    protected float mDeviceRadian;
    private boolean j;
    
    public boolean isStickerVisibility() {
        return this.j;
    }
    
    public void setStickerVisibility(final boolean j) {
        this.j = j;
    }
    
    public TuSDKMap2DFilter() {
        this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}", "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
    }
    
    public TuSDKMap2DFilter(final String s) {
        this("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;uniform mat4 uMVPMatrix;uniform mat4 uTexMatrix;void main(){    gl_Position = uMVPMatrix * position;\n    textureCoordinate = (uTexMatrix * inputTextureCoordinate).xy;}", s);
    }
    
    public TuSDKMap2DFilter(final String s, final String s2) {
        super(s, s2);
        this.e = new float[16];
        this.f = new float[16];
        this.mDeviceRadian = 0.0f;
        this.a = SelesFilter.buildBuffer(TuSDKMap2DFilter.stickerVertices);
        this.b = SelesFilter.buildBuffer(TuSDKMap2DFilter.noRotationTextureCoordinates);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.c = this.mFilterProgram.uniformIndex("uTexMatrix");
        this.d = this.mFilterProgram.uniformIndex("uMVPMatrix");
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.mFirstInputFramebuffer == null) {
            return;
        }
        this.b.clear();
        this.b.put(SelesFilter.textureCoordinates(this.mInputRotation)).position(0);
        this.renderToTexture(this.a, this.b);
        this.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        this.runPendingOnDrawTasks();
        if (this.isPreventRendering()) {
            this.inputFramebufferUnlock();
            return;
        }
        SelesContext.setActiveShaderProgram(this.mFilterProgram);
        (this.mOutputFramebuffer = this.mFirstInputFramebuffer).activateFramebuffer();
        if (this.mUsingNextFrameForImageCapture) {
            this.mOutputFramebuffer.lock();
        }
        this.checkGLError(this.getClass().getSimpleName() + " activateFramebuffer");
        this.setUniformsForProgramAtIndex(0);
        if (this.isStickerVisibility() && this.getStickerCount() > 0) {
            GLES20.glEnable(3042);
            this.a(floatBuffer, floatBuffer2);
            GLES20.glDisable(3042);
        }
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
        this.cacaptureImageBuffer();
    }
    
    private void a(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        int n = 0;
        while (n < this.getStickerCount() && n < this.g.size()) {
            final TuSDKLiveStickerImage tuSDKLiveStickerImage = this.g.get(n);
            if (tuSDKLiveStickerImage == null) {
                continue;
            }
            final StickerPositionInfo positionInfo = tuSDKLiveStickerImage.getSticker().positionInfo;
            if (positionInfo == null) {
                continue;
            }
            switch (positionInfo.getRenderType().ordinal()) {
                case 1: {
                    GLES20.glBlendFunc(774, 771);
                    break;
                }
                case 2: {
                    GLES20.glBlendFunc(775, 1);
                    break;
                }
                default: {
                    GLES20.glBlendFunc(1, 771);
                    break;
                }
            }
            if (tuSDKLiveStickerImage.getSticker().requireFaceFeature()) {
                for (int a = this.a(), i = 0; i < a; ++i) {
                    this.a(tuSDKLiveStickerImage, floatBuffer, floatBuffer2, i);
                }
            }
            else {
                this.a(tuSDKLiveStickerImage, floatBuffer, floatBuffer2, -1);
            }
            ++n;
        }
    }
    
    private void a(final TuSDKLiveStickerImage tuSDKLiveStickerImage, final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2, final int n) {
        if (tuSDKLiveStickerImage == null || !tuSDKLiveStickerImage.isEnabled() || tuSDKLiveStickerImage.getCurrentTextureID() < 1) {
            return;
        }
        final TuSdkSize sizeOfFBO = this.sizeOfFBO();
        final StickerPositionInfo positionInfo = tuSDKLiveStickerImage.getSticker().positionInfo;
        float[] a = { -0.5f, -0.5f, 0.0f, 1.0f, 0.5f, -0.5f, 0.0f, 1.0f, -0.5f, 0.5f, 0.0f, 1.0f, 0.5f, 0.5f, 0.0f, 1.0f };
        if (n < 0) {
            this.a(positionInfo, sizeOfFBO, tuSDKLiveStickerImage.getTextureSize());
        }
        else if (n >= 0) {
            if (!this.a(positionInfo, sizeOfFBO, n, floatBuffer)) {
                return;
            }
            a = this.a(this.a(n), a);
        }
        floatBuffer.clear();
        floatBuffer.put(a).position(0);
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, tuSDKLiveStickerImage.getCurrentTextureID());
        GLES20.glUniform1i(this.mFilterInputTextureUniform, 2);
        GLES20.glUniformMatrix4fv(this.d, 1, false, this.f, 0);
        GLES20.glUniformMatrix4fv(this.c, 1, false, this.e, 0);
        GLES20.glVertexAttribPointer(this.mFilterPositionAttribute, 4, 5126, false, 0, (Buffer)floatBuffer);
        GLES20.glVertexAttribPointer(this.mFilterTextureCoordinateAttribute, 2, 5126, false, 0, (Buffer)floatBuffer2);
        GLES20.glDrawArrays(5, 0, 4);
    }
    
    private float[] a(final FaceAligment faceAligment, final float[] array) {
        if (faceAligment == null || array == null) {
            return array;
        }
        float n;
        if (faceAligment.yaw < 0.0f) {
            n = ((faceAligment.yaw < -30.0f) ? -30.0f : faceAligment.yaw) / 80.0f;
        }
        else {
            n = ((faceAligment.yaw > 30.0f) ? 30.0f : faceAligment.yaw) / 80.0f;
        }
        final float n2 = -n;
        final int n3 = 3;
        array[n3] += -n2;
        final int n4 = 7;
        array[n4] += n2;
        final int n5 = 11;
        array[n5] += -n2;
        final int n6 = 15;
        array[n6] += n2;
        float n7;
        if (faceAligment.pitch < 0.0f) {
            n7 = ((faceAligment.pitch < -30.0f) ? -30.0f : faceAligment.pitch) / 120.0f;
        }
        else {
            n7 = ((faceAligment.pitch > 30.0f) ? 30.0f : faceAligment.pitch) / 120.0f;
        }
        final int n8 = 3;
        array[n8] -= n7;
        final int n9 = 7;
        array[n9] -= n7;
        return array;
    }
    
    public void updateFaceFeatures(final FaceAligment[] mFaces, final float n) {
        this.mFaces = mFaces;
        this.mDeviceRadian = (float)(-Math.toRadians(n));
    }
    
    public void updateStickers(final List<TuSDKLiveStickerImage> g) {
        this.g = g;
    }
    
    public void setDisplayRect(final RectF h, final float i) {
        this.h = h;
        this.i = i;
    }
    
    public void seekStickerToFrameTime(final long n) {
        if (this.g == null) {
            return;
        }
        final Iterator<TuSDKLiveStickerImage> iterator = this.g.iterator();
        while (iterator.hasNext()) {
            iterator.next().seekStickerToFrameTime(n);
        }
    }
    
    public void setBenchmarkTime(final long benchmarkTime) {
        if (this.g == null) {
            return;
        }
        final Iterator<TuSDKLiveStickerImage> iterator = this.g.iterator();
        while (iterator.hasNext()) {
            iterator.next().setBenchmarkTime(benchmarkTime);
        }
    }
    
    public void setEnableAutoplayMode(final boolean enableAutoplayMode) {
        if (this.g == null) {
            return;
        }
        final Iterator<TuSDKLiveStickerImage> iterator = this.g.iterator();
        while (iterator.hasNext()) {
            iterator.next().setEnableAutoplayMode(enableAutoplayMode);
        }
    }
    
    private int a() {
        if (this.mFaces == null) {
            return 0;
        }
        return this.mFaces.length;
    }
    
    private FaceAligment a(final int n) {
        if (n < 0 || n >= this.a() || this.mFaces == null || this.mFaces.length < 1) {
            return null;
        }
        return this.mFaces[n];
    }
    
    protected int getStickerCount() {
        if (this.g == null) {
            return 0;
        }
        return this.g.size();
    }
    
    public TuSDKLiveStickerImage getStickerImageByData(final StickerData obj) {
        if (this.g == null || this.g.size() < 1) {
            return null;
        }
        for (int i = 0; i < this.g.size(); ++i) {
            if (this.g.get(i).equals(obj)) {
                return this.g.get(i);
            }
        }
        return null;
    }
    
    public int[] getCurrentStickerIndexs() {
        if (this.g == null || this.g.size() < 1) {
            return new int[] { 0 };
        }
        final int[] array = new int[this.g.size()];
        for (int i = 0; i < this.g.size(); ++i) {
            array[i] = this.g.get(i).getCurrentFrameIndex();
        }
        return array;
    }
    
    public void setCurrentStickerIndexs(final int[] array) {
        if (this.g == null || this.g.size() != array.length) {
            return;
        }
        for (int i = 0; i < this.g.size(); ++i) {
            this.g.get(i).setCurrentFrameIndex(array[i]);
        }
    }
    
    private void a(final StickerPositionInfo stickerPositionInfo, final TuSdkSize tuSdkSize, final TuSdkSize tuSdkSize2) {
        RectF h = this.h;
        final PointF pointF = new PointF(0.0f, 0.0f);
        final PointF pointF2 = new PointF(0.0f, 0.0f);
        final PointF pointF3 = new PointF(0.0f, 0.0f);
        final PointF pointF4 = new PointF((float)tuSdkSize.width, (float)tuSdkSize.height);
        if (h == null || h.isEmpty()) {
            if (this.i > 0.0f) {
                final TuSdkSize create = TuSdkSize.create(tuSdkSize);
                create.width = (int)(tuSdkSize.height * this.i);
                final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(create, new Rect(0, 0, tuSdkSize.width, tuSdkSize.height));
                h = new RectF(rectWithAspectRatioInsideRect.left / (float)tuSdkSize.width, rectWithAspectRatioInsideRect.top / (float)tuSdkSize.height, rectWithAspectRatioInsideRect.right / (float)tuSdkSize.width, rectWithAspectRatioInsideRect.bottom / (float)tuSdkSize.height);
            }
            else {
                h = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
            }
        }
        if (this.i > 0.0f) {
            if (tuSdkSize.width / (float)tuSdkSize.height > this.i) {
                pointF4.x = tuSdkSize.height * this.i;
                pointF4.y = (float)tuSdkSize.height;
            }
            else {
                pointF4.x = (float)tuSdkSize.width;
                pointF4.y = tuSdkSize.width / this.i;
            }
        }
        else {
            pointF4.x = tuSdkSize.width * (h.left + h.right);
            pointF4.y = tuSdkSize.height * (h.top + h.bottom);
        }
        pointF3.x = tuSdkSize.width * h.left;
        pointF3.y = tuSdkSize.height * h.top;
        float rotation = stickerPositionInfo.rotation;
        pointF.x = tuSdkSize2.width * stickerPositionInfo.scale;
        pointF.y = tuSdkSize2.width * stickerPositionInfo.scale / stickerPositionInfo.ratio;
        final float a = pointF4.x / stickerPositionInfo.getDesignScreenSize().width;
        final float b = pointF4.y / stickerPositionInfo.getDesignScreenSize().height;
        if (a != 1.0f && b != 1.0f) {
            final float max = Math.max(a, b);
            pointF.x *= max;
            pointF.y *= max;
        }
        switch (stickerPositionInfo.getPosType().ordinal()) {
            case 1: {
                rotation = 0.0f;
                pointF.x = pointF4.x;
                pointF.y = pointF4.y;
                pointF2.x = pointF4.x / 2.0f + pointF3.x;
                pointF2.y = pointF4.y / 2.0f + pointF3.y;
                break;
            }
            case 2: {
                pointF2.x = pointF.x / 2.0f + pointF4.x * stickerPositionInfo.offsetX + pointF3.x;
                pointF2.y = pointF.y / 2.0f + pointF4.y * stickerPositionInfo.offsetY + pointF3.y;
                break;
            }
            case 3: {
                pointF2.x = pointF4.x - pointF.x / 2.0f + pointF4.x * stickerPositionInfo.offsetX + pointF3.x;
                pointF2.y = pointF.y / 2.0f + pointF4.y * stickerPositionInfo.offsetY + pointF3.y;
                break;
            }
            case 4: {
                pointF2.x = pointF.x / 2.0f + pointF4.x * stickerPositionInfo.offsetX + pointF3.x;
                pointF2.y = pointF4.y - pointF.y / 2.0f + pointF4.y * stickerPositionInfo.offsetY + pointF3.y;
                break;
            }
            case 5: {
                pointF2.x = pointF4.x - pointF.x / 2.0f + pointF4.x * stickerPositionInfo.offsetX + pointF3.x;
                pointF2.y = pointF4.y - pointF.y / 2.0f + pointF4.y * stickerPositionInfo.offsetY + pointF3.y;
                break;
            }
            case 6: {
                pointF2.x = pointF4.x / 2.0f + pointF4.x * stickerPositionInfo.offsetX + pointF3.x;
                pointF2.y = pointF4.y / 2.0f + pointF4.y * stickerPositionInfo.offsetY + pointF3.y;
                break;
            }
            case 7: {
                pointF2.x = pointF4.x / 2.0f + pointF4.x * stickerPositionInfo.offsetX + pointF3.x;
                pointF2.y = pointF.y / 2.0f + pointF4.y * stickerPositionInfo.offsetY + pointF3.y;
                break;
            }
            case 8: {
                pointF2.x = pointF.x / 2.0f + pointF4.x * stickerPositionInfo.offsetX + pointF3.x;
                pointF2.y = pointF4.y / 2.0f + pointF4.y * stickerPositionInfo.offsetY + pointF3.y;
                break;
            }
            case 9: {
                pointF2.x = pointF4.x - pointF.x / 2.0f + pointF4.x * stickerPositionInfo.offsetX + pointF3.x;
                pointF2.y = pointF4.y / 2.0f + pointF4.y * stickerPositionInfo.offsetY + pointF3.y;
                break;
            }
            case 10: {
                pointF2.x = pointF4.x / 2.0f + pointF4.x * stickerPositionInfo.offsetX + pointF3.x;
                pointF2.y = pointF4.y - pointF.y / 2.0f + pointF4.y * stickerPositionInfo.offsetY + pointF3.y;
                break;
            }
        }
        this.a(tuSdkSize, pointF, pointF2, rotation);
    }
    
    private boolean a(final StickerPositionInfo stickerPositionInfo, final TuSdkSize tuSdkSize, final int n, final FloatBuffer floatBuffer) {
        final FaceAligment a = this.a(n);
        if (a == null) {
            return false;
        }
        final PointF[] marks = a.getMarks();
        final PointF a2 = this.a(this.a(stickerPositionInfo.getPosType(), true), marks);
        final PointF a3 = this.a(this.a(stickerPositionInfo.getPosType(), false), marks);
        final PointF pointF = a2;
        pointF.x *= tuSdkSize.width;
        final PointF pointF2 = a2;
        pointF2.y *= tuSdkSize.height;
        final PointF pointF3 = a3;
        pointF3.x *= tuSdkSize.width;
        final PointF pointF4 = a3;
        pointF4.y *= tuSdkSize.height;
        final PointF pointF5 = marks[this.a(stickerPositionInfo.getPosType())];
        final PointF pointF6 = new PointF(pointF5.x * tuSdkSize.width, pointF5.y * tuSdkSize.height);
        final float abs = Math.abs(RectHelper.getDistanceOfTwoPoints(a2, a3));
        final float n2 = abs * stickerPositionInfo.offsetX;
        final float n3 = abs * stickerPositionInfo.offsetY;
        final float n4 = (float)(-(this.mDeviceRadian + Math.toRadians(a.roll)));
        final PointF pointF7 = new PointF(0.0f, 0.0f);
        pointF7.x = (float)(pointF6.x + n2 * Math.cos(n4) + n3 * Math.sin(n4));
        pointF7.y = (float)(pointF6.y - n2 * Math.sin(n4) + n3 * Math.cos(n4));
        this.a(tuSdkSize, new PointF(abs * stickerPositionInfo.scale, abs * stickerPositionInfo.scale / stickerPositionInfo.ratio), pointF7, (float)Math.toDegrees((float)Math.atan2(a3.y - a2.y, a3.x - a2.x)));
        return true;
    }
    
    private void a(final TuSdkSize tuSdkSize, final PointF pointF, final PointF pointF2, final float n) {
        final float[] array = new float[16];
        Matrix.setIdentityM(array, 0);
        final float[] array2 = new float[16];
        Matrix.setIdentityM(array2, 0);
        Matrix.setIdentityM(this.e, 0);
        Matrix.orthoM(array, 0, 0.0f, (float)tuSdkSize.width, 0.0f, (float)tuSdkSize.height, -1.0f, 1.0f);
        Matrix.translateM(array2, 0, pointF2.x, pointF2.y, 0.0f);
        if (n != 0.0f) {
            Matrix.rotateM(array2, 0, n, 0.0f, 0.0f, 1.0f);
        }
        Matrix.scaleM(array2, 0, pointF.x, pointF.y, 1.0f);
        Matrix.multiplyMM(this.f, 0, array, 0, array2, 0);
    }
    
    private int a(final StickerPositionInfo.StickerPositionType stickerPositionType) {
        switch (stickerPositionType.ordinal()) {
            case 11: {
                return 27;
            }
            case 12: {
                return 66;
            }
            case 13:
            case 14: {
                return 30;
            }
            case 15: {
                return 27;
            }
            default: {
                return 0;
            }
        }
    }
    
    private int[] a(final StickerPositionInfo.StickerPositionType stickerPositionType, final boolean b) {
        int[] array = null;
        switch (stickerPositionType.ordinal()) {
            case 11: {
                if (b) {
                    array = new int[] { 36, 37, 38, 39, 40, 41 };
                    break;
                }
                array = new int[] { 42, 43, 44, 45, 46, 47 };
                break;
            }
            case 12: {
                if (b) {
                    array = new int[] { 48, 49, 59 };
                    break;
                }
                array = new int[] { 53, 54, 55 };
                break;
            }
            case 13:
            case 14: {
                if (b) {
                    array = new int[] { 2, 29, 30, 31, 32 };
                    break;
                }
                array = new int[] { 14, 29, 30, 34, 35 };
                break;
            }
            case 15: {
                if (b) {
                    array = new int[] { 36, 37, 38, 39, 40, 41 };
                    break;
                }
                array = new int[] { 42, 43, 44, 45, 46, 47 };
                break;
            }
        }
        return array;
    }
    
    private PointF a(final int[] array, final PointF[] array2) {
        final PointF pointF = new PointF();
        final int length = array.length;
        for (int i = 0; i < length; ++i) {
            final PointF pointF2 = array2[array[i]];
            final PointF pointF3 = pointF;
            pointF3.x += pointF2.x;
            final PointF pointF4 = pointF;
            pointF4.y += pointF2.y;
        }
        pointF.x /= length;
        pointF.y /= length;
        return pointF;
    }
    
    static {
        stickerVertices = new float[] { -0.5f, -0.5f, 0.0f, 1.0f, 0.5f, -0.5f, 0.0f, 1.0f, -0.5f, 0.5f, 0.0f, 1.0f, 0.5f, 0.5f, 0.0f, 1.0f };
    }
}
