// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.PointF;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkFace
{
    public RectF rect;
    public int score;
    public int id;
    public PointF leftEye;
    public PointF rightEye;
    public PointF mouth;
    
    public TuSdkFace() {
        this.id = -1;
        this.leftEye = null;
        this.rightEye = null;
        this.mouth = null;
    }
    
    @Override
    public String toString() {
        return String.format("Class [%s]: %s \n detail id[%s]: rect[%s], score[%s], leftEye[%s], rightEye[%s], mouth[%s]", this.hashCode(), this.getClass().getName(), this.id, this.rect, this.score, this.leftEye, this.rightEye, this.mouth);
    }
    
    public static void convertOrientation(final TuSdkFace tuSdkFace, final ImageOrientation imageOrientation) {
        if (tuSdkFace == null || imageOrientation == null) {
            return;
        }
        tuSdkFace.leftEye = a(tuSdkFace.leftEye, imageOrientation);
        tuSdkFace.rightEye = a(tuSdkFace.rightEye, imageOrientation);
        tuSdkFace.mouth = a(tuSdkFace.mouth, imageOrientation);
        final PointF a = a(new PointF(tuSdkFace.rect.left, tuSdkFace.rect.top), imageOrientation);
        final PointF a2 = a(new PointF(tuSdkFace.rect.right, tuSdkFace.rect.bottom), imageOrientation);
        tuSdkFace.rect = new RectF();
        tuSdkFace.rect.left = Math.min(a.x, a2.x);
        tuSdkFace.rect.top = Math.min(a.y, a2.y);
        tuSdkFace.rect.right = Math.max(a.x, a2.x);
        tuSdkFace.rect.bottom = Math.max(a.y, a2.y);
    }
    
    private static PointF a(final PointF pointF, final ImageOrientation imageOrientation) {
        if (pointF == null) {
            return pointF;
        }
        final PointF pointF2 = new PointF();
        pointF2.set(pointF);
        switch (imageOrientation.ordinal()) {
            case 1: {
                pointF2.x = 1.0f - pointF.y;
                pointF2.y = pointF.x;
                break;
            }
            case 2: {
                pointF2.x = 1.0f - pointF.x;
                pointF2.y = 1.0f - pointF.y;
                break;
            }
            case 3: {
                pointF2.x = pointF.y;
                pointF2.y = 1.0f - pointF.x;
                break;
            }
            case 4: {
                pointF2.x = 1.0f - pointF.x;
                pointF2.y = pointF.y;
                break;
            }
            case 5: {
                pointF2.x = pointF.y;
                pointF2.y = pointF.x;
                break;
            }
            case 6: {
                pointF2.x = pointF.x;
                pointF2.y = 1.0f - pointF.y;
                break;
            }
            case 7: {
                pointF2.x = 1.0f - pointF.y;
                pointF2.y = 1.0f - pointF.x;
                break;
            }
        }
        return pointF2;
    }
}
