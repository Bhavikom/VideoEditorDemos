// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.io.UnsupportedEncodingException;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.Arrays;
import java.util.ArrayList;
import java.nio.ByteOrder;
import java.util.List;

class ExifData
{
    private static final byte[] a;
    private static final byte[] b;
    private static final byte[] c;
    private List<ExifParser.Section> d;
    private final IfdData[] e;
    private final ByteOrder f;
    private byte[] g;
    private ArrayList<byte[]> h;
    private int i;
    private int j;
    private int k;
    private short l;
    public int mUncompressedDataPosition;
    
    ExifData(final ByteOrder f) {
        this.e = new IfdData[5];
        this.h = new ArrayList<byte[]>();
        this.i = 0;
        this.j = -1;
        this.k = -1;
        this.l = 0;
        this.mUncompressedDataPosition = 0;
        this.f = f;
    }
    
    protected byte[] getCompressedThumbnail() {
        return this.g;
    }
    
    protected void setCompressedThumbnail(final byte[] g) {
        this.g = g;
    }
    
    protected boolean hasCompressedThumbnail() {
        return this.g != null;
    }
    
    protected void setStripBytes(final int index, final byte[] array) {
        if (index < this.h.size()) {
            this.h.set(index, array);
        }
        else {
            for (int i = this.h.size(); i < index; ++i) {
                this.h.add(null);
            }
            this.h.add(array);
        }
    }
    
    protected int getStripCount() {
        return this.h.size();
    }
    
    protected byte[] getStrip(final int index) {
        return this.h.get(index);
    }
    
    protected boolean hasUncompressedStrip() {
        return this.h.size() != 0;
    }
    
    protected ByteOrder getByteOrder() {
        return this.f;
    }
    
    protected void addIfdData(final IfdData ifdData) {
        this.e[ifdData.getId()] = ifdData;
    }
    
    protected ExifTag getTag(final short n, final int n2) {
        final IfdData ifdData = this.e[n2];
        return (ifdData == null) ? null : ifdData.getTag(n);
    }
    
    protected ExifTag addTag(final ExifTag exifTag) {
        if (exifTag != null) {
            return this.addTag(exifTag, exifTag.getIfd());
        }
        return null;
    }
    
    protected ExifTag addTag(final ExifTag tag, final int n) {
        if (tag != null && ExifTag.isValidIfd(n)) {
            return this.getOrCreateIfdData(n).setTag(tag);
        }
        return null;
    }
    
    protected IfdData getOrCreateIfdData(final int n) {
        IfdData ifdData = this.e[n];
        if (ifdData == null) {
            ifdData = new IfdData(n);
            this.e[n] = ifdData;
        }
        return ifdData;
    }
    
    protected void removeThumbnailData() {
        this.clearThumbnailAndStrips();
        this.e[1] = null;
    }
    
    protected void clearThumbnailAndStrips() {
        this.g = null;
        this.h.clear();
    }
    
    protected void removeTag(final short n, final int n2) {
        final IfdData ifdData = this.e[n2];
        if (ifdData == null) {
            return;
        }
        ifdData.removeTag(n);
    }
    
    protected String getUserComment() {
        final IfdData ifdData = this.e[0];
        if (ifdData == null) {
            return null;
        }
        final ExifTag tag = ifdData.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_USER_COMMENT));
        if (tag == null) {
            return null;
        }
        if (tag.getComponentCount() < 8) {
            return null;
        }
        final byte[] bytes = new byte[tag.getComponentCount()];
        tag.getBytes(bytes);
        final byte[] a = new byte[8];
        System.arraycopy(bytes, 0, a, 0, 8);
        try {
            if (Arrays.equals(a, ExifData.a)) {
                return new String(bytes, 8, bytes.length - 8, "US-ASCII");
            }
            if (Arrays.equals(a, ExifData.b)) {
                return new String(bytes, 8, bytes.length - 8, "EUC-JP");
            }
            if (Arrays.equals(a, ExifData.c)) {
                return new String(bytes, 8, bytes.length - 8, "UTF-16");
            }
            return null;
        }
        catch (UnsupportedEncodingException ex) {
            TLog.w("%s Failed to decode the user comment", "ExifData");
            return null;
        }
    }
    
    protected List<ExifTag> getAllTags() {
        final ArrayList<ExifTag> list = new ArrayList<ExifTag>();
        for (final IfdData ifdData : this.e) {
            if (ifdData != null) {
                final ExifTag[] allTags = ifdData.getAllTags();
                if (allTags != null) {
                    final ExifTag[] array = allTags;
                    for (int length2 = array.length, j = 0; j < length2; ++j) {
                        list.add(array[j]);
                    }
                }
            }
        }
        if (list.size() == 0) {
            return null;
        }
        return list;
    }
    
    protected List<ExifTag> getAllTagsForIfd(final int n) {
        final IfdData ifdData = this.e[n];
        if (ifdData == null) {
            return null;
        }
        final ExifTag[] allTags = ifdData.getAllTags();
        if (allTags == null) {
            return null;
        }
        final ArrayList list = new ArrayList<ExifTag>(allTags.length);
        final ExifTag[] array = allTags;
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(array[i]);
        }
        if (list.size() == 0) {
            return null;
        }
        return (List<ExifTag>)list;
    }
    
    protected List<ExifTag> getAllTagsForTagId(final short n) {
        final ArrayList<ExifTag> list = new ArrayList<ExifTag>();
        for (final IfdData ifdData : this.e) {
            if (ifdData != null) {
                final ExifTag tag = ifdData.getTag(n);
                if (tag != null) {
                    list.add(tag);
                }
            }
        }
        if (list.size() == 0) {
            return null;
        }
        return list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof ExifData)) {
            return false;
        }
        final ExifData exifData = (ExifData)o;
        if (exifData.f != this.f || exifData.h.size() != this.h.size() || !Arrays.equals(exifData.g, this.g)) {
            return false;
        }
        for (int i = 0; i < this.h.size(); ++i) {
            if (!Arrays.equals(exifData.h.get(i), this.h.get(i))) {
                return false;
            }
        }
        for (int j = 0; j < 5; ++j) {
            final IfdData ifdData = exifData.getIfdData(j);
            final IfdData ifdData2 = this.getIfdData(j);
            if (ifdData != ifdData2 && ifdData != null && !ifdData.equals(ifdData2)) {
                return false;
            }
        }
        return true;
    }
    
    protected IfdData getIfdData(final int n) {
        if (ExifTag.isValidIfd(n)) {
            return this.e[n];
        }
        return null;
    }
    
    protected void setQualityGuess(final int i) {
        this.i = i;
    }
    
    public int getQualityGuess() {
        return this.i;
    }
    
    protected void setImageSize(final int k, final int j) {
        this.k = k;
        this.j = j;
    }
    
    public int[] getImageSize() {
        return new int[] { this.k, this.j };
    }
    
    public void setJpegProcess(final short l) {
        this.l = l;
    }
    
    public short getJpegProcess() {
        return this.l;
    }
    
    public void setSections(final List<ExifParser.Section> d) {
        this.d = d;
    }
    
    public List<ExifParser.Section> getSections() {
        return this.d;
    }
    
    static {
        a = new byte[] { 65, 83, 67, 73, 73, 0, 0, 0 };
        b = new byte[] { 74, 73, 83, 0, 0, 0, 0, 0 };
        c = new byte[] { 85, 78, 73, 67, 79, 68, 69, 0 };
    }
}
