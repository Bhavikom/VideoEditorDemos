// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKAVPacket;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.common.TuSDKAVPacket;
import java.util.LinkedList;

public class TuSDKVideoTimeEffectRepeatController extends TuSDKVideoTimeEffectController
{
    private boolean a;
    private LinkedList<TuSDKAVPacket> b;
    
    public TuSDKVideoTimeEffectRepeatController() {
        this.a = false;
        this.b = new LinkedList<TuSDKAVPacket>();
    }
    
    @Override
    public void doPacketTimeEffectExtract(final LinkedList<TuSDKAVPacket> list) {
        super.doPacketTimeEffectExtract(list);
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).getSampleTimeUs() > this.mTimeRange.getEndTimeUS()) {
                this.a = true;
            }
            if (list.get(i).getSampleTimeUs() >= this.mTimeRange.getStartTimeUS() && list.get(i).getSampleTimeUs() <= this.mTimeRange.getEndTimeUS()) {
                final TuSDKAVPacket e = new TuSDKAVPacket(list.get(i).getByteBuffer(), list.get(i).getSampleTimeUs(), list.get(i).getPacketType());
                e.setChunkSize(list.get(i).getChunkSize());
                this.mCachePackets.addLast(e);
            }
            else if (this.a && this.mCachePackets.size() > 0) {
                if (this.mCounter == this.mCachePackets.size() - 1) {
                    this.mCounter = 0;
                    ++this.mCopyTimes;
                }
                if (this.mCopyTimes >= this.mTimes) {
                    final TuSDKAVPacket e2 = new TuSDKAVPacket(list.get(i).getByteBuffer(), list.get(i).getSampleTimeUs(), list.get(i).getPacketType());
                    e2.setChunkSize(list.get(i).getChunkSize());
                    this.b.addLast(e2);
                    list.get(i).setByteBuffer(ByteBuffer.wrap(this.b.getFirst().getByteBuffer().array()));
                    list.get(i).setChunkSize(this.b.getFirst().getChunkSize());
                    this.b.removeFirst();
                }
                else {
                    final TuSDKAVPacket e3 = new TuSDKAVPacket(list.get(i).getByteBuffer(), list.get(i).getSampleTimeUs(), list.get(i).getPacketType());
                    e3.setChunkSize(list.get(i).getChunkSize());
                    this.b.addLast(e3);
                    list.get(i).setByteBuffer(ByteBuffer.wrap(this.mCachePackets.get(this.mCounter).getByteBuffer().array()));
                    list.get(i).setChunkSize(this.mCachePackets.get(this.mCounter).getChunkSize());
                    ++this.mCounter;
                }
            }
        }
    }
    
    @Override
    public void reset() {
        super.reset();
        this.a = false;
        this.b.clear();
    }
}
