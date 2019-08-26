// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.particle;

import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkBundle;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;
//import org.lasque.tusdk.core.utils.json.JsonHelper;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.TuSdkBundle;

public class ParticleExamples extends ParticleManager
{
    public ParticleExamples(final int n) {
        super(n);
    }
    
    public static ParticleManager indexWith(final int n, final int n2) {
        switch (n) {
            case 1: {
                return new ParticleFire(n2);
            }
            case 2: {
                return new ParticleFireworks(n2);
            }
            case 3: {
                return new ParticleSun(n2);
            }
            case 4: {
                return new ParticleGalaxy(n2);
            }
            case 5: {
                return new ParticleFlower(n2);
            }
            case 6: {
                return new ParticleMeteor(n2);
            }
            case 7: {
                return new ParticleSpiral(n2);
            }
            case 8: {
                return new ParticleExplosion(n2);
            }
            case 9: {
                return new ParticleSmoke(n2);
            }
            case 10: {
                return new ParticleSnow(n2);
            }
            case 11: {
                return new ParticleRain(n2);
            }
            case 12: {
                return new ParticleManager(JsonHelper.json(TuSdkContext.getAssetsText(TuSdkBundle.sdkBundleTexture("particle_clear.json"))));
            }
            default: {
                return new ParticleFire(n2);
            }
        }
    }
    
    public static class ParticleRain extends ParticleManager
    {
        public ParticleRain(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.duration = -1.0f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(10.0f, -10.0f);
            config.gravity.radialAccel = 0.0f;
            config.gravity.radialAccelVar = 1.0f;
            config.gravity.tangentialAccel = 0.0f;
            config.gravity.tangentialAccelVar = 1.0f;
            config.gravity.speed = 130.0f;
            config.gravity.speedVar = 30.0f;
            config.angle = -90.0f;
            config.angleVar = 5.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.positionVar = new PointF(0.5f, 0.0f);
            config.life = 4.5f;
            config.lifeVar = 0.0f;
            config.startSize = 4.0f;
            config.startSizeVar = 2.0f;
            config.endSize = -1.0f;
            config.emissionRate = 20.0f;
            config.startColor = new float[] { 0.7f, 0.8f, 1.0f, 1.0f };
            config.startColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.endColor = new float[] { 0.7f, 0.8f, 1.0f, 0.5f };
            config.endColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.texture = "circle";
            config.setBlendAdditive(false);
        }
    }
    
    public static class ParticleSnow extends ParticleManager
    {
        public ParticleSnow(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.duration = -1.0f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(0.0f, -1.0f);
            config.gravity.speed = 5.0f;
            config.gravity.speedVar = 1.0f;
            config.gravity.radialAccel = 0.0f;
            config.gravity.radialAccelVar = 1.0f;
            config.gravity.tangentialAccel = 0.0f;
            config.gravity.tangentialAccelVar = 1.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.positionVar = new PointF(0.5f, 0.0f);
            config.angle = -90.0f;
            config.angleVar = 5.0f;
            config.life = 45.0f;
            config.lifeVar = 15.0f;
            config.startSize = 10.0f;
            config.startSizeVar = 5.0f;
            config.endSize = -1.0f;
            config.emissionRate = 10.0f;
            config.startColor = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
            config.startColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.endColor = new float[] { 1.0f, 1.0f, 1.0f, 0.0f };
            config.endColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.texture = "snow2";
            config.setBlendAdditive(false);
        }
    }
    
    public static class ParticleSmoke extends ParticleManager
    {
        public ParticleSmoke(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.duration = -1.0f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(0.0f, 0.0f);
            config.gravity.radialAccel = 0.0f;
            config.gravity.radialAccelVar = 0.0f;
            config.gravity.speed = 25.0f;
            config.gravity.speedVar = 10.0f;
            config.angle = 90.0f;
            config.angleVar = 5.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.positionVar = new PointF(20.0f, 0.0f);
            config.life = 4.0f;
            config.lifeVar = 1.0f;
            config.startSize = 60.0f;
            config.startSizeVar = 10.0f;
            config.endSize = -1.0f;
            config.emissionRate = config.maxParticles / config.life;
            config.startColor = new float[] { 0.8f, 0.8f, 0.8f, 1.0f };
            config.startColorVar = new float[] { 0.02f, 0.02f, 0.02f, 0.0f };
            config.endColor = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
            config.endColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.texture = "smoke";
            config.setBlendAdditive(false);
        }
    }
    
    public static class ParticleExplosion extends ParticleManager
    {
        public ParticleExplosion(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.duration = 0.1f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(0.0f, 0.0f);
            config.gravity.speed = 70.0f;
            config.gravity.speedVar = 40.0f;
            config.gravity.radialAccel = 0.0f;
            config.gravity.radialAccelVar = 0.0f;
            config.gravity.tangentialAccel = 0.0f;
            config.gravity.tangentialAccelVar = 0.0f;
            config.angle = 90.0f;
            config.angleVar = 360.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.positionVar = new PointF(0.0f, 0.0f);
            config.life = 5.0f;
            config.lifeVar = 2.0f;
            config.startSize = 15.0f;
            config.startSizeVar = 10.0f;
            config.endSize = -1.0f;
            config.emissionRate = config.maxParticles / config.life;
            config.startColor = new float[] { 0.7f, 0.1f, 0.2f, 1.0f };
            config.startColorVar = new float[] { 0.5f, 0.5f, 0.5f, 0.0f };
            config.endColor = new float[] { 0.5f, 0.5f, 0.5f, 0.0f };
            config.endColorVar = new float[] { 0.5f, 0.5f, 0.5f, 0.0f };
            config.texture = "circle";
            config.setBlendAdditive(false);
        }
    }
    
    public static class ParticleSpiral extends ParticleManager
    {
        public ParticleSpiral(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.duration = -1.0f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(0.0f, 0.0f);
            config.gravity.speed = 150.0f;
            config.gravity.speedVar = 10.0f;
            config.gravity.radialAccel = -380.0f;
            config.gravity.radialAccelVar = 0.0f;
            config.gravity.tangentialAccel = 45.0f;
            config.gravity.tangentialAccelVar = 0.0f;
            config.angle = 90.0f;
            config.angleVar = 0.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.positionVar = new PointF(0.0f, 0.0f);
            config.life = 12.0f;
            config.lifeVar = 0.0f;
            config.startSize = 20.0f;
            config.startSizeVar = 0.0f;
            config.endSize = -1.0f;
            config.emissionRate = config.maxParticles / config.life;
            config.startColor = new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
            config.startColorVar = new float[] { 0.5f, 0.5f, 0.5f, 0.0f };
            config.endColor = new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
            config.endColorVar = new float[] { 0.5f, 0.5f, 0.5f, 0.0f };
            config.texture = "star_line";
            config.setBlendAdditive(false);
        }
    }
    
    public static class ParticleMeteor extends ParticleManager
    {
        public ParticleMeteor(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.duration = -1.0f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(-200.0f, 200.0f);
            config.gravity.speed = 15.0f;
            config.gravity.speedVar = 5.0f;
            config.gravity.radialAccel = 0.0f;
            config.gravity.radialAccelVar = 0.0f;
            config.gravity.tangentialAccel = 0.0f;
            config.gravity.tangentialAccelVar = 0.0f;
            config.angle = 90.0f;
            config.angleVar = 360.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.positionVar = new PointF(0.0f, 0.0f);
            config.life = 2.0f;
            config.lifeVar = 1.0f;
            config.startSize = 60.0f;
            config.startSizeVar = 10.0f;
            config.endSize = -1.0f;
            config.emissionRate = config.maxParticles / config.life;
            config.startColor = new float[] { 0.2f, 0.4f, 0.7f, 1.0f };
            config.startColorVar = new float[] { 0.0f, 0.0f, 0.2f, 0.1f };
            config.endColor = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
            config.endColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.texture = "stars";
            config.setBlendAdditive(true);
        }
    }
    
    public static class ParticleFlower extends ParticleManager
    {
        public ParticleFlower(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.duration = -1.0f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(0.0f, 0.0f);
            config.gravity.speed = 80.0f;
            config.gravity.speedVar = 10.0f;
            config.gravity.radialAccel = -60.0f;
            config.gravity.radialAccelVar = 0.0f;
            config.gravity.tangentialAccel = 15.0f;
            config.gravity.tangentialAccelVar = 0.0f;
            config.angle = 90.0f;
            config.angleVar = 360.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.positionVar = new PointF(0.0f, 0.0f);
            config.life = 4.0f;
            config.lifeVar = 1.0f;
            config.startSize = 30.0f;
            config.startSizeVar = 10.0f;
            config.endSize = -1.0f;
            config.emissionRate = config.maxParticles / config.life;
            config.startColor = new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
            config.startColorVar = new float[] { 0.5f, 0.5f, 0.5f, 0.5f };
            config.endColor = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
            config.endColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.texture = "star";
            config.setBlendAdditive(true);
        }
    }
    
    public static class ParticleGalaxy extends ParticleManager
    {
        public ParticleGalaxy(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.duration = -1.0f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(0.0f, 0.0f);
            config.gravity.speed = 60.0f;
            config.gravity.speedVar = 10.0f;
            config.gravity.radialAccel = -80.0f;
            config.gravity.radialAccelVar = 0.0f;
            config.gravity.tangentialAccel = 80.0f;
            config.gravity.tangentialAccelVar = 0.0f;
            config.angle = 90.0f;
            config.angleVar = 360.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.positionVar = new PointF(0.0f, 0.0f);
            config.life = 4.0f;
            config.lifeVar = 1.0f;
            config.startSize = 37.0f;
            config.startSizeVar = 10.0f;
            config.endSize = -1.0f;
            config.emissionRate = config.maxParticles / config.life;
            config.startColor = new float[] { 0.12f, 0.25f, 0.76f, 1.0f };
            config.startColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.endColor = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
            config.endColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.texture = "fire";
            config.setBlendAdditive(true);
        }
    }
    
    public static class ParticleSun extends ParticleManager
    {
        public ParticleSun(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.setBlendAdditive(true);
            config.duration = -1.0f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(0.0f, 0.0f);
            config.gravity.radialAccel = 0.0f;
            config.gravity.radialAccelVar = 0.0f;
            config.gravity.speed = 20.0f;
            config.gravity.speedVar = 5.0f;
            config.angle = 90.0f;
            config.angleVar = 360.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.positionVar = new PointF(0.0f, 0.0f);
            config.life = 1.0f;
            config.lifeVar = 0.5f;
            config.startSize = 30.0f;
            config.startSizeVar = 10.0f;
            config.endSize = -1.0f;
            config.emissionRate = config.maxParticles / config.life;
            config.startColor = new float[] { 0.76f, 0.25f, 0.12f, 1.0f };
            config.startColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.endColor = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
            config.endColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.texture = "fire";
        }
    }
    
    public static class ParticleFireworks extends ParticleManager
    {
        public ParticleFireworks(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.duration = -1.0f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(0.0f, -90.0f);
            config.gravity.radialAccel = 0.0f;
            config.gravity.radialAccelVar = 0.0f;
            config.gravity.speed = 180.0f;
            config.gravity.speedVar = 50.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.angle = 90.0f;
            config.angleVar = 20.0f;
            config.life = 3.5f;
            config.lifeVar = 1.0f;
            config.emissionRate = config.maxParticles / config.life;
            config.startColor = new float[] { 0.5f, 0.5f, 0.5f, 1.0f };
            config.startColorVar = new float[] { 0.5f, 0.5f, 0.5f, 0.1f };
            config.endColor = new float[] { 0.1f, 0.1f, 0.1f, 0.2f };
            config.endColorVar = new float[] { 0.1f, 0.1f, 0.1f, 0.2f };
            config.startSize = 8.0f;
            config.startSizeVar = 2.0f;
            config.endSize = -1.0f;
            config.texture = "fire";
            config.setBlendAdditive(false);
        }
    }
    
    public static class ParticleFire extends ParticleManager
    {
        public ParticleFire(final int n) {
            super(n);
        }
        
        @Override
        public void reset(final int n) {
            super.reset(n);
            final ParticleConfig config = this.config();
            config.duration = -1.0f;
            config.emitterMode = ParticleConfig.ParticleMode.Gravity;
            config.gravity = new ParticleConfig.GravityConfig();
            config.gravity.gravity = new PointF(0.0f, 0.0f);
            config.gravity.radialAccel = 0.0f;
            config.gravity.radialAccelVar = 0.0f;
            config.gravity.speed = 60.0f;
            config.gravity.speedVar = 20.0f;
            config.angle = 90.0f;
            config.angleVar = 10.0f;
            config.position = new PointF(0.0f, 0.0f);
            config.positionVar = new PointF(40.0f, 20.0f);
            config.life = 3.0f;
            config.lifeVar = 0.25f;
            config.startSize = 54.0f;
            config.startSizeVar = 10.0f;
            config.endSize = -1.0f;
            config.emissionRate = config.maxParticles / config.life;
            config.startColor = new float[] { 0.76f, 0.25f, 0.12f, 1.0f };
            config.startColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.endColor = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
            config.endColorVar = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
            config.texture = "fire";
            config.setBlendAdditive(true);
        }
    }
}
