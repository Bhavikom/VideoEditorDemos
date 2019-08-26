// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Iterator;
import java.util.ArrayList;
import java.nio.ByteOrder;
import java.io.BufferedOutputStream;
import java.io.IOException;
//import org.lasque.tusdk.core.utils.TLog;
import java.io.OutputStream;
import java.nio.ByteBuffer;

class ExifOutputStream
{
    private final ExifInterface a;
    private ExifData b;
    private ByteBuffer c;
    
    protected ExifOutputStream(final ExifInterface a) {
        this.c = ByteBuffer.allocate(4);
        this.a = a;
    }
    
    protected ExifData getExifData() {
        return this.b;
    }
    
    protected void setExifData(final ExifData b) {
        this.b = b;
    }
    
    public void writeExifData(final OutputStream out) {
        if (this.b == null) {
            return;
        }
        TLog.i("%s Writing exif data...", "ExifOutputStream");
        final ArrayList<ExifTag> a = this.a(this.b);
        this.a();
        final int b = this.b();
        if (b + 8 > 65535) {
            try {
                throw new IOException("Exif header is too large (>64Kb)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final OrderedDataOutputStream orderedDataOutputStream = new OrderedDataOutputStream(new BufferedOutputStream(out, 65536));
        orderedDataOutputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        try {
            orderedDataOutputStream.write(255);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            orderedDataOutputStream.write(225);
        } catch (IOException e) {
            e.printStackTrace();
        }
        orderedDataOutputStream.writeShort((short)(b + 8));
        orderedDataOutputStream.writeInt(1165519206);
        orderedDataOutputStream.writeShort((short)0);
        if (this.b.getByteOrder() == ByteOrder.BIG_ENDIAN) {
            orderedDataOutputStream.writeShort((short)19789);
        }
        else {
            orderedDataOutputStream.writeShort((short)18761);
        }
        orderedDataOutputStream.setByteOrder(this.b.getByteOrder());
        orderedDataOutputStream.writeShort((short)42);
        orderedDataOutputStream.writeInt(8);
        this.b(orderedDataOutputStream);
        this.a(orderedDataOutputStream);
        final Iterator<ExifTag> iterator = a.iterator();
        while (iterator.hasNext()) {
            this.b.addTag(iterator.next());
        }
        try {
            orderedDataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private ArrayList<ExifTag> a(final ExifData exifData) {
        final ArrayList<ExifTag> list = new ArrayList<ExifTag>();
        for (final ExifTag e : exifData.getAllTags()) {
            if (e.getValue() == null && !ExifInterface.isOffsetTag(e.getTagId())) {
                exifData.removeTag(e.getTagId(), e.getIfd());
                list.add(e);
            }
        }
        return list;
    }
    
    private void a(final OrderedDataOutputStream orderedDataOutputStream) {
        if (this.b.hasCompressedThumbnail()) {
            TLog.d("%s writing thumbnail..", "ExifOutputStream");
            try {
                orderedDataOutputStream.write(this.b.getCompressedThumbnail());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (this.b.hasUncompressedStrip()) {
            TLog.d("%s writing uncompressed strip..", "ExifOutputStream");
            for (int i = 0; i < this.b.getStripCount(); ++i) {
                try {
                    orderedDataOutputStream.write(this.b.getStrip(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void b(final OrderedDataOutputStream orderedDataOutputStream) {
        this.a(this.b.getIfdData(0), orderedDataOutputStream);
        this.a(this.b.getIfdData(2), orderedDataOutputStream);
        final IfdData ifdData = this.b.getIfdData(3);
        if (ifdData != null) {
            this.a(ifdData, orderedDataOutputStream);
        }
        final IfdData ifdData2 = this.b.getIfdData(4);
        if (ifdData2 != null) {
            this.a(ifdData2, orderedDataOutputStream);
        }
        if (this.b.getIfdData(1) != null) {
            this.a(this.b.getIfdData(1), orderedDataOutputStream);
        }
    }
    
    private void a(final IfdData ifdData, final OrderedDataOutputStream orderedDataOutputStream) {
        final ExifTag[] allTags = ifdData.getAllTags();
        orderedDataOutputStream.writeShort((short)allTags.length);
        for (final ExifTag exifTag : allTags) {
            orderedDataOutputStream.writeShort(exifTag.getTagId());
            orderedDataOutputStream.writeShort(exifTag.getDataType());
            orderedDataOutputStream.writeInt(exifTag.getComponentCount());
            if (exifTag.getDataSize() > 4) {
                orderedDataOutputStream.writeInt(exifTag.getOffset());
            }
            else {
                a(exifTag, orderedDataOutputStream);
                for (int j = 0; j < 4 - exifTag.getDataSize(); ++j) {
                    try {
                        orderedDataOutputStream.write(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        orderedDataOutputStream.writeInt(ifdData.getOffsetToNextIfd());
        for (final ExifTag exifTag2 : allTags) {
            if (exifTag2.getDataSize() > 4) {
                a(exifTag2, orderedDataOutputStream);
            }
        }
    }
    
    private int a(final IfdData ifdData, int offset) {
        offset += 2 + ifdData.getTagCount() * 12 + 4;
        for (final ExifTag exifTag : ifdData.getAllTags()) {
            if (exifTag.getDataSize() > 4) {
                exifTag.setOffset(offset);
                offset += exifTag.getDataSize();
            }
        }
        return offset;
    }
    
    private void a() {
        IfdData ifdData = this.b.getIfdData(0);
        if (ifdData == null) {
            ifdData = new IfdData(0);
            this.b.addIfdData(ifdData);
        }
        final ExifTag buildUninitializedTag = this.a.buildUninitializedTag(ExifInterface.TAG_EXIF_IFD);
        if (buildUninitializedTag == null) {
            try {
                throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_EXIF_IFD);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ifdData.setTag(buildUninitializedTag);
        IfdData ifdData2 = this.b.getIfdData(2);
        if (ifdData2 == null) {
            ifdData2 = new IfdData(2);
            this.b.addIfdData(ifdData2);
        }
        if (this.b.getIfdData(4) != null) {
            final ExifTag buildUninitializedTag2 = this.a.buildUninitializedTag(ExifInterface.TAG_GPS_IFD);
            if (buildUninitializedTag2 == null) {
                try {
                    throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_GPS_IFD);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ifdData.setTag(buildUninitializedTag2);
        }
        if (this.b.getIfdData(3) != null) {
            final ExifTag buildUninitializedTag3 = this.a.buildUninitializedTag(ExifInterface.TAG_INTEROPERABILITY_IFD);
            if (buildUninitializedTag3 == null) {
                try {
                    throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_INTEROPERABILITY_IFD);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ifdData2.setTag(buildUninitializedTag3);
        }
        IfdData ifdData3 = this.b.getIfdData(1);
        if (this.b.hasCompressedThumbnail()) {
            if (ifdData3 == null) {
                ifdData3 = new IfdData(1);
                this.b.addIfdData(ifdData3);
            }
            final ExifTag buildUninitializedTag4 = this.a.buildUninitializedTag(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT);
            if (buildUninitializedTag4 == null) {
                try {
                    throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ifdData3.setTag(buildUninitializedTag4);
            final ExifTag buildUninitializedTag5 = this.a.buildUninitializedTag(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
            if (buildUninitializedTag5 == null) {
                try {
                    throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            buildUninitializedTag5.setValue(this.b.getCompressedThumbnail().length);
            ifdData3.setTag(buildUninitializedTag5);
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS));
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS));
        }
        else if (this.b.hasUncompressedStrip()) {
            if (ifdData3 == null) {
                ifdData3 = new IfdData(1);
                this.b.addIfdData(ifdData3);
            }
            final int stripCount = this.b.getStripCount();
            final ExifTag buildUninitializedTag6 = this.a.buildUninitializedTag(ExifInterface.TAG_STRIP_OFFSETS);
            if (buildUninitializedTag6 == null) {
                try {
                    throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_STRIP_OFFSETS);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            final ExifTag buildUninitializedTag7 = this.a.buildUninitializedTag(ExifInterface.TAG_STRIP_BYTE_COUNTS);
            if (buildUninitializedTag7 == null) {
                try {
                    throw new IOException("No definition for crucial exif tag: " + ExifInterface.TAG_STRIP_BYTE_COUNTS);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            final long[] value = new long[stripCount];
            for (int i = 0; i < this.b.getStripCount(); ++i) {
                value[i] = this.b.getStrip(i).length;
            }
            buildUninitializedTag7.setValue(value);
            ifdData3.setTag(buildUninitializedTag6);
            ifdData3.setTag(buildUninitializedTag7);
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT));
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH));
        }
        else if (ifdData3 != null) {
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS));
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS));
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT));
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH));
        }
    }
    
    private int b() {
        final int n = 8;
        final IfdData ifdData = this.b.getIfdData(0);
        final int a = this.a(ifdData, n);
        ifdData.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_EXIF_IFD)).setValue(a);
        final IfdData ifdData2 = this.b.getIfdData(2);
        int n2 = this.a(ifdData2, a);
        final IfdData ifdData3 = this.b.getIfdData(3);
        if (ifdData3 != null) {
            ifdData2.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_INTEROPERABILITY_IFD)).setValue(n2);
            n2 = this.a(ifdData3, n2);
        }
        final IfdData ifdData4 = this.b.getIfdData(4);
        if (ifdData4 != null) {
            ifdData.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_GPS_IFD)).setValue(n2);
            n2 = this.a(ifdData4, n2);
        }
        final IfdData ifdData5 = this.b.getIfdData(1);
        if (ifdData5 != null) {
            ifdData.setOffsetToNextIfd(n2);
            n2 = this.a(ifdData5, n2);
        }
        if (this.b.hasCompressedThumbnail()) {
            ifdData5.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT)).setValue(n2);
            n2 += this.b.getCompressedThumbnail().length;
        }
        else if (this.b.hasUncompressedStrip()) {
            final long[] value = new long[this.b.getStripCount()];
            for (int i = 0; i < this.b.getStripCount(); ++i) {
                value[i] = n2;
                n2 += this.b.getStrip(i).length;
            }
            ifdData5.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS)).setValue(value);
        }
        return n2;
    }
    
    static void a(final ExifTag exifTag, final OrderedDataOutputStream orderedDataOutputStream) {
        switch (exifTag.getDataType()) {
            case 2: {
                final byte[] stringByte = exifTag.getStringByte();
                if (stringByte.length == exifTag.getComponentCount()) {
                    stringByte[stringByte.length - 1] = 0;
                    try {
                        orderedDataOutputStream.write(stringByte);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                try {
                    orderedDataOutputStream.write(stringByte);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    orderedDataOutputStream.write(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 4:
            case 9: {
                for (int i = 0; i < exifTag.getComponentCount(); ++i) {
                    orderedDataOutputStream.writeInt((int)exifTag.getValueAt(i));
                }
                break;
            }
            case 5:
            case 10: {
                for (int j = 0; j < exifTag.getComponentCount(); ++j) {
                    orderedDataOutputStream.writeRational(exifTag.getRational(j));
                }
                break;
            }
            case 1:
            case 7: {
                final byte[] b = new byte[exifTag.getComponentCount()];
                exifTag.getBytes(b);
                try {
                    orderedDataOutputStream.write(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 3: {
                for (int k = 0; k < exifTag.getComponentCount(); ++k) {
                    orderedDataOutputStream.writeShort((short)exifTag.getValueAt(k));
                }
                break;
            }
        }
    }
}
