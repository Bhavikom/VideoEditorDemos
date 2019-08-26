package org.lasque.tusdk.core.exif;

import android.util.SparseIntArray;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.lasque.tusdk.core.utils.TLog;

class ExifParser
{
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
  private static final short e = ExifInterface.getTrueTagKey(ExifInterface.TAG_EXIF_IFD);
  private static final short f = ExifInterface.getTrueTagKey(ExifInterface.TAG_GPS_IFD);
  private static final short g = ExifInterface.getTrueTagKey(ExifInterface.TAG_INTEROPERABILITY_IFD);
  private static final short h = ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT);
  private static final short i = ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
  private static final short j = ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS);
  private static final short k = ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS);
  private final int l;
  private final ExifInterface m;
  private final TreeMap<Integer, Object> n = new TreeMap();
  private final CountedDataInputStream o;
  private int p = 0;
  private int q = 0;
  private int r;
  private ExifTag s;
  private ImageEvent t;
  private ExifTag u;
  private ExifTag v;
  private boolean w;
  private byte[] x;
  private int y;
  private int z;
  private int A;
  private int B;
  private short C = 0;
  private List<Section> D;
  private int E = 0;
  static final int[] a = { 16, 11, 12, 14, 12, 10, 16, 14, 13, 14, 18, 17, 16, 19, 24, 40, 26, 24, 22, 22, 24, 49, 35, 37, 29, 40, 58, 51, 61, 60, 57, 51, 56, 55, 64, 72, 92, 78, 64, 68, 87, 69, 55, 56, 80, 109, 81, 87, 95, 98, 103, 104, 103, 62, 77, 113, 121, 112, 100, 120, 92, 101, 103, 99 };
  static final int[] b = { 17, 18, 18, 24, 21, 24, 47, 26, 26, 47, 99, 66, 56, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99 };
  static final int[][] c = { a, b };
  private final byte[] F = new byte[8];
  private final ByteBuffer G = ByteBuffer.wrap(this.F);
  
  private ExifParser(InputStream paramInputStream, int paramInt, ExifInterface paramExifInterface)
  {
    if (paramInputStream == null) {
      throw new IOException("Null argument inputStream to ExifParser");
    }
    TLog.i("%s Reading exif...", new Object[] { "ExifParser" });
    this.D = new ArrayList(0);
    this.m = paramExifInterface;
    this.o = a(paramInputStream);
    this.l = paramInt;
    if (this.o == null) {
      return;
    }
    a(this.o);
    long l1 = this.o.readUnsignedInt();
    if (l1 > 2147483647L) {
      throw new ExifInvalidFormatException("Invalid offset " + l1);
    }
    this.y = ((int)l1);
    this.r = 0;
    if ((a(0)) || (a()))
    {
      a(0, l1);
      if (l1 != 8L)
      {
        this.x = new byte[(int)l1 - 8];
        read(this.x);
      }
    }
  }
  
  private int b(byte[] paramArrayOfByte, int paramInt)
  {
    this.G.rewind();
    this.G.put(paramArrayOfByte, paramInt, 4);
    this.G.rewind();
    return this.G.getInt();
  }
  
  private short c(byte[] paramArrayOfByte, int paramInt)
  {
    this.G.rewind();
    this.G.put(paramArrayOfByte, paramInt, 2);
    this.G.rewind();
    return this.G.getShort();
  }
  
  private CountedDataInputStream a(InputStream paramInputStream)
  {
    CountedDataInputStream localCountedDataInputStream1 = new CountedDataInputStream(paramInputStream);
    CountedDataInputStream localCountedDataInputStream2 = null;
    int i1 = localCountedDataInputStream1.readUnsignedByte();
    int i2 = localCountedDataInputStream1.readUnsignedByte();
    if ((i1 != 255) || (i2 != 216))
    {
      TLog.e("%s invalid jpeg header", new Object[] { "ExifParser" });
      return null;
    }
    for (;;)
    {
      int i4 = 0;
      int i5;
      for (i1 = 0;; i1++)
      {
        i5 = localCountedDataInputStream1.readUnsignedByte();
        if ((i5 != 255) && (i4 == 255)) {
          break;
        }
        i4 = i5;
      }
      if (i1 > 10) {
        TLog.w("%s Extraneous %s padding bytes before section %s", new Object[] { "ExifParser", Integer.valueOf(i1 - 1), Integer.valueOf(i5) });
      }
      Section localSection = new Section();
      localSection.b = i5;
      int i7 = localCountedDataInputStream1.readByte();
      int i6 = localCountedDataInputStream1.readByte();
      int i3 = (i7 & 0xFF) << 8 | i6 & 0xFF;
      if (i3 < 2) {
        throw new ExifInvalidFormatException("Invalid marker");
      }
      localSection.a = i3;
      byte[] arrayOfByte = new byte[i3];
      arrayOfByte[0] = i7;
      arrayOfByte[1] = i6;
      int i8 = a(localCountedDataInputStream1, arrayOfByte, 2, i3 - 2);
      if (i8 != i3 - 2) {
        throw new ExifInvalidFormatException("Premature end of file? Expecting " + (i3 - 2) + ", received " + i8);
      }
      localSection.c = arrayOfByte;
      int i9 = 0;
      switch (i5)
      {
      case 218: 
        this.D.add(localSection);
        this.E = localCountedDataInputStream1.getReadByteCount();
        return localCountedDataInputStream2;
      case 219: 
        e(arrayOfByte, i3);
        break;
      case 196: 
        break;
      case 217: 
        TLog.w("%s No image in jpeg!", new Object[] { "ExifParser" });
        return null;
      case 254: 
        i9 = 1;
        break;
      case 224: 
        if (i3 < 16) {
          i9 = 1;
        }
        break;
      case 237: 
        break;
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
        d(arrayOfByte, i5);
        break;
      case 225: 
        if (i3 >= 8)
        {
          int i10 = b(arrayOfByte, 2);
          int i11 = c(arrayOfByte, 6);
          if ((i10 == 1165519206) && (i11 == 0))
          {
            localCountedDataInputStream2 = new CountedDataInputStream(new ByteArrayInputStream(arrayOfByte, 8, i3 - 8));
            localCountedDataInputStream2.setEnd(i3 - 6);
            i9 = 0;
          }
          else
          {
            TLog.i("%s Image cotains XMP section", new Object[] { "ExifParser" });
          }
        }
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
        TLog.w("%s Unknown marker: 0x%2X, length: %s", new Object[] { "ExifParser", Integer.valueOf(i5), Integer.valueOf(i3) });
      }
      if (i9 == 0) {
        this.D.add(localSection);
      } else {
        TLog.i("%s ignoring marker: 0x%2X, length: %s", new Object[] { "ExifParser", Integer.valueOf(i5), Integer.valueOf(i3) });
      }
    }
  }
  
  private int a(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i1 = 0;
    int i2;
    for (int i3 = Math.min(1024, paramInt2); 0 < (i2 = paramInputStream.read(paramArrayOfByte, paramInt1, i3)); i3 = Math.min(i3, paramInt2 - i1))
    {
      i1 += i2;
      paramInt1 += i2;
    }
    return i1;
  }
  
  static int a(byte[] paramArrayOfByte, int paramInt)
  {
    int i1 = (paramArrayOfByte[paramInt] & 0xFF) << 8;
    int i2 = paramArrayOfByte[(paramInt + 1)] & 0xFF;
    return i1 | i2;
  }
  
  private void d(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramArrayOfByte.length > 7)
    {
      this.B = a(paramArrayOfByte, 3);
      this.A = a(paramArrayOfByte, 5);
    }
    this.C = ((short)paramInt);
  }
  
  private void e(byte[] paramArrayOfByte, int paramInt)
  {
    int i1 = 2;
    double d1 = 0.0D;
    int[] arrayOfInt = null;
    int i5 = 1;
    while (i1 < paramArrayOfByte.length)
    {
      int i2 = paramArrayOfByte[(i1++)];
      int i3 = i2 & 0xF;
      if (i3 < 2) {
        arrayOfInt = c[i3];
      }
      for (int i4 = 0; i4 < 64; i4++)
      {
        int i6;
        if (i2 >> 4 != 0)
        {
          int i7 = paramArrayOfByte[(i1++)];
          i7 *= 256;
          i6 = paramArrayOfByte[(i1++)] + i7;
        }
        else
        {
          i6 = paramArrayOfByte[(i1++)];
        }
        if (arrayOfInt != null)
        {
          double d3 = 100.0D * i6 / arrayOfInt[i4];
          d1 += d3;
          if (i6 != 1) {
            i5 = 0;
          }
        }
      }
      if (arrayOfInt != null)
      {
        d1 /= 64.0D;
        double d2;
        if (i5 != 0) {
          d2 = 100.0D;
        } else if (d1 <= 100.0D) {
          d2 = (200.0D - d1) / 2.0D;
        } else {
          d2 = 5000.0D / d1;
        }
        if (i3 == 0) {
          this.z = ((int)(d2 + 0.5D));
        }
      }
    }
  }
  
  private void a(CountedDataInputStream paramCountedDataInputStream)
  {
    int i1 = paramCountedDataInputStream.readShort();
    if (18761 == i1) {
      paramCountedDataInputStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    } else if (19789 == i1) {
      paramCountedDataInputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
    } else {
      throw new ExifInvalidFormatException("Invalid TIFF header");
    }
    if (paramCountedDataInputStream.readShort() != 42) {
      throw new ExifInvalidFormatException("Invalid TIFF header");
    }
  }
  
  private boolean a(int paramInt)
  {
    switch (paramInt)
    {
    case 0: 
      return (this.l & 0x1) != 0;
    case 1: 
      return (this.l & 0x2) != 0;
    case 2: 
      return (this.l & 0x4) != 0;
    case 4: 
      return (this.l & 0x8) != 0;
    case 3: 
      return (this.l & 0x10) != 0;
    }
    return false;
  }
  
  private boolean a()
  {
    switch (this.r)
    {
    case 0: 
      return (a(2)) || (a(4)) || (a(3)) || (a(1));
    case 1: 
      return b();
    case 2: 
      return a(3);
    }
    return false;
  }
  
  private void a(int paramInt, long paramLong)
  {
    this.n.put(Integer.valueOf((int)paramLong), new IfdEvent(paramInt, a(paramInt)));
  }
  
  protected int read(byte[] paramArrayOfByte)
  {
    return this.o.read(paramArrayOfByte);
  }
  
  private boolean b()
  {
    return (this.l & 0x20) != 0;
  }
  
  protected static ExifParser parse(InputStream paramInputStream, int paramInt, ExifInterface paramExifInterface)
  {
    return new ExifParser(paramInputStream, paramInt, paramExifInterface);
  }
  
  protected int next()
  {
    if (null == this.o) {
      return 5;
    }
    int i1 = this.o.getReadByteCount();
    int i2 = this.p + 2 + 12 * this.q;
    if (i1 < i2)
    {
      this.s = c();
      if (this.s == null) {
        return next();
      }
      if (this.w) {
        a(this.s);
      }
      return 1;
    }
    if (i1 == i2) {
      if (this.r == 0)
      {
        long l1 = readUnsignedLong();
        if (((a(1)) || (b())) && (l1 != 0L)) {
          a(1, l1);
        }
      }
      else
      {
        int i3 = 4;
        if (this.n.size() > 0) {
          i3 = ((Integer)this.n.firstEntry().getKey()).intValue() - this.o.getReadByteCount();
        }
        if (i3 < 4)
        {
          TLog.w("%s Invalid size of link to next IFD: %s", new Object[] { "ExifParser", Integer.valueOf(i3) });
        }
        else
        {
          long l2 = readUnsignedLong();
          if (l2 != 0L) {
            TLog.w("%s Invalid link to next IFD: %s", new Object[] { "ExifParser", Long.valueOf(l2) });
          }
        }
      }
    }
    while (this.n.size() != 0)
    {
      Map.Entry localEntry = this.n.pollFirstEntry();
      Object localObject = localEntry.getValue();
      try
      {
        b(((Integer)localEntry.getKey()).intValue());
      }
      catch (IOException localIOException)
      {
        TLog.w("%s Failed to skip to data at: %s for %s, the file may be broken.", new Object[] { "ExifParser", localEntry.getKey(), localObject.getClass().getName() });
      }
      continue;
      if ((localObject instanceof IfdEvent))
      {
        this.r = ((IfdEvent)localObject).a;
        this.q = this.o.readUnsignedShort();
        this.p = ((Integer)localEntry.getKey()).intValue();
        if (this.q * 12 + this.p + 2 > this.o.getEnd())
        {
          TLog.w("%s Invalid size of IFD %s", new Object[] { "ExifParser", Integer.valueOf(this.r) });
          return 5;
        }
        this.w = a();
        if (((IfdEvent)localObject).b) {
          return 0;
        }
        skipRemainingTagsInCurrentIfd();
      }
      else
      {
        if ((localObject instanceof ImageEvent))
        {
          this.t = ((ImageEvent)localObject);
          return this.t.b;
        }
        ExifTagEvent localExifTagEvent = (ExifTagEvent)localObject;
        this.s = localExifTagEvent.a;
        if (this.s.getDataType() != 7)
        {
          readFullTagValue(this.s);
          a(this.s);
        }
        if (localExifTagEvent.b) {
          return 2;
        }
      }
    }
    return 5;
  }
  
  protected void skipRemainingTagsInCurrentIfd()
  {
    int i1 = this.p + 2 + 12 * this.q;
    int i2 = this.o.getReadByteCount();
    if (i2 > i1) {
      return;
    }
    if (this.w) {
      while (i2 < i1)
      {
        this.s = c();
        i2 += 12;
        if (this.s != null) {
          a(this.s);
        }
      }
    }
    b(i1);
    long l1 = readUnsignedLong();
    if ((this.r == 0) && ((a(1)) || (b())) && (l1 > 0L)) {
      a(1, l1);
    }
  }
  
  protected ExifTag getTag()
  {
    return this.s;
  }
  
  public int getTagCountInCurrentIfd()
  {
    return this.q;
  }
  
  protected int getCurrentIfd()
  {
    return this.r;
  }
  
  protected int getStripIndex()
  {
    return this.t.a;
  }
  
  protected int getStripSize()
  {
    if (this.u == null) {
      return 0;
    }
    return (int)this.u.getValueAt(0);
  }
  
  protected int getCompressedImageSize()
  {
    if (this.v == null) {
      return 0;
    }
    return (int)this.v.getValueAt(0);
  }
  
  private void b(int paramInt)
  {
    this.o.skipTo(paramInt);
    while ((!this.n.isEmpty()) && (((Integer)this.n.firstKey()).intValue() < paramInt)) {
      this.n.pollFirstEntry();
    }
  }
  
  protected void registerForTagValue(ExifTag paramExifTag)
  {
    if (paramExifTag.getOffset() >= this.o.getReadByteCount()) {
      this.n.put(Integer.valueOf(paramExifTag.getOffset()), new ExifTagEvent(paramExifTag, true));
    }
  }
  
  private void a(long paramLong)
  {
    this.n.put(Integer.valueOf((int)paramLong), new ImageEvent(3));
  }
  
  private void b(int paramInt, long paramLong)
  {
    this.n.put(Integer.valueOf((int)paramLong), new ImageEvent(4, paramInt));
  }
  
  private ExifTag c()
  {
    short s1 = this.o.readShort();
    short s2 = this.o.readShort();
    long l1 = this.o.readUnsignedInt();
    if (l1 > 2147483647L) {
      throw new ExifInvalidFormatException("Number of component is larger then Integer.MAX_VALUE");
    }
    if (!ExifTag.isValidType(s2))
    {
      TLog.w("%s Tag %04x: Invalid data type %d", new Object[] { "ExifParser", Short.valueOf(s1), Short.valueOf(s2) });
      this.o.skip(4L);
      return null;
    }
    ExifTag localExifTag = new ExifTag(s1, s2, (int)l1, this.r, (int)l1 != 0);
    int i1 = localExifTag.getDataSize();
    if (i1 > 4)
    {
      long l2 = this.o.readUnsignedInt();
      if (l2 > 2147483647L) {
        throw new ExifInvalidFormatException("offset is larger then Integer.MAX_VALUE");
      }
      if ((l2 < this.y) && (s2 == 7))
      {
        byte[] arrayOfByte = new byte[(int)l1];
        System.arraycopy(this.x, (int)l2 - 8, arrayOfByte, 0, (int)l1);
        localExifTag.setValue(arrayOfByte);
      }
      else
      {
        localExifTag.setOffset((int)l2);
      }
    }
    else
    {
      boolean bool = localExifTag.hasDefinedCount();
      localExifTag.setHasDefinedCount(false);
      readFullTagValue(localExifTag);
      localExifTag.setHasDefinedCount(bool);
      this.o.skip(4 - i1);
      localExifTag.setOffset(this.o.getReadByteCount() - 4);
    }
    return localExifTag;
  }
  
  private void a(ExifTag paramExifTag)
  {
    if (paramExifTag.getComponentCount() == 0) {
      return;
    }
    int i1 = paramExifTag.getTagId();
    int i2 = paramExifTag.getIfd();
    if ((i1 == e) && (checkAllowed(i2, ExifInterface.TAG_EXIF_IFD)))
    {
      if ((a(2)) || (a(3))) {
        a(2, paramExifTag.getValueAt(0));
      }
    }
    else if ((i1 == f) && (checkAllowed(i2, ExifInterface.TAG_GPS_IFD)))
    {
      if (a(4)) {
        a(4, paramExifTag.getValueAt(0));
      }
    }
    else if ((i1 == g) && (checkAllowed(i2, ExifInterface.TAG_INTEROPERABILITY_IFD)))
    {
      if (a(3)) {
        a(3, paramExifTag.getValueAt(0));
      }
    }
    else if ((i1 == h) && (checkAllowed(i2, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT)))
    {
      if (b()) {
        a(paramExifTag.getValueAt(0));
      }
    }
    else if ((i1 == i) && (checkAllowed(i2, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH)))
    {
      if (b()) {
        this.v = paramExifTag;
      }
    }
    else if ((i1 == j) && (checkAllowed(i2, ExifInterface.TAG_STRIP_OFFSETS)))
    {
      if (b()) {
        if (paramExifTag.hasValue()) {
          for (int i3 = 0; i3 < paramExifTag.getComponentCount(); i3++) {
            if (paramExifTag.getDataType() == 3) {
              b(i3, paramExifTag.getValueAt(i3));
            } else {
              b(i3, paramExifTag.getValueAt(i3));
            }
          }
        } else {
          this.n.put(Integer.valueOf(paramExifTag.getOffset()), new ExifTagEvent(paramExifTag, false));
        }
      }
    }
    else if ((i1 == k) && (checkAllowed(i2, ExifInterface.TAG_STRIP_BYTE_COUNTS)) && (b()) && (paramExifTag.hasValue())) {
      this.u = paramExifTag;
    }
  }
  
  public boolean isDefinedTag(int paramInt1, int paramInt2)
  {
    return this.m.getTagInfo().get(ExifInterface.defineTag(paramInt1, (short)paramInt2)) != 0;
  }
  
  public boolean checkAllowed(int paramInt1, int paramInt2)
  {
    int i1 = this.m.getTagInfo().get(paramInt2);
    if (i1 == 0) {
      return false;
    }
    return ExifInterface.isIfdAllowed(i1, paramInt1);
  }
  
  protected void readFullTagValue(ExifTag paramExifTag)
  {
    int i1 = paramExifTag.getDataType();
    int i2 = paramExifTag.getComponentCount();
    if (i2 >= 1711276032) {
      throw new IOException("size out of bounds");
    }
    if ((i1 == 2) || (i1 == 7) || (i1 == 1))
    {
      int i3 = paramExifTag.getComponentCount();
      if ((this.n.size() > 0) && (((Integer)this.n.firstEntry().getKey()).intValue() < this.o.getReadByteCount() + i3))
      {
        Object localObject2 = this.n.firstEntry().getValue();
        if ((localObject2 instanceof ImageEvent))
        {
          TLog.w("%s Thumbnail overlaps value for tag: %s\n", new Object[] { "ExifParser", paramExifTag.toString() });
          Map.Entry localEntry = this.n.pollFirstEntry();
          TLog.w("%s Invalid thumbnail offset: %s", new Object[] { "ExifParser", localEntry.getKey() });
        }
        else
        {
          if ((localObject2 instanceof IfdEvent)) {
            TLog.w("%s Ifd %s overlaps value for tag: %s \n", new Object[] { "ExifParser", Integer.valueOf(((IfdEvent)localObject2).a), paramExifTag.toString() });
          } else if ((localObject2 instanceof ExifTagEvent)) {
            TLog.w("%s Tag value for tag: \n%s overlaps value for tag: %s\n", new Object[] { "ExifParser", ((ExifTagEvent)localObject2).a.toString(), paramExifTag.toString() });
          }
          i3 = ((Integer)this.n.firstEntry().getKey()).intValue() - this.o.getReadByteCount();
          TLog.w("%s Invalid size of tag: \n%s setting count to: %s", new Object[] { "ExifParser", paramExifTag.toString(), Integer.valueOf(i3) });
          paramExifTag.forceSetComponentCount(i3);
        }
      }
    }
    Object localObject1;
    int i4;
    int i5;
    switch (paramExifTag.getDataType())
    {
    case 1: 
    case 7: 
      localObject1 = new byte[i2];
      read((byte[])localObject1);
      paramExifTag.setValue((byte[])localObject1);
      break;
    case 2: 
      paramExifTag.setValue(readString(i2));
      break;
    case 4: 
      localObject1 = new long[i2];
      i4 = 0;
      i5 = localObject1.length;
      while (i4 < i5)
      {
        localObject1[i4] = readUnsignedLong();
        i4++;
      }
      paramExifTag.setValue((long[])localObject1);
      break;
    case 5: 
      localObject1 = new Rational[i2];
      i4 = 0;
      i5 = localObject1.length;
      while (i4 < i5)
      {
        localObject1[i4] = readUnsignedRational();
        i4++;
      }
      paramExifTag.setValue((Rational[])localObject1);
      break;
    case 3: 
      localObject1 = new int[i2];
      i4 = 0;
      i5 = localObject1.length;
      while (i4 < i5)
      {
        localObject1[i4] = readUnsignedShort();
        i4++;
      }
      paramExifTag.setValue((int[])localObject1);
      break;
    case 9: 
      localObject1 = new int[i2];
      i4 = 0;
      i5 = localObject1.length;
      while (i4 < i5)
      {
        localObject1[i4] = readLong();
        i4++;
      }
      paramExifTag.setValue((int[])localObject1);
      break;
    case 10: 
      localObject1 = new Rational[i2];
      i4 = 0;
      i5 = localObject1.length;
      while (i4 < i5)
      {
        localObject1[i4] = readRational();
        i4++;
      }
      paramExifTag.setValue((Rational[])localObject1);
    }
  }
  
  protected int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return this.o.read(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  protected String readString(int paramInt)
  {
    return readString(paramInt, d);
  }
  
  protected String readString(int paramInt, Charset paramCharset)
  {
    if (paramInt > 0) {
      return this.o.readString(paramInt, paramCharset);
    }
    return "";
  }
  
  protected int readUnsignedShort()
  {
    return this.o.readShort() & 0xFFFF;
  }
  
  protected long readUnsignedLong()
  {
    return readLong() & 0xFFFFFFFF;
  }
  
  protected Rational readUnsignedRational()
  {
    long l1 = readUnsignedLong();
    long l2 = readUnsignedLong();
    return new Rational(l1, l2);
  }
  
  protected int readLong()
  {
    return this.o.readInt();
  }
  
  protected Rational readRational()
  {
    int i1 = readLong();
    int i2 = readLong();
    return new Rational(i1, i2);
  }
  
  protected ByteOrder getByteOrder()
  {
    if (null != this.o) {
      return this.o.getByteOrder();
    }
    return null;
  }
  
  public int getQualityGuess()
  {
    return this.z;
  }
  
  public int getImageWidth()
  {
    return this.A;
  }
  
  public short getJpegProcess()
  {
    return this.C;
  }
  
  public int getImageLength()
  {
    return this.B;
  }
  
  public List<Section> getSections()
  {
    return this.D;
  }
  
  public int getUncompressedDataPosition()
  {
    return this.E;
  }
  
  public static class Section
  {
    int a;
    int b;
    byte[] c;
  }
  
  private static class ExifTagEvent
  {
    ExifTag a;
    boolean b;
    
    ExifTagEvent(ExifTag paramExifTag, boolean paramBoolean)
    {
      this.a = paramExifTag;
      this.b = paramBoolean;
    }
  }
  
  private static class IfdEvent
  {
    int a;
    boolean b;
    
    IfdEvent(int paramInt, boolean paramBoolean)
    {
      this.a = paramInt;
      this.b = paramBoolean;
    }
  }
  
  private static class ImageEvent
  {
    int a;
    int b;
    
    ImageEvent(int paramInt)
    {
      this.a = 0;
      this.b = paramInt;
    }
    
    ImageEvent(int paramInt1, int paramInt2)
    {
      this.b = paramInt1;
      this.a = paramInt2;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\ExifParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */