// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

import java.io.IOException;
import java.nio.ByteOrder;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.io.FilterOutputStream;

class OrderedDataOutputStream extends FilterOutputStream
{
    private final ByteBuffer a;
    
    public OrderedDataOutputStream(final OutputStream out) {
        super(out);
        this.a = ByteBuffer.allocate(4);
    }
    
    public OrderedDataOutputStream setByteOrder(final ByteOrder bo) {
        this.a.order(bo);
        return this;
    }
    
    public OrderedDataOutputStream writeShort(final short n) {
        this.a.rewind();
        this.a.putShort(n);
        try {
            this.out.write(this.a.array(), 0, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public OrderedDataOutputStream writeRational(final Rational rational) {
        this.writeInt((int)rational.getNumerator());
        this.writeInt((int)rational.getDenominator());
        return this;
    }
    
    public OrderedDataOutputStream writeInt(final int n) {
        this.a.rewind();
        this.a.putInt(n);
        try {
            this.out.write(this.a.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
}
