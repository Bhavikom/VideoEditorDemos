// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.ByteOrder;
import java.io.EOFException;
import android.annotation.SuppressLint;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.io.FilterInputStream;

class CountedDataInputStream extends FilterInputStream {
    private final byte[] b = new byte[8];
    private final ByteBuffer c;
    private int d;
    private int e;

    protected CountedDataInputStream(InputStream var1) {
        super(var1);
        this.c = ByteBuffer.wrap(this.b);
        this.d = 0;
        this.e = 0;
    }

    public void setEnd(int var1) {
        this.e = var1;
    }

    public int getEnd() {
        return this.e;
    }

    public int getReadByteCount() {
        return this.d;
    }

    public int read(byte[] var1) {
        int var2 = 0;
        try {
            var2 = this.in.read(var1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        this.d += var2 >= 0 ? var2 : 0;
        return var2;
    }

    public int read() {
        int var1 = 0;
        try {
            var1 = this.in.read();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        this.d += var1 >= 0 ? 1 : 0;
        return var1;
    }

    public int read(byte[] var1, int var2, int var3) {
        int var4 = 0;
        try {
            var4 = this.in.read(var1, var2, var3);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        this.d += var4 >= 0 ? var4 : 0;
        return var4;
    }

    public long skip(long var1) {
        long var3 = 0;
        try {
            var3 = this.in.skip(var1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        this.d = (int)((long)this.d + var3);
        return var3;
    }

    @SuppressLint({"Assert"})
    public void skipTo(long var1) {
        long var3 = (long)this.d;
        long var5 = var1 - var3;

        assert var5 >= 0L;

        this.skipOrThrow(var5);
    }

    public void skipOrThrow(long var1) {
        if (this.skip(var1) != var1) {
            try {
                throw new EOFException();
            } catch (EOFException e1) {
                e1.printStackTrace();
            }
        }
    }

    public ByteOrder getByteOrder() {
        return this.c.order();
    }

    public void setByteOrder(ByteOrder var1) {
        this.c.order(var1);
    }

    public int readUnsignedShort() {
        return this.readShort() & '\uffff';
    }

    public short readShort() {
        this.readOrThrow(this.b, 0, 2);
        this.c.rewind();
        return this.c.getShort();
    }

    public byte readByte() {
        this.readOrThrow(this.b, 0, 1);
        this.c.rewind();
        return this.c.get();
    }

    public int readUnsignedByte() {
        this.readOrThrow(this.b, 0, 1);
        this.c.rewind();
        return this.c.get() & 255;
    }

    public void readOrThrow(byte[] var1, int var2, int var3) {
        int var4 = this.read(var1, var2, var3);
        if (var4 != var3) {
            try {
                throw new EOFException();
            } catch (EOFException e1) {
                e1.printStackTrace();
            }
        }
    }

    public long readUnsignedInt() {
        return (long)this.readInt() & 4294967295L;
    }

    public int readInt() {
        this.readOrThrow(this.b, 0, 4);
        this.c.rewind();
        return this.c.getInt();
    }

    public long readLong() {
        this.readOrThrow(this.b, 0, 8);
        this.c.rewind();
        return this.c.getLong();
    }

    public String readString(int var1) {
        byte[] var2 = new byte[var1];
        this.readOrThrow(var2);
        try {
            return new String(var2, "UTF8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public void readOrThrow(byte[] var1) {
        this.readOrThrow(var1, 0, var1.length);
    }

    public String readString(int var1, Charset var2) {
        byte[] var3 = new byte[var1];
        this.readOrThrow(var3);
        return new String(var3, var2);
    }
}
