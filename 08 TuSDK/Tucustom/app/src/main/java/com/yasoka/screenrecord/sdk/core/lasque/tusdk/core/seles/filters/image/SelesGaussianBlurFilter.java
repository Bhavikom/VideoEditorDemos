// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.image;

import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.SelesGLProgram;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoPassFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoPassTextureSamplingFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.filters.SelesTwoPassTextureSamplingFilter;

public class SelesGaussianBlurFilter extends SelesTwoPassTextureSamplingFilter
{
    private boolean a;
    private float b;
    private float c;
    private float d;
    private float e;
    private int f;
    
    public SelesGaussianBlurFilter() {
        this(vertexShaderForOptimizedBlur(4, 2.0f), fragmentShaderForOptimizedBlur(4, 2.0f));
    }
    
    public SelesGaussianBlurFilter(final String s, final String s2) {
        this(s, s2, s, s2);
    }
    
    public SelesGaussianBlurFilter(final String s, final String s2, final String s3, final String s4) {
        super(s, s2, s3, s4);
        this.setTexelSpacingMultiplier(1.0f);
        this.b = 2.0f;
        this.a = false;
    }
    
    @Override
    public void setupFilterForSize(final TuSdkSize tuSdkSize) {
        super.setupFilterForSize(tuSdkSize);
        if (this.a) {
            if (this.getBlurRadiusAsFractionOfImageWidth() > 0.0f) {
                this.setBlurRadiusInPixels(tuSdkSize.width * this.getBlurRadiusAsFractionOfImageWidth());
            }
            else {
                this.setBlurRadiusInPixels(tuSdkSize.height * this.getBlurRadiusAsFractionOfImageHeight());
            }
        }
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        for (int i = 1; i < this.f; ++i) {
            super.renderToTexture(floatBuffer, this.mNoRotationTextureBuffer);
        }
    }
    
    public void switchTo(final String s, final String s2) {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesGaussianBlurFilter.this.mFilterProgram = SelesContext.program(s, s2);
                if (!SelesGaussianBlurFilter.this.mFilterProgram.isInitialized()) {
                    SelesGaussianBlurFilter.this.initializeAttributes();
                    if (!SelesGaussianBlurFilter.this.mFilterProgram.link()) {
                        TLog.i("Program link log: %s", SelesGaussianBlurFilter.this.mFilterProgram.getProgramLog());
                        TLog.i("Fragment shader compile log: %s", SelesGaussianBlurFilter.this.mFilterProgram.getFragmentShaderLog());
                        TLog.i("Vertex link log: %s", SelesGaussianBlurFilter.this.mFilterProgram.getVertexShaderLog());
                        SelesGaussianBlurFilter.this.mFilterProgram = null;
                        TLog.e("Filter shader link failed: %s", this.getClass());
                        return;
                    }
                }
                SelesGaussianBlurFilter.this.mFilterPositionAttribute = SelesGaussianBlurFilter.this.mFilterProgram.attributeIndex("position");
                SelesGaussianBlurFilter.this.mFilterTextureCoordinateAttribute = SelesGaussianBlurFilter.this.mFilterProgram.attributeIndex("inputTextureCoordinate");
                SelesGaussianBlurFilter.this.mFilterInputTextureUniform = SelesGaussianBlurFilter.this.mFilterProgram.uniformIndex("inputImageTexture");
                SelesGaussianBlurFilter.this.mVerticalPassTexelWidthOffsetUniform = SelesGaussianBlurFilter.this.mFilterProgram.uniformIndex("texelWidthOffset");
                SelesGaussianBlurFilter.this.mVerticalPassTexelHeightOffsetUniform = SelesGaussianBlurFilter.this.mFilterProgram.uniformIndex("texelHeightOffset");
                SelesContext.setActiveShaderProgram(SelesGaussianBlurFilter.this.mFilterProgram);
                GLES20.glEnableVertexAttribArray(SelesGaussianBlurFilter.this.mFilterPositionAttribute);
                GLES20.glEnableVertexAttribArray(SelesGaussianBlurFilter.this.mFilterTextureCoordinateAttribute);
                SelesGaussianBlurFilter.this.mSecondFilterProgram = SelesContext.program(s, s2);
                if (!SelesGaussianBlurFilter.this.mSecondFilterProgram.isInitialized()) {
                    SelesGaussianBlurFilter.this.initializeSecondaryAttributes();
                    if (!SelesGaussianBlurFilter.this.mSecondFilterProgram.link()) {
                        TLog.i("Program link log: %s", SelesGaussianBlurFilter.this.mSecondFilterProgram.getProgramLog());
                        TLog.i("Fragment shader compile log: %s", SelesGaussianBlurFilter.this.mSecondFilterProgram.getFragmentShaderLog());
                        TLog.i("Vertex link log: %s", SelesGaussianBlurFilter.this.mSecondFilterProgram.getVertexShaderLog());
                        SelesGaussianBlurFilter.this.mSecondFilterProgram = null;
                        TLog.e("Filter shader link failed: %s", this.getClass());
                        return;
                    }
                }
                SelesGaussianBlurFilter.this.mSecondFilterPositionAttribute = SelesGaussianBlurFilter.this.mSecondFilterProgram.attributeIndex("position");
                SelesGaussianBlurFilter.this.mSecondFilterTextureCoordinateAttribute = SelesGaussianBlurFilter.this.mSecondFilterProgram.attributeIndex("inputTextureCoordinate");
                SelesGaussianBlurFilter.this.mSecondFilterInputTextureUniform = SelesGaussianBlurFilter.this.mSecondFilterProgram.uniformIndex("inputImageTexture");
                SelesGaussianBlurFilter.this.mSecondFilterInputTextureUniform2 = SelesGaussianBlurFilter.this.mSecondFilterProgram.uniformIndex("inputImageTexture2");
                SelesGaussianBlurFilter.this.mHorizontalPassTexelWidthOffsetUniform = SelesGaussianBlurFilter.this.mFilterProgram.uniformIndex("texelWidthOffset");
                SelesGaussianBlurFilter.this.mHorizontalPassTexelHeightOffsetUniform = SelesGaussianBlurFilter.this.mFilterProgram.uniformIndex("texelHeightOffset");
                SelesContext.setActiveShaderProgram(SelesGaussianBlurFilter.this.mSecondFilterProgram);
                GLES20.glEnableVertexAttribArray(SelesGaussianBlurFilter.this.mSecondFilterPositionAttribute);
                GLES20.glEnableVertexAttribArray(SelesGaussianBlurFilter.this.mSecondFilterTextureCoordinateAttribute);
                SelesGaussianBlurFilter.this.setupFilterForSize(SelesGaussianBlurFilter.this.sizeOfFBO());
                GLES20.glFlush();
            }
        });
    }
    
    public boolean isShouldResizeBlurRadiusWithImageSize() {
        return this.a;
    }
    
    public void setShouldResizeBlurRadiusWithImageSize(final boolean a) {
        this.a = a;
    }
    
    public float getBlurRadiusInPixels() {
        return this.b;
    }
    
    public void setBlurRadiusInPixels(final float n) {
        if (Math.round(n) != this.b) {
            this.b = (float)Math.round(n);
            int n2 = 0;
            if (this.b >= 1.0f) {
                final int n3 = (int)Math.floor(Math.sqrt(-2.0 * Math.pow(this.b, 2.0) * Math.log(0.00390625f * Math.sqrt(6.283185307179586 * Math.pow(this.b, 2.0)))));
                n2 = n3 + n3 % 2;
            }
            this.switchTo(vertexShaderForOptimizedBlur(n2, this.b), fragmentShaderForOptimizedBlur(n2, this.b));
        }
        this.a = false;
    }
    
    public float getTexelSpacingMultiplier() {
        return this.c;
    }
    
    public void setTexelSpacingMultiplier(final float mHorizontalTexelSpacing) {
        this.c = mHorizontalTexelSpacing;
        this.mVerticalTexelSpacing = mHorizontalTexelSpacing;
        this.mHorizontalTexelSpacing = mHorizontalTexelSpacing;
        this.setupFilterForSize(this.sizeOfFBO());
    }
    
    public float getBlurRadiusAsFractionOfImageWidth() {
        return this.d;
    }
    
    public void setBlurRadiusAsFractionOfImageWidth(final float d) {
        if (d < 0.0f) {
            return;
        }
        this.a = (this.d != d && d > 0.0f);
        this.d = d;
        this.e = 0.0f;
    }
    
    public float getBlurRadiusAsFractionOfImageHeight() {
        return this.e;
    }
    
    public void setBlurRadiusAsFractionOfImageHeight(final float e) {
        if (e < 0.0f) {
            return;
        }
        this.a = (this.e != e && e > 0.0f);
        this.e = e;
        this.d = 0.0f;
    }
    
    public int getBlurPasses() {
        return this.f;
    }
    
    public void setBlurPasses(final int f) {
        this.f = f;
    }
    
    public static String vertexShaderForOptimizedBlur(final int n, final float n2) {
        if (n < 1) {
            return "attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}";
        }
        final float[] array = new float[n + 1];
        float n3 = 0.0f;
        for (int i = 0; i < n + 1; ++i) {
            array[i] = (float)(1.0 / Math.sqrt(6.283185307179586 * Math.pow(n2, 2.0)) * Math.exp(-Math.pow(i, 2.0) / (2.0 * Math.pow(n2, 2.0))));
            if (i == 0) {
                n3 += array[i];
            }
            else {
                n3 += (float)(2.0 * array[i]);
            }
        }
        for (int j = 0; j < n + 1; ++j) {
            array[j] /= n3;
        }
        final int min = Math.min(n / 2 + n % 2, 7);
        final float[] array2 = new float[min];
        for (int k = 0; k < min; ++k) {
            int n4 = k * 2 + 1;
            float n5 = 0.0f;
            if (n4 < array.length) {
                n5 = array[n4];
            }
            ++n4;
            float n6 = 0.0f;
            if (n4 < array.length) {
                n6 = array[n4];
            }
            array2[k] = (n5 * (k * 2 + 1) + n6 * (k * 2 + 2)) / (n5 + n6);
        }
        String s = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n\nuniform float texelWidthOffset;\nuniform float texelHeightOffset;\n\nvarying vec2 blurCoordinates[" + (1 + min * 2) + "];\n\nvoid main()\n{\n   gl_Position = position;\n   \n   vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n" + "blurCoordinates[0] = inputTextureCoordinate.xy;\n";
        for (int l = 0; l < min; ++l) {
            s = s + String.format("blurCoordinates[%d] = inputTextureCoordinate.xy + singleStepOffset * %f;\n", l * 2 + 1, array2[l]) + String.format("blurCoordinates[%d] = inputTextureCoordinate.xy - singleStepOffset * %f;\n", l * 2 + 2, array2[l]);
        }
        return s + "}\n";
    }
    
    public static String fragmentShaderForOptimizedBlur(final int n, final float n2) {
        if (n < 1) {
            return "varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}";
        }
        final float[] array = new float[n + 1];
        float n3 = 0.0f;
        for (int i = 0; i < n + 1; ++i) {
            array[i] = (float)(1.0 / Math.sqrt(6.283185307179586 * Math.pow(n2, 2.0)) * Math.exp(-Math.pow(i, 2.0) / (2.0 * Math.pow(n2, 2.0))));
            if (i == 0) {
                n3 += array[i];
            }
            else {
                n3 += (float)(2.0 * array[i]);
            }
        }
        for (int j = 0; j < n + 1; ++j) {
            array[j] /= n3;
        }
        final int min = Math.min(n / 2 + n % 2, 7);
        final int n4 = n / 2 + n % 2;
        String s = "uniform sampler2D inputImageTexture;\nuniform highp float texelWidthOffset;\nuniform highp float texelHeightOffset;\n\nvarying highp vec2 blurCoordinates[" + (1 + min * 2) + "];\n\nvoid main()\n{\n   lowp vec4 sum = vec4(0.0);\n" + String.format("sum += texture2D(inputImageTexture, blurCoordinates[0]) * %f;\n", array[0]);
        for (int k = 0; k < min; ++k) {
            int n5 = k * 2 + 1;
            float n6 = 0.0f;
            if (n5 < array.length) {
                n6 = array[n5];
            }
            ++n5;
            float n7 = 0.0f;
            if (n5 < array.length) {
                n7 = array[n5];
            }
            final float n8 = n6 + n7;
            s = s + String.format("sum += texture2D(inputImageTexture, blurCoordinates[%d]) * %f;\n", k * 2 + 1, n8) + String.format("sum += texture2D(inputImageTexture, blurCoordinates[%d]) * %f;\n", k * 2 + 2, n8);
        }
        if (n4 > min) {
            s += "highp vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n";
            for (int l = min; l < n4; ++l) {
                int n9 = l * 2 + 1;
                float n10 = 0.0f;
                if (n9 < array.length) {
                    n10 = array[n9];
                }
                ++n9;
                float n11 = 0.0f;
                if (n9 < array.length) {
                    n11 = array[n9];
                }
                final float n12 = n10 + n11;
                final float n13 = (n10 * (l * 2 + 1) + n11 * (l * 2 + 2)) / n12;
                s = s + String.format("sum += texture2D(inputImageTexture, blurCoordinates[0] + singleStepOffset * %f) * %f;\n", n13, n12) + String.format("sum += texture2D(inputImageTexture, blurCoordinates[0] - singleStepOffset * %f) * %f;\n", n13, n12);
            }
        }
        return s + "\tgl_FragColor = sum;\n}\n";
    }
}
