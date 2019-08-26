package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import org.lasque.tusdk.core.utils.TLog;

public class AVAssetTrackPipeMediaExtractor
  implements AVAssetTrackOutputSouce
{
  private TrackStreamPipeTimeline a;
  
  public AVAssetTrackPipeMediaExtractor(List<AVAssetTrack> paramList)
  {
    this.a = new TrackStreamPipeTimeline(paramList);
  }
  
  public AVSampleBuffer readNextSampleBuffer(int paramInt)
  {
    Assert.assertNotNull("No pipe node output is currently available.", TrackStreamPipeTimeline.a(this.a));
    AVSampleBuffer localAVSampleBuffer = readSampleBuffer(paramInt);
    advance();
    return localAVSampleBuffer;
  }
  
  public AVSampleBuffer readSampleBuffer(int paramInt)
  {
    Assert.assertNotNull("No pipe node output is currently available.", TrackStreamPipeTimeline.a(this.a));
    AVSampleBuffer localAVSampleBuffer = TrackStreamPipeTimeline.a(this.a).readSampleBuffer(paramInt);
    if ((localAVSampleBuffer == null) && (!isOutputDone()))
    {
      if (AVAssetTrackPipeMediaExtractor.TrackStreamPipeTimeline.TrackStreamPipeNode.a(TrackStreamPipeTimeline.a(this.a)) != null) {
        return new AVSampleBuffer(AVAssetTrackPipeMediaExtractor.TrackStreamPipeTimeline.TrackStreamPipeNode.a(TrackStreamPipeTimeline.a(this.a)).inputTrack().mediaFormat());
      }
      advance();
    }
    return localAVSampleBuffer;
  }
  
  public void setTimeRange(AVTimeRange paramAVTimeRange)
  {
    TrackStreamPipeTimeline.a(this.a, paramAVTimeRange);
  }
  
  public boolean seekTo(long paramLong, int paramInt)
  {
    return TrackStreamPipeTimeline.a(this.a, paramLong, paramInt);
  }
  
  public boolean advance()
  {
    if (TrackStreamPipeTimeline.a(this.a) == null)
    {
      TLog.w("advance no data", new Object[0]);
      return false;
    }
    if (!TrackStreamPipeTimeline.a(this.a).advance()) {
      return TrackStreamPipeTimeline.b(this.a);
    }
    return true;
  }
  
  public boolean isDecodeOnly(long paramLong)
  {
    if ((paramLong < 0L) || (TrackStreamPipeTimeline.a(this.a) == null)) {
      return false;
    }
    return TrackStreamPipeTimeline.a(this.a).isDecodeOnly(paramLong);
  }
  
  public boolean isOutputDone()
  {
    for (AVAssetTrackPipeMediaExtractor.TrackStreamPipeTimeline.TrackStreamPipeNode localTrackStreamPipeNode = TrackStreamPipeTimeline.a(this.a); localTrackStreamPipeNode != null; localTrackStreamPipeNode = AVAssetTrackPipeMediaExtractor.TrackStreamPipeTimeline.TrackStreamPipeNode.a(localTrackStreamPipeNode)) {
      if (!localTrackStreamPipeNode.isOutputDone()) {
        return false;
      }
    }
    return true;
  }
  
  public long durationTimeUs()
  {
    return TrackStreamPipeTimeline.c(this.a);
  }
  
  public long outputTimeUs()
  {
    if (TrackStreamPipeTimeline.a(this.a) == null) {
      return 0L;
    }
    return AVAssetTrackPipeMediaExtractor.TrackStreamPipeTimeline.TrackStreamPipeNode.b(TrackStreamPipeTimeline.a(this.a)) + TrackStreamPipeTimeline.a(this.a).outputTimeUs();
  }
  
  public long calOutputTimeUs(long paramLong)
  {
    if (TrackStreamPipeTimeline.a(this.a) == null) {
      return 0L;
    }
    return AVAssetTrackPipeMediaExtractor.TrackStreamPipeTimeline.TrackStreamPipeNode.a(TrackStreamPipeTimeline.a(this.a), paramLong);
  }
  
  public void setAlwaysCopiesSampleData(boolean paramBoolean)
  {
    for (AVAssetTrackPipeMediaExtractor.TrackStreamPipeTimeline.TrackStreamPipeNode localTrackStreamPipeNode = TrackStreamPipeTimeline.a(this.a); localTrackStreamPipeNode != null; localTrackStreamPipeNode = AVAssetTrackPipeMediaExtractor.TrackStreamPipeTimeline.TrackStreamPipeNode.a(localTrackStreamPipeNode)) {
      localTrackStreamPipeNode.setAlwaysCopiesSampleData(paramBoolean);
    }
  }
  
  public AVAssetTrack inputTrack()
  {
    AVAssetTrackPipeMediaExtractor.TrackStreamPipeTimeline.TrackStreamPipeNode localTrackStreamPipeNode = TrackStreamPipeTimeline.a(this.a);
    if (localTrackStreamPipeNode != null) {
      return localTrackStreamPipeNode.inputTrack();
    }
    return null;
  }
  
  public void reset()
  {
    TrackStreamPipeTimeline.d(this.a);
  }
  
  private class TrackStreamPipeTimeline
  {
    private List<AVAssetTrack> b;
    private List<TrackStreamPipeNode> c;
    private TrackStreamPipeNode d;
    
    public TrackStreamPipeTimeline()
    {
      List localList;
      Assert.assertEquals("Please input a valid tracks.", true, localList.size() > 0);
      this.b = localList;
      a();
      Assert.assertEquals("Please input a valid tracks.", true, this.c.size() > 0);
      this.d = ((TrackStreamPipeNode)this.c.get(0));
    }
    
    private boolean a(long paramLong, int paramInt)
    {
      Iterator localIterator = this.c.iterator();
      while (localIterator.hasNext())
      {
        TrackStreamPipeNode localTrackStreamPipeNode = (TrackStreamPipeNode)localIterator.next();
        localTrackStreamPipeNode.reset();
        if (TrackStreamPipeNode.b(localTrackStreamPipeNode, paramLong))
        {
          this.d = localTrackStreamPipeNode;
          return localTrackStreamPipeNode.seekTo(TrackStreamPipeNode.c(localTrackStreamPipeNode, paramLong), paramInt);
        }
      }
      return false;
    }
    
    private void a()
    {
      Assert.assertNotNull("Please input a valid tracks.", this.b);
      Assert.assertEquals("Please input a valid track", true, this.b.size() > 0);
      this.c = new ArrayList(2);
      Object localObject = null;
      long l1 = 0L;
      long l2 = 0L;
      Iterator localIterator = this.b.iterator();
      while (localIterator.hasNext())
      {
        AVAssetTrack localAVAssetTrack = (AVAssetTrack)localIterator.next();
        TrackStreamPipeNode localTrackStreamPipeNode = new TrackStreamPipeNode(localAVAssetTrack);
        if (localObject == null)
        {
          localObject = localTrackStreamPipeNode;
        }
        else
        {
          TrackStreamPipeNode.a((TrackStreamPipeNode)localObject, localTrackStreamPipeNode);
          localObject = localTrackStreamPipeNode;
        }
        TrackStreamPipeNode.d(localTrackStreamPipeNode, l1);
        TrackStreamPipeNode.e(localTrackStreamPipeNode, l1 + localTrackStreamPipeNode.durationTimeUs());
        l1 = TrackStreamPipeNode.c(localTrackStreamPipeNode);
        TrackStreamPipeNode.f(localTrackStreamPipeNode, l2);
        TrackStreamPipeNode.g(localTrackStreamPipeNode, l2 + localTrackStreamPipeNode.inputTrack().durationTimeUs());
        l2 = TrackStreamPipeNode.d(localTrackStreamPipeNode);
        this.c.add(localTrackStreamPipeNode);
      }
    }
    
    private void a(AVTimeRange paramAVTimeRange)
    {
      List localList = this.c;
      long l1 = 0L;
      Iterator localIterator = localList.iterator();
      TrackStreamPipeNode localTrackStreamPipeNode;
      while (localIterator.hasNext())
      {
        localTrackStreamPipeNode = (TrackStreamPipeNode)localIterator.next();
        AVTimeRange localAVTimeRange = TrackStreamPipeNode.a(localTrackStreamPipeNode, paramAVTimeRange);
        if (localAVTimeRange == null)
        {
          TrackStreamPipeNode.a(localTrackStreamPipeNode, false);
        }
        else
        {
          TrackStreamPipeNode.a(localTrackStreamPipeNode, true);
          long l2 = localAVTimeRange.durationUs();
          localTrackStreamPipeNode.setTimeRange(localAVTimeRange);
          TrackStreamPipeNode.d(localTrackStreamPipeNode, l1);
          TrackStreamPipeNode.e(localTrackStreamPipeNode, l1 + l2);
          l1 = TrackStreamPipeNode.c(localTrackStreamPipeNode);
        }
      }
      this.d = null;
      localIterator = this.c.iterator();
      while (localIterator.hasNext())
      {
        localTrackStreamPipeNode = (TrackStreamPipeNode)localIterator.next();
        if (TrackStreamPipeNode.e(localTrackStreamPipeNode))
        {
          this.d = localTrackStreamPipeNode;
          break;
        }
      }
      if (this.d == null)
      {
        a();
        this.d = ((TrackStreamPipeNode)this.c.get(0));
        TLog.e("Please set a valid cropping time.", new Object[0]);
      }
    }
    
    private boolean b()
    {
      if (this.d == null) {
        return false;
      }
      TrackStreamPipeNode localTrackStreamPipeNode = TrackStreamPipeNode.a(this.d);
      if (localTrackStreamPipeNode == null) {
        return false;
      }
      localTrackStreamPipeNode.reset();
      this.d = localTrackStreamPipeNode;
      return true;
    }
    
    private void c()
    {
      this.d = null;
      Iterator localIterator = this.c.iterator();
      while (localIterator.hasNext())
      {
        TrackStreamPipeNode localTrackStreamPipeNode = (TrackStreamPipeNode)localIterator.next();
        localTrackStreamPipeNode.reset();
      }
      if (this.c.size() > 0) {
        this.d = ((TrackStreamPipeNode)this.c.get(0));
      }
    }
    
    private long d()
    {
      long l = 0L;
      Iterator localIterator = this.c.iterator();
      while (localIterator.hasNext())
      {
        TrackStreamPipeNode localTrackStreamPipeNode = (TrackStreamPipeNode)localIterator.next();
        l += (TrackStreamPipeNode.e(localTrackStreamPipeNode) ? localTrackStreamPipeNode.durationTimeUs() : 0L);
      }
      return l;
    }
    
    private class TrackStreamPipeNode
      extends AVAssetTrackMediaExtractor
    {
      private boolean b = true;
      private TrackStreamPipeNode c;
      private long d = -1L;
      private long e = -1L;
      private long f = -1L;
      private long g = -1L;
      
      public TrackStreamPipeNode(AVAssetTrack paramAVAssetTrack)
      {
        super();
      }
      
      private TrackStreamPipeNode a()
      {
        if ((this.b) && (this.c != null) && (this.c.b)) {
          return this.c;
        }
        return null;
      }
      
      private boolean a(long paramLong)
      {
        return (paramLong >= this.d) && (paramLong <= this.e);
      }
      
      private long b(long paramLong)
      {
        return paramLong - this.d + timeRange().startUs();
      }
      
      private long c(long paramLong)
      {
        return Math.max(0L, this.d + paramLong - timeRange().startUs());
      }
      
      private AVTimeRange a(AVTimeRange paramAVTimeRange)
      {
        AVTimeRange localAVTimeRange = AVTimeRange.AVTimeRangeMake(this.f, this.g - this.f);
        if (this.f >= paramAVTimeRange.endUs()) {
          return null;
        }
        long l1 = this.g - this.f;
        if ((paramAVTimeRange.startUs() <= this.f) && (paramAVTimeRange.endUs() >= this.g)) {
          return AVTimeRange.AVTimeRangeMake(0L, l1);
        }
        if ((!localAVTimeRange.containsTimeUs(paramAVTimeRange.startUs())) && (!localAVTimeRange.containsTimeUs(paramAVTimeRange.endUs()))) {
          return null;
        }
        long l2 = Math.max(paramAVTimeRange.startUs() - this.f, 0L);
        long l3 = Math.min(l1, paramAVTimeRange.endUs() - l2);
        if (paramAVTimeRange.endUs() <= this.g) {
          l3 = Math.min(l3, paramAVTimeRange.endUs() - this.f - l2);
        }
        long l4 = Math.min(this.g - l2, l3);
        if (l4 <= 0L) {
          return null;
        }
        return AVTimeRange.AVTimeRangeMake(l2, l4);
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAssetTrackPipeMediaExtractor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */