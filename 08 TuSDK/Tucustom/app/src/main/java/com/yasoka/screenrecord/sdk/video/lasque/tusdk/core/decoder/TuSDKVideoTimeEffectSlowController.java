// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKAVPacket;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.common.TuSDKAVPacket;
import java.util.LinkedList;

public class TuSDKVideoTimeEffectSlowController extends TuSDKVideoTimeEffectController
{
    @Override
    public void doPacketTimeEffectExtract(final LinkedList<TuSDKAVPacket> list) {
        super.doPacketTimeEffectExtract(list);
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).getSampleTimeUs() >= this.mTimeRange.getStartTimeUS()) {
                final TuSDKAVPacket e = new TuSDKAVPacket(list.get(i).getByteBuffer(), list.get(i).getSampleTimeUs(), list.get(i).getPacketType());
                e.setChunkSize(list.get(i).getChunkSize());
                this.mCachePackets.addLast(e);
            }
            if (this.mCachePackets.size() > 0) {
                if (this.mCachePackets.getFirst().getSampleTimeUs() <= this.mTimeRange.getEndTimeUS()) {
                    list.get(i).setByteBuffer(ByteBuffer.wrap(this.mCachePackets.getFirst().getByteBuffer().array()));
                    list.get(i).setChunkSize(this.mCachePackets.getFirst().getChunkSize());
                    if (this.mCounter == this.mTimes) {
                        this.mCachePackets.removeFirst();
                        this.mCounter = 0;
                    }
                    else {
                        ++this.mCounter;
                    }
                }
                else {
                    list.get(i).setByteBuffer(ByteBuffer.wrap(this.mCachePackets.getFirst().getByteBuffer().array()));
                    list.get(i).setChunkSize(this.mCachePackets.getFirst().getChunkSize());
                    this.mCachePackets.removeFirst();
                }
            }
        }
    }
}
