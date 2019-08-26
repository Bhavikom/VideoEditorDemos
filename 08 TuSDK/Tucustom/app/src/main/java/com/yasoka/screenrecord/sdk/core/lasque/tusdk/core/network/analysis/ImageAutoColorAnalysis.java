// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis;

//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.SelesGLProgram;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import java.io.File;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;

public class ImageAutoColorAnalysis {
    private ImageColorArgument a;
    private ImageOnlineAnalysis b;
    private ImageAutoColorAnalysis._ImageAutoColorAnalysisWrap c;

    public ImageAutoColorAnalysis() {
    }

    public ImageAutoColorAnalysis._ImageAutoColorAnalysisWrap getFilter() {
        if (this.c != null) {
            return this.c.clone();
        } else {
            this.c = ImageAutoColorAnalysis._ImageAutoColorAnalysisWrap.b();
            return this.c;
        }
    }

    public void reset() {
        if (this.b != null) {
            this.b.cancel();
            this.b = null;
        }

        this.c = null;
        this.a = null;
    }

    public void analysisWithImage(final Bitmap var1, final ImageAutoColorAnalysis.ImageAutoColorAnalysisListener var2) {
        if (var2 != null) {
            if (var1 == null) {
                var2.onImageAutoColorAnalysisCompleted((Bitmap)null, ImageOnlineAnalysis.ImageAnalysisType.NotInputImage);
            } else if (this.a != null) {
                this.a(var1, var2);
            } else {
                this.b = new ImageOnlineAnalysis();
                this.b.setImage(var1);
                this.b.analysisColor(new ImageOnlineAnalysis.ImageAnalysisListener() {
                    public <T extends JsonBaseBean> void onImageAnalysisCompleted(T var1x, ImageOnlineAnalysis.ImageAnalysisType var2x) {
                        if (var2x != ImageOnlineAnalysis.ImageAnalysisType.Succeed) {
                            var2.onImageAutoColorAnalysisCompleted((Bitmap)null, var2x);
                        } else {
                            ImageAnalysisResult var3 = (ImageAnalysisResult)var1x;
                            if (var3 != null && var3.color != null) {
                                ImageAutoColorAnalysis.this.a = var3.color;
                                ImageAutoColorAnalysis.this.getFilter().a(ImageAutoColorAnalysis.this.a);
                                StatisticsManger.appendComponent(ComponentActType.image_Analysis_color);
                            }

                            ImageAutoColorAnalysis.this.a(var1, var2);
                        }
                    }
                });
            }
        }
    }

    public void copyAnalysis(final File var1, final File var2, final ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener var3) {
        if (var3 != null && this.a != null) {
            if (var1 != null && var1.exists() && var2 != null) {
                ThreadHelper.runThread(new Runnable() {
                    public void run() {
                        Bitmap var1x = BitmapHelper.getBitmap(var1, TuSdkSize.create(TuSdkGPU.getMaxTextureOptimizedSize()), true);
                        var1x = ImageAutoColorAnalysis.this.getFilter().process(var1x);
                        final boolean var2x = BitmapHelper.saveBitmap(var2, var1x, 95);
                        var1x = null;
                        ThreadHelper.post(new Runnable() {
                            public void run() {
                                var3.onImageAutoColorAnalysisCopyCompleted(var2x ? var2 : null);
                            }
                        });
                    }
                });
            } else {
                var3.onImageAutoColorAnalysisCopyCompleted((File)null);
            }
        }
    }

    private void a(final Bitmap var1, final ImageAutoColorAnalysis.ImageAutoColorAnalysisListener var2) {
        if (var1 == null) {
            var2.onImageAutoColorAnalysisCompleted((Bitmap)null, ImageOnlineAnalysis.ImageAnalysisType.NotInputImage);
        } else if (this.a == null) {
            var2.onImageAutoColorAnalysisCompleted((Bitmap)null, ImageOnlineAnalysis.ImageAnalysisType.ServiceFailed);
        } else {
            ThreadHelper.runThread(new Runnable() {
                public void run() {
                    final Bitmap var1x = ImageAutoColorAnalysis.this.getFilter().process(var1);
                    ThreadHelper.post(new Runnable() {
                        public void run() {
                            var2.onImageAutoColorAnalysisCompleted(var1x, ImageOnlineAnalysis.ImageAnalysisType.Succeed);
                        }
                    });
                }
            });
        }
    }

    public void analysisWithThumb(Bitmap var1, final File var2, final File var3, final ImageAutoColorAnalysis.ImageAutoColorAnalysisListener var4, final ImageAutoColorAnalysis.ImageAutoColorAnalysisCopyListener var5) {
        if (var4 != null) {
            this.analysisWithImage(var1, new ImageAutoColorAnalysis.ImageAutoColorAnalysisListener() {
                public void onImageAutoColorAnalysisCompleted(Bitmap var1, ImageOnlineAnalysis.ImageAnalysisType var2x) {
                    var4.onImageAutoColorAnalysisCompleted(var1, var2x);
                    if (var2x != ImageOnlineAnalysis.ImageAnalysisType.Succeed) {
                        if (var5 != null) {
                            var5.onImageAutoColorAnalysisCopyCompleted((File)null);
                        }

                    } else {
                        ImageAutoColorAnalysis.this.copyAnalysis(var2, var3, var5);
                    }
                }
            });
        }
    }

    private static class _ImageAutoColorAnalysisFiler extends SelesFilter {
        private int a;
        private int b;
        private int c;
        private ImageColorArgument d;

        private _ImageAutoColorAnalysisFiler() {
            super("precision lowp float;varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;uniform lowp vec4 maxRGBA;uniform lowp vec4 minRGBA;uniform lowp vec3 midRGB;highp vec3 handleAutoTone(highp vec3 color){\thighp vec3 nColor = (color - minRGBA.rgb) / (maxRGBA.rgb - minRGBA.rgb);\treturn nColor;}highp vec3 handleAutoColor(highp vec3 color){\thighp vec3 nColor = handleAutoTone(color);\thighp vec3 alphaM = nColor * (0.5 / midRGB);\thighp vec3 alphaP = (nColor - midRGB) * (0.5 / (1.0 - midRGB)) + 0.5;\thighp float r = nColor.r < midRGB.r ? alphaM.r : alphaP.r;\thighp float g = nColor.g < midRGB.g ? alphaM.g : alphaP.g;\thighp float b = nColor.b < midRGB.b ? alphaM.b : alphaP.b;\treturn vec3(r,g,b);}void main(){\thighp vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;\ttextureColor = handleAutoColor(textureColor);\tgl_FragColor = vec4(textureColor, 1.0);}");
            this.d = new ImageColorArgument();
            this.d.maxR = 1.0F;
            this.d.maxG = 1.0F;
            this.d.maxB = 1.0F;
            this.d.maxY = 1.0F;
            this.d.minR = 0.0F;
            this.d.minG = 0.0F;
            this.d.minB = 0.0F;
            this.d.minY = 0.0F;
            this.d.midR = 0.5F;
            this.d.midG = 0.5F;
            this.d.midB = 0.5F;
        }

        protected void onInitOnGLThread() {
            super.onInitOnGLThread();
            this.a = this.mFilterProgram.uniformIndex("maxRGBA");
            this.b = this.mFilterProgram.uniformIndex("minRGBA");
            this.c = this.mFilterProgram.uniformIndex("midRGB");
            this.a(this.d);
        }

        private void a(final float var1, final float var2, final float var3, final float var4) {
            this.runOnDraw(new Runnable() {
                public void run() {
                    float[] var1x = new float[]{var1, var2, var3, var4};
                    SelesContext.setActiveShaderProgram(_ImageAutoColorAnalysisFiler.this.mFilterProgram);
                    GLES20.glUniform4fv(_ImageAutoColorAnalysisFiler.this.a, 1, FloatBuffer.wrap(var1x));
                }
            });
        }

        private void b(final float var1, final float var2, final float var3, final float var4) {
            this.runOnDraw(new Runnable() {
                public void run() {
                    float[] var1x = new float[]{var1, var2, var3, var4};
                    SelesContext.setActiveShaderProgram(_ImageAutoColorAnalysisFiler.this.mFilterProgram);
                    GLES20.glUniform4fv(_ImageAutoColorAnalysisFiler.this.b, 1, FloatBuffer.wrap(var1x));
                }
            });
        }

        private void a(final float var1, final float var2, final float var3) {
            this.runOnDraw(new Runnable() {
                public void run() {
                    float[] var1x = new float[]{var1, var2, var3};
                    SelesContext.setActiveShaderProgram(_ImageAutoColorAnalysisFiler.this.mFilterProgram);
                    GLES20.glUniform3fv(_ImageAutoColorAnalysisFiler.this.c, 1, FloatBuffer.wrap(var1x));
                }
            });
        }

        private ImageColorArgument a() {
            return this.d;
        }

        private void a(ImageColorArgument var1) {
            if (var1 != null) {
                this.d = var1;
                this.a(var1.maxR, var1.maxG, var1.maxB, var1.maxY);
                this.b(var1.minR, var1.minG, var1.minB, var1.minY);
                this.a(var1.midR, var1.midG, var1.midB);
            }
        }
    }

    private static class _ImageAutoColorAnalysisWrap extends FilterWrap {
        private static ImageAutoColorAnalysis._ImageAutoColorAnalysisWrap b() {
            FilterOption var0 = new FilterOption() {
                public SelesOutInput getFilter() {
                    return new ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler();
                }
            };
            return new ImageAutoColorAnalysis._ImageAutoColorAnalysisWrap(var0);
        }

        private _ImageAutoColorAnalysisWrap(FilterOption var1) {
            super(var1);
        }

        public ImageAutoColorAnalysis._ImageAutoColorAnalysisWrap clone() {
            ImageAutoColorAnalysis._ImageAutoColorAnalysisWrap var1 = b();
            if (var1 != null) {
                var1.setFilterParameter(this.getFilterParameter());
                ((ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler)var1.getFilter()).a(((ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler)this.getFilter()).a());
            }

            return var1;
        }

        private void a(ImageColorArgument var1) {
            ((ImageAutoColorAnalysis._ImageAutoColorAnalysisFiler)this.getFilter()).a(var1);
        }
    }

    public interface ImageAutoColorAnalysisCopyListener {
        void onImageAutoColorAnalysisCopyCompleted(File var1);
    }

    public interface ImageAutoColorAnalysisListener {
        void onImageAutoColorAnalysisCompleted(Bitmap var1, ImageOnlineAnalysis.ImageAnalysisType var2);
    }
}
