// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles;

//import org.lasque.tusdk.core.utils.hardware.TuSdkCorePatch;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.utils.StringHelper;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.egl.SelesEGLContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGLContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkCorePatch;

import java.util.ArrayList;

public class SelesGLProgram
{
    private ArrayList<String> a;
    private int b;
    private int c;
    private int d;
    private boolean e;
    private String f;
    private String g;
    private String h;
    private SelesEGLContext i;
    
    public boolean isInitialized() {
        return this.e;
    }
    
    public String getVertexShaderLog() {
        return this.f;
    }
    
    public String getFragmentShaderLog() {
        return this.g;
    }
    
    public String getProgramLog() {
        return this.h;
    }
    
    public SelesEGLContext getEglContext() {
        return this.i;
    }
    
    public static SelesGLProgram create(final String s, final String s2) {
        return new SelesGLProgram(s, s2);
    }
    
    private SelesGLProgram(final String s, final String s2) {
        this.e = false;
        this.a = new ArrayList<String>();
        this.i = new SelesEGLContext();
        this.c = this.a(s, 35633);
        if (this.c == 0) {
            TLog.e("Failed to compile vertex shader", new Object[0]);
            return;
        }
        this.d = this.a(s2, 35632);
        if (this.d == 0) {
            TLog.e("Failed to compile fragment shader", new Object[0]);
            return;
        }
        this.b = GLES20.glCreateProgram();
        TLog.dump("SelesGLProgram  create() program : %s  %s %s", this.b, this, SelesContext.currentEGLContext());
        GLES20.glAttachShader(this.b, this.c);
        GLES20.glAttachShader(this.b, this.d);
    }
    
    private int a(final String s, final int n) {
        if (StringHelper.isBlank(s)) {
            TLog.e("Failed to load vertex shader", new Object[0]);
            return 0;
        }
        final int[] array = { 0 };
        final String compileShader = SdkValid.shared.compileShader(s, n, array);
        if (array[0] == 0) {
            switch (n) {
                case 35633: {
                    this.f = compileShader;
                    break;
                }
                case 35632: {
                    this.g = compileShader;
                    break;
                }
            }
            return 0;
        }
        return array[0];
    }
    
    public void addAttribute(final String o) {
        if (this.a.contains(o)) {
            return;
        }
        this.a.add(o);
        GLES20.glBindAttribLocation(this.b, this.a.indexOf(o), o);
    }
    
    public int attributeIndex(final String o) {
        return this.a.indexOf(o);
    }
    
    public int uniformIndex(final String s) {
        return GLES20.glGetUniformLocation(this.b, s);
    }
    
    public boolean link() {
        final int[] array = { 0 };
        GLES20.glLinkProgram(this.b);
        GLES20.glGetProgramiv(this.b, 35714, array, 0);
        if (array[0] <= 0) {
            return false;
        }
        if (this.c != 0) {
            GLES20.glDeleteShader(this.c);
            this.c = 0;
        }
        if (this.d != 0) {
            GLES20.glDeleteShader(this.d);
            this.d = 0;
        }
        return this.e = true;
    }
    
    public void use() {
        GLES20.glUseProgram(this.b);
    }
    
    public void validate() {
        GLES20.glValidateProgram(this.b);
        this.h = GLES20.glGetProgramInfoLog(this.b);
    }
    
    public void destory() {
        TLog.dump("%s  program : %s  destory()  %s|%s|%s context : %s", "SelesGLProgram", this.b, this.c, this.d, this.b, SelesContext.currentEGLContext());
        if (this.c > 0) {
            GLES20.glDeleteShader(this.c);
        }
        if (this.d > 0) {
            GLES20.glDeleteShader(this.d);
        }
        if (!TuSdkCorePatch.applyDeletedProgramPatch() && this.b > 0) {
            GLES20.glDeleteProgram(this.b);
        }
        this.c = 0;
        this.d = 0;
        this.b = 0;
    }
    
    @Override
    protected void finalize() {
        TLog.dump("%s  program : %s  finalize()  context : %s", "SelesGLProgram", this.b, SelesContext.currentEGLContext());
        this.destory();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
