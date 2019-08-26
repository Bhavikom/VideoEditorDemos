package org.lasque.tusdk.core.seles;

import android.opengl.GLES20;
import java.util.ArrayList;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.seles.egl.SelesEGLContext;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSdkCorePatch;

public class SelesGLProgram
{
  private ArrayList<String> a = new ArrayList();
  private int b;
  private int c = a(paramString1, 35633);
  private int d;
  private boolean e = false;
  private String f;
  private String g;
  private String h;
  private SelesEGLContext i = new SelesEGLContext();
  
  public boolean isInitialized()
  {
    return this.e;
  }
  
  public String getVertexShaderLog()
  {
    return this.f;
  }
  
  public String getFragmentShaderLog()
  {
    return this.g;
  }
  
  public String getProgramLog()
  {
    return this.h;
  }
  
  public SelesEGLContext getEglContext()
  {
    return this.i;
  }
  
  public static SelesGLProgram create(String paramString1, String paramString2)
  {
    return new SelesGLProgram(paramString1, paramString2);
  }
  
  private SelesGLProgram(String paramString1, String paramString2)
  {
    if (this.c == 0)
    {
      TLog.e("Failed to compile vertex shader", new Object[0]);
      return;
    }
    this.d = a(paramString2, 35632);
    if (this.d == 0)
    {
      TLog.e("Failed to compile fragment shader", new Object[0]);
      return;
    }
    this.b = GLES20.glCreateProgram();
    TLog.dump("SelesGLProgram  create() program : %s  %s %s", new Object[] { Integer.valueOf(this.b), this, SelesContext.currentEGLContext() });
    GLES20.glAttachShader(this.b, this.c);
    GLES20.glAttachShader(this.b, this.d);
  }
  
  private int a(String paramString, int paramInt)
  {
    if (StringHelper.isBlank(paramString))
    {
      TLog.e("Failed to load vertex shader", new Object[0]);
      return 0;
    }
    int[] arrayOfInt = new int[1];
    String str = SdkValid.shared.compileShader(paramString, paramInt, arrayOfInt);
    if (arrayOfInt[0] == 0)
    {
      switch (paramInt)
      {
      case 35633: 
        this.f = str;
        break;
      case 35632: 
        this.g = str;
        break;
      }
      return 0;
    }
    return arrayOfInt[0];
  }
  
  public void addAttribute(String paramString)
  {
    if (this.a.contains(paramString)) {
      return;
    }
    this.a.add(paramString);
    GLES20.glBindAttribLocation(this.b, this.a.indexOf(paramString), paramString);
  }
  
  public int attributeIndex(String paramString)
  {
    return this.a.indexOf(paramString);
  }
  
  public int uniformIndex(String paramString)
  {
    return GLES20.glGetUniformLocation(this.b, paramString);
  }
  
  public boolean link()
  {
    int[] arrayOfInt = new int[1];
    GLES20.glLinkProgram(this.b);
    GLES20.glGetProgramiv(this.b, 35714, arrayOfInt, 0);
    if (arrayOfInt[0] <= 0) {
      return false;
    }
    if (this.c != 0)
    {
      GLES20.glDeleteShader(this.c);
      this.c = 0;
    }
    if (this.d != 0)
    {
      GLES20.glDeleteShader(this.d);
      this.d = 0;
    }
    this.e = true;
    return true;
  }
  
  public void use()
  {
    GLES20.glUseProgram(this.b);
  }
  
  public void validate()
  {
    GLES20.glValidateProgram(this.b);
    this.h = GLES20.glGetProgramInfoLog(this.b);
  }
  
  public void destory()
  {
    TLog.dump("%s  program : %s  destory()  %s|%s|%s context : %s", new Object[] { "SelesGLProgram", Integer.valueOf(this.b), Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.b), SelesContext.currentEGLContext() });
    if (this.c > 0) {
      GLES20.glDeleteShader(this.c);
    }
    if (this.d > 0) {
      GLES20.glDeleteShader(this.d);
    }
    if ((!TuSdkCorePatch.applyDeletedProgramPatch()) && (this.b > 0)) {
      GLES20.glDeleteProgram(this.b);
    }
    this.c = 0;
    this.d = 0;
    this.b = 0;
  }
  
  protected void finalize()
  {
    TLog.dump("%s  program : %s  finalize()  context : %s", new Object[] { "SelesGLProgram", Integer.valueOf(this.b), SelesContext.currentEGLContext() });
    destory();
    super.finalize();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\SelesGLProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */