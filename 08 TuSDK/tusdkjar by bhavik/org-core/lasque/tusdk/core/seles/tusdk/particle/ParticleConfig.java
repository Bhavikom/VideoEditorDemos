package org.lasque.tusdk.core.seles.tusdk.particle;

import android.graphics.PointF;
import java.io.Serializable;
import org.json.JSONObject;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class ParticleConfig
  extends JsonBaseBean
  implements Serializable
{
  private JSONObject a;
  @DataBase("windowWidth")
  public float windowWidth;
  @DataBase("maxParticles")
  public int maxParticles;
  @DataBase("configName")
  public String configName;
  @DataBase("textureFileName")
  public String texture;
  @DataBase("textureTiles")
  public int textureTiles;
  @DataBase("particleLifespan")
  public float life;
  @DataBase("particleLifespanVariance")
  public float lifeVar;
  @DataBase("angle")
  public float angle;
  @DataBase("angleVariance")
  public float angleVar;
  @DataBase("duration")
  public float duration;
  public int blendFuncSrc = a(1);
  public int blendFuncDst = a(5);
  public float[] startColor;
  public float[] startColorVar;
  public float[] endColor;
  public float[] endColorVar;
  @DataBase("startParticleSize")
  public float startSize;
  @DataBase("startParticleSizeVariance")
  public float startSizeVar;
  @DataBase("finishParticleSize")
  public float endSize;
  @DataBase("finishParticleSizeVariance")
  public float endSizeVar;
  @DataBase("rotationStart")
  public float startSpin;
  @DataBase("rotationStartVariance")
  public float startSpinVar;
  @DataBase("rotationEnd")
  public float endSpin;
  @DataBase("rotationEndVariance")
  public float endSpinVar;
  public PointF position = new PointF(0.0F, 0.0F);
  public PointF positionVar = new PointF(0.0F, 0.0F);
  public ParticleMode emitterMode = ParticleMode.Gravity;
  public PositionType positionType = PositionType.Free;
  public float emissionRate;
  @DataBase("yCoordFlipped")
  public int yCoordFlipped = 1;
  public GravityConfig gravity;
  public RadiusConfig radius;
  private TuSdkSize b;
  private float c = 1.0F;
  
  public void setTextureSize(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    if ((this.b != null) && (this.b.equals(paramTuSdkSize))) {
      return;
    }
    this.b = paramTuSdkSize;
    updateParticleSettings();
  }
  
  public void setSizeRate(float paramFloat)
  {
    if (this.c <= 0.0F) {
      return;
    }
    this.c = (1.0F + paramFloat);
    updateParticleSettings();
  }
  
  public void updateParticleSettings()
  {
    float f = 1.0F;
    if (this.b != null) {
      f = Math.min(this.b.width, this.b.height) / this.windowWidth;
    }
    this.startSize = (this.startSize * f * this.c);
    this.startSizeVar = (this.startSizeVar * f * this.c);
    this.endSize = (this.endSize * f * this.c);
    this.endSizeVar = (this.endSizeVar * f * this.c);
    this.positionVar = new PointF(this.positionVar.x * f * this.c, this.positionVar.y * f * this.c);
    if (this.gravity != null)
    {
      this.gravity.gravity = new PointF(this.gravity.gravity.x * f * this.c, this.gravity.gravity.y * f * this.c);
      this.gravity.speed = (this.gravity.speed * f * this.c);
      this.gravity.speedVar = (this.gravity.speedVar * f * this.c);
      this.gravity.tangentialAccel = (this.gravity.tangentialAccel * f * this.c);
    }
  }
  
  public JSONObject getJSON()
  {
    return this.a;
  }
  
  public void setJson(JSONObject paramJSONObject)
  {
    super.setJson(paramJSONObject);
    if (paramJSONObject == null) {
      return;
    }
    this.a = paramJSONObject;
    this.blendFuncSrc = a(paramJSONObject.optInt("blendFuncSource", 1));
    this.blendFuncDst = a(paramJSONObject.optInt("blendFuncDestination", 5));
    this.startColor = new float[] { (float)paramJSONObject.optDouble("startColorRed", 0.0D), (float)paramJSONObject.optDouble("startColorGreen", 0.0D), (float)paramJSONObject.optDouble("startColorBlue", 0.0D), (float)paramJSONObject.optDouble("startColorAlpha", 0.0D) };
    this.startColorVar = new float[] { (float)paramJSONObject.optDouble("startColorVarianceRed", 0.0D), (float)paramJSONObject.optDouble("startColorVarianceGreen", 0.0D), (float)paramJSONObject.optDouble("startColorVarianceBlue", 0.0D), (float)paramJSONObject.optDouble("startColorVarianceAlpha", 0.0D) };
    this.endColor = new float[] { (float)paramJSONObject.optDouble("finishColorRed", 0.0D), (float)paramJSONObject.optDouble("finishColorGreen", 0.0D), (float)paramJSONObject.optDouble("finishColorBlue", 0.0D), (float)paramJSONObject.optDouble("finishColorAlpha", 0.0D) };
    this.endColorVar = new float[] { (float)paramJSONObject.optDouble("finishColorVarianceRed", 0.0D), (float)paramJSONObject.optDouble("finishColorVarianceGreen", 0.0D), (float)paramJSONObject.optDouble("finishColorVarianceBlue", 0.0D), (float)paramJSONObject.optDouble("finishColorVarianceAlpha", 0.0D) };
    this.position = new PointF((float)paramJSONObject.optDouble("sourcePositionx", 0.0D), (float)paramJSONObject.optDouble("sourcePositiony", 0.0D));
    this.positionVar = new PointF((float)paramJSONObject.optDouble("sourcePositionVariancex", 0.0D), (float)paramJSONObject.optDouble("sourcePositionVariancey", 0.0D));
    if (paramJSONObject.optInt("emitterType") != 0) {
      this.emitterMode = ParticleMode.Radius;
    }
    if (paramJSONObject.optInt("positionType") != 0) {
      this.positionType = PositionType.Grouped;
    }
    this.emissionRate = (this.maxParticles / this.life);
    if (this.textureTiles < 1) {
      this.textureTiles = 1;
    }
    if (this.emitterMode == ParticleMode.Gravity)
    {
      this.gravity = new GravityConfig();
      this.gravity.setJson(paramJSONObject);
    }
    else
    {
      this.radius = new RadiusConfig();
      this.radius.setJson(paramJSONObject);
    }
  }
  
  public void setBlendAdditive(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.blendFuncSrc = 770;
      this.blendFuncDst = 1;
      return;
    }
    this.blendFuncSrc = 1;
    this.blendFuncDst = 771;
  }
  
  public boolean isBlendAdditive()
  {
    return (this.blendFuncSrc == 770) && (this.blendFuncDst == 1);
  }
  
  private int a(int paramInt)
  {
    switch (paramInt)
    {
    case 1: 
      return 1;
    case 2: 
    case 768: 
      return 768;
    case 3: 
    case 769: 
      return 769;
    case 4: 
    case 770: 
      return 770;
    case 5: 
    case 771: 
      return 771;
    case 6: 
    case 772: 
      return 772;
    case 7: 
    case 773: 
      return 773;
    case 8: 
    case 774: 
      return 774;
    case 9: 
    case 775: 
      return 775;
    case 10: 
    case 776: 
      return 776;
    }
    return 0;
  }
  
  public static class RadiusConfig
    extends JsonBaseBean
    implements Serializable
  {
    @DataBase("maxRadius")
    public float startRadius;
    @DataBase("maxRadiusVariance")
    public float startRadiusVar;
    @DataBase("minRadius")
    public float endRadius;
    @DataBase("minRadiusVariance")
    public float endRadiusVar;
    @DataBase("rotatePerSecond")
    public float rotatePerSecond;
    @DataBase("rotatePerSecondVariance")
    public float rotatePerSecondVar;
  }
  
  public static class GravityConfig
    extends JsonBaseBean
    implements Serializable
  {
    public PointF gravity = new PointF(0.0F, 0.0F);
    @DataBase("speed")
    public float speed;
    @DataBase("speedVariance")
    public float speedVar;
    @DataBase("radialAcceleration")
    public float radialAccel;
    @DataBase("radialAccelVariance")
    public float radialAccelVar;
    @DataBase("tangentialAcceleration")
    public float tangentialAccel;
    @DataBase("tangentialAccelVariance")
    public float tangentialAccelVar;
    @DataBase("rotationIsDir")
    public boolean rotationIsDir;
    
    public void setJson(JSONObject paramJSONObject)
    {
      super.setJson(paramJSONObject);
      if (paramJSONObject == null) {
        return;
      }
      this.gravity = new PointF((float)paramJSONObject.optDouble("gravityx", 0.0D), (float)paramJSONObject.optDouble("gravityy", 0.0D));
    }
  }
  
  public static enum PositionType
  {
    private PositionType() {}
  }
  
  public static enum ParticleMode
  {
    private ParticleMode() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\particle\ParticleConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */