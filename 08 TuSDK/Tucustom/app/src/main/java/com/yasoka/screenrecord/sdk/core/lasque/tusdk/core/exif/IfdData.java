// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

import java.util.HashMap;
import java.util.Map;

class IfdData
{
    private static final int[] a;
    private final int b;
    private final Map<Short, ExifTag> c;
    private int d;
    
    IfdData(final int b) {
        this.c = new HashMap<Short, ExifTag>();
        this.d = 0;
        this.b = b;
    }
    
    protected static int[] getIfds() {
        return IfdData.a;
    }
    
    protected ExifTag getTag(final short s) {
        return this.c.get(s);
    }
    
    protected ExifTag setTag(final ExifTag exifTag) {
        exifTag.setIfd(this.b);
        return this.c.put(exifTag.getTagId(), exifTag);
    }
    
    protected boolean checkCollision(final short s) {
        return this.c.get(s) != null;
    }
    
    protected void removeTag(final short s) {
        this.c.remove(s);
    }
    
    protected int getOffsetToNextIfd() {
        return this.d;
    }
    
    protected void setOffsetToNextIfd(final int d) {
        this.d = d;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof IfdData) {
            final IfdData ifdData = (IfdData)o;
            if (ifdData.getId() == this.b && ifdData.getTagCount() == this.getTagCount()) {
                for (final ExifTag exifTag : ifdData.getAllTags()) {
                    if (!ExifInterface.isOffsetTag(exifTag.getTagId())) {
                        if (!exifTag.equals(this.c.get(exifTag.getTagId()))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    protected int getTagCount() {
        return this.c.size();
    }
    
    protected int getId() {
        return this.b;
    }
    
    protected ExifTag[] getAllTags() {
        return this.c.values().toArray(new ExifTag[this.c.size()]);
    }
    
    static {
        a = new int[] { 0, 1, 2, 3, 4 };
    }
}
