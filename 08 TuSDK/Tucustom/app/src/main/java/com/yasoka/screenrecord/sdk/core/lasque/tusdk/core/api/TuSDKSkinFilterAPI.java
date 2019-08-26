// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api;

//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.TuSdk;
//import org.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import java.io.File;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.network.analysis.ImageOnlineAnalysis;
//import org.lasque.tusdk.core.network.analysis.ImageMarkFaceResult;
//import org.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis;
import android.graphics.Bitmap;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageAutoColorAnalysis;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageMark5FaceArgument;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageMarkFaceAnalysis;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageMarkFaceResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis.ImageOnlineAnalysis;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinWhiteningFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinWhiteningFilter;
//import org.lasque.tusdk.core.network.analysis.ImageMarkFaceAnalysis;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

public class TuSDKSkinFilterAPI
{
    private float a;
    private float b;
    private float c;
    private float d;
    private float e;
    private FilterWrap f;
    private ImageMarkFaceAnalysis g;
    private TuSDKSkinWhiteningFilter h;
    private PointF[] i;
    private Object j;
    private SkinFilterManagerDelegate k;
    private SkinFilterManagerDelegate l;
    private AutoAdjustResultDelegate m;
    private Bitmap n;
    private boolean o;
    private ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener p;
    private ImageAutoColorAnalysis q;
    private ImageAutoColorAnalysis.ImageAutoColorAnalysisListener r;
    private ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener s;
    
    public TuSDKSkinFilterAPI(final float n, final float n2, final float n3, final float n4, final float n5) {
        this.a = 0.3f;
        this.b = 0.3f;
        this.c = 5000.0f;
        this.d = 1.045f;
        this.e = 0.048f;
        this.j = new Object();
        this.o = true;
        this.p = new ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener() {
            @Override
            public void onImageFaceAnalysisCompleted(final ImageMarkFaceResult imageMarkFaceResult, final ImageOnlineAnalysis.ImageAnalysisType imageAnalysisType) {
                synchronized (TuSDKSkinFilterAPI.this.j) {
                    switch (imageAnalysisType.ordinal()) {
                        case 1: {
                            if (imageMarkFaceResult == null || imageMarkFaceResult.count <= 0) {
                                TLog.e("Error: no face detected", new Object[0]);
                                if (TuSDKSkinFilterAPI.this.k != null) {
                                    TuSDKSkinFilterAPI.this.k.onFaceMarkResult(TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeNoFaceDetected);
                                    break;
                                }
                                break;
                            }
                            else {
                                if (imageMarkFaceResult.count == 1) {
                                    TuSDKSkinFilterAPI.this.i = TuSDKSkinFilterAPI.this.a(imageMarkFaceResult);
                                    final FaceAligment faceAligment = new FaceAligment();
                                    faceAligment.setOrginMarks(TuSDKSkinFilterAPI.this.i);
                                    TuSDKSkinFilterAPI.this.h.updateFaceFeatures(new FaceAligment[] { faceAligment }, 0.0f);
                                    TuSDKSkinFilterAPI.this.j.notify();
                                    if (TuSDKSkinFilterAPI.this.k != null) {
                                        TuSDKSkinFilterAPI.this.k.onFaceMarkResult(TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeSucceed);
                                    }
                                    return;
                                }
                                TLog.e("Error: multiple faces detected", new Object[0]);
                                if (TuSDKSkinFilterAPI.this.k != null) {
                                    TuSDKSkinFilterAPI.this.k.onFaceMarkResult(TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeFailedMultipleFacesDetected);
                                    break;
                                }
                                break;
                            }
                        }
                        case 2: {
                            TLog.e("You are not allowed to use the face mark api, please see http://tusdk.com", new Object[0]);
                            if (TuSDKSkinFilterAPI.this.k != null) {
                                TuSDKSkinFilterAPI.this.k.onFaceMarkResult(TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeFailed);
                                break;
                            }
                            break;
                        }
                        default: {
                            if (TuSDKSkinFilterAPI.this.k != null) {
                                TuSDKSkinFilterAPI.this.k.onFaceMarkResult(TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeFailed);
                            }
                            TLog.e("error on face mark :%s", imageAnalysisType);
                            break;
                        }
                    }
                    TuSDKSkinFilterAPI.this.j.notify();
                }
            }
        };
        this.r = new ImageAutoColorAnalysis.ImageAutoColorAnalysisListener() {
            @Override
            public void onImageAutoColorAnalysisCompleted(final Bitmap bitmap, final ImageOnlineAnalysis.ImageAnalysisType imageAnalysisType) {
                if (imageAnalysisType == ImageOnlineAnalysis.ImageAnalysisType.Succeed) {
                    if (TuSDKSkinFilterAPI.this.m != null) {
                        TuSDKSkinFilterAPI.this.m.onGetAutoAdjustResult(bitmap);
                    }
                }
                else {
                    if (TuSDKSkinFilterAPI.this.m != null) {
                        TuSDKSkinFilterAPI.this.m.onGetAutoAdjustResult(TuSDKSkinFilterAPI.this.n);
                    }
                    TLog.e("error on auto adjust:%s", imageAnalysisType);
                }
            }
        };
        this.s = new ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener() {
            @Override
            public void onImageAutoColorAnalysisCopyCompleted(final File file) {
            }
        };
        this.setParameters(n, n2, n3, n4, n5);
    }
    
    public TuSDKSkinFilterAPI() {
        this.a = 0.3f;
        this.b = 0.3f;
        this.c = 5000.0f;
        this.d = 1.045f;
        this.e = 0.048f;
        this.j = new Object();
        this.o = true;
        this.p = new ImageMarkFaceAnalysis.ImageFaceMarkAnalysisListener() {
            @Override
            public void onImageFaceAnalysisCompleted(final ImageMarkFaceResult imageMarkFaceResult, final ImageOnlineAnalysis.ImageAnalysisType imageAnalysisType) {
                synchronized (TuSDKSkinFilterAPI.this.j) {
                    switch (imageAnalysisType.ordinal()) {
                        case 1: {
                            if (imageMarkFaceResult == null || imageMarkFaceResult.count <= 0) {
                                TLog.e("Error: no face detected", new Object[0]);
                                if (TuSDKSkinFilterAPI.this.k != null) {
                                    TuSDKSkinFilterAPI.this.k.onFaceMarkResult(TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeNoFaceDetected);
                                    break;
                                }
                                break;
                            }
                            else {
                                if (imageMarkFaceResult.count == 1) {
                                    TuSDKSkinFilterAPI.this.i = TuSDKSkinFilterAPI.this.a(imageMarkFaceResult);
                                    final FaceAligment faceAligment = new FaceAligment();
                                    faceAligment.setOrginMarks(TuSDKSkinFilterAPI.this.i);
                                    TuSDKSkinFilterAPI.this.h.updateFaceFeatures(new FaceAligment[] { faceAligment }, 0.0f);
                                    TuSDKSkinFilterAPI.this.j.notify();
                                    if (TuSDKSkinFilterAPI.this.k != null) {
                                        TuSDKSkinFilterAPI.this.k.onFaceMarkResult(TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeSucceed);
                                    }
                                    return;
                                }
                                TLog.e("Error: multiple faces detected", new Object[0]);
                                if (TuSDKSkinFilterAPI.this.k != null) {
                                    TuSDKSkinFilterAPI.this.k.onFaceMarkResult(TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeFailedMultipleFacesDetected);
                                    break;
                                }
                                break;
                            }
                        }
                        case 2: {
                            TLog.e("You are not allowed to use the face mark api, please see http://tusdk.com", new Object[0]);
                            if (TuSDKSkinFilterAPI.this.k != null) {
                                TuSDKSkinFilterAPI.this.k.onFaceMarkResult(TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeFailed);
                                break;
                            }
                            break;
                        }
                        default: {
                            if (TuSDKSkinFilterAPI.this.k != null) {
                                TuSDKSkinFilterAPI.this.k.onFaceMarkResult(TuSDKSkinFilterFaceMarkResultType.TuSDKSkinFilterFaceMarkResultTypeFailed);
                            }
                            TLog.e("error on face mark :%s", imageAnalysisType);
                            break;
                        }
                    }
                    TuSDKSkinFilterAPI.this.j.notify();
                }
            }
        };
        this.r = new ImageAutoColorAnalysis.ImageAutoColorAnalysisListener() {
            @Override
            public void onImageAutoColorAnalysisCompleted(final Bitmap bitmap, final ImageOnlineAnalysis.ImageAnalysisType imageAnalysisType) {
                if (imageAnalysisType == ImageOnlineAnalysis.ImageAnalysisType.Succeed) {
                    if (TuSDKSkinFilterAPI.this.m != null) {
                        TuSDKSkinFilterAPI.this.m.onGetAutoAdjustResult(bitmap);
                    }
                }
                else {
                    if (TuSDKSkinFilterAPI.this.m != null) {
                        TuSDKSkinFilterAPI.this.m.onGetAutoAdjustResult(TuSDKSkinFilterAPI.this.n);
                    }
                    TLog.e("error on auto adjust:%s", imageAnalysisType);
                }
            }
        };
        this.s = new ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener() {
            @Override
            public void onImageAutoColorAnalysisCopyCompleted(final File file) {
            }
        };
    }
    
    private FilterWrap a() {
        if (this.f == null) {}
        final FilterOption filterOption = new FilterOption() {
            @Override
            public SelesOutInput getFilter() {
                final TuSDKSkinWhiteningFilter tuSDKSkinWhiteningFilter = new TuSDKSkinWhiteningFilter();
                tuSDKSkinWhiteningFilter.setSmoothing(TuSDKSkinFilterAPI.this.a);
                tuSDKSkinWhiteningFilter.setWhitening(TuSDKSkinFilterAPI.this.b);
                tuSDKSkinWhiteningFilter.setSkinColor(TuSDKSkinFilterAPI.this.c);
                tuSDKSkinWhiteningFilter.setEyeEnlargeSize(TuSDKSkinFilterAPI.this.d);
                tuSDKSkinWhiteningFilter.setChinSize(TuSDKSkinFilterAPI.this.e);
                TuSDKSkinFilterAPI.this.h = tuSDKSkinWhiteningFilter;
                return tuSDKSkinWhiteningFilter;
            }
        };
        filterOption.id = Long.MAX_VALUE;
        filterOption.canDefinition = true;
        filterOption.isInternal = true;
        final ArrayList<String> internalTextures = new ArrayList<String>();
        internalTextures.add("f8a6ed3ec939d6941c94a272aff1791b");
        filterOption.internalTextures = internalTextures;
        return FilterWrap.creat(filterOption);
    }
    
    public TuSDKSkinFilterAPI setSmoothing(final float a) {
        if (a < 0.0f || a > 1.0f) {
            return this;
        }
        this.a = a;
        return this;
    }
    
    public TuSDKSkinFilterAPI setWhitening(final float b) {
        if (b < 0.0f || b > 1.0f) {
            return this;
        }
        this.b = b;
        return this;
    }
    
    public TuSDKSkinFilterAPI setSkinColor(final float c) {
        if (c < 4000.0f || c > 6000.0f) {
            return this;
        }
        this.c = c;
        return this;
    }
    
    public TuSDKSkinFilterAPI setEyeSize(final float d) {
        if (d < 1.0f || d > 1.2f) {
            return this;
        }
        this.d = d;
        return this;
    }
    
    public TuSDKSkinFilterAPI setChinSize(final float e) {
        if (e < 0.0f || e > 0.1f) {
            return this;
        }
        this.e = e;
        return this;
    }
    
    public TuSDKSkinFilterAPI setParameters(final float smoothing, final float whitening, final float skinColor, final float eyeSize, final float chinSize) {
        return this.setSmoothing(smoothing).setWhitening(whitening).setSkinColor(skinColor).setEyeSize(eyeSize).setChinSize(chinSize);
    }
    
    public void process(final Bitmap n, final SkinFilterManagerDelegate k) {
        if (!this.b()) {
            if (k != null) {
                k.onGetSkinFilterResult(n);
            }
            return;
        }
        this.k = k;
        this.o = (this.n == null || this.n.isRecycled() || this.n != n || this.i == null || this.i.length == 0);
        this.a(this.n = n, ImageOrientation.Up);
        if (this.o) {
            this.b(n);
        }
    }
    
    private void a(final Bitmap bitmap, final ImageOrientation imageOrientation) {
        this.a(bitmap, imageOrientation, 0.0f);
    }
    
    private void a(final Bitmap bitmap, final ImageOrientation imageOrientation, final float n) {
        this.a(bitmap, null, imageOrientation, n);
    }

    private void a(final Bitmap var1, SelesParameters var2, final ImageOrientation var3, final float var4) {
        if (this.o) {
            this.i = null;
            this.a(var1);
        }

        final FilterWrap var5 = this.a();
        var5.setFilterParameter(var2);
        final TuSDKSkinWhiteningFilter var6 = (TuSDKSkinWhiteningFilter)var5.getFilter();
        ThreadHelper.runThread(new Runnable() {
            public void run() {
                synchronized(TuSDKSkinFilterAPI.this.j) {
                    try {
                        if (TuSDKSkinFilterAPI.this.o) {
                            TuSDKSkinFilterAPI.this.j.wait();
                        }
                    } catch (InterruptedException var4x) {
                        var4x.printStackTrace();
                    }
                }

                if (var6 != null) {
                    FaceAligment var1x = new FaceAligment();
                    var1x.setOrginMarks(TuSDKSkinFilterAPI.this.i);
                    var6.updateFaceFeatures(new FaceAligment[]{var1x}, 0.0F);
                }

                Bitmap var6x = var5.process(var1, var3, var4);
                if (TuSDKSkinFilterAPI.this.k != null) {
                    TuSDKSkinFilterAPI.this.k.onGetSkinFilterResult(var6x);
                }

            }
        });
    }
    
    public void handleLocalSkinFilterProcess(final Bitmap bitmap, final SkinFilterManagerDelegate l) {
        if (!this.b()) {
            if (l != null) {
                l.onGetSkinFilterResult(bitmap);
            }
            return;
        }
        this.l = l;
        this.b(bitmap, null, ImageOrientation.Up, 0.0f);
    }
    
    private void b(final Bitmap bitmap, final SelesParameters selesParameters, final ImageOrientation imageOrientation, final float n) {
        final Bitmap process = this.a().process(bitmap, imageOrientation, n);
        if (this.l != null) {
            this.l.onGetSkinFilterResult(process);
        }
    }
    
    private void a(final Bitmap bitmap) {
        if (this.g == null) {
            this.g = new ImageMarkFaceAnalysis();
        }
        else {
            this.g.reset();
        }
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                TuSDKSkinFilterAPI.this.g.analysisWithThumb(bitmap, TuSDKSkinFilterAPI.this.p);
            }
        });
    }
    
    private PointF[] a(final ImageMarkFaceResult imageMarkFaceResult) {
        final PointF[] array = new PointF[5];
        int n = 0;
        final ImageMark5FaceArgument.ImageItems imageItems = imageMarkFaceResult.items.get(0);
        final ImageMark5FaceArgument.ImageMarksPoints eye_left = imageItems.marks.eye_left;
        array[n++] = new PointF(eye_left.x, eye_left.y);
        final ImageMark5FaceArgument.ImageMarksPoints eye_right = imageItems.marks.eye_right;
        array[n++] = new PointF(eye_right.x, eye_right.y);
        final ImageMark5FaceArgument.ImageMarksPoints nose = imageItems.marks.nose;
        array[n++] = new PointF(nose.x, nose.y);
        final ImageMark5FaceArgument.ImageMarksPoints mouth_left = imageItems.marks.mouth_left;
        array[n++] = new PointF(mouth_left.x, mouth_left.y);
        final ImageMark5FaceArgument.ImageMarksPoints mouth_right = imageItems.marks.mouth_right;
        array[n++] = new PointF(mouth_right.x, mouth_right.y);
        return array;
    }
    
    private void b(final Bitmap bitmap) {
        if (this.q == null) {
            this.q = new ImageAutoColorAnalysis();
        }
        else {
            this.q.reset();
        }
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                TuSDKSkinFilterAPI.this.c(bitmap);
            }
        });
    }
    
    private void c(final Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        final File file = new File(TuSdk.getAppTempPath(), String.format("captureImage_%s.tmp", StringHelper.timeStampString()));
        BitmapHelper.saveBitmap(file, bitmap, 95);
        this.q.analysisWithThumb(bitmap, file, null, this.r, this.s);
    }
    
    private boolean b() {
        if (!SdkValid.shared.sdkValid()) {
            TLog.e("Configuration not found! Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
            return false;
        }
        if (SdkValid.shared.isExpired()) {
            TLog.e("Your account has expired Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
            return false;
        }
        return true;
    }
    
    public interface AutoAdjustResultDelegate
    {
        void onGetAutoAdjustResult(final Bitmap p0);
    }
    
    public interface SkinFilterManagerDelegate
    {
        void onGetSkinFilterResult(final Bitmap p0);
        
        void onFaceMarkResult(final TuSDKSkinFilterFaceMarkResultType p0);
    }
    
    public enum TuSDKSkinFilterFaceMarkResultType
    {
        TuSDKSkinFilterFaceMarkResultTypeSucceed, 
        TuSDKSkinFilterFaceMarkResultTypeFailed, 
        TuSDKSkinFilterFaceMarkResultTypeFailedMultipleFacesDetected, 
        TuSDKSkinFilterFaceMarkResultTypeNoFaceDetected;
    }
}
