// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Map;
import java.nio.ByteOrder;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.TLog;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.TreeMap;
import java.nio.charset.Charset;

class ExifParser {
    public static final int EVENT_START_OF_IFD = 0;
    public static final int EVENT_NEW_TAG = 1;
    public static final int EVENT_VALUE_OF_REGISTERED_TAG = 2;
    public static final int EVENT_COMPRESSED_IMAGE = 3;
    public static final int EVENT_UNCOMPRESSED_STRIP = 4;
    public static final int EVENT_END = 5;
    protected static final int EXIF_HEADER = 1165519206;
    protected static final short EXIF_HEADER_TAIL = 0;
    protected static final short LITTLE_ENDIAN_TAG = 18761;
    protected static final short BIG_ENDIAN_TAG = 19789;
    protected static final short TIFF_HEADER_TAIL = 42;
    protected static final int TAG_SIZE = 12;
    protected static final int OFFSET_SIZE = 2;
    protected static final int DEFAULT_IFD0_OFFSET = 8;
    private static final Charset d = Charset.forName("US-ASCII");
    private static final short e;
    private static final short f;
    private static final short g;
    private static final short h;
    private static final short i;
    private static final short j;
    private static final short k;
    private int l = 0;
    private ExifInterface m = null;
    private final TreeMap<Integer, Object> n = new TreeMap();
    private CountedDataInputStream o =null;
    private int p = 0;
    private int q = 0;
    private int r;
    private ExifTag s;
    private ExifParser.ImageEvent t;
    private ExifTag u;
    private ExifTag v;
    private boolean w;
    private byte[] x;
    private int y;
    private int z;
    private int A;
    private int B;
    private short C = 0;
    private List<ExifParser.Section> D;
    private int E = 0;
    static final int[] a;
    static final int[] b;
    static final int[][] c;
    private final byte[] F = new byte[8];
    private final ByteBuffer G;

    private ExifParser(InputStream var1, int var2, ExifInterface var3) {
        this.G = ByteBuffer.wrap(this.F);
        if (var1 == null) {
            try {
                throw new IOException("Null argument inputStream to ExifParser");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            TLog.i("%s Reading exif...", new Object[]{"ExifParser"});
            this.D = new ArrayList(0);
            this.m = var3;
            this.o = this.a(var1);
            this.l = var2;
            if (this.o != null) {
                this.a(this.o);
                long var4 = this.o.readUnsignedInt();
                if (var4 > 2147483647L) {
                    try {
                        throw new ExifInvalidFormatException("Invalid offset " + var4);
                    } catch (ExifInvalidFormatException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    this.y = (int)var4;
                    this.r = 0;
                    if (this.a(0) || this.a()) {
                        this.a(0, var4);
                        if (var4 != 8L) {
                            this.x = new byte[(int)var4 - 8];
                            this.read(this.x);
                        }
                    }

                }
            }
        }
    }

    private int b(byte[] var1, int var2) {
        this.G.rewind();
        this.G.put(var1, var2, 4);
        this.G.rewind();
        return this.G.getInt();
    }

    private short c(byte[] var1, int var2) {
        this.G.rewind();
        this.G.put(var1, var2, 2);
        this.G.rewind();
        return this.G.getShort();
    }

    private CountedDataInputStream a(InputStream var1) {
        CountedDataInputStream var2 = new CountedDataInputStream(var1);
        CountedDataInputStream var3 = null;
        int var4 = var2.readUnsignedByte();
        int var5 = var2.readUnsignedByte();
        if (var4 == 255 && var5 == 216) {
            while(true) {
                int var7 = 0;
                var4 = 0;

                while(true) {
                    int var8 = var2.readUnsignedByte();
                    if (var8 != 255 && var7 == 255) {
                        if (var4 > 10) {
                            TLog.w("%s Extraneous %s padding bytes before section %s", new Object[]{"ExifParser", var4 - 1, var8});
                        }

                        ExifParser.Section var13 = new ExifParser.Section();
                        var13.b = var8;
                        byte var10 = var2.readByte();
                        byte var9 = var2.readByte();
                        int var6 = (var10 & 255) << 8 | var9 & 255;
                        if (var6 < 2) {
                            try {
                                throw new ExifInvalidFormatException("Invalid marker");
                            } catch (ExifInvalidFormatException e1) {
                                e1.printStackTrace();
                            }
                        }

                        var13.a = var6;
                        byte[] var12 = new byte[var6];
                        var12[0] = var10;
                        var12[1] = var9;
                        int var11 = 0;
                        try {
                            var11 = this.a(var2, var12, 2, var6 - 2);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        if (var11 != var6 - 2) {
                            try {
                                throw new ExifInvalidFormatException("Premature end of file? Expecting " + (var6 - 2) + ", received " + var11);
                            } catch (ExifInvalidFormatException e1) {
                                e1.printStackTrace();
                            }
                        }

                        var13.c = var12;
                        boolean var14 = false;
                        switch(var8) {
                            case 192:
                            case 193:
                            case 194:
                            case 195:
                            case 197:
                            case 198:
                            case 199:
                            case 201:
                            case 202:
                            case 203:
                            case 205:
                            case 206:
                            case 207:
                                this.d(var12, var8);
                            case 196:
                            case 237:
                                break;
                            case 200:
                            case 204:
                            case 208:
                            case 209:
                            case 210:
                            case 211:
                            case 212:
                            case 213:
                            case 214:
                            case 215:
                            case 216:
                            case 220:
                            case 221:
                            case 222:
                            case 223:
                            case 226:
                            case 227:
                            case 228:
                            case 229:
                            case 230:
                            case 231:
                            case 232:
                            case 233:
                            case 234:
                            case 235:
                            case 236:
                            case 238:
                            case 239:
                            case 240:
                            case 241:
                            case 242:
                            case 243:
                            case 244:
                            case 245:
                            case 246:
                            case 247:
                            case 248:
                            case 249:
                            case 250:
                            case 251:
                            case 252:
                            case 253:
                            default:
                                TLog.w("%s Unknown marker: 0x%2X, length: %s", new Object[]{"ExifParser", var8, var6});
                                break;
                            case 217:
                                TLog.w("%s No image in jpeg!", new Object[]{"ExifParser"});
                                return null;
                            case 218:
                                this.D.add(var13);
                                this.E = var2.getReadByteCount();
                                return var3;
                            case 219:
                                this.e(var12, var6);
                                break;
                            case 224:
                                if (var6 < 16) {
                                    var14 = true;
                                }
                                break;
                            case 225:
                                if (var6 >= 8) {
                                    int var15 = this.b(var12, 2);
                                    short var16 = this.c(var12, 6);
                                    if (var15 == 1165519206 && var16 == 0) {
                                        var3 = new CountedDataInputStream(new ByteArrayInputStream(var12, 8, var6 - 8));
                                        var3.setEnd(var6 - 6);
                                        var14 = false;
                                    } else {
                                        TLog.i("%s Image cotains XMP section", new Object[]{"ExifParser"});
                                    }
                                }
                                break;
                            case 254:
                                var14 = true;
                        }

                        if (!var14) {
                            this.D.add(var13);
                        } else {
                            TLog.i("%s ignoring marker: 0x%2X, length: %s", new Object[]{"ExifParser", var8, var6});
                        }
                        break;
                    }

                    var7 = var8;
                    ++var4;
                }
            }
        } else {
            TLog.e("%s invalid jpeg header", new Object[]{"ExifParser"});
            return null;
        }
    }

    private int a(InputStream var1, byte[] var2, int var3, int var4) throws IOException {
        int var5 = 0;

        int var6;
        for(int var7 = Math.min(1024, var4); 0 < (var6 = var1.read(var2, var3, var7)); var7 = Math.min(var7, var4 - var5)) {
            var5 += var6;
            var3 += var6;
        }

        return var5;
    }

    static int a(byte[] var0, int var1) {
        int var2 = (var0[var1] & 255) << 8;
        int var3 = var0[var1 + 1] & 255;
        return var2 | var3;
    }

    private void d(byte[] var1, int var2) {
        if (var1.length > 7) {
            this.B = a(var1, 3);
            this.A = a(var1, 5);
        }

        this.C = (short)var2;
    }

    private void e(byte[] var1, int var2) {
        int var3 = 2;
        double var7 = 0.0D;
        int[] var9 = null;
        boolean var10 = true;

        while(var3 < var1.length) {
            byte var4 = var1[var3++];
            int var5 = var4 & 15;
            if (var5 < 2) {
                var9 = c[var5];
            }

            for(int var6 = 0; var6 < 64; ++var6) {
                int var11;
                if (var4 >> 4 != 0) {
                    byte var12 = var1[var3++];
                    int var15 = var12 * 256;
                    var11 = var1[var3++] + var15;
                } else {
                    var11 = var1[var3++];
                }

                if (var9 != null) {
                    double var16 = 100.0D * (double)var11 / (double)var9[var6];
                    var7 += var16;
                    if (var11 != 1) {
                        var10 = false;
                    }
                }
            }

            if (var9 != null) {
                var7 /= 64.0D;
                double var14;
                if (var10) {
                    var14 = 100.0D;
                } else if (var7 <= 100.0D) {
                    var14 = (200.0D - var7) / 2.0D;
                } else {
                    var14 = 5000.0D / var7;
                }

                if (var5 == 0) {
                    this.z = (int)(var14 + 0.5D);
                }
            }
        }

    }

    private void a(CountedDataInputStream var1) {
        short var2 = var1.readShort();
        if (18761 == var2) {
            var1.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        } else {
            if (19789 != var2) {
                try {
                    throw new ExifInvalidFormatException("Invalid TIFF header");
                } catch (ExifInvalidFormatException e1) {
                    e1.printStackTrace();
                }
            }

            var1.setByteOrder(ByteOrder.BIG_ENDIAN);
        }

        if (var1.readShort() != 42) {
            try {
                throw new ExifInvalidFormatException("Invalid TIFF header");
            } catch (ExifInvalidFormatException e1) {
                e1.printStackTrace();
            }
        }
    }

    private boolean a(int var1) {
        switch(var1) {
            case 0:
                return (this.l & 1) != 0;
            case 1:
                return (this.l & 2) != 0;
            case 2:
                return (this.l & 4) != 0;
            case 3:
                return (this.l & 16) != 0;
            case 4:
                return (this.l & 8) != 0;
            default:
                return false;
        }
    }

    private boolean a() {
        switch(this.r) {
            case 0:
                return this.a(2) || this.a(4) || this.a(3) || this.a(1);
            case 1:
                return this.b();
            case 2:
                return this.a(3);
            default:
                return false;
        }
    }

    private void a(int var1, long var2) {
        this.n.put((int)var2, new ExifParser.IfdEvent(var1, this.a(var1)));
    }

    protected int read(byte[] var1) {
        return this.o.read(var1);
    }

    private boolean b() {
        return (this.l & 32) != 0;
    }

    protected static ExifParser parse(InputStream var0, int var1, ExifInterface var2) {
        return new ExifParser(var0, var1, var2);
    }

    protected int next() {
        if (null == this.o) {
            return 5;
        } else {
            int var1 = this.o.getReadByteCount();
            int var2 = this.p + 2 + 12 * this.q;
            if (var1 < var2) {
                this.s = this.c();
                if (this.s == null) {
                    return this.next();
                } else {
                    if (this.w) {
                        this.a(this.s);
                    }

                    return 1;
                }
            } else {
                if (var1 == var2) {
                    if (this.r == 0) {
                        long var3 = this.readUnsignedLong();
                        if ((this.a(1) || this.b()) && var3 != 0L) {
                            this.a(1, var3);
                        }
                    } else {
                        int var7 = 4;
                        if (this.n.size() > 0) {
                            var7 = (Integer)this.n.firstEntry().getKey() - this.o.getReadByteCount();
                        }

                        if (var7 < 4) {
                            TLog.w("%s Invalid size of link to next IFD: %s", new Object[]{"ExifParser", var7});
                        } else {
                            long var4 = this.readUnsignedLong();
                            if (var4 != 0L) {
                                TLog.w("%s Invalid link to next IFD: %s", new Object[]{"ExifParser", var4});
                            }
                        }
                    }
                }

                while(this.n.size() != 0) {
                    Map.Entry var8 = this.n.pollFirstEntry();
                    Object var9 = var8.getValue();

                    this.b((Integer)var8.getKey());

                    if (var9 instanceof ExifParser.IfdEvent) {
                        this.r = ((ExifParser.IfdEvent)var9).a;
                        this.q = this.o.readUnsignedShort();
                        this.p = (Integer)var8.getKey();
                        if (this.q * 12 + this.p + 2 > this.o.getEnd()) {
                            TLog.w("%s Invalid size of IFD %s", new Object[]{"ExifParser", this.r});
                            return 5;
                        }

                        this.w = this.a();
                        if (((ExifParser.IfdEvent)var9).b) {
                            return 0;
                        }

                        this.skipRemainingTagsInCurrentIfd();
                    } else {
                        if (var9 instanceof ExifParser.ImageEvent) {
                            this.t = (ExifParser.ImageEvent)var9;
                            return this.t.b;
                        }

                        ExifParser.ExifTagEvent var5 = (ExifParser.ExifTagEvent)var9;
                        this.s = var5.a;
                        if (this.s.getDataType() != 7) {
                            this.readFullTagValue(this.s);
                            this.a(this.s);
                        }

                        if (var5.b) {
                            return 2;
                        }
                    }
                }

                return 5;
            }
        }
    }

    protected void skipRemainingTagsInCurrentIfd() {
        int var1 = this.p + 2 + 12 * this.q;
        int var2 = this.o.getReadByteCount();
        if (var2 <= var1) {
            if (this.w) {
                while(var2 < var1) {
                    this.s = this.c();
                    var2 += 12;
                    if (this.s != null) {
                        this.a(this.s);
                    }
                }
            } else {
                this.b(var1);
            }

            long var3 = this.readUnsignedLong();
            if (this.r == 0 && (this.a(1) || this.b()) && var3 > 0L) {
                this.a(1, var3);
            }

        }
    }

    protected ExifTag getTag() {
        return this.s;
    }

    public int getTagCountInCurrentIfd() {
        return this.q;
    }

    protected int getCurrentIfd() {
        return this.r;
    }

    protected int getStripIndex() {
        return this.t.a;
    }

    protected int getStripSize() {
        return this.u == null ? 0 : (int)this.u.getValueAt(0);
    }

    protected int getCompressedImageSize() {
        return this.v == null ? 0 : (int)this.v.getValueAt(0);
    }

    private void b(int var1) {
        this.o.skipTo((long)var1);

        while(!this.n.isEmpty() && (Integer)this.n.firstKey() < var1) {
            this.n.pollFirstEntry();
        }

    }

    protected void registerForTagValue(ExifTag var1) {
        if (var1.getOffset() >= this.o.getReadByteCount()) {
            this.n.put(var1.getOffset(), new ExifParser.ExifTagEvent(var1, true));
        }

    }

    private void a(long var1) {
        this.n.put((int)var1, new ExifParser.ImageEvent(3));
    }

    private void b(int var1, long var2) {
        this.n.put((int)var2, new ExifParser.ImageEvent(4, var1));
    }

    private ExifTag c() {
        short var1 = this.o.readShort();
        short var2 = this.o.readShort();
        long var3 = this.o.readUnsignedInt();
        if (var3 > 2147483647L) {
            try {
                throw new ExifInvalidFormatException("Number of component is larger then Integer.MAX_VALUE");
            } catch (ExifInvalidFormatException e1) {
                e1.printStackTrace();
            }
        } else if (!ExifTag.isValidType(var2)) {
            TLog.w("%s Tag %04x: Invalid data type %d", new Object[]{"ExifParser", var1, var2});
            this.o.skip(4L);
            return null;
        } else {
            ExifTag var5 = new ExifTag(var1, var2, (int)var3, this.r, (int)var3 != 0);
            int var6 = var5.getDataSize();
            if (var6 > 4) {
                long var7 = this.o.readUnsignedInt();
                if (var7 > 2147483647L) {
                    try {
                        throw new ExifInvalidFormatException("offset is larger then Integer.MAX_VALUE");
                    } catch (ExifInvalidFormatException e1) {
                        e1.printStackTrace();
                    }
                }

                if (var7 < (long)this.y && var2 == 7) {
                    byte[] var9 = new byte[(int)var3];
                    System.arraycopy(this.x, (int)var7 - 8, var9, 0, (int)var3);
                    var5.setValue(var9);
                } else {
                    var5.setOffset((int)var7);
                }
            } else {
                boolean var10 = var5.hasDefinedCount();
                var5.setHasDefinedCount(false);
                this.readFullTagValue(var5);
                var5.setHasDefinedCount(var10);
                this.o.skip((long)(4 - var6));
                var5.setOffset(this.o.getReadByteCount() - 4);
            }

            return var5;
        }
        return null;
    }

    private void a(ExifTag var1) {
        if (var1.getComponentCount() != 0) {
            short var2 = var1.getTagId();
            int var3 = var1.getIfd();
            if (var2 == e && this.checkAllowed(var3, ExifInterface.TAG_EXIF_IFD)) {
                if (this.a(2) || this.a(3)) {
                    this.a(2, var1.getValueAt(0));
                }
            } else if (var2 == f && this.checkAllowed(var3, ExifInterface.TAG_GPS_IFD)) {
                if (this.a(4)) {
                    this.a(4, var1.getValueAt(0));
                }
            } else if (var2 == g && this.checkAllowed(var3, ExifInterface.TAG_INTEROPERABILITY_IFD)) {
                if (this.a(3)) {
                    this.a(3, var1.getValueAt(0));
                }
            } else if (var2 == h && this.checkAllowed(var3, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT)) {
                if (this.b()) {
                    this.a(var1.getValueAt(0));
                }
            } else if (var2 == i && this.checkAllowed(var3, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH)) {
                if (this.b()) {
                    this.v = var1;
                }
            } else if (var2 == j && this.checkAllowed(var3, ExifInterface.TAG_STRIP_OFFSETS)) {
                if (this.b()) {
                    if (var1.hasValue()) {
                        for(int var4 = 0; var4 < var1.getComponentCount(); ++var4) {
                            if (var1.getDataType() == 3) {
                                this.b(var4, var1.getValueAt(var4));
                            } else {
                                this.b(var4, var1.getValueAt(var4));
                            }
                        }
                    } else {
                        this.n.put(var1.getOffset(), new ExifParser.ExifTagEvent(var1, false));
                    }
                }
            } else if (var2 == k && this.checkAllowed(var3, ExifInterface.TAG_STRIP_BYTE_COUNTS) && this.b() && var1.hasValue()) {
                this.u = var1;
            }

        }
    }

    public boolean isDefinedTag(int var1, int var2) {
        return this.m.getTagInfo().get(ExifInterface.defineTag(var1, (short)var2)) != 0;
    }

    public boolean checkAllowed(int var1, int var2) {
        int var3 = this.m.getTagInfo().get(var2);
        return var3 == 0 ? false : ExifInterface.isIfdAllowed(var3, var1);
    }

    protected void readFullTagValue(ExifTag var1) {
        short var2 = var1.getDataType();
        int var3 = var1.getComponentCount();
        if (var3 >= 1711276032) {
            try {
                throw new IOException("size out of bounds");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            if (var2 == 2 || var2 == 7 || var2 == 1) {
                int var4 = var1.getComponentCount();
                if (this.n.size() > 0 && (Integer)this.n.firstEntry().getKey() < this.o.getReadByteCount() + var4) {
                    Object var5 = this.n.firstEntry().getValue();
                    if (var5 instanceof ExifParser.ImageEvent) {
                        TLog.w("%s Thumbnail overlaps value for tag: %s\n", new Object[]{"ExifParser", var1.toString()});
                        Map.Entry var6 = this.n.pollFirstEntry();
                        TLog.w("%s Invalid thumbnail offset: %s", new Object[]{"ExifParser", var6.getKey()});
                    } else {
                        if (var5 instanceof ExifParser.IfdEvent) {
                            TLog.w("%s Ifd %s overlaps value for tag: %s \n", new Object[]{"ExifParser", ((ExifParser.IfdEvent)var5).a, var1.toString()});
                        } else if (var5 instanceof ExifParser.ExifTagEvent) {
                            TLog.w("%s Tag value for tag: \n%s overlaps value for tag: %s\n", new Object[]{"ExifParser", ((ExifParser.ExifTagEvent)var5).a.toString(), var1.toString()});
                        }

                        var4 = (Integer)this.n.firstEntry().getKey() - this.o.getReadByteCount();
                        TLog.w("%s Invalid size of tag: \n%s setting count to: %s", new Object[]{"ExifParser", var1.toString(), var4});
                        var1.forceSetComponentCount(var4);
                    }
                }
            }

            Rational[] var7;
            int[] var8;
            int var9;
            int var12;
            switch(var1.getDataType()) {
                case 1:
                case 7:
                    byte[] var11 = new byte[var3];
                    this.read(var11);
                    var1.setValue(var11);
                    break;
                case 2:
                    var1.setValue(this.readString(var3));
                    break;
                case 3:
                    var8 = new int[var3];
                    var9 = 0;

                    for(var12 = var8.length; var9 < var12; ++var9) {
                        var8[var9] = this.readUnsignedShort();
                    }

                    var1.setValue(var8);
                    break;
                case 4:
                    long[] var10 = new long[var3];
                    var9 = 0;

                    for(var12 = var10.length; var9 < var12; ++var9) {
                        var10[var9] = this.readUnsignedLong();
                    }

                    var1.setValue(var10);
                    break;
                case 5:
                    var7 = new Rational[var3];
                    var9 = 0;

                    for(var12 = var7.length; var9 < var12; ++var9) {
                        var7[var9] = this.readUnsignedRational();
                    }

                    var1.setValue(var7);
                case 6:
                case 8:
                default:
                    break;
                case 9:
                    var8 = new int[var3];
                    var9 = 0;

                    for(var12 = var8.length; var9 < var12; ++var9) {
                        var8[var9] = this.readLong();
                    }

                    var1.setValue(var8);
                    break;
                case 10:
                    var7 = new Rational[var3];
                    var9 = 0;

                    for(var12 = var7.length; var9 < var12; ++var9) {
                        var7[var9] = this.readRational();
                    }

                    var1.setValue(var7);
            }

        }
    }

    protected int read(byte[] var1, int var2, int var3) {
        return this.o.read(var1, var2, var3);
    }

    protected String readString(int var1) {
        return this.readString(var1, d);
    }

    protected String readString(int var1, Charset var2) {
        return var1 > 0 ? this.o.readString(var1, var2) : "";
    }

    protected int readUnsignedShort() {
        return this.o.readShort() & '\uffff';
    }

    protected long readUnsignedLong() {
        return (long)this.readLong() & 4294967295L;
    }

    protected Rational readUnsignedRational() {
        long var1 = this.readUnsignedLong();
        long var3 = this.readUnsignedLong();
        return new Rational(var1, var3);
    }

    protected int readLong() {
        return this.o.readInt();
    }

    protected Rational readRational() {
        int var1 = this.readLong();
        int var2 = this.readLong();
        return new Rational((long)var1, (long)var2);
    }

    protected ByteOrder getByteOrder() {
        return null != this.o ? this.o.getByteOrder() : null;
    }

    public int getQualityGuess() {
        return this.z;
    }

    public int getImageWidth() {
        return this.A;
    }

    public short getJpegProcess() {
        return this.C;
    }

    public int getImageLength() {
        return this.B;
    }

    public List<ExifParser.Section> getSections() {
        return this.D;
    }

    public int getUncompressedDataPosition() {
        return this.E;
    }

    static {
        e = ExifInterface.getTrueTagKey(ExifInterface.TAG_EXIF_IFD);
        f = ExifInterface.getTrueTagKey(ExifInterface.TAG_GPS_IFD);
        g = ExifInterface.getTrueTagKey(ExifInterface.TAG_INTEROPERABILITY_IFD);
        h = ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT);
        i = ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        j = ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS);
        k = ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS);
        a = new int[]{16, 11, 12, 14, 12, 10, 16, 14, 13, 14, 18, 17, 16, 19, 24, 40, 26, 24, 22, 22, 24, 49, 35, 37, 29, 40, 58, 51, 61, 60, 57, 51, 56, 55, 64, 72, 92, 78, 64, 68, 87, 69, 55, 56, 80, 109, 81, 87, 95, 98, 103, 104, 103, 62, 77, 113, 121, 112, 100, 120, 92, 101, 103, 99};
        b = new int[]{17, 18, 18, 24, 21, 24, 47, 26, 26, 47, 99, 66, 56, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99};
        c = new int[][]{a, b};
    }

    public static class Section {
        int a;
        int b;
        byte[] c;

        public Section() {
        }
    }

    private static class ExifTagEvent {
        ExifTag a;
        boolean b;

        ExifTagEvent(ExifTag var1, boolean var2) {
            this.a = var1;
            this.b = var2;
        }
    }

    private static class IfdEvent {
        int a;
        boolean b;

        IfdEvent(int var1, boolean var2) {
            this.a = var1;
            this.b = var2;
        }
    }

    private static class ImageEvent {
        int a;
        int b;

        ImageEvent(int var1) {
            this.a = 0;
            this.b = var1;
        }

        ImageEvent(int var1, int var2) {
            this.b = var1;
            this.a = var2;
        }
    }
}
