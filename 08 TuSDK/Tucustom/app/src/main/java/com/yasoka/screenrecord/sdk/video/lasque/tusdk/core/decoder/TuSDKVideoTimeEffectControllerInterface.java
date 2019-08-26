// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

//import org.lasque.tusdk.core.common.TuSDKAVPacket;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKAVPacket;

import java.util.LinkedList;

public interface TuSDKVideoTimeEffectControllerInterface
{
    void doPacketTimeEffectExtract(final LinkedList<TuSDKAVPacket> p0);
    
    void reset();
}
