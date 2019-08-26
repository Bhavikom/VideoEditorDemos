// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

import java.util.Arrays;
import java.util.Date;
import java.nio.charset.Charset;
import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

public class ExifTag
{
    public static final short TYPE_UNSIGNED_BYTE = 1;
    public static final short TYPE_ASCII = 2;
    public static final short TYPE_UNSIGNED_SHORT = 3;
    public static final short TYPE_UNSIGNED_LONG = 4;
    public static final short TYPE_UNSIGNED_RATIONAL = 5;
    public static final short TYPE_UNDEFINED = 7;
    public static final short TYPE_LONG = 9;
    public static final short TYPE_RATIONAL = 10;
    private static final int[] a;
    @SuppressLint({ "SimpleDateFormat" })
    private static final SimpleDateFormat b;
    private static Charset c;
    private final short d;
    private final short e;
    private boolean f;
    private int g;
    private int h;
    private Object i;
    private int j;
    
    ExifTag(final short d, final short e, final int g, final int h, final boolean f) {
        this.d = d;
        this.e = e;
        this.g = g;
        this.f = f;
        this.h = h;
        this.i = null;
    }
    
    public static boolean isValidIfd(final int n) {
        return n == 0 || n == 1 || n == 2 || n == 3 || n == 4;
    }
    
    public static boolean isValidType(final short n) {
        return n == 1 || n == 2 || n == 3 || n == 4 || n == 5 || n == 7 || n == 9 || n == 10;
    }
    
    public int getIfd() {
        return this.h;
    }
    
    protected void setIfd(final int h) {
        this.h = h;
    }
    
    public short getTagId() {
        return this.d;
    }
    
    public int getDataSize() {
        return this.getComponentCount() * getElementSize(this.getDataType());
    }
    
    public int getComponentCount() {
        return this.g;
    }
    
    public static int getElementSize(final short n) {
        return ExifTag.a[n];
    }
    
    public short getDataType() {
        return this.e;
    }
    
    protected void forceSetComponentCount(final int g) {
        this.g = g;
    }
    
    public boolean hasValue() {
        return this.i != null;
    }
    
    public boolean setValue(final int[] array) {
        if (this.a(array.length)) {
            return false;
        }
        if (this.e != 3 && this.e != 9 && this.e != 4) {
            return false;
        }
        if (this.e == 3 && this.a(array)) {
            return false;
        }
        if (this.e == 4 && this.b(array)) {
            return false;
        }
        final long[] i = new long[array.length];
        for (int j = 0; j < array.length; ++j) {
            i[j] = array[j];
        }
        this.i = i;
        this.g = array.length;
        return true;
    }
    
    public boolean setValue(final int n) {
        return this.setValue(new int[] { n });
    }
    
    public boolean setValue(final long[] i) {
        if (this.a(i.length) || this.e != 4) {
            return false;
        }
        if (this.a(i)) {
            return false;
        }
        this.i = i;
        this.g = i.length;
        return true;
    }
    
    public boolean setValue(final long n) {
        return this.setValue(new long[] { n });
    }
    
    public boolean setValue(final Rational[] i) {
        if (this.a(i.length)) {
            return false;
        }
        if (this.e != 5 && this.e != 10) {
            return false;
        }
        if (this.e == 5 && this.a(i)) {
            return false;
        }
        if (this.e == 10 && this.b(i)) {
            return false;
        }
        this.i = i;
        this.g = i.length;
        return true;
    }
    
    public boolean setValue(final Rational rational) {
        return this.setValue(new Rational[] { rational });
    }
    
    public boolean setValue(final byte[] array, final int n, final int g) {
        if (this.a(g)) {
            return false;
        }
        if (this.e != 1 && this.e != 7) {
            return false;
        }
        System.arraycopy(array, n, this.i = new byte[g], 0, g);
        this.g = g;
        return true;
    }
    
    public boolean setValue(final byte[] array) {
        return this.setValue(array, 0, array.length);
    }
    
    public boolean setValue(final byte b) {
        return this.setValue(new byte[] { b });
    }
    
    public boolean setValue(final Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Short) {
            return this.setValue((short)o & 0xFFFF);
        }
        if (o instanceof String) {
            return this.setValue((String)o);
        }
        if (o instanceof int[]) {
            return this.setValue((int[])o);
        }
        if (o instanceof long[]) {
            return this.setValue((long[])o);
        }
        if (o instanceof Rational) {
            return this.setValue((Rational)o);
        }
        if (o instanceof Rational[]) {
            return this.setValue((Rational[])o);
        }
        if (o instanceof byte[]) {
            return this.setValue((byte[])o);
        }
        if (o instanceof Integer) {
            return this.setValue((int)o);
        }
        if (o instanceof Long) {
            return this.setValue((long)o);
        }
        if (o instanceof Byte) {
            return this.setValue((byte)o);
        }
        if (o instanceof Short[]) {
            final Short[] array = (Short[])o;
            final int[] value = new int[array.length];
            for (int i = 0; i < array.length; ++i) {
                value[i] = ((array[i] == null) ? 0 : (array[i] & 0xFFFF));
            }
            return this.setValue(value);
        }
        if (o instanceof Integer[]) {
            final Integer[] array2 = (Integer[])o;
            final int[] value2 = new int[array2.length];
            for (int j = 0; j < array2.length; ++j) {
                value2[j] = ((array2[j] == null) ? 0 : array2[j]);
            }
            return this.setValue(value2);
        }
        if (o instanceof Long[]) {
            final Long[] array3 = (Long[])o;
            final long[] value3 = new long[array3.length];
            for (int k = 0; k < array3.length; ++k) {
                value3[k] = ((array3[k] == null) ? 0L : array3[k]);
            }
            return this.setValue(value3);
        }
        if (o instanceof Byte[]) {
            final Byte[] array4 = (Byte[])o;
            final byte[] value4 = new byte[array4.length];
            for (int l = 0; l < array4.length; ++l) {
                value4[l] = (byte)((array4[l] == null) ? 0 : ((byte)array4[l]));
            }
            return this.setValue(value4);
        }
        return false;
    }
    
    public boolean setTimeValue(final long date) {
        synchronized (ExifTag.b) {
            return this.setValue(ExifTag.b.format(new Date(date)));
        }
    }
    
    public boolean setValue(final String s) {
        if (this.e != 2 && this.e != 7) {
            return false;
        }
        byte[] bytes;
        final byte[] original = bytes = s.getBytes(ExifTag.c);
        if (original.length > 0) {
            bytes = ((original[original.length - 1] == 0 || this.e == 7) ? original : Arrays.copyOf(original, original.length + 1));
        }
        else if (this.e == 2 && this.g == 1) {
            bytes = new byte[] { 0 };
        }
        final int length = bytes.length;
        if (this.a(length)) {
            return false;
        }
        this.g = length;
        this.i = bytes;
        return true;
    }
    
    private boolean a(final int n) {
        return this.f && this.g != n;
    }
    
    public String getValueAsString(final String s) {
        final String valueAsString = this.getValueAsString();
        if (valueAsString == null) {
            return s;
        }
        return valueAsString;
    }
    
    public String getValueAsString() {
        if (this.i == null) {
            return null;
        }
        if (this.i instanceof String) {
            return (String)this.i;
        }
        if (this.i instanceof byte[]) {
            return new String((byte[])this.i, ExifTag.c);
        }
        return null;
    }
    
    public byte getValueAsByte(final byte b) {
        final byte[] valueAsBytes = this.getValueAsBytes();
        if (valueAsBytes == null || valueAsBytes.length < 1) {
            return b;
        }
        return valueAsBytes[0];
    }
    
    public byte[] getValueAsBytes() {
        if (this.i instanceof byte[]) {
            return (byte[])this.i;
        }
        return null;
    }
    
    public Rational getValueAsRational(final long n) {
        return this.getValueAsRational(new Rational(n, 1L));
    }
    
    public Rational getValueAsRational(final Rational rational) {
        final Rational[] valueAsRationals = this.getValueAsRationals();
        if (valueAsRationals == null || valueAsRationals.length < 1) {
            return rational;
        }
        return valueAsRationals[0];
    }
    
    public Rational[] getValueAsRationals() {
        if (this.i instanceof Rational[]) {
            return (Rational[])this.i;
        }
        return null;
    }
    
    public int getValueAsInt(final int n) {
        final int[] valueAsInts = this.getValueAsInts();
        if (valueAsInts == null || valueAsInts.length < 1) {
            return n;
        }
        return valueAsInts[0];
    }
    
    public int[] getValueAsInts() {
        if (this.i == null) {
            return null;
        }
        if (this.i instanceof long[]) {
            final long[] array = (long[])this.i;
            final int[] array2 = new int[array.length];
            for (int i = 0; i < array.length; ++i) {
                array2[i] = (int)array[i];
            }
            return array2;
        }
        return null;
    }
    
    public long getValueAsLong(final long n) {
        final long[] valueAsLongs = this.getValueAsLongs();
        if (valueAsLongs == null || valueAsLongs.length < 1) {
            return n;
        }
        return valueAsLongs[0];
    }
    
    public long[] getValueAsLongs() {
        if (this.i instanceof long[]) {
            return (long[])this.i;
        }
        return null;
    }
    
    public Object getValue() {
        return this.i;
    }
    
    public long forceGetValueAsLong(final long n) {
        final long[] valueAsLongs = this.getValueAsLongs();
        if (valueAsLongs != null && valueAsLongs.length >= 1) {
            return valueAsLongs[0];
        }
        final byte[] valueAsBytes = this.getValueAsBytes();
        if (valueAsBytes != null && valueAsBytes.length >= 1) {
            return valueAsBytes[0];
        }
        final Rational[] valueAsRationals = this.getValueAsRationals();
        if (valueAsRationals != null && valueAsRationals.length >= 1 && valueAsRationals[0].getDenominator() != 0L) {
            return (long)valueAsRationals[0].toDouble();
        }
        return n;
    }
    
    protected long getValueAt(final int n) {
        if (this.i instanceof long[]) {
            return ((long[])this.i)[n];
        }
        if (this.i instanceof byte[]) {
            return ((byte[])this.i)[n];
        }
        throw new IllegalArgumentException("Cannot get integer value from " + a(this.e));
    }
    
    private static String a(final short n) {
        switch (n) {
            case 1: {
                return "UNSIGNED_BYTE";
            }
            case 2: {
                return "ASCII";
            }
            case 3: {
                return "UNSIGNED_SHORT";
            }
            case 4: {
                return "UNSIGNED_LONG";
            }
            case 5: {
                return "UNSIGNED_RATIONAL";
            }
            case 7: {
                return "UNDEFINED";
            }
            case 9: {
                return "LONG";
            }
            case 10: {
                return "RATIONAL";
            }
            default: {
                return "";
            }
        }
    }
    
    protected String getString() {
        if (this.e != 2) {
            throw new IllegalArgumentException("Cannot get ASCII value from " + a(this.e));
        }
        return new String((byte[])this.i, ExifTag.c);
    }
    
    protected byte[] getStringByte() {
        return (byte[])this.i;
    }
    
    protected Rational getRational(final int n) {
        if (this.e != 10 && this.e != 5) {
            throw new IllegalArgumentException("Cannot get RATIONAL value from " + a(this.e));
        }
        return ((Rational[])this.i)[n];
    }
    
    protected void getBytes(final byte[] array) {
        this.getBytes(array, 0, array.length);
    }
    
    protected void getBytes(final byte[] array, final int n, final int n2) {
        if (this.e != 7 && this.e != 1) {
            throw new IllegalArgumentException("Cannot get BYTE value from " + a(this.e));
        }
        System.arraycopy(this.i, 0, array, n, (n2 > this.g) ? this.g : n2);
    }
    
    protected int getOffset() {
        return this.j;
    }
    
    protected void setOffset(final int j) {
        this.j = j;
    }
    
    protected void setHasDefinedCount(final boolean f) {
        this.f = f;
    }
    
    protected boolean hasDefinedCount() {
        return this.f;
    }
    
    private boolean a(final int[] array) {
        for (final int n : array) {
            if (n > 65535 || n < 0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean a(final long[] array) {
        for (final long n : array) {
            if (n < 0L || n > 4294967295L) {
                return true;
            }
        }
        return false;
    }
    
    private boolean b(final int[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] < 0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean a(final Rational[] array) {
        for (final Rational rational : array) {
            if (rational.getNumerator() < 0L || rational.getDenominator() < 0L || rational.getNumerator() > 4294967295L || rational.getDenominator() > 4294967295L) {
                return true;
            }
        }
        return false;
    }
    
    private boolean b(final Rational[] array) {
        for (final Rational rational : array) {
            if (rational.getNumerator() < -2147483648L || rational.getDenominator() < -2147483648L || rational.getNumerator() > 2147483647L || rational.getDenominator() > 2147483647L) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof ExifTag)) {
            return false;
        }
        final ExifTag exifTag = (ExifTag)o;
        if (exifTag.d != this.d || exifTag.g != this.g || exifTag.e != this.e) {
            return false;
        }
        if (this.i == null) {
            return exifTag.i == null;
        }
        if (exifTag.i == null) {
            return false;
        }
        if (this.i instanceof long[]) {
            return exifTag.i instanceof long[] && Arrays.equals((long[])this.i, (long[])exifTag.i);
        }
        if (this.i instanceof Rational[]) {
            return exifTag.i instanceof Rational[] && Arrays.equals((Object[])this.i, (Object[])exifTag.i);
        }
        if (this.i instanceof byte[]) {
            return exifTag.i instanceof byte[] && Arrays.equals((byte[])this.i, (byte[])exifTag.i);
        }
        return this.i.equals(exifTag.i);
    }
    
    @Override
    public String toString() {
        return String.format("tag id: %04X\n", this.d) + "ifd id: " + this.h + "\ntype: " + a(this.e) + "\ncount: " + this.g + "\noffset: " + this.j + "\nvalue: " + this.forceGetValueAsString() + "\n";
    }
    
    public String forceGetValueAsString() {
        if (this.i == null) {
            return "";
        }
        if (this.i instanceof byte[]) {
            if (this.e == 2) {
                return new String((byte[])this.i, ExifTag.c);
            }
            return Arrays.toString((byte[])this.i);
        }
        else if (this.i instanceof long[]) {
            if (((long[])this.i).length == 1) {
                return String.valueOf(((long[])this.i)[0]);
            }
            return Arrays.toString((long[])this.i);
        }
        else {
            if (!(this.i instanceof Object[])) {
                return this.i.toString();
            }
            if (((Object[])this.i).length != 1) {
                return Arrays.toString((Object[])this.i);
            }
            final Object o = ((Object[])this.i)[0];
            if (o == null) {
                return "";
            }
            return o.toString();
        }
    }
    
    static {
        (a = new int[11])[1] = 1;
        ExifTag.a[2] = 1;
        ExifTag.a[3] = 2;
        ExifTag.a[4] = 4;
        ExifTag.a[5] = 8;
        ExifTag.a[7] = 1;
        ExifTag.a[9] = 4;
        ExifTag.a[10] = 8;
        b = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
        ExifTag.c = Charset.forName("US-ASCII");
    }
}
