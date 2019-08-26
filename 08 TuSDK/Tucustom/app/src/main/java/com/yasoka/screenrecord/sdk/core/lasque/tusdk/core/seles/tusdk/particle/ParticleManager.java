// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.particle;

//import org.lasque.tusdk.core.seles.SelesContext;
import android.graphics.Color;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesVertexbuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.Iterator;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.Random;
import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.SelesVertexbuffer;
import java.nio.FloatBuffer;
import org.json.JSONObject;

public class ParticleManager
{
    public static final int PARTICLE_DURATIONIN_FINITY = -1;
    public static final int PARTICLE_STARTSIZE_EQUAL_TO_ENDSIZE = -1;
    public static final int PARTICLE_STARTRADIUS_EQUAL_TO_ENDRADIUS = -1;
    public static final int VERTEX_POSITION_SIZE = 2;
    public static final int VERTEX_COLOR_SIZE = 4;
    public static final int VERTEX_APPEAR_SIZE = 4;
    public static final int VERTEX_STRIDE = 10;
    public static final int VERTEX_STRIDE_BYTE = 40;
    public static final int VERTEX_OFFSET_COLOR_BYTE = 8;
    public static final int VERTEX_OFFSET_APPEAR_BYTE = 24;
    private JSONObject a;
    private FloatBuffer b;
    private SelesVertexbuffer c;
    private ArrayList<ParticleItem> d;
    private ArrayList<ParticleItem> e;
    private boolean f;
    private double g;
    private double h;
    private boolean i;
    private ParticleConfig j;
    private Random k;
    private TuSdkSize l;
    private float[][] m;
    private float[] n;
    
    public float[] getTextureTile() {
        return this.n;
    }
    
    public void setTextureSize(final TuSdkSize tuSdkSize) {
        this.l = tuSdkSize;
        this.j.setTextureSize(tuSdkSize);
    }
    
    public void setActive(final boolean f) {
        this.f = f;
    }
    
    public boolean isActive() {
        return this.f;
    }
    
    public int drawTotal() {
        return this.e.size();
    }
    
    public ParticleConfig config() {
        return this.j;
    }
    
    public boolean isFull() {
        return this.e.size() == this.j.maxParticles;
    }
    
    public boolean isPaused() {
        return this.i;
    }
    
    public void setIsPaused(final boolean i) {
        this.i = i;
    }
    
    public void start() {
        this.f = true;
        this.g = 0.0;
        final Iterator<ParticleItem> iterator = this.e.iterator();
        while (iterator.hasNext()) {
            iterator.next().timeToLive = -1.0f;
        }
    }
    
    public void stop() {
        this.f = false;
        this.g = this.j.duration;
        this.h = 0.0;
    }
    
    public ParticleManager(final int n) {
        this.m = new float[][] { { 0.0f, 0.0f } };
        this.n = new float[] { 1.0f, 0.5f };
        this.reset(n);
    }
    
    public ParticleManager(final JSONObject a) {
        this.m = new float[][] { { 0.0f, 0.0f } };
        this.n = new float[] { 1.0f, 0.5f };
        this.a = a;
        this.reset(0);
    }
    
    public void destory() {
        if (this.c != null) {
            this.c.destory();
            this.c = null;
        }
    }
    
    @Override
    protected void finalize() {
        this.destory();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public void reset(int maxParticles) {
        this.k = new Random();
        (this.j = new ParticleConfig()).setJson(this.a);
        if (maxParticles > 0) {
            this.j.maxParticles = maxParticles;
        }
        else {
            maxParticles = this.j.maxParticles;
        }
        if (this.b == null || this.b.capacity() != 10 * maxParticles) {
            this.b = ByteBuffer.allocateDirect(10 * maxParticles * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        }
        this.d = new ArrayList<ParticleItem>(maxParticles);
        this.e = new ArrayList<ParticleItem>(maxParticles);
        for (int i = 0; i < maxParticles; ++i) {
            this.d.add(new ParticleItem());
        }
        this.b(this.j.textureTiles);
        this.f = true;
        this.h = 0.0;
    }
    
    private void a(final int a) {
        if (this.isPaused()) {
            return;
        }
        for (int i = 0; i < Math.min(a, this.d.size()); ++i) {
            final ParticleItem e = this.d.remove(0);
            this.a(e);
            this.e.add(e);
        }
    }
    
    private void a(final ParticleItem particleItem) {
        if (this.j.position == null) {
            return;
        }
        particleItem.timeToLive = Math.max(0.0f, this.j.life + this.j.lifeVar * this.a());
        particleItem.pos = new PointF(this.j.positionVar.x * this.a(), this.j.positionVar.y * this.a());
        particleItem.color = new float[] { this.a(this.j.startColor[0], this.j.startColorVar[0]), this.a(this.j.startColor[1], this.j.startColorVar[1]), this.a(this.j.startColor[2], this.j.startColorVar[2]), this.a(this.j.startColor[3], this.j.startColorVar[3]) };
        particleItem.deltaColor = new float[] { this.a(this.j.endColor[0], this.j.endColorVar[0]), this.a(this.j.endColor[1], this.j.endColorVar[1]), this.a(this.j.endColor[2], this.j.endColorVar[2]), this.a(this.j.endColor[3], this.j.endColorVar[3]) };
        particleItem.deltaColor = new float[] { this.b(particleItem.color[0], particleItem.deltaColor[0], particleItem.timeToLive), this.b(particleItem.color[1], particleItem.deltaColor[1], particleItem.timeToLive), this.b(particleItem.color[2], particleItem.deltaColor[2], particleItem.timeToLive), this.b(particleItem.color[3], particleItem.deltaColor[3], particleItem.timeToLive) };
        particleItem.size = Math.max(0.0f, this.j.startSize + this.j.startSizeVar * this.a());
        if (this.j.endSize == -1.0f) {
            particleItem.deltaSize = 0.0f;
        }
        else {
            particleItem.deltaSize = (Math.max(0.0f, this.j.endSize + this.j.endSizeVar * this.a()) - particleItem.size) / particleItem.timeToLive;
        }
        particleItem.rotation = this.j.startSpin + this.j.startSpinVar * this.a();
        particleItem.deltaRotation = (this.j.endSpin + this.j.endSpinVar * this.a() - particleItem.rotation) / particleItem.timeToLive;
        final PointF startPos = new PointF(0.0f, 0.0f);
        if (this.j.positionType == ParticleConfig.PositionType.Free) {
            startPos.x = this.j.position.x;
            startPos.y = this.j.position.y;
        }
        particleItem.startPos = startPos;
        particleItem.tileIndex = ((this.j.textureTiles < 1) ? 0 : this.k.nextInt(this.j.textureTiles));
        if (this.j.emitterMode == ParticleConfig.ParticleMode.Gravity) {
            final ParticleItem.ParticleGravity gravityMode = new ParticleItem.ParticleGravity();
            gravityMode.radialAccel = this.j.gravity.radialAccel + this.j.gravity.radialAccelVar * this.a();
            gravityMode.tangentialAccel = this.j.gravity.tangentialAccel + this.j.gravity.tangentialAccelVar * this.a();
            final double radians = Math.toRadians(this.j.angle + this.j.angleVar * this.a());
            final PointF pointF = new PointF((float)Math.cos(radians), (float)Math.sin(radians));
            final float n = this.j.gravity.speed + this.j.gravity.speedVar * this.a();
            final PointF pointF2 = new PointF(pointF.x * n, pointF.y * n);
            gravityMode.dirX = pointF2.x;
            gravityMode.dirY = pointF2.y;
            particleItem.gravityMode = gravityMode;
            if (this.j.gravity.rotationIsDir) {
                particleItem.rotation = -(float)Math.toDegrees(Math.atan2(pointF2.y, pointF2.x));
            }
        }
        else {
            final ParticleItem.ParticleRadius radiusMode = new ParticleItem.ParticleRadius();
            radiusMode.radius = this.j.radius.startRadius + this.j.radius.startRadiusVar * this.a();
            radiusMode.angle = (float)Math.toRadians(this.j.angle + this.j.angleVar * this.a());
            radiusMode.degreesPerSecond = (float)Math.toRadians(this.j.radius.rotatePerSecond + this.j.radius.rotatePerSecondVar * this.a());
            if (this.j.radius.endRadius == -1.0f) {
                radiusMode.deltaRadius = 0.0f;
            }
            else {
                radiusMode.deltaRadius = (this.j.radius.endRadius + this.j.radius.endRadiusVar * this.a() - radiusMode.radius) / particleItem.timeToLive;
            }
            particleItem.radiusMode = radiusMode;
        }
    }
    
    public void updateParticleSize(final float sizeRate) {
        if (this.isActive()) {
            this.j.setSizeRate(sizeRate);
            for (final ParticleItem particleItem : this.e) {
                particleItem.size = Math.max(0.0f, this.j.startSize + this.j.startSizeVar * this.a());
                if (this.j.endSize == -1.0f) {
                    particleItem.deltaSize = 0.0f;
                }
                else {
                    particleItem.deltaSize = (Math.max(0.0f, this.j.endSize + this.j.endSizeVar * this.a()) - particleItem.size) / particleItem.timeToLive;
                }
            }
        }
    }
    
    public void updateParticleColor(final int n) {
        if (this.isActive()) {
            if (n == 0 && this.j.getJSON() != null) {
                this.j.startColor = new float[] { (float)this.j.getJSON().optDouble("startColorRed", 0.0), (float)this.j.getJSON().optDouble("startColorGreen", 0.0), (float)this.j.getJSON().optDouble("startColorBlue", 0.0), (float)this.j.getJSON().optDouble("startColorAlpha", 0.0) };
            }
            else if (this.j.getJSON() != null) {
                this.j.startColor = new float[] { (float)Color.red(n), (float)Color.green(n), (float)Color.blue(n), (float)Color.alpha(n) };
            }
            for (final ParticleItem particleItem : this.e) {
                particleItem.color = new float[] { this.a(this.j.startColor[0], this.j.startColorVar[0]), this.a(this.j.startColor[1], this.j.startColorVar[1]), this.a(this.j.startColor[2], this.j.startColorVar[2]), this.a(this.j.startColor[3], this.j.startColorVar[3]) };
                particleItem.deltaColor = new float[] { this.a(this.j.endColor[0], this.j.endColorVar[0]), this.a(this.j.endColor[1], this.j.endColorVar[1]), this.a(this.j.endColor[2], this.j.endColorVar[2]), this.a(this.j.endColor[3], this.j.endColorVar[3]) };
                particleItem.deltaColor = new float[] { this.b(particleItem.color[0], particleItem.deltaColor[0], particleItem.timeToLive), this.b(particleItem.color[1], particleItem.deltaColor[1], particleItem.timeToLive), this.b(particleItem.color[2], particleItem.deltaColor[2], particleItem.timeToLive), this.b(particleItem.color[3], particleItem.deltaColor[3], particleItem.timeToLive) };
            }
        }
    }
    
    public void update(final float n) {
        if (this.f && this.j.emissionRate != 0.0) {
            final double n2 = 1.0 / this.j.emissionRate;
            if (this.e.size() < this.j.maxParticles) {
                this.h += n;
                if (this.h < 0.0) {
                    this.h = 0.0;
                }
            }
            final int n3 = (int)Math.min(this.j.maxParticles - this.e.size(), this.h / n2);
            this.a(n3);
            this.h -= n2 * n3;
            this.g += n;
            if (this.g < 0.0) {
                this.g = 0.0;
            }
            if (this.j.duration != -1.0f && this.j.duration < this.g) {
                this.stop();
            }
        }
        final Iterator<ParticleItem> iterator = new ArrayList<ParticleItem>(this.e).iterator();
        while (iterator.hasNext()) {
            this.a(iterator.next(), n);
        }
        this.updateParticleQuads();
    }
    
    private void a(final ParticleItem particleItem, final float n) {
        particleItem.timeToLive -= n;
        if (particleItem.timeToLive < 0.0f) {
            this.e.remove(particleItem);
            this.d.add(particleItem);
            return;
        }
        if (this.j.emitterMode == ParticleConfig.ParticleMode.Gravity) {
            final PointF pointF = new PointF(0.0f, 0.0f);
            PointF a = new PointF(0.0f, 0.0f);
            if (particleItem.pos.x != 0.0f || particleItem.pos.y != 0.0f) {
                a = this.a(particleItem.pos);
            }
            final PointF pointF2 = new PointF(a.x, a.y);
            final PointF pointF3 = a;
            pointF3.x *= particleItem.gravityMode.radialAccel;
            final PointF pointF4 = a;
            pointF4.y *= particleItem.gravityMode.radialAccel;
            final PointF pointF6;
            final PointF pointF5 = pointF6 = new PointF(pointF2.y, pointF2.x);
            pointF6.x *= -particleItem.gravityMode.tangentialAccel;
            final PointF pointF7 = pointF5;
            pointF7.y *= particleItem.gravityMode.tangentialAccel;
            pointF.x = a.x + pointF5.x + this.j.gravity.gravity.x;
            pointF.y = a.y + pointF5.y + this.j.gravity.gravity.y;
            final PointF pointF8 = pointF;
            pointF8.x *= n;
            final PointF pointF9 = pointF;
            pointF9.y *= n;
            final ParticleItem.ParticleGravity gravityMode = particleItem.gravityMode;
            gravityMode.dirX += pointF.x;
            final ParticleItem.ParticleGravity gravityMode2 = particleItem.gravityMode;
            gravityMode2.dirY += pointF.y;
            pointF.x = particleItem.gravityMode.dirX * n * this.j.yCoordFlipped;
            pointF.y = particleItem.gravityMode.dirY * n * this.j.yCoordFlipped;
            final PointF pos = particleItem.pos;
            pos.x += pointF.x;
            final PointF pos2 = particleItem.pos;
            pos2.y += pointF.y;
        }
        else {
            final ParticleItem.ParticleRadius radiusMode = particleItem.radiusMode;
            radiusMode.angle += particleItem.radiusMode.degreesPerSecond * n;
            final ParticleItem.ParticleRadius radiusMode2 = particleItem.radiusMode;
            radiusMode2.radius += particleItem.radiusMode.deltaRadius * n;
            final PointF pos3 = new PointF(0.0f, 0.0f);
            pos3.x = -(float)Math.cos(particleItem.radiusMode.angle) * particleItem.radiusMode.radius;
            pos3.y = -(float)Math.sin(particleItem.radiusMode.angle) * particleItem.radiusMode.radius * this.j.yCoordFlipped;
            particleItem.pos = pos3;
        }
        final float[] color = particleItem.color;
        final int n2 = 0;
        color[n2] += particleItem.deltaColor[0] * n;
        final float[] color2 = particleItem.color;
        final int n3 = 1;
        color2[n3] += particleItem.deltaColor[1] * n;
        final float[] color3 = particleItem.color;
        final int n4 = 2;
        color3[n4] += particleItem.deltaColor[2] * n;
        final float[] color4 = particleItem.color;
        final int n5 = 3;
        color4[n5] += particleItem.deltaColor[3] * n;
        particleItem.size = Math.max(0.0f, particleItem.size + particleItem.deltaSize * n);
        particleItem.rotation += particleItem.deltaRotation * n;
    }
    
    private PointF b(final ParticleItem particleItem) {
        final PointF pointF = new PointF(0.0f, 0.0f);
        if (this.j.positionType == ParticleConfig.PositionType.Free) {
            pointF.x = particleItem.pos.x + particleItem.startPos.x;
            pointF.y = particleItem.pos.y + particleItem.startPos.y;
        }
        else {
            pointF.x = particleItem.pos.x + this.j.position.x;
            pointF.y = particleItem.pos.y + this.j.position.y;
        }
        return pointF;
    }
    
    public void updateParticleQuads() {
        final ArrayList<ParticleItem> list = new ArrayList<ParticleItem>(this.e);
        if (list.size() < 1) {
            return;
        }
        this.a(list);
    }
    
    private void a(final ArrayList<ParticleItem> list) {
        this.b.position(0);
        for (final ParticleItem particleItem : list) {
            final PointF b = this.b(particleItem);
            this.b.put(b.x / this.l.width * 2.0f - 1.0f);
            this.b.put(1.0f - b.y / this.l.height * 2.0f);
            this.b.put(particleItem.color);
            this.b.put(this.m[particleItem.tileIndex]);
            this.b.put(particleItem.size);
            this.b.put(particleItem.rotation);
        }
        this.b.position(0);
    }
    
    public void updateWithNoTime() {
        this.update(0.0f);
    }
    
    public void freshVBO() {
        this.b.position(0);
        this.b.limit(this.b.capacity());
        if (this.c == null) {
            this.c = SelesContext.fetchVertexbuffer(this.b);
        }
        else {
            this.c.fresh(0, this.drawTotal() * 10, this.b);
        }
    }
    
    private float a() {
        double n = this.k.nextDouble() * 2.0 - 1.0;
        if (n == 0.0) {
            n = this.a();
        }
        return (float)n;
    }
    
    private float a(final float a, final float b, final float b2) {
        return Math.min(Math.max(a, b), b2);
    }
    
    private float a(final float n, final float n2) {
        return this.a(n + n2 * this.a(), 0.0f, 1.0f);
    }
    
    private float b(final float n, final float n2, final float n3) {
        if (n3 == 0.0f) {
            return 0.0f;
        }
        return (n2 - n) / n3;
    }
    
    private PointF a(PointF pointF) {
        final double a = pointF.x * pointF.x + pointF.y * pointF.y;
        if (a == 1.0) {
            return new PointF(0.0f, 0.0f);
        }
        final double sqrt = Math.sqrt(a);
        if (sqrt < 1.999999982195158E-37) {
            return new PointF(0.0f, 0.0f);
        }
        pointF = new PointF(pointF.x, pointF.y);
        final double n = 1.0 / sqrt;
        final PointF pointF2 = pointF;
        pointF2.x *= (float)n;
        final PointF pointF3 = pointF;
        pointF3.y *= (float)n;
        return pointF;
    }
    
    private void b(int i) {
        if (i < 1) {
            i = 1;
        }
        int n;
        for (n = 1; i > n * n; ++n) {}
        final float[][] m = new float[n * n][2];
        final float n2 = 1.0f / n;
        final float n3 = n2 / 2.0f;
        for (int j = 0; j < n; ++j) {
            for (int k = 0; k < n; ++k) {
                m[j * n + k][0] = k * n2 + n3;
                m[j * n + k][1] = (n - 1 - j) * n2 + n3;
            }
        }
        this.n = new float[] { (float)n, n3 };
        this.m = m;
    }
}
