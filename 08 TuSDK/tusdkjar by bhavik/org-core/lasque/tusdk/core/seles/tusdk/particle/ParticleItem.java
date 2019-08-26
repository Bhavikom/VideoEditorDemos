package org.lasque.tusdk.core.seles.tusdk.particle;

import android.graphics.PointF;

public class ParticleItem
{
  public PointF pos;
  public PointF startPos;
  public float[] color;
  public float[] deltaColor;
  public float size;
  public float deltaSize;
  public float rotation;
  public float deltaRotation;
  public float timeToLive;
  public int tileIndex;
  public ParticleGravity gravityMode;
  public ParticleRadius radiusMode;
  
  public static class ParticleRadius
  {
    public float angle;
    public float degreesPerSecond;
    public float radius;
    public float deltaRadius;
  }
  
  public static class ParticleGravity
  {
    public float dirX;
    public float dirY;
    public float radialAccel;
    public float tangentialAccel;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\particle\ParticleItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */