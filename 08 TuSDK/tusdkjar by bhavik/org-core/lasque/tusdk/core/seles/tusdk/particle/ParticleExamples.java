package org.lasque.tusdk.core.seles.tusdk.particle;

import android.graphics.PointF;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkBundle;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.json.JsonHelper;

public class ParticleExamples
  extends ParticleManager
{
  public ParticleExamples(int paramInt)
  {
    super(paramInt);
  }
  
  public static ParticleManager indexWith(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 1: 
      return new ParticleFire(paramInt2);
    case 2: 
      return new ParticleFireworks(paramInt2);
    case 3: 
      return new ParticleSun(paramInt2);
    case 4: 
      return new ParticleGalaxy(paramInt2);
    case 5: 
      return new ParticleFlower(paramInt2);
    case 6: 
      return new ParticleMeteor(paramInt2);
    case 7: 
      return new ParticleSpiral(paramInt2);
    case 8: 
      return new ParticleExplosion(paramInt2);
    case 9: 
      return new ParticleSmoke(paramInt2);
    case 10: 
      return new ParticleSnow(paramInt2);
    case 11: 
      return new ParticleRain(paramInt2);
    case 12: 
      String str1 = TuSdkBundle.sdkBundleTexture("particle_clear.json");
      String str2 = TuSdkContext.getAssetsText(str1);
      JSONObject localJSONObject = JsonHelper.json(str2);
      return new ParticleManager(localJSONObject);
    }
    return new ParticleFire(paramInt2);
  }
  
  public static class ParticleRain
    extends ParticleManager
  {
    public ParticleRain(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.duration = -1.0F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(10.0F, -10.0F);
      localParticleConfig.gravity.radialAccel = 0.0F;
      localParticleConfig.gravity.radialAccelVar = 1.0F;
      localParticleConfig.gravity.tangentialAccel = 0.0F;
      localParticleConfig.gravity.tangentialAccelVar = 1.0F;
      localParticleConfig.gravity.speed = 130.0F;
      localParticleConfig.gravity.speedVar = 30.0F;
      localParticleConfig.angle = -90.0F;
      localParticleConfig.angleVar = 5.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.positionVar = new PointF(0.5F, 0.0F);
      localParticleConfig.life = 4.5F;
      localParticleConfig.lifeVar = 0.0F;
      localParticleConfig.startSize = 4.0F;
      localParticleConfig.startSizeVar = 2.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.emissionRate = 20.0F;
      localParticleConfig.startColor = new float[] { 0.7F, 0.8F, 1.0F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.endColor = new float[] { 0.7F, 0.8F, 1.0F, 0.5F };
      localParticleConfig.endColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.texture = "circle";
      localParticleConfig.setBlendAdditive(false);
    }
  }
  
  public static class ParticleSnow
    extends ParticleManager
  {
    public ParticleSnow(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.duration = -1.0F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(0.0F, -1.0F);
      localParticleConfig.gravity.speed = 5.0F;
      localParticleConfig.gravity.speedVar = 1.0F;
      localParticleConfig.gravity.radialAccel = 0.0F;
      localParticleConfig.gravity.radialAccelVar = 1.0F;
      localParticleConfig.gravity.tangentialAccel = 0.0F;
      localParticleConfig.gravity.tangentialAccelVar = 1.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.positionVar = new PointF(0.5F, 0.0F);
      localParticleConfig.angle = -90.0F;
      localParticleConfig.angleVar = 5.0F;
      localParticleConfig.life = 45.0F;
      localParticleConfig.lifeVar = 15.0F;
      localParticleConfig.startSize = 10.0F;
      localParticleConfig.startSizeVar = 5.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.emissionRate = 10.0F;
      localParticleConfig.startColor = new float[] { 1.0F, 1.0F, 1.0F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.endColor = new float[] { 1.0F, 1.0F, 1.0F, 0.0F };
      localParticleConfig.endColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.texture = "snow2";
      localParticleConfig.setBlendAdditive(false);
    }
  }
  
  public static class ParticleSmoke
    extends ParticleManager
  {
    public ParticleSmoke(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.duration = -1.0F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(0.0F, 0.0F);
      localParticleConfig.gravity.radialAccel = 0.0F;
      localParticleConfig.gravity.radialAccelVar = 0.0F;
      localParticleConfig.gravity.speed = 25.0F;
      localParticleConfig.gravity.speedVar = 10.0F;
      localParticleConfig.angle = 90.0F;
      localParticleConfig.angleVar = 5.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.positionVar = new PointF(20.0F, 0.0F);
      localParticleConfig.life = 4.0F;
      localParticleConfig.lifeVar = 1.0F;
      localParticleConfig.startSize = 60.0F;
      localParticleConfig.startSizeVar = 10.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.emissionRate = (localParticleConfig.maxParticles / localParticleConfig.life);
      localParticleConfig.startColor = new float[] { 0.8F, 0.8F, 0.8F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.02F, 0.02F, 0.02F, 0.0F };
      localParticleConfig.endColor = new float[] { 0.0F, 0.0F, 0.0F, 1.0F };
      localParticleConfig.endColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.texture = "smoke";
      localParticleConfig.setBlendAdditive(false);
    }
  }
  
  public static class ParticleExplosion
    extends ParticleManager
  {
    public ParticleExplosion(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.duration = 0.1F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(0.0F, 0.0F);
      localParticleConfig.gravity.speed = 70.0F;
      localParticleConfig.gravity.speedVar = 40.0F;
      localParticleConfig.gravity.radialAccel = 0.0F;
      localParticleConfig.gravity.radialAccelVar = 0.0F;
      localParticleConfig.gravity.tangentialAccel = 0.0F;
      localParticleConfig.gravity.tangentialAccelVar = 0.0F;
      localParticleConfig.angle = 90.0F;
      localParticleConfig.angleVar = 360.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.positionVar = new PointF(0.0F, 0.0F);
      localParticleConfig.life = 5.0F;
      localParticleConfig.lifeVar = 2.0F;
      localParticleConfig.startSize = 15.0F;
      localParticleConfig.startSizeVar = 10.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.emissionRate = (localParticleConfig.maxParticles / localParticleConfig.life);
      localParticleConfig.startColor = new float[] { 0.7F, 0.1F, 0.2F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.5F, 0.5F, 0.5F, 0.0F };
      localParticleConfig.endColor = new float[] { 0.5F, 0.5F, 0.5F, 0.0F };
      localParticleConfig.endColorVar = new float[] { 0.5F, 0.5F, 0.5F, 0.0F };
      localParticleConfig.texture = "circle";
      localParticleConfig.setBlendAdditive(false);
    }
  }
  
  public static class ParticleSpiral
    extends ParticleManager
  {
    public ParticleSpiral(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.duration = -1.0F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(0.0F, 0.0F);
      localParticleConfig.gravity.speed = 150.0F;
      localParticleConfig.gravity.speedVar = 10.0F;
      localParticleConfig.gravity.radialAccel = -380.0F;
      localParticleConfig.gravity.radialAccelVar = 0.0F;
      localParticleConfig.gravity.tangentialAccel = 45.0F;
      localParticleConfig.gravity.tangentialAccelVar = 0.0F;
      localParticleConfig.angle = 90.0F;
      localParticleConfig.angleVar = 0.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.positionVar = new PointF(0.0F, 0.0F);
      localParticleConfig.life = 12.0F;
      localParticleConfig.lifeVar = 0.0F;
      localParticleConfig.startSize = 20.0F;
      localParticleConfig.startSizeVar = 0.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.emissionRate = (localParticleConfig.maxParticles / localParticleConfig.life);
      localParticleConfig.startColor = new float[] { 0.5F, 0.5F, 0.5F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.5F, 0.5F, 0.5F, 0.0F };
      localParticleConfig.endColor = new float[] { 0.5F, 0.5F, 0.5F, 1.0F };
      localParticleConfig.endColorVar = new float[] { 0.5F, 0.5F, 0.5F, 0.0F };
      localParticleConfig.texture = "star_line";
      localParticleConfig.setBlendAdditive(false);
    }
  }
  
  public static class ParticleMeteor
    extends ParticleManager
  {
    public ParticleMeteor(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.duration = -1.0F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(-200.0F, 200.0F);
      localParticleConfig.gravity.speed = 15.0F;
      localParticleConfig.gravity.speedVar = 5.0F;
      localParticleConfig.gravity.radialAccel = 0.0F;
      localParticleConfig.gravity.radialAccelVar = 0.0F;
      localParticleConfig.gravity.tangentialAccel = 0.0F;
      localParticleConfig.gravity.tangentialAccelVar = 0.0F;
      localParticleConfig.angle = 90.0F;
      localParticleConfig.angleVar = 360.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.positionVar = new PointF(0.0F, 0.0F);
      localParticleConfig.life = 2.0F;
      localParticleConfig.lifeVar = 1.0F;
      localParticleConfig.startSize = 60.0F;
      localParticleConfig.startSizeVar = 10.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.emissionRate = (localParticleConfig.maxParticles / localParticleConfig.life);
      localParticleConfig.startColor = new float[] { 0.2F, 0.4F, 0.7F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.0F, 0.0F, 0.2F, 0.1F };
      localParticleConfig.endColor = new float[] { 0.0F, 0.0F, 0.0F, 1.0F };
      localParticleConfig.endColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.texture = "stars";
      localParticleConfig.setBlendAdditive(true);
    }
  }
  
  public static class ParticleFlower
    extends ParticleManager
  {
    public ParticleFlower(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.duration = -1.0F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(0.0F, 0.0F);
      localParticleConfig.gravity.speed = 80.0F;
      localParticleConfig.gravity.speedVar = 10.0F;
      localParticleConfig.gravity.radialAccel = -60.0F;
      localParticleConfig.gravity.radialAccelVar = 0.0F;
      localParticleConfig.gravity.tangentialAccel = 15.0F;
      localParticleConfig.gravity.tangentialAccelVar = 0.0F;
      localParticleConfig.angle = 90.0F;
      localParticleConfig.angleVar = 360.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.positionVar = new PointF(0.0F, 0.0F);
      localParticleConfig.life = 4.0F;
      localParticleConfig.lifeVar = 1.0F;
      localParticleConfig.startSize = 30.0F;
      localParticleConfig.startSizeVar = 10.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.emissionRate = (localParticleConfig.maxParticles / localParticleConfig.life);
      localParticleConfig.startColor = new float[] { 0.5F, 0.5F, 0.5F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.5F, 0.5F, 0.5F, 0.5F };
      localParticleConfig.endColor = new float[] { 0.0F, 0.0F, 0.0F, 1.0F };
      localParticleConfig.endColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.texture = "star";
      localParticleConfig.setBlendAdditive(true);
    }
  }
  
  public static class ParticleGalaxy
    extends ParticleManager
  {
    public ParticleGalaxy(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.duration = -1.0F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(0.0F, 0.0F);
      localParticleConfig.gravity.speed = 60.0F;
      localParticleConfig.gravity.speedVar = 10.0F;
      localParticleConfig.gravity.radialAccel = -80.0F;
      localParticleConfig.gravity.radialAccelVar = 0.0F;
      localParticleConfig.gravity.tangentialAccel = 80.0F;
      localParticleConfig.gravity.tangentialAccelVar = 0.0F;
      localParticleConfig.angle = 90.0F;
      localParticleConfig.angleVar = 360.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.positionVar = new PointF(0.0F, 0.0F);
      localParticleConfig.life = 4.0F;
      localParticleConfig.lifeVar = 1.0F;
      localParticleConfig.startSize = 37.0F;
      localParticleConfig.startSizeVar = 10.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.emissionRate = (localParticleConfig.maxParticles / localParticleConfig.life);
      localParticleConfig.startColor = new float[] { 0.12F, 0.25F, 0.76F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.endColor = new float[] { 0.0F, 0.0F, 0.0F, 1.0F };
      localParticleConfig.endColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.texture = "fire";
      localParticleConfig.setBlendAdditive(true);
    }
  }
  
  public static class ParticleSun
    extends ParticleManager
  {
    public ParticleSun(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.setBlendAdditive(true);
      localParticleConfig.duration = -1.0F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(0.0F, 0.0F);
      localParticleConfig.gravity.radialAccel = 0.0F;
      localParticleConfig.gravity.radialAccelVar = 0.0F;
      localParticleConfig.gravity.speed = 20.0F;
      localParticleConfig.gravity.speedVar = 5.0F;
      localParticleConfig.angle = 90.0F;
      localParticleConfig.angleVar = 360.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.positionVar = new PointF(0.0F, 0.0F);
      localParticleConfig.life = 1.0F;
      localParticleConfig.lifeVar = 0.5F;
      localParticleConfig.startSize = 30.0F;
      localParticleConfig.startSizeVar = 10.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.emissionRate = (localParticleConfig.maxParticles / localParticleConfig.life);
      localParticleConfig.startColor = new float[] { 0.76F, 0.25F, 0.12F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.endColor = new float[] { 0.0F, 0.0F, 0.0F, 1.0F };
      localParticleConfig.endColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.texture = "fire";
    }
  }
  
  public static class ParticleFireworks
    extends ParticleManager
  {
    public ParticleFireworks(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.duration = -1.0F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(0.0F, -90.0F);
      localParticleConfig.gravity.radialAccel = 0.0F;
      localParticleConfig.gravity.radialAccelVar = 0.0F;
      localParticleConfig.gravity.speed = 180.0F;
      localParticleConfig.gravity.speedVar = 50.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.angle = 90.0F;
      localParticleConfig.angleVar = 20.0F;
      localParticleConfig.life = 3.5F;
      localParticleConfig.lifeVar = 1.0F;
      localParticleConfig.emissionRate = (localParticleConfig.maxParticles / localParticleConfig.life);
      localParticleConfig.startColor = new float[] { 0.5F, 0.5F, 0.5F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.5F, 0.5F, 0.5F, 0.1F };
      localParticleConfig.endColor = new float[] { 0.1F, 0.1F, 0.1F, 0.2F };
      localParticleConfig.endColorVar = new float[] { 0.1F, 0.1F, 0.1F, 0.2F };
      localParticleConfig.startSize = 8.0F;
      localParticleConfig.startSizeVar = 2.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.texture = "fire";
      localParticleConfig.setBlendAdditive(false);
    }
  }
  
  public static class ParticleFire
    extends ParticleManager
  {
    public ParticleFire(int paramInt)
    {
      super();
    }
    
    public void reset(int paramInt)
    {
      super.reset(paramInt);
      ParticleConfig localParticleConfig = config();
      localParticleConfig.duration = -1.0F;
      localParticleConfig.emitterMode = ParticleConfig.ParticleMode.Gravity;
      localParticleConfig.gravity = new ParticleConfig.GravityConfig();
      localParticleConfig.gravity.gravity = new PointF(0.0F, 0.0F);
      localParticleConfig.gravity.radialAccel = 0.0F;
      localParticleConfig.gravity.radialAccelVar = 0.0F;
      localParticleConfig.gravity.speed = 60.0F;
      localParticleConfig.gravity.speedVar = 20.0F;
      localParticleConfig.angle = 90.0F;
      localParticleConfig.angleVar = 10.0F;
      localParticleConfig.position = new PointF(0.0F, 0.0F);
      localParticleConfig.positionVar = new PointF(40.0F, 20.0F);
      localParticleConfig.life = 3.0F;
      localParticleConfig.lifeVar = 0.25F;
      localParticleConfig.startSize = 54.0F;
      localParticleConfig.startSizeVar = 10.0F;
      localParticleConfig.endSize = -1.0F;
      localParticleConfig.emissionRate = (localParticleConfig.maxParticles / localParticleConfig.life);
      localParticleConfig.startColor = new float[] { 0.76F, 0.25F, 0.12F, 1.0F };
      localParticleConfig.startColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.endColor = new float[] { 0.0F, 0.0F, 0.0F, 1.0F };
      localParticleConfig.endColorVar = new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
      localParticleConfig.texture = "fire";
      localParticleConfig.setBlendAdditive(true);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\particle\ParticleExamples.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */