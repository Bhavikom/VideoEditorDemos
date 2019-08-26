// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

class JpegHeader
{
    public static final int TAG_SOI = 216;
    public static final int TAG_M_JFIF = 224;
    public static final int TAG_M_EXIF = 225;
    public static final int TAG_M_COM = 254;
    public static final int TAG_M_DQT = 219;
    public static final int TAG_M_SOF0 = 192;
    public static final int TAG_M_SOF1 = 193;
    public static final int TAG_M_SOF2 = 194;
    public static final int TAG_M_SOF3 = 195;
    public static final int TAG_M_DHT = 196;
    public static final int TAG_M_SOF5 = 197;
    public static final int TAG_M_SOF6 = 198;
    public static final int TAG_M_SOF7 = 199;
    public static final int TAG_M_SOF9 = 201;
    public static final int TAG_M_SOF10 = 202;
    public static final int TAG_M_SOF11 = 203;
    public static final int TAG_M_SOF13 = 205;
    public static final int TAG_M_SOF14 = 206;
    public static final int TAG_M_SOF15 = 207;
    public static final int TAG_M_SOS = 218;
    public static final int TAG_M_EOI = 217;
    public static final int TAG_M_IPTC = 237;
    public static final byte[] JFIF_HEADER;
    public static final short SOI = -40;
    public static final short M_EXIF = -31;
    public static final short M_JFIF = -32;
    public static final short M_EOI = -39;
    public static final short M_SOF0 = -64;
    public static final short M_SOF1 = -63;
    public static final short M_SOF2 = -62;
    public static final short M_SOF3 = -61;
    public static final short M_SOF5 = -59;
    public static final short M_SOF6 = -58;
    public static final short M_SOF7 = -57;
    public static final short M_SOF9 = -55;
    public static final short M_SOF10 = -54;
    public static final short M_SOF11 = -53;
    public static final short M_SOF13 = -51;
    public static final short M_SOF14 = -50;
    public static final short M_SOF15 = -49;
    public static final short M_DHT = -60;
    public static final short JPG = -56;
    public static final short DAC = -52;
    public static final short M_DQT = -37;
    public static final short M_IPTC = -19;
    public static final short M_SOS = -38;
    public static final short M_COM = -2;
    
    public static final boolean isSofMarker(final short n) {
        return n >= -64 && n <= -49 && n != -60 && n != -56 && n != -52;
    }
    
    static {
        JFIF_HEADER = new byte[] { -1, -32, 0, 16, 74, 70, 73, 70, 0, 1, 1, 1, 1, 44, 1, 44, 0, 0 };
    }
}
