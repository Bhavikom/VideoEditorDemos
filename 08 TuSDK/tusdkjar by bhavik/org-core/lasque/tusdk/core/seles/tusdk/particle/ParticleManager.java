package org.lasque.tusdk.core.seles.tusdk.particle;

import android.graphics.Color;
import android.graphics.PointF;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.json.JSONObject;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesVertexbuffer;
import org.lasque.tusdk.core.struct.TuSdkSize;

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
  private float[][] m = { { 0.0F, 0.0F } };
  private float[] n = { 1.0F, 0.5F };
  
  public float[] getTextureTile()
  {
    return this.n;
  }
  
  public void setTextureSize(TuSdkSize paramTuSdkSize)
  {
    this.l = paramTuSdkSize;
    this.j.setTextureSize(paramTuSdkSize);
  }
  
  public void setActive(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
  
  public boolean isActive()
  {
    return this.f;
  }
  
  public int drawTotal()
  {
    return this.e.size();
  }
  
  public ParticleConfig config()
  {
    return this.j;
  }
  
  public boolean isFull()
  {
    return this.e.size() == this.j.maxParticles;
  }
  
  public boolean isPaused()
  {
    return this.i;
  }
  
  public void setIsPaused(boolean paramBoolean)
  {
    this.i = paramBoolean;
  }
  
  public void start()
  {
    this.f = true;
    this.g = 0.0D;
    Iterator localIterator = this.e.iterator();
    while (localIterator.hasNext())
    {
      ParticleItem localParticleItem = (ParticleItem)localIterator.next();
      localParticleItem.timeToLive = -1.0F;
    }
  }
  
  public void stop()
  {
    this.f = false;
    this.g = this.j.duration;
    this.h = 0.0D;
  }
  
  public ParticleManager(int paramInt)
  {
    reset(paramInt);
  }
  
  public ParticleManager(JSONObject paramJSONObject)
  {
    this.a = paramJSONObject;
    reset(0);
  }
  
  public void destory()
  {
    if (this.c != null)
    {
      this.c.destory();
      this.c = null;
    }
  }
  
  protected void finalize()
  {
    destory();
    super.finalize();
  }
  
  public void reset(int paramInt)
  {
    this.k = new Random();
    this.j = new ParticleConfig();
    this.j.setJson(this.a);
    if (paramInt > 0) {
      this.j.maxParticles = paramInt;
    } else {
      paramInt = this.j.maxParticles;
    }
    if ((this.b == null) || (this.b.capacity() != 10 * paramInt)) {
      this.b = ByteBuffer.allocateDirect(10 * paramInt * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }
    this.d = new ArrayList(paramInt);
    this.e = new ArrayList(paramInt);
    for (int i1 = 0; i1 < paramInt; i1++)
    {
      ParticleItem localParticleItem = new ParticleItem();
      this.d.add(localParticleItem);
    }
    b(this.j.textureTiles);
    this.f = true;
    this.h = 0.0D;
  }
  
  private void a(int paramInt)
  {
    if (isPaused()) {
      return;
    }
    int i1 = 0;
    int i2 = Math.min(paramInt, this.d.size());
    while (i1 < i2)
    {
      ParticleItem localParticleItem = (ParticleItem)this.d.remove(0);
      a(localParticleItem);
      this.e.add(localParticleItem);
      i1++;
    }
  }
  
  private void a(ParticleItem paramParticleItem)
  {
    if (this.j.position == null) {
      return;
    }
    float f1 = this.j.life + this.j.lifeVar * a();
    paramParticleItem.timeToLive = Math.max(0.0F, f1);
    paramParticleItem.pos = new PointF(this.j.positionVar.x * a(), this.j.positionVar.y * a());
    paramParticleItem.color = new float[] { a(this.j.startColor[0], this.j.startColorVar[0]), a(this.j.startColor[1], this.j.startColorVar[1]), a(this.j.startColor[2], this.j.startColorVar[2]), a(this.j.startColor[3], this.j.startColorVar[3]) };
    paramParticleItem.deltaColor = new float[] { a(this.j.endColor[0], this.j.endColorVar[0]), a(this.j.endColor[1], this.j.endColorVar[1]), a(this.j.endColor[2], this.j.endColorVar[2]), a(this.j.endColor[3], this.j.endColorVar[3]) };
    paramParticleItem.deltaColor = new float[] { b(paramParticleItem.color[0], paramParticleItem.deltaColor[0], paramParticleItem.timeToLive), b(paramParticleItem.color[1], paramParticleItem.deltaColor[1], paramParticleItem.timeToLive), b(paramParticleItem.color[2], paramParticleItem.deltaColor[2], paramParticleItem.timeToLive), b(paramParticleItem.color[3], paramParticleItem.deltaColor[3], paramParticleItem.timeToLive) };
    paramParticleItem.size = Math.max(0.0F, this.j.startSize + this.j.startSizeVar * a());
    if (this.j.endSize == -1.0F)
    {
      paramParticleItem.deltaSize = 0.0F;
    }
    else
    {
      f2 = Math.max(0.0F, this.j.endSize + this.j.endSizeVar * a());
      paramParticleItem.deltaSize = ((f2 - paramParticleItem.size) / paramParticleItem.timeToLive);
    }
    paramParticleItem.rotation = (this.j.startSpin + this.j.startSpinVar * a());
    float f2 = this.j.endSpin + this.j.endSpinVar * a();
    paramParticleItem.deltaRotation = ((f2 - paramParticleItem.rotation) / paramParticleItem.timeToLive);
    PointF localPointF1 = new PointF(0.0F, 0.0F);
    if (this.j.positionType == ParticleConfig.PositionType.Free)
    {
      localPointF1.x = this.j.position.x;
      localPointF1.y = this.j.position.y;
    }
    paramParticleItem.startPos = localPointF1;
    paramParticleItem.tileIndex = (this.j.textureTiles < 1 ? 0 : this.k.nextInt(this.j.textureTiles));
    Object localObject;
    if (this.j.emitterMode == ParticleConfig.ParticleMode.Gravity)
    {
      localObject = new ParticleItem.ParticleGravity();
      ((ParticleItem.ParticleGravity)localObject).radialAccel = (this.j.gravity.radialAccel + this.j.gravity.radialAccelVar * a());
      ((ParticleItem.ParticleGravity)localObject).tangentialAccel = (this.j.gravity.tangentialAccel + this.j.gravity.tangentialAccelVar * a());
      double d1 = Math.toRadians(this.j.angle + this.j.angleVar * a());
      PointF localPointF2 = new PointF((float)Math.cos(d1), (float)Math.sin(d1));
      float f4 = this.j.gravity.speed + this.j.gravity.speedVar * a();
      PointF localPointF3 = new PointF(localPointF2.x * f4, localPointF2.y * f4);
      ((ParticleItem.ParticleGravity)localObject).dirX = localPointF3.x;
      ((ParticleItem.ParticleGravity)localObject).dirY = localPointF3.y;
      paramParticleItem.gravityMode = ((ParticleItem.ParticleGravity)localObject);
      if (this.j.gravity.rotationIsDir) {
        paramParticleItem.rotation = (-(float)Math.toDegrees(Math.atan2(localPointF3.y, localPointF3.x)));
      }
    }
    else
    {
      localObject = new ParticleItem.ParticleRadius();
      ((ParticleItem.ParticleRadius)localObject).radius = (this.j.radius.startRadius + this.j.radius.startRadiusVar * a());
      ((ParticleItem.ParticleRadius)localObject).angle = ((float)Math.toRadians(this.j.angle + this.j.angleVar * a()));
      ((ParticleItem.ParticleRadius)localObject).degreesPerSecond = ((float)Math.toRadians(this.j.radius.rotatePerSecond + this.j.radius.rotatePerSecondVar * a()));
      if (this.j.radius.endRadius == -1.0F)
      {
        ((ParticleItem.ParticleRadius)localObject).deltaRadius = 0.0F;
      }
      else
      {
        float f3 = this.j.radius.endRadius + this.j.radius.endRadiusVar * a();
        ((ParticleItem.ParticleRadius)localObject).deltaRadius = ((f3 - ((ParticleItem.ParticleRadius)localObject).radius) / paramParticleItem.timeToLive);
      }
      paramParticleItem.radiusMode = ((ParticleItem.ParticleRadius)localObject);
    }
  }
  
  public void updateParticleSize(float paramFloat)
  {
    if (isActive())
    {
      this.j.setSizeRate(paramFloat);
      Iterator localIterator = this.e.iterator();
      while (localIterator.hasNext())
      {
        ParticleItem localParticleItem = (ParticleItem)localIterator.next();
        localParticleItem.size = Math.max(0.0F, this.j.startSize + this.j.startSizeVar * a());
        if (this.j.endSize == -1.0F)
        {
          localParticleItem.deltaSize = 0.0F;
        }
        else
        {
          float f1 = Math.max(0.0F, this.j.endSize + this.j.endSizeVar * a());
          localParticleItem.deltaSize = ((f1 - localParticleItem.size) / localParticleItem.timeToLive);
        }
      }
    }
  }
  
  public void updateParticleColor(int paramInt)
  {
    if (isActive())
    {
      if ((paramInt == 0) && (this.j.getJSON() != null)) {
        this.j.startColor = new float[] { (float)this.j.getJSON().optDouble("startColorRed", 0.0D), (float)this.j.getJSON().optDouble("startColorGreen", 0.0D), (float)this.j.getJSON().optDouble("startColorBlue", 0.0D), (float)this.j.getJSON().optDouble("startColorAlpha", 0.0D) };
      } else if (this.j.getJSON() != null) {
        this.j.startColor = new float[] { Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), Color.alpha(paramInt) };
      }
      Iterator localIterator = this.e.iterator();
      while (localIterator.hasNext())
      {
        ParticleItem localParticleItem = (ParticleItem)localIterator.next();
        localParticleItem.color = new float[] { a(this.j.startColor[0], this.j.startColorVar[0]), a(this.j.startColor[1], this.j.startColorVar[1]), a(this.j.startColor[2], this.j.startColorVar[2]), a(this.j.startColor[3], this.j.startColorVar[3]) };
        localParticleItem.deltaColor = new float[] { a(this.j.endColor[0], this.j.endColorVar[0]), a(this.j.endColor[1], this.j.endColorVar[1]), a(this.j.endColor[2], this.j.endColorVar[2]), a(this.j.endColor[3], this.j.endColorVar[3]) };
        localParticleItem.deltaColor = new float[] { b(localParticleItem.color[0], localParticleItem.deltaColor[0], localParticleItem.timeToLive), b(localParticleItem.color[1], localParticleItem.deltaColor[1], localParticleItem.timeToLive), b(localParticleItem.color[2], localParticleItem.deltaColor[2], localParticleItem.timeToLive), b(localParticleItem.color[3], localParticleItem.deltaColor[3], localParticleItem.timeToLive) };
      }
    }
  }
  
  public void update(float paramFloat)
  {
    if ((this.f) && (this.j.emissionRate != 0.0D))
    {
      double d1 = 1.0D / this.j.emissionRate;
      if (this.e.size() < this.j.maxParticles)
      {
        this.h += paramFloat;
        if (this.h < 0.0D) {
          this.h = 0.0D;
        }
      }
      int i1 = (int)Math.min(this.j.maxParticles - this.e.size(), this.h / d1);
      a(i1);
      this.h -= d1 * i1;
      this.g += paramFloat;
      if (this.g < 0.0D) {
        this.g = 0.0D;
      }
      if ((this.j.duration != -1.0F) && (this.j.duration < this.g)) {
        stop();
      }
    }
    ArrayList localArrayList = new ArrayList(this.e);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      ParticleItem localParticleItem = (ParticleItem)localIterator.next();
      a(localParticleItem, paramFloat);
    }
    updateParticleQuads();
  }
  
  private void a(ParticleItem paramParticleItem, float paramFloat)
  {
    paramParticleItem.timeToLive -= paramFloat;
    if (paramParticleItem.timeToLive < 0.0F)
    {
      this.e.remove(paramParticleItem);
      this.d.add(paramParticleItem);
      return;
    }
    PointF localPointF1;
    if (this.j.emitterMode == ParticleConfig.ParticleMode.Gravity)
    {
      localPointF1 = new PointF(0.0F, 0.0F);
      PointF localPointF2 = new PointF(0.0F, 0.0F);
      if ((paramParticleItem.pos.x != 0.0F) || (paramParticleItem.pos.y != 0.0F)) {
        localPointF2 = a(paramParticleItem.pos);
      }
      PointF localPointF3 = new PointF(localPointF2.x, localPointF2.y);
      localPointF2.x *= paramParticleItem.gravityMode.radialAccel;
      localPointF2.y *= paramParticleItem.gravityMode.radialAccel;
      localPointF3 = new PointF(localPointF3.y, localPointF3.x);
      localPointF3.x *= -paramParticleItem.gravityMode.tangentialAccel;
      localPointF3.y *= paramParticleItem.gravityMode.tangentialAccel;
      localPointF1.x = (localPointF2.x + localPointF3.x + this.j.gravity.gravity.x);
      localPointF1.y = (localPointF2.y + localPointF3.y + this.j.gravity.gravity.y);
      localPointF1.x *= paramFloat;
      localPointF1.y *= paramFloat;
      paramParticleItem.gravityMode.dirX += localPointF1.x;
      paramParticleItem.gravityMode.dirY += localPointF1.y;
      localPointF1.x = (paramParticleItem.gravityMode.dirX * paramFloat * this.j.yCoordFlipped);
      localPointF1.y = (paramParticleItem.gravityMode.dirY * paramFloat * this.j.yCoordFlipped);
      paramParticleItem.pos.x += localPointF1.x;
      paramParticleItem.pos.y += localPointF1.y;
    }
    else
    {
      paramParticleItem.radiusMode.angle += paramParticleItem.radiusMode.degreesPerSecond * paramFloat;
      paramParticleItem.radiusMode.radius += paramParticleItem.radiusMode.deltaRadius * paramFloat;
      localPointF1 = new PointF(0.0F, 0.0F);
      localPointF1.x = (-(float)Math.cos(paramParticleItem.radiusMode.angle) * paramParticleItem.radiusMode.radius);
      localPointF1.y = (-(float)Math.sin(paramParticleItem.radiusMode.angle) * paramParticleItem.radiusMode.radius * this.j.yCoordFlipped);
      paramParticleItem.pos = localPointF1;
    }
    paramParticleItem.color[0] += paramParticleItem.deltaColor[0] * paramFloat;
    paramParticleItem.color[1] += paramParticleItem.deltaColor[1] * paramFloat;
    paramParticleItem.color[2] += paramParticleItem.deltaColor[2] * paramFloat;
    paramParticleItem.color[3] += paramParticleItem.deltaColor[3] * paramFloat;
    paramParticleItem.size = Math.max(0.0F, paramParticleItem.size + paramParticleItem.deltaSize * paramFloat);
    paramParticleItem.rotation += paramParticleItem.deltaRotation * paramFloat;
  }
  
  private PointF b(ParticleItem paramParticleItem)
  {
    PointF localPointF = new PointF(0.0F, 0.0F);
    if (this.j.positionType == ParticleConfig.PositionType.Free)
    {
      localPointF.x = (paramParticleItem.pos.x + paramParticleItem.startPos.x);
      localPointF.y = (paramParticleItem.pos.y + paramParticleItem.startPos.y);
    }
    else
    {
      localPointF.x = (paramParticleItem.pos.x + this.j.position.x);
      localPointF.y = (paramParticleItem.pos.y + this.j.position.y);
    }
    return localPointF;
  }
  
  public void updateParticleQuads()
  {
    ArrayList localArrayList = new ArrayList(this.e);
    if (localArrayList.size() < 1) {
      return;
    }
    a(localArrayList);
  }
  
  private void a(ArrayList<ParticleItem> paramArrayList)
  {
    this.b.position(0);
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      ParticleItem localParticleItem = (ParticleItem)localIterator.next();
      PointF localPointF = b(localParticleItem);
      this.b.put(localPointF.x / this.l.width * 2.0F - 1.0F);
      this.b.put(1.0F - localPointF.y / this.l.height * 2.0F);
      this.b.put(localParticleItem.color);
      this.b.put(this.m[localParticleItem.tileIndex]);
      this.b.put(localParticleItem.size);
      this.b.put(localParticleItem.rotation);
    }
    this.b.position(0);
  }
  
  public void updateWithNoTime()
  {
    update(0.0F);
  }
  
  public void freshVBO()
  {
    this.b.position(0);
    this.b.limit(this.b.capacity());
    if (this.c == null) {
      this.c = SelesContext.fetchVertexbuffer(this.b);
    } else {
      this.c.fresh(0, drawTotal() * 10, this.b);
    }
  }
  
  private float a()
  {
    double d1 = this.k.nextDouble() * 2.0D - 1.0D;
    if (d1 == 0.0D) {
      d1 = a();
    }
    return (float)d1;
  }
  
  private float a(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return Math.min(Math.max(paramFloat1, paramFloat2), paramFloat3);
  }
  
  private float a(float paramFloat1, float paramFloat2)
  {
    float f1 = a(paramFloat1 + paramFloat2 * a(), 0.0F, 1.0F);
    return f1;
  }
  
  private float b(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat3 == 0.0F) {
      return 0.0F;
    }
    float f1 = (paramFloat2 - paramFloat1) / paramFloat3;
    return f1;
  }
  
  private PointF a(PointF paramPointF)
  {
    double d1 = paramPointF.x * paramPointF.x + paramPointF.y * paramPointF.y;
    if (d1 == 1.0D) {
      return new PointF(0.0F, 0.0F);
    }
    d1 = Math.sqrt(d1);
    if (d1 < 1.999999982195158E-37D) {
      return new PointF(0.0F, 0.0F);
    }
    paramPointF = new PointF(paramPointF.x, paramPointF.y);
    d1 = 1.0D / d1;
    PointF tmp81_80 = paramPointF;
    tmp81_80.x = ((float)(tmp81_80.x * d1));
    PointF tmp93_92 = paramPointF;
    tmp93_92.y = ((float)(tmp93_92.y * d1));
    return paramPointF;
  }
  
  private void b(int paramInt)
  {
    if (paramInt < 1) {
      paramInt = 1;
    }
    for (int i1 = 1; paramInt > i1 * i1; i1++) {}
    float[][] arrayOfFloat = new float[i1 * i1][2];
    float f1 = 1.0F / i1;
    float f2 = f1 / 2.0F;
    for (int i2 = 0; i2 < i1; i2++) {
      for (int i3 = 0; i3 < i1; i3++)
      {
        arrayOfFloat[(i2 * i1 + i3)][0] = (i3 * f1 + f2);
        arrayOfFloat[(i2 * i1 + i3)][1] = ((i1 - 1 - i2) * f1 + f2);
      }
    }
    this.n = new float[] { i1, f2 };
    this.m = arrayOfFloat;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\particle\ParticleManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */