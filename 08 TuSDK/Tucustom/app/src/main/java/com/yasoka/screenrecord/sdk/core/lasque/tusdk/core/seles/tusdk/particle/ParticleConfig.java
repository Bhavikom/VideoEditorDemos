// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.particle;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.PointF;
//import org.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;

import org.json.JSONObject;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class ParticleConfig extends JsonBaseBean implements Serializable
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
    public int blendFuncSrc;
    public int blendFuncDst;
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
    public PointF position;
    public PointF positionVar;
    public ParticleMode emitterMode;
    public PositionType positionType;
    public float emissionRate;
    @DataBase("yCoordFlipped")
    public int yCoordFlipped;
    public GravityConfig gravity;
    public RadiusConfig radius;
    private TuSdkSize b;
    private float c;
    
    public ParticleConfig() {
        this.position = new PointF(0.0f, 0.0f);
        this.positionVar = new PointF(0.0f, 0.0f);
        this.c = 1.0f;
        this.blendFuncSrc = this.a(1);
        this.blendFuncDst = this.a(5);
        this.yCoordFlipped = 1;
        this.emitterMode = ParticleMode.Gravity;
        this.positionType = PositionType.Free;
    }
    
    public void setTextureSize(final TuSdkSize b) {
        if (b == null || !b.isSize()) {
            return;
        }
        if (this.b != null && this.b.equals(b)) {
            return;
        }
        this.b = b;
        this.updateParticleSettings();
    }
    
    public void setSizeRate(final float n) {
        if (this.c <= 0.0f) {
            return;
        }
        this.c = 1.0f + n;
        this.updateParticleSettings();
    }
    
    public void updateParticleSettings() {
        float n = 1.0f;
        if (this.b != null) {
            n = Math.min(this.b.width, this.b.height) / this.windowWidth;
        }
        this.startSize = this.startSize * n * this.c;
        this.startSizeVar = this.startSizeVar * n * this.c;
        this.endSize = this.endSize * n * this.c;
        this.endSizeVar = this.endSizeVar * n * this.c;
        this.positionVar = new PointF(this.positionVar.x * n * this.c, this.positionVar.y * n * this.c);
        if (this.gravity != null) {
            this.gravity.gravity = new PointF(this.gravity.gravity.x * n * this.c, this.gravity.gravity.y * n * this.c);
            this.gravity.speed = this.gravity.speed * n * this.c;
            this.gravity.speedVar = this.gravity.speedVar * n * this.c;
            this.gravity.tangentialAccel = this.gravity.tangentialAccel * n * this.c;
        }
    }
    
    public JSONObject getJSON() {
        return this.a;
    }
    
    @Override
    public void setJson(final JSONObject jsonObject) {
        super.setJson(jsonObject);
        if (jsonObject == null) {
            return;
        }
        this.a = jsonObject;
        this.blendFuncSrc = this.a(jsonObject.optInt("blendFuncSource", 1));
        this.blendFuncDst = this.a(jsonObject.optInt("blendFuncDestination", 5));
        this.startColor = new float[] { (float)jsonObject.optDouble("startColorRed", 0.0), (float)jsonObject.optDouble("startColorGreen", 0.0), (float)jsonObject.optDouble("startColorBlue", 0.0), (float)jsonObject.optDouble("startColorAlpha", 0.0) };
        this.startColorVar = new float[] { (float)jsonObject.optDouble("startColorVarianceRed", 0.0), (float)jsonObject.optDouble("startColorVarianceGreen", 0.0), (float)jsonObject.optDouble("startColorVarianceBlue", 0.0), (float)jsonObject.optDouble("startColorVarianceAlpha", 0.0) };
        this.endColor = new float[] { (float)jsonObject.optDouble("finishColorRed", 0.0), (float)jsonObject.optDouble("finishColorGreen", 0.0), (float)jsonObject.optDouble("finishColorBlue", 0.0), (float)jsonObject.optDouble("finishColorAlpha", 0.0) };
        this.endColorVar = new float[] { (float)jsonObject.optDouble("finishColorVarianceRed", 0.0), (float)jsonObject.optDouble("finishColorVarianceGreen", 0.0), (float)jsonObject.optDouble("finishColorVarianceBlue", 0.0), (float)jsonObject.optDouble("finishColorVarianceAlpha", 0.0) };
        this.position = new PointF((float)jsonObject.optDouble("sourcePositionx", 0.0), (float)jsonObject.optDouble("sourcePositiony", 0.0));
        this.positionVar = new PointF((float)jsonObject.optDouble("sourcePositionVariancex", 0.0), (float)jsonObject.optDouble("sourcePositionVariancey", 0.0));
        if (jsonObject.optInt("emitterType") != 0) {
            this.emitterMode = ParticleMode.Radius;
        }
        if (jsonObject.optInt("positionType") != 0) {
            this.positionType = PositionType.Grouped;
        }
        this.emissionRate = this.maxParticles / this.life;
        if (this.textureTiles < 1) {
            this.textureTiles = 1;
        }
        if (this.emitterMode == ParticleMode.Gravity) {
            (this.gravity = new GravityConfig()).setJson(jsonObject);
        }
        else {
            (this.radius = new RadiusConfig()).setJson(jsonObject);
        }
    }
    
    public void setBlendAdditive(final boolean b) {
        if (b) {
            this.blendFuncSrc = 770;
            this.blendFuncDst = 1;
            return;
        }
        this.blendFuncSrc = 1;
        this.blendFuncDst = 771;
    }
    
    public boolean isBlendAdditive() {
        return this.blendFuncSrc == 770 && this.blendFuncDst == 1;
    }
    
    private int a(final int n) {
        switch (n) {
            case 1: {
                return 1;
            }
            case 2:
            case 768: {
                return 768;
            }
            case 3:
            case 769: {
                return 769;
            }
            case 4:
            case 770: {
                return 770;
            }
            case 5:
            case 771: {
                return 771;
            }
            case 6:
            case 772: {
                return 772;
            }
            case 7:
            case 773: {
                return 773;
            }
            case 8:
            case 774: {
                return 774;
            }
            case 9:
            case 775: {
                return 775;
            }
            case 10:
            case 776: {
                return 776;
            }
            default: {
                return 0;
            }
        }
    }
    
    public static class RadiusConfig extends JsonBaseBean implements Serializable
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
    
    public static class GravityConfig extends JsonBaseBean implements Serializable
    {
        public PointF gravity;
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
        
        public GravityConfig() {
            this.gravity = new PointF(0.0f, 0.0f);
        }
        
        @Override
        public void setJson(final JSONObject json) {
            super.setJson(json);
            if (json == null) {
                return;
            }
            this.gravity = new PointF((float)json.optDouble("gravityx", 0.0), (float)json.optDouble("gravityy", 0.0));
        }
    }
    
    public enum PositionType
    {
        Free, 
        Grouped;
    }
    
    public enum ParticleMode
    {
        Gravity, 
        Radius;
    }
}
