// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.graphics.BitmapFactory;
import java.util.Arrays;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import java.nio.channels.FileChannel;
import java.io.OutputStream;
import java.io.FileOutputStream;
//import org.lasque.tusdk.core.utils.FileHelper;
import java.io.File;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.Collection;
import java.io.IOException;
import java.io.Closeable;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.TimeZone;
import android.util.SparseIntArray;
import java.util.Calendar;
import java.util.HashSet;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.text.DateFormat;
import java.nio.ByteOrder;

public class ExifInterface
{
    public static final int TAG_NULL = -1;
    public static final int IFD_NULL = -1;
    public static final int DEFINITION_NULL = 0;
    public static final int TAG_IMAGE_WIDTH;
    public static final int TAG_IMAGE_LENGTH;
    public static final int TAG_BITS_PER_SAMPLE;
    public static final int TAG_COMPRESSION;
    public static final int TAG_PHOTOMETRIC_INTERPRETATION;
    public static final int TAG_IMAGE_DESCRIPTION;
    public static final int TAG_MAKE;
    public static final int TAG_MODEL;
    public static final int TAG_STRIP_OFFSETS;
    public static final int TAG_ORIENTATION;
    public static final int TAG_SAMPLES_PER_PIXEL;
    public static final int TAG_ROWS_PER_STRIP;
    public static final int TAG_STRIP_BYTE_COUNTS;
    public static final int TAG_INTEROP_VERSION;
    public static final int TAG_X_RESOLUTION;
    public static final int TAG_Y_RESOLUTION;
    public static final int TAG_PLANAR_CONFIGURATION;
    public static final int TAG_RESOLUTION_UNIT;
    public static final int TAG_TRANSFER_FUNCTION;
    public static final int TAG_SOFTWARE;
    public static final int TAG_DATE_TIME;
    public static final int TAG_ARTIST;
    public static final int TAG_WHITE_POINT;
    public static final int TAG_PRIMARY_CHROMATICITIES;
    public static final int TAG_Y_CB_CR_COEFFICIENTS;
    public static final int TAG_Y_CB_CR_SUB_SAMPLING;
    public static final int TAG_Y_CB_CR_POSITIONING;
    public static final int TAG_REFERENCE_BLACK_WHITE;
    public static final int TAG_COPYRIGHT;
    public static final int TAG_EXIF_IFD;
    public static final int TAG_GPS_IFD;
    public static final int TAG_JPEG_INTERCHANGE_FORMAT;
    public static final int TAG_JPEG_INTERCHANGE_FORMAT_LENGTH;
    public static final int TAG_EXPOSURE_TIME;
    public static final int TAG_F_NUMBER;
    public static final int TAG_EXPOSURE_PROGRAM;
    public static final int TAG_SPECTRAL_SENSITIVITY;
    public static final int TAG_ISO_SPEED_RATINGS;
    public static final int TAG_OECF;
    public static final int TAG_EXIF_VERSION;
    public static final int TAG_DATE_TIME_ORIGINAL;
    public static final int TAG_DATE_TIME_DIGITIZED;
    public static final int TAG_COMPONENTS_CONFIGURATION;
    public static final int TAG_COMPRESSED_BITS_PER_PIXEL;
    public static final int TAG_SHUTTER_SPEED_VALUE;
    public static final int TAG_APERTURE_VALUE;
    public static final int TAG_BRIGHTNESS_VALUE;
    public static final int TAG_EXPOSURE_BIAS_VALUE;
    public static final int TAG_MAX_APERTURE_VALUE;
    public static final int TAG_SUBJECT_DISTANCE;
    public static final int TAG_METERING_MODE;
    public static final int TAG_LIGHT_SOURCE;
    public static final int TAG_FLASH;
    public static final int TAG_FOCAL_LENGTH;
    public static final int TAG_SUBJECT_AREA;
    public static final int TAG_MAKER_NOTE;
    public static final int TAG_USER_COMMENT;
    public static final int TAG_SUB_SEC_TIME;
    public static final int TAG_SUB_SEC_TIME_ORIGINAL;
    public static final int TAG_SUB_SEC_TIME_DIGITIZED;
    public static final int TAG_FLASHPIX_VERSION;
    public static final int TAG_COLOR_SPACE;
    public static final int TAG_PIXEL_X_DIMENSION;
    public static final int TAG_PIXEL_Y_DIMENSION;
    public static final int TAG_RELATED_SOUND_FILE;
    public static final int TAG_INTEROPERABILITY_IFD;
    public static final int TAG_FLASH_ENERGY;
    public static final int TAG_SPATIAL_FREQUENCY_RESPONSE;
    public static final int TAG_FOCAL_PLANE_X_RESOLUTION;
    public static final int TAG_FOCAL_PLANE_Y_RESOLUTION;
    public static final int TAG_FOCAL_PLANE_RESOLUTION_UNIT;
    public static final int TAG_SUBJECT_LOCATION;
    public static final int TAG_EXPOSURE_INDEX;
    public static final int TAG_SENSING_METHOD;
    public static final int TAG_FILE_SOURCE;
    public static final int TAG_SCENE_TYPE;
    public static final int TAG_CFA_PATTERN;
    public static final int TAG_CUSTOM_RENDERED;
    public static final int TAG_EXPOSURE_MODE;
    public static final int TAG_WHITE_BALANCE;
    public static final int TAG_DIGITAL_ZOOM_RATIO;
    public static final int TAG_FOCAL_LENGTH_IN_35_MM_FILE;
    public static final int TAG_SCENE_CAPTURE_TYPE;
    public static final int TAG_GAIN_CONTROL;
    public static final int TAG_CONTRAST;
    public static final int TAG_SATURATION;
    public static final int TAG_SHARPNESS;
    public static final int TAG_DEVICE_SETTING_DESCRIPTION;
    public static final int TAG_SUBJECT_DISTANCE_RANGE;
    public static final int TAG_IMAGE_UNIQUE_ID;
    public static final int TAG_LENS_SPECS;
    public static final int TAG_LENS_MAKE;
    public static final int TAG_LENS_MODEL;
    public static final int TAG_SENSITIVITY_TYPE;
    public static final int TAG_GPS_VERSION_ID;
    public static final int TAG_GPS_LATITUDE_REF;
    public static final int TAG_GPS_LATITUDE;
    public static final int TAG_GPS_LONGITUDE_REF;
    public static final int TAG_GPS_LONGITUDE;
    public static final int TAG_GPS_ALTITUDE_REF;
    public static final int TAG_GPS_ALTITUDE;
    public static final int TAG_GPS_TIME_STAMP;
    public static final int TAG_GPS_SATTELLITES;
    public static final int TAG_GPS_STATUS;
    public static final int TAG_GPS_MEASURE_MODE;
    public static final int TAG_GPS_DOP;
    public static final int TAG_GPS_SPEED_REF;
    public static final int TAG_GPS_SPEED;
    public static final int TAG_GPS_TRACK_REF;
    public static final int TAG_GPS_TRACK;
    public static final int TAG_GPS_IMG_DIRECTION_REF;
    public static final int TAG_GPS_IMG_DIRECTION;
    public static final int TAG_GPS_MAP_DATUM;
    public static final int TAG_GPS_DEST_LATITUDE_REF;
    public static final int TAG_GPS_DEST_LATITUDE;
    public static final int TAG_GPS_DEST_LONGITUDE_REF;
    public static final int TAG_GPS_DEST_LONGITUDE;
    public static final int TAG_GPS_DEST_BEARING_REF;
    public static final int TAG_GPS_DEST_BEARING;
    public static final int TAG_GPS_DEST_DISTANCE_REF;
    public static final int TAG_GPS_DEST_DISTANCE;
    public static final int TAG_GPS_PROCESSING_METHOD;
    public static final int TAG_GPS_AREA_INFORMATION;
    public static final int TAG_GPS_DATE_STAMP;
    public static final int TAG_GPS_DIFFERENTIAL;
    public static final int TAG_INTEROPERABILITY_INDEX;
    public static final ByteOrder DEFAULT_BYTE_ORDER;
    private ExifData a;
    @SuppressLint({ "SimpleDateFormat" })
    private static final DateFormat b;
    @SuppressLint({ "SimpleDateFormat" })
    private static final DateFormat c;
    private static HashSet<Short> d;
    protected static HashSet<Short> sBannedDefines;
    private final Calendar e;
    private SparseIntArray f;
    
    public ExifInterface() {
        this.a = new ExifData(ExifInterface.DEFAULT_BYTE_ORDER);
        this.e = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.f = null;
        ExifInterface.b.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    protected static boolean isOffsetTag(final short s) {
        return ExifInterface.d.contains(s);
    }
    
    public static short getOrientationValueForRotation(int n) {
        n %= 360;
        if (n < 0) {
            n += 360;
        }
        if (n < 90) {
            return 1;
        }
        if (n < 180) {
            return 6;
        }
        if (n < 270) {
            return 4;
        }
        return 7;
    }
    
    public static int getRotationForOrientationValue(final short n) {
        switch (n) {
            case 1: {
                return 0;
            }
            case 6: {
                return 90;
            }
            case 4: {
                return 180;
            }
            case 7: {
                return 270;
            }
            default: {
                return 0;
            }
        }
    }
    
    public double getResolutionUnit(final int n) {
        switch (n) {
            case 1:
            case 2: {
                return 25.4;
            }
            case 3: {
                return 10.0;
            }
            case 4: {
                return 1.0;
            }
            case 5: {
                return 0.001;
            }
            default: {
                return 25.4;
            }
        }
    }
    
    public static double convertLatOrLongToDouble(final Rational[] array, final String s) {
        try {
            final double n = array[0].toDouble() + array[1].toDouble() / 60.0 + array[2].toDouble() / 3600.0;
            if (s.startsWith("S") || s.startsWith("W")) {
                return -n;
            }
            return n;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException();
        }
    }
    
    protected static int[] getAllowedIfdsFromInfo(final int n) {
        final int allowedIfdFlagsFromInfo = getAllowedIfdFlagsFromInfo(n);
        final int[] ifds = IfdData.getIfds();
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 5; ++i) {
            if ((allowedIfdFlagsFromInfo >> i & 0x1) == 0x1) {
                list.add(ifds[i]);
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        final int[] array = new int[list.size()];
        int n2 = 0;
        final Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            array[n2++] = iterator.next();
        }
        return array;
    }
    
    public void readExif(final String name, final int n) {
        if (name == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(name));
            this.readExif(inputStream, n);
            inputStream.close();
        }
        catch (IOException ex) {
            closeSilently(inputStream);
            try {
                throw ex;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
    
    public void readExif(final InputStream inputStream, final int n) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        ExifData read;
        read = new ExifReader(this).read(inputStream, n);
        this.a = read;
    }
    
    protected static void closeSilently(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (Throwable t) {}
        }
    }
    
    public void setExif(final Collection<ExifTag> tags) {
        this.clearExif();
        this.setTags(tags);
    }
    
    public void clearExif() {
        this.a = new ExifData(ExifInterface.DEFAULT_BYTE_ORDER);
    }
    
    public void setTags(final Collection<ExifTag> collection) {
        if (null == collection) {
            return;
        }
        final Iterator<ExifTag> iterator = collection.iterator();
        while (iterator.hasNext()) {
            this.setTag(iterator.next());
        }
    }
    
    public ExifTag setTag(final ExifTag exifTag) {
        return this.a.addTag(exifTag);
    }
    
    public void writeExif(final String s) {
        TLog.i("%s writeExif: %s", "ExifInterface", s);
        final File dest = new File(s);
        final File file = new File(s + ".t");
        file.delete();
        try {
            if (!this.writeExif(dest.getAbsolutePath(), file.getAbsolutePath())) {
                return;
            }
            file.renameTo(dest);
        } finally {
            file.delete();
        }
    }
    
    public boolean writeExif(final String name, final String s) {
        TLog.i(" %s writeExif: %s", "ExifInterface", s);
        if (name.equals(s)) {
            return false;
        }
        final FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(name);
            final FileChannel channel = fileInputStream.getChannel();
            if (channel == null || channel.size() <= 0L) {
                FileHelper.safeClose(fileInputStream);
                return false;
            }
            final FileOutputStream fileOutputStream = new FileOutputStream(s);
            final int a = a(fileInputStream, fileOutputStream, this.a);
            final FileChannel channel2 = fileOutputStream.getChannel();
            if (a >= 0 && channel.size() - a >= 0L) {
                channel.transferTo(a, channel.size() - a, channel2);
            }
            else {
                TLog.e("try to call FileChannel.transferTo() with negative number.", new Object[0]);
            }
            fileOutputStream.flush();
            FileHelper.safeClose(fileInputStream);
            FileHelper.safeClose(fileOutputStream);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return true;
    }
    
    public void writeExif(final InputStream inputStream, final String name) {
        TLog.i(" %s writeExif: %s", "ExifInterface", name);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(name);
            a(inputStream, fileOutputStream, this.a);
            FileHelper.copy(inputStream, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
    
    public void writeExif(final Bitmap bitmap, final String s, final int n) {
        TLog.i(" %s writeExif: %s", "ExifInterface", s);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, n, (OutputStream)byteArrayOutputStream);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try {
            byteArrayOutputStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        this.writeExif(byteArrayInputStream, s);
    }
    
    private static int a(final InputStream inputStream, final OutputStream outputStream, final ExifData exifData) {
        final ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(inputStream, 0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            outputStream.write(255);
            outputStream.write(216);
            final List<ExifParser.Section> sections = exifInterface.a.getSections();
            if (sections.get(0).b != 224) {
                TLog.w("%s first section is not a JFIF or EXIF tag", "ExifInterface");
                outputStream.write(JpegHeader.JFIF_HEADER);
            }
            final ExifOutputStream exifOutputStream = new ExifOutputStream(exifInterface);
            exifOutputStream.setExifData(exifData);
            exifOutputStream.writeExifData(outputStream);
            for (int i = 0; i < sections.size() - 1; ++i) {
                final ExifParser.Section section = sections.get(i);
                outputStream.write(255);
                outputStream.write(section.b);
                outputStream.write(section.c);
            }
            final ExifParser.Section section2 = sections.get(sections.size() - 1);
            outputStream.write(255);
            outputStream.write(section2.b);
            outputStream.write(section2.c);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return exifInterface.a.mUncompressedDataPosition;
    }
    
    public List<ExifTag> getAllTags() {
        return this.a.getAllTags();
    }
    
    public void readExif(final byte[] buf, final int n) {
        try {
            this.readExif(new ByteArrayInputStream(buf), n);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    public List<ExifTag> getTagsForTagId(final short n) {
        return this.a.getAllTagsForTagId(n);
    }
    
    public List<ExifTag> getTagsForIfdId(final int n) {
        return this.a.getAllTagsForIfd(n);
    }
    
    public ExifTag getTag(final int n) {
        return this.getTag(n, this.getDefinedTagDefaultIfd(n));
    }
    
    public int getDefinedTagDefaultIfd(final int n) {
        if (this.getTagInfo().get(n) == 0) {
            return -1;
        }
        return getTrueIfd(n);
    }
    
    public ExifTag getTag(final int n, final int n2) {
        if (!ExifTag.isValidIfd(n2)) {
            return null;
        }
        return this.a.getTag(getTrueTagKey(n), n2);
    }
    
    protected SparseIntArray getTagInfo() {
        if (this.f == null) {
            this.f = new SparseIntArray();
            this.a();
        }
        return this.f;
    }
    
    public static int getTrueIfd(final int n) {
        return n >>> 16;
    }
    
    public static short getTrueTagKey(final int n) {
        return (short)n;
    }
    
    private void a() {
        final int n = getFlagsFromAllowedIfds(new int[] { 0, 1 }) << 24;
        this.f.put(ExifInterface.TAG_MAKE, n | 0x20000);
        this.f.put(ExifInterface.TAG_IMAGE_WIDTH, n | 0x40000 | 0x1);
        this.f.put(ExifInterface.TAG_IMAGE_LENGTH, n | 0x40000 | 0x1);
        this.f.put(ExifInterface.TAG_BITS_PER_SAMPLE, n | 0x30000 | 0x3);
        this.f.put(ExifInterface.TAG_COMPRESSION, n | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_PHOTOMETRIC_INTERPRETATION, n | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_ORIENTATION, n | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_SAMPLES_PER_PIXEL, n | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_PLANAR_CONFIGURATION, n | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_Y_CB_CR_SUB_SAMPLING, n | 0x30000 | 0x2);
        this.f.put(ExifInterface.TAG_Y_CB_CR_POSITIONING, n | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_X_RESOLUTION, n | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_Y_RESOLUTION, n | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_RESOLUTION_UNIT, n | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_STRIP_OFFSETS, n | 0x40000);
        this.f.put(ExifInterface.TAG_ROWS_PER_STRIP, n | 0x40000 | 0x1);
        this.f.put(ExifInterface.TAG_STRIP_BYTE_COUNTS, n | 0x40000);
        this.f.put(ExifInterface.TAG_TRANSFER_FUNCTION, n | 0x30000 | 0x300);
        this.f.put(ExifInterface.TAG_WHITE_POINT, n | 0x50000 | 0x2);
        this.f.put(ExifInterface.TAG_PRIMARY_CHROMATICITIES, n | 0x50000 | 0x6);
        this.f.put(ExifInterface.TAG_Y_CB_CR_COEFFICIENTS, n | 0x50000 | 0x3);
        this.f.put(ExifInterface.TAG_REFERENCE_BLACK_WHITE, n | 0x50000 | 0x6);
        this.f.put(ExifInterface.TAG_DATE_TIME, n | 0x20000 | 0x14);
        this.f.put(ExifInterface.TAG_IMAGE_DESCRIPTION, n | 0x20000);
        this.f.put(ExifInterface.TAG_MODEL, n | 0x20000);
        this.f.put(ExifInterface.TAG_SOFTWARE, n | 0x20000);
        this.f.put(ExifInterface.TAG_ARTIST, n | 0x20000);
        this.f.put(ExifInterface.TAG_COPYRIGHT, n | 0x20000);
        this.f.put(ExifInterface.TAG_EXIF_IFD, n | 0x40000 | 0x1);
        this.f.put(ExifInterface.TAG_GPS_IFD, n | 0x40000 | 0x1);
        final int n2 = getFlagsFromAllowedIfds(new int[] { 1 }) << 24;
        this.f.put(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT, n2 | 0x40000 | 0x1);
        this.f.put(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, n2 | 0x40000 | 0x1);
        final int n3 = getFlagsFromAllowedIfds(new int[] { 2 }) << 24;
        this.f.put(ExifInterface.TAG_EXIF_VERSION, n3 | 0x70000 | 0x4);
        this.f.put(ExifInterface.TAG_FLASHPIX_VERSION, n3 | 0x70000 | 0x4);
        this.f.put(ExifInterface.TAG_COLOR_SPACE, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_COMPONENTS_CONFIGURATION, n3 | 0x70000 | 0x4);
        this.f.put(ExifInterface.TAG_COMPRESSED_BITS_PER_PIXEL, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_PIXEL_X_DIMENSION, n3 | 0x40000 | 0x1);
        this.f.put(ExifInterface.TAG_PIXEL_Y_DIMENSION, n3 | 0x40000 | 0x1);
        this.f.put(ExifInterface.TAG_MAKER_NOTE, n3 | 0x70000);
        this.f.put(ExifInterface.TAG_USER_COMMENT, n3 | 0x70000);
        this.f.put(ExifInterface.TAG_RELATED_SOUND_FILE, n3 | 0x20000 | 0xD);
        this.f.put(ExifInterface.TAG_DATE_TIME_ORIGINAL, n3 | 0x20000 | 0x14);
        this.f.put(ExifInterface.TAG_DATE_TIME_DIGITIZED, n3 | 0x20000 | 0x14);
        this.f.put(ExifInterface.TAG_SUB_SEC_TIME, n3 | 0x20000);
        this.f.put(ExifInterface.TAG_SUB_SEC_TIME_ORIGINAL, n3 | 0x20000);
        this.f.put(ExifInterface.TAG_SUB_SEC_TIME_DIGITIZED, n3 | 0x20000);
        this.f.put(ExifInterface.TAG_IMAGE_UNIQUE_ID, n3 | 0x20000 | 0x21);
        this.f.put(ExifInterface.TAG_LENS_SPECS, n3 | 0xA0000 | 0x4);
        this.f.put(ExifInterface.TAG_LENS_MAKE, n3 | 0x20000);
        this.f.put(ExifInterface.TAG_LENS_MODEL, n3 | 0x20000);
        this.f.put(ExifInterface.TAG_SENSITIVITY_TYPE, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_EXPOSURE_TIME, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_F_NUMBER, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_EXPOSURE_PROGRAM, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_SPECTRAL_SENSITIVITY, n3 | 0x20000);
        this.f.put(ExifInterface.TAG_ISO_SPEED_RATINGS, n3 | 0x30000);
        this.f.put(ExifInterface.TAG_OECF, n3 | 0x70000);
        this.f.put(ExifInterface.TAG_SHUTTER_SPEED_VALUE, n3 | 0xA0000 | 0x1);
        this.f.put(ExifInterface.TAG_APERTURE_VALUE, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_BRIGHTNESS_VALUE, n3 | 0xA0000 | 0x1);
        this.f.put(ExifInterface.TAG_EXPOSURE_BIAS_VALUE, n3 | 0xA0000 | 0x1);
        this.f.put(ExifInterface.TAG_MAX_APERTURE_VALUE, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_SUBJECT_DISTANCE, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_METERING_MODE, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_LIGHT_SOURCE, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_FLASH, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_FOCAL_LENGTH, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_SUBJECT_AREA, n3 | 0x30000);
        this.f.put(ExifInterface.TAG_FLASH_ENERGY, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_SPATIAL_FREQUENCY_RESPONSE, n3 | 0x70000);
        this.f.put(ExifInterface.TAG_FOCAL_PLANE_X_RESOLUTION, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_FOCAL_PLANE_Y_RESOLUTION, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_FOCAL_PLANE_RESOLUTION_UNIT, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_SUBJECT_LOCATION, n3 | 0x30000 | 0x2);
        this.f.put(ExifInterface.TAG_EXPOSURE_INDEX, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_SENSING_METHOD, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_FILE_SOURCE, n3 | 0x70000 | 0x1);
        this.f.put(ExifInterface.TAG_SCENE_TYPE, n3 | 0x70000 | 0x1);
        this.f.put(ExifInterface.TAG_CFA_PATTERN, n3 | 0x70000);
        this.f.put(ExifInterface.TAG_CUSTOM_RENDERED, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_EXPOSURE_MODE, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_WHITE_BALANCE, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_DIGITAL_ZOOM_RATIO, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_FOCAL_LENGTH_IN_35_MM_FILE, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_SCENE_CAPTURE_TYPE, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_GAIN_CONTROL, n3 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_CONTRAST, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_SATURATION, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_SHARPNESS, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION, n3 | 0x70000);
        this.f.put(ExifInterface.TAG_SUBJECT_DISTANCE_RANGE, n3 | 0x30000 | 0x1);
        this.f.put(ExifInterface.TAG_INTEROPERABILITY_IFD, n3 | 0x40000 | 0x1);
        final int n4 = getFlagsFromAllowedIfds(new int[] { 4 }) << 24;
        this.f.put(ExifInterface.TAG_GPS_VERSION_ID, n4 | 0x10000 | 0x4);
        this.f.put(ExifInterface.TAG_GPS_LATITUDE_REF, n4 | 0x20000 | 0x2);
        this.f.put(ExifInterface.TAG_GPS_LONGITUDE_REF, n4 | 0x20000 | 0x2);
        this.f.put(ExifInterface.TAG_GPS_LATITUDE, n4 | 0xA0000 | 0x3);
        this.f.put(ExifInterface.TAG_GPS_LONGITUDE, n4 | 0xA0000 | 0x3);
        this.f.put(ExifInterface.TAG_GPS_ALTITUDE_REF, n4 | 0x10000 | 0x1);
        this.f.put(ExifInterface.TAG_GPS_ALTITUDE, n4 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_GPS_TIME_STAMP, n4 | 0x50000 | 0x3);
        this.f.put(ExifInterface.TAG_GPS_SATTELLITES, n4 | 0x20000);
        this.f.put(ExifInterface.TAG_GPS_STATUS, n4 | 0x20000 | 0x2);
        this.f.put(ExifInterface.TAG_GPS_MEASURE_MODE, n4 | 0x20000 | 0x2);
        this.f.put(ExifInterface.TAG_GPS_DOP, n4 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_GPS_SPEED_REF, n4 | 0x20000 | 0x2);
        this.f.put(ExifInterface.TAG_GPS_SPEED, n4 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_GPS_TRACK_REF, n4 | 0x20000 | 0x2);
        this.f.put(ExifInterface.TAG_GPS_TRACK, n4 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_GPS_IMG_DIRECTION_REF, n4 | 0x20000 | 0x2);
        this.f.put(ExifInterface.TAG_GPS_IMG_DIRECTION, n4 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_GPS_MAP_DATUM, n4 | 0x20000);
        this.f.put(ExifInterface.TAG_GPS_DEST_LATITUDE_REF, n4 | 0x20000 | 0x2);
        this.f.put(ExifInterface.TAG_GPS_DEST_LATITUDE, n4 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_GPS_DEST_BEARING_REF, n4 | 0x20000 | 0x2);
        this.f.put(ExifInterface.TAG_GPS_DEST_BEARING, n4 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_GPS_DEST_DISTANCE_REF, n4 | 0x20000 | 0x2);
        this.f.put(ExifInterface.TAG_GPS_DEST_DISTANCE, n4 | 0x50000 | 0x1);
        this.f.put(ExifInterface.TAG_GPS_PROCESSING_METHOD, n4 | 0x70000);
        this.f.put(ExifInterface.TAG_GPS_AREA_INFORMATION, n4 | 0x70000);
        this.f.put(ExifInterface.TAG_GPS_DATE_STAMP, n4 | 0x20000 | 0xB);
        this.f.put(ExifInterface.TAG_GPS_DIFFERENTIAL, n4 | 0x30000 | 0xB);
        final int n5 = getFlagsFromAllowedIfds(new int[] { 3 }) << 24;
        this.f.put(ExifInterface.TAG_INTEROPERABILITY_INDEX, n5 | 0x20000);
        this.f.put(ExifInterface.TAG_INTEROP_VERSION, n5 | 0x70000 | 0x4);
    }
    
    protected static int getFlagsFromAllowedIfds(final int[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        int n = 0;
        final int[] ifds = IfdData.getIfds();
        for (int i = 0; i < 5; ++i) {
            for (int length = array.length, j = 0; j < length; ++j) {
                if (ifds[i] == array[j]) {
                    n |= 1 << i;
                    break;
                }
            }
        }
        return n;
    }
    
    public Object getTagValue(final int n) {
        return this.getTagValue(n, this.getDefinedTagDefaultIfd(n));
    }
    
    public Object getTagValue(final int n, final int n2) {
        final ExifTag tag = this.getTag(n, n2);
        return (tag == null) ? null : tag.getValue();
    }
    
    public String getTagStringValue(final int n, final int n2) {
        final ExifTag tag = this.getTag(n, n2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsString();
    }
    
    public String getTagStringValue(final int n) {
        return this.getTagStringValue(n, this.getDefinedTagDefaultIfd(n));
    }
    
    public Long getTagLongValue(final int n) {
        return this.getTagLongValue(n, this.getDefinedTagDefaultIfd(n));
    }
    
    @SuppressLint({ "UseValueOf" })
    public Long getTagLongValue(final int n, final int n2) {
        final long[] tagLongValues = this.getTagLongValues(n, n2);
        if (tagLongValues == null || tagLongValues.length <= 0) {
            return null;
        }
        return new Long(tagLongValues[0]);
    }
    
    @SuppressLint({ "UseValueOf" })
    public long[] getTagLongValues(final int n, final int n2) {
        final ExifTag tag = this.getTag(n, n2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsLongs();
    }
    
    public Integer getTagIntValue(final int n) {
        return this.getTagIntValue(n, this.getDefinedTagDefaultIfd(n));
    }
    
    @SuppressLint({ "UseValueOf" })
    public Integer getTagIntValue(final int n, final int n2) {
        final int[] tagIntValues = this.getTagIntValues(n, n2);
        if (tagIntValues == null || tagIntValues.length <= 0) {
            return null;
        }
        return new Integer(tagIntValues[0]);
    }
    
    public int[] getTagIntValues(final int n, final int n2) {
        final ExifTag tag = this.getTag(n, n2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsInts();
    }
    
    public Byte getTagByteValue(final int n) {
        return this.getTagByteValue(n, this.getDefinedTagDefaultIfd(n));
    }
    
    @SuppressLint({ "UseValueOf" })
    public Byte getTagByteValue(final int n, final int n2) {
        final byte[] tagByteValues = this.getTagByteValues(n, n2);
        if (tagByteValues == null || tagByteValues.length <= 0) {
            return null;
        }
        return new Byte(tagByteValues[0]);
    }
    
    public byte[] getTagByteValues(final int n, final int n2) {
        final ExifTag tag = this.getTag(n, n2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsBytes();
    }
    
    public Rational getTagRationalValue(final int n) {
        return this.getTagRationalValue(n, this.getDefinedTagDefaultIfd(n));
    }
    
    public Rational getTagRationalValue(final int n, final int n2) {
        final Rational[] tagRationalValues = this.getTagRationalValues(n, n2);
        if (tagRationalValues == null || tagRationalValues.length == 0) {
            return null;
        }
        return new Rational(tagRationalValues[0]);
    }
    
    public Rational[] getTagRationalValues(final int n, final int n2) {
        final ExifTag tag = this.getTag(n, n2);
        if (tag == null) {
            return null;
        }
        return tag.getValueAsRationals();
    }
    
    public long[] getTagLongValues(final int n) {
        return this.getTagLongValues(n, this.getDefinedTagDefaultIfd(n));
    }
    
    public int[] getTagIntValues(final int n) {
        return this.getTagIntValues(n, this.getDefinedTagDefaultIfd(n));
    }
    
    public byte[] getTagByteValues(final int n) {
        return this.getTagByteValues(n, this.getDefinedTagDefaultIfd(n));
    }
    
    public Rational[] getTagRationalValues(final int n) {
        return this.getTagRationalValues(n, this.getDefinedTagDefaultIfd(n));
    }
    
    public boolean isTagCountDefined(final int n) {
        final int value = this.getTagInfo().get(n);
        return value != 0 && getComponentCountFromInfo(value) != 0;
    }
    
    protected static int getComponentCountFromInfo(final int n) {
        return n & 0xFFFF;
    }
    
    public int getDefinedTagCount(final int n) {
        final int value = this.getTagInfo().get(n);
        if (value == 0) {
            return 0;
        }
        return getComponentCountFromInfo(value);
    }
    
    public int getActualTagCount(final int n, final int n2) {
        final ExifTag tag = this.getTag(n, n2);
        if (tag == null) {
            return 0;
        }
        return tag.getComponentCount();
    }
    
    public short getDefinedTagType(final int n) {
        final int value = this.getTagInfo().get(n);
        if (value == 0) {
            return -1;
        }
        return getTypeFromInfo(value);
    }
    
    protected static short getTypeFromInfo(final int n) {
        return (short)(n >> 16 & 0xFF);
    }
    
    protected ExifTag buildUninitializedTag(final int n) {
        final int value = this.getTagInfo().get(n);
        if (value == 0) {
            return null;
        }
        final short typeFromInfo = getTypeFromInfo(value);
        final int componentCountFromInfo = getComponentCountFromInfo(value);
        return new ExifTag(getTrueTagKey(n), typeFromInfo, componentCountFromInfo, getTrueIfd(n), componentCountFromInfo != 0);
    }
    
    public boolean setTagValue(final int n, final Object o) {
        return this.setTagValue(n, this.getDefinedTagDefaultIfd(n), o);
    }
    
    public boolean setTagValue(final int n, final int n2, final Object value) {
        final ExifTag tag = this.getTag(n, n2);
        return tag != null && tag.setValue(value);
    }
    
    public void deleteTag(final int n) {
        this.deleteTag(n, this.getDefinedTagDefaultIfd(n));
    }
    
    public void deleteTag(final int n, final int n2) {
        this.a.removeTag(getTrueTagKey(n), n2);
    }
    
    public int setTagDefinition(final short s, final int n, final short n2, final short n3, final int[] array) {
        if (ExifInterface.sBannedDefines.contains(s)) {
            return -1;
        }
        if (!ExifTag.isValidType(n2) || !ExifTag.isValidIfd(n)) {
            return -1;
        }
        final int defineTag = defineTag(n, s);
        if (defineTag == -1) {
            return -1;
        }
        final int[] tagDefinitionsForTagId = this.getTagDefinitionsForTagId(s);
        final SparseIntArray tagInfo = this.getTagInfo();
        boolean b = false;
        for (final int n4 : array) {
            if (n == n4) {
                b = true;
            }
            if (!ExifTag.isValidIfd(n4)) {
                return -1;
            }
        }
        if (!b) {
            return -1;
        }
        final int flagsFromAllowedIfds = getFlagsFromAllowedIfds(array);
        if (tagDefinitionsForTagId != null) {
            final int[] array2 = tagDefinitionsForTagId;
            for (int length2 = array2.length, j = 0; j < length2; ++j) {
                if ((flagsFromAllowedIfds & getAllowedIfdFlagsFromInfo(tagInfo.get(array2[j]))) != 0x0) {
                    return -1;
                }
            }
        }
        this.getTagInfo().put(defineTag, flagsFromAllowedIfds << 24 | n2 << 16 | n3);
        return defineTag;
    }
    
    protected int getTagDefinition(final short n, final int n2) {
        return this.getTagInfo().get(defineTag(n2, n));
    }
    
    public static int defineTag(final int n, final short n2) {
        return (n2 & 0xFFFF) | n << 16;
    }
    
    protected int[] getTagDefinitionsForTagId(final short n) {
        final int[] ifds = IfdData.getIfds();
        final int[] original = new int[ifds.length];
        int to = 0;
        final SparseIntArray tagInfo = this.getTagInfo();
        final int[] array = ifds;
        for (int length = array.length, i = 0; i < length; ++i) {
            final int defineTag = defineTag(array[i], n);
            if (tagInfo.get(defineTag) != 0) {
                original[to++] = defineTag;
            }
        }
        if (to == 0) {
            return null;
        }
        return Arrays.copyOfRange(original, 0, to);
    }
    
    protected int getTagDefinitionForTag(final ExifTag exifTag) {
        return this.getTagDefinitionForTag(exifTag.getTagId(), exifTag.getDataType(), exifTag.getComponentCount(), exifTag.getIfd());
    }
    
    protected int getTagDefinitionForTag(final short n, final short n2, final int n3, final int n4) {
        final int[] tagDefinitionsForTagId = this.getTagDefinitionsForTagId(n);
        if (tagDefinitionsForTagId == null) {
            return -1;
        }
        final SparseIntArray tagInfo = this.getTagInfo();
        int n5 = -1;
        for (final int n6 : tagDefinitionsForTagId) {
            final int value = tagInfo.get(n6);
            final short typeFromInfo = getTypeFromInfo(value);
            final int componentCountFromInfo = getComponentCountFromInfo(value);
            final int[] allowedIfdsFromInfo = getAllowedIfdsFromInfo(value);
            boolean b = false;
            final int[] array2 = allowedIfdsFromInfo;
            for (int length2 = array2.length, j = 0; j < length2; ++j) {
                if (array2[j] == n4) {
                    b = true;
                    break;
                }
            }
            if (b && n2 == typeFromInfo && (n3 == componentCountFromInfo || componentCountFromInfo == 0)) {
                n5 = n6;
                break;
            }
        }
        return n5;
    }
    
    public void removeTagDefinition(final int n) {
        this.getTagInfo().delete(n);
    }
    
    public void resetTagDefinitions() {
        this.f = null;
    }
    
    public Bitmap getThumbnailBitmap() {
        if (this.a.hasCompressedThumbnail()) {
            final byte[] compressedThumbnail = this.a.getCompressedThumbnail();
            return BitmapFactory.decodeByteArray(compressedThumbnail, 0, compressedThumbnail.length);
        }
        if (this.a.hasUncompressedStrip()) {}
        return null;
    }
    
    public byte[] getThumbnailBytes() {
        if (this.a.hasCompressedThumbnail()) {
            return this.a.getCompressedThumbnail();
        }
        if (this.a.hasUncompressedStrip()) {}
        return null;
    }
    
    public byte[] getThumbnail() {
        return this.a.getCompressedThumbnail();
    }
    
    public int getQualityGuess() {
        return this.a.getQualityGuess();
    }
    
    public short getJpegProcess() {
        return this.a.getJpegProcess();
    }
    
    public int[] getImageSize() {
        return this.a.getImageSize();
    }
    
    public boolean isThumbnailCompressed() {
        return this.a.hasCompressedThumbnail();
    }
    
    public boolean hasThumbnail() {
        return this.a.hasCompressedThumbnail();
    }
    
    public boolean setCompressedThumbnail(final Bitmap bitmap) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        return bitmap.compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)byteArrayOutputStream) && this.setCompressedThumbnail(byteArrayOutputStream.toByteArray());
    }
    
    public boolean setCompressedThumbnail(final byte[] compressedThumbnail) {
        this.a.clearThumbnailAndStrips();
        this.a.setCompressedThumbnail(compressedThumbnail);
        return true;
    }
    
    public void removeCompressedThumbnail() {
        this.a.setCompressedThumbnail(null);
    }
    
    public String getUserComment() {
        return this.a.getUserComment();
    }
    
    public double getAltitude(final double n) {
        final Byte tagByteValue = this.getTagByteValue(ExifInterface.TAG_GPS_ALTITUDE_REF);
        final Rational tagRationalValue = this.getTagRationalValue(ExifInterface.TAG_GPS_ALTITUDE);
        int n2 = 1;
        if (null != tagByteValue) {
            n2 = ((tagByteValue == 1) ? -1 : 1);
        }
        if (tagRationalValue != null) {
            return tagRationalValue.toDouble() * n2;
        }
        return n;
    }
    
    public double[] getLatLongAsDoubles() {
        final Rational[] tagRationalValues = this.getTagRationalValues(ExifInterface.TAG_GPS_LATITUDE);
        final String tagStringValue = this.getTagStringValue(ExifInterface.TAG_GPS_LATITUDE_REF);
        final Rational[] tagRationalValues2 = this.getTagRationalValues(ExifInterface.TAG_GPS_LONGITUDE);
        final String tagStringValue2 = this.getTagStringValue(ExifInterface.TAG_GPS_LONGITUDE_REF);
        if (tagRationalValues == null || tagRationalValues2 == null || tagStringValue == null || tagStringValue2 == null || tagRationalValues.length < 3 || tagRationalValues2.length < 3) {
            return null;
        }
        return new double[] { convertLatOrLongToDouble(tagRationalValues, tagStringValue), convertLatOrLongToDouble(tagRationalValues2, tagStringValue2) };
    }
    
    public String getLatitude() {
        final Rational[] tagRationalValues = this.getTagRationalValues(ExifInterface.TAG_GPS_LATITUDE);
        final String tagStringValue = this.getTagStringValue(ExifInterface.TAG_GPS_LATITUDE_REF);
        if (null == tagRationalValues || null == tagStringValue) {
            return null;
        }
        return a(tagRationalValues, tagStringValue);
    }
    
    public String getLongitude() {
        final Rational[] tagRationalValues = this.getTagRationalValues(ExifInterface.TAG_GPS_LONGITUDE);
        final String tagStringValue = this.getTagStringValue(ExifInterface.TAG_GPS_LONGITUDE_REF);
        if (null == tagRationalValues || null == tagStringValue) {
            return null;
        }
        return a(tagRationalValues, tagStringValue);
    }
    
    private static String a(final Rational[] array, String substring) {
        try {
            final double double1 = array[0].toDouble();
            final double double2 = array[1].toDouble();
            final double double3 = array[2].toDouble();
            substring = substring.substring(0, 1);
            return String.format(Locale.getDefault(), "%1$.0f° %2$.0f' %3$.0f\" %4$s", double1, double2, double3, substring.toUpperCase(Locale.getDefault()));
        }
        catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException ex2) {
            ex2.printStackTrace();
        }
        return null;
    }
    
    @SuppressLint({ "SimpleDateFormat" })
    public static Date getDateTime(final String source, final TimeZone timeZone) {
        if (source == null) {
            return null;
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
        simpleDateFormat.setTimeZone(timeZone);
        try {
            return simpleDateFormat.parse(source);
        }
        catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        catch (ParseException ex2) {
            ex2.printStackTrace();
        }
        return null;
    }
    
    public boolean addDateTimeStampTag(final int n, final long l, final TimeZone timeZone) {
        if (n != ExifInterface.TAG_DATE_TIME && n != ExifInterface.TAG_DATE_TIME_DIGITIZED && n != ExifInterface.TAG_DATE_TIME_ORIGINAL) {
            return false;
        }
        ExifInterface.c.setTimeZone(timeZone);
        final ExifTag buildTag = this.buildTag(n, ExifInterface.c.format(l));
        if (buildTag == null) {
            return false;
        }
        this.setTag(buildTag);
        return true;
    }
    
    public ExifTag buildTag(final int n, final Object o) {
        return this.buildTag(n, getTrueIfd(n), o);
    }
    
    public ExifTag buildTag(final int n, final int n2, final Object value) {
        final int value2 = this.getTagInfo().get(n);
        if (value2 == 0 || value == null) {
            return null;
        }
        final short typeFromInfo = getTypeFromInfo(value2);
        final int componentCountFromInfo = getComponentCountFromInfo(value2);
        final boolean b = componentCountFromInfo != 0;
        if (!isIfdAllowed(value2, n2)) {
            return null;
        }
        final ExifTag exifTag = new ExifTag(getTrueTagKey(n), typeFromInfo, componentCountFromInfo, n2, b);
        if (!exifTag.setValue(value)) {
            return null;
        }
        return exifTag;
    }
    
    protected static boolean isIfdAllowed(final int n, final int n2) {
        final int[] ifds = IfdData.getIfds();
        final int allowedIfdFlagsFromInfo = getAllowedIfdFlagsFromInfo(n);
        for (int i = 0; i < ifds.length; ++i) {
            if (n2 == ifds[i] && (allowedIfdFlagsFromInfo >> i & 0x1) == 0x1) {
                return true;
            }
        }
        return false;
    }
    
    protected static int getAllowedIfdFlagsFromInfo(final int n) {
        return n >>> 24;
    }
    
    public boolean addGpsTags(final double n, final double n2) {
        final ExifTag buildTag = this.buildTag(ExifInterface.TAG_GPS_LATITUDE, a(n));
        final ExifTag buildTag2 = this.buildTag(ExifInterface.TAG_GPS_LONGITUDE, a(n2));
        final ExifTag buildTag3 = this.buildTag(ExifInterface.TAG_GPS_LATITUDE_REF, (n >= 0.0) ? "N" : "S");
        final ExifTag buildTag4 = this.buildTag(ExifInterface.TAG_GPS_LONGITUDE_REF, (n2 >= 0.0) ? "E" : "W");
        if (buildTag == null || buildTag2 == null || buildTag3 == null || buildTag4 == null) {
            return false;
        }
        this.setTag(buildTag);
        this.setTag(buildTag2);
        this.setTag(buildTag3);
        this.setTag(buildTag4);
        return true;
    }
    
    private static Rational[] a(double abs) {
        abs = Math.abs(abs);
        final int n = (int)abs;
        abs = (abs - n) * 60.0;
        final int n2 = (int)abs;
        abs = (abs - n2) * 6000.0;
        return new Rational[] { new Rational(n, 1L), new Rational(n2, 1L), new Rational((int)abs, 100L) };
    }
    
    public boolean addGpsDateTimeStampTag(final long n) {
        final ExifTag buildTag = this.buildTag(ExifInterface.TAG_GPS_DATE_STAMP, ExifInterface.b.format(n));
        if (buildTag == null) {
            return false;
        }
        this.setTag(buildTag);
        this.e.setTimeInMillis(n);
        final ExifTag buildTag2 = this.buildTag(ExifInterface.TAG_GPS_TIME_STAMP, new Rational[] { new Rational(this.e.get(11), 1L), new Rational(this.e.get(12), 1L), new Rational(this.e.get(13), 1L) });
        if (buildTag2 == null) {
            return false;
        }
        this.setTag(buildTag2);
        return true;
    }
    
    public double getApertureSize() {
        final Rational tagRationalValue = this.getTagRationalValue(ExifInterface.TAG_F_NUMBER);
        if (null != tagRationalValue && tagRationalValue.toDouble() > 0.0) {
            return tagRationalValue.toDouble();
        }
        final Rational tagRationalValue2 = this.getTagRationalValue(ExifInterface.TAG_APERTURE_VALUE);
        if (null != tagRationalValue2 && tagRationalValue2.toDouble() > 0.0) {
            return Math.exp(tagRationalValue2.toDouble() * Math.log(2.0) * 0.5);
        }
        return 0.0;
    }
    
    public String getLensModelDescription() {
        final String tagStringValue = this.getTagStringValue(ExifInterface.TAG_LENS_MODEL);
        if (null != tagStringValue) {
            return tagStringValue;
        }
        final Rational[] tagRationalValues = this.getTagRationalValues(ExifInterface.TAG_LENS_SPECS);
        if (null != tagRationalValues) {
            return ExifUtil.processLensSpecifications(tagRationalValues);
        }
        return null;
    }
    
    public static byte[] toBitArray(final short n) {
        final byte[] array = new byte[16];
        for (int i = 0; i < 16; ++i) {
            array[15 - i] = (byte)(n >> i & 0x1);
        }
        return array;
    }
    
    static {
        TAG_IMAGE_WIDTH = defineTag(0, (short)256);
        TAG_IMAGE_LENGTH = defineTag(0, (short)257);
        TAG_BITS_PER_SAMPLE = defineTag(0, (short)258);
        TAG_COMPRESSION = defineTag(0, (short)259);
        TAG_PHOTOMETRIC_INTERPRETATION = defineTag(0, (short)262);
        TAG_IMAGE_DESCRIPTION = defineTag(0, (short)270);
        TAG_MAKE = defineTag(0, (short)271);
        TAG_MODEL = defineTag(0, (short)272);
        TAG_STRIP_OFFSETS = defineTag(0, (short)273);
        TAG_ORIENTATION = defineTag(0, (short)274);
        TAG_SAMPLES_PER_PIXEL = defineTag(0, (short)277);
        TAG_ROWS_PER_STRIP = defineTag(0, (short)278);
        TAG_STRIP_BYTE_COUNTS = defineTag(0, (short)279);
        TAG_INTEROP_VERSION = defineTag(3, (short)2);
        TAG_X_RESOLUTION = defineTag(0, (short)282);
        TAG_Y_RESOLUTION = defineTag(0, (short)283);
        TAG_PLANAR_CONFIGURATION = defineTag(0, (short)284);
        TAG_RESOLUTION_UNIT = defineTag(0, (short)296);
        TAG_TRANSFER_FUNCTION = defineTag(0, (short)301);
        TAG_SOFTWARE = defineTag(0, (short)305);
        TAG_DATE_TIME = defineTag(0, (short)306);
        TAG_ARTIST = defineTag(0, (short)315);
        TAG_WHITE_POINT = defineTag(0, (short)318);
        TAG_PRIMARY_CHROMATICITIES = defineTag(0, (short)319);
        TAG_Y_CB_CR_COEFFICIENTS = defineTag(0, (short)529);
        TAG_Y_CB_CR_SUB_SAMPLING = defineTag(0, (short)530);
        TAG_Y_CB_CR_POSITIONING = defineTag(0, (short)531);
        TAG_REFERENCE_BLACK_WHITE = defineTag(0, (short)532);
        TAG_COPYRIGHT = defineTag(0, (short)(-32104));
        TAG_EXIF_IFD = defineTag(0, (short)(-30871));
        TAG_GPS_IFD = defineTag(0, (short)(-30683));
        TAG_JPEG_INTERCHANGE_FORMAT = defineTag(1, (short)513);
        TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = defineTag(1, (short)514);
        TAG_EXPOSURE_TIME = defineTag(2, (short)(-32102));
        TAG_F_NUMBER = defineTag(2, (short)(-32099));
        TAG_EXPOSURE_PROGRAM = defineTag(2, (short)(-30686));
        TAG_SPECTRAL_SENSITIVITY = defineTag(2, (short)(-30684));
        TAG_ISO_SPEED_RATINGS = defineTag(2, (short)(-30681));
        TAG_OECF = defineTag(2, (short)(-30680));
        TAG_EXIF_VERSION = defineTag(2, (short)(-28672));
        TAG_DATE_TIME_ORIGINAL = defineTag(2, (short)(-28669));
        TAG_DATE_TIME_DIGITIZED = defineTag(2, (short)(-28668));
        TAG_COMPONENTS_CONFIGURATION = defineTag(2, (short)(-28415));
        TAG_COMPRESSED_BITS_PER_PIXEL = defineTag(2, (short)(-28414));
        TAG_SHUTTER_SPEED_VALUE = defineTag(2, (short)(-28159));
        TAG_APERTURE_VALUE = defineTag(2, (short)(-28158));
        TAG_BRIGHTNESS_VALUE = defineTag(2, (short)(-28157));
        TAG_EXPOSURE_BIAS_VALUE = defineTag(2, (short)(-28156));
        TAG_MAX_APERTURE_VALUE = defineTag(2, (short)(-28155));
        TAG_SUBJECT_DISTANCE = defineTag(2, (short)(-28154));
        TAG_METERING_MODE = defineTag(2, (short)(-28153));
        TAG_LIGHT_SOURCE = defineTag(2, (short)(-28152));
        TAG_FLASH = defineTag(2, (short)(-28151));
        TAG_FOCAL_LENGTH = defineTag(2, (short)(-28150));
        TAG_SUBJECT_AREA = defineTag(2, (short)(-28140));
        TAG_MAKER_NOTE = defineTag(2, (short)(-28036));
        TAG_USER_COMMENT = defineTag(2, (short)(-28026));
        TAG_SUB_SEC_TIME = defineTag(2, (short)(-28016));
        TAG_SUB_SEC_TIME_ORIGINAL = defineTag(2, (short)(-28015));
        TAG_SUB_SEC_TIME_DIGITIZED = defineTag(2, (short)(-28014));
        TAG_FLASHPIX_VERSION = defineTag(2, (short)(-24576));
        TAG_COLOR_SPACE = defineTag(2, (short)(-24575));
        TAG_PIXEL_X_DIMENSION = defineTag(2, (short)(-24574));
        TAG_PIXEL_Y_DIMENSION = defineTag(2, (short)(-24573));
        TAG_RELATED_SOUND_FILE = defineTag(2, (short)(-24572));
        TAG_INTEROPERABILITY_IFD = defineTag(2, (short)(-24571));
        TAG_FLASH_ENERGY = defineTag(2, (short)(-24053));
        TAG_SPATIAL_FREQUENCY_RESPONSE = defineTag(2, (short)(-24052));
        TAG_FOCAL_PLANE_X_RESOLUTION = defineTag(2, (short)(-24050));
        TAG_FOCAL_PLANE_Y_RESOLUTION = defineTag(2, (short)(-24049));
        TAG_FOCAL_PLANE_RESOLUTION_UNIT = defineTag(2, (short)(-24048));
        TAG_SUBJECT_LOCATION = defineTag(2, (short)(-24044));
        TAG_EXPOSURE_INDEX = defineTag(2, (short)(-24043));
        TAG_SENSING_METHOD = defineTag(2, (short)(-24041));
        TAG_FILE_SOURCE = defineTag(2, (short)(-23808));
        TAG_SCENE_TYPE = defineTag(2, (short)(-23807));
        TAG_CFA_PATTERN = defineTag(2, (short)(-23806));
        TAG_CUSTOM_RENDERED = defineTag(2, (short)(-23551));
        TAG_EXPOSURE_MODE = defineTag(2, (short)(-23550));
        TAG_WHITE_BALANCE = defineTag(2, (short)(-23549));
        TAG_DIGITAL_ZOOM_RATIO = defineTag(2, (short)(-23548));
        TAG_FOCAL_LENGTH_IN_35_MM_FILE = defineTag(2, (short)(-23547));
        TAG_SCENE_CAPTURE_TYPE = defineTag(2, (short)(-23546));
        TAG_GAIN_CONTROL = defineTag(2, (short)(-23545));
        TAG_CONTRAST = defineTag(2, (short)(-23544));
        TAG_SATURATION = defineTag(2, (short)(-23543));
        TAG_SHARPNESS = defineTag(2, (short)(-23542));
        TAG_DEVICE_SETTING_DESCRIPTION = defineTag(2, (short)(-23541));
        TAG_SUBJECT_DISTANCE_RANGE = defineTag(2, (short)(-23540));
        TAG_IMAGE_UNIQUE_ID = defineTag(2, (short)(-23520));
        TAG_LENS_SPECS = defineTag(2, (short)(-23502));
        TAG_LENS_MAKE = defineTag(2, (short)(-23501));
        TAG_LENS_MODEL = defineTag(2, (short)(-23500));
        TAG_SENSITIVITY_TYPE = defineTag(2, (short)(-30672));
        TAG_GPS_VERSION_ID = defineTag(4, (short)0);
        TAG_GPS_LATITUDE_REF = defineTag(4, (short)1);
        TAG_GPS_LATITUDE = defineTag(4, (short)2);
        TAG_GPS_LONGITUDE_REF = defineTag(4, (short)3);
        TAG_GPS_LONGITUDE = defineTag(4, (short)4);
        TAG_GPS_ALTITUDE_REF = defineTag(4, (short)5);
        TAG_GPS_ALTITUDE = defineTag(4, (short)6);
        TAG_GPS_TIME_STAMP = defineTag(4, (short)7);
        TAG_GPS_SATTELLITES = defineTag(4, (short)8);
        TAG_GPS_STATUS = defineTag(4, (short)9);
        TAG_GPS_MEASURE_MODE = defineTag(4, (short)10);
        TAG_GPS_DOP = defineTag(4, (short)11);
        TAG_GPS_SPEED_REF = defineTag(4, (short)12);
        TAG_GPS_SPEED = defineTag(4, (short)13);
        TAG_GPS_TRACK_REF = defineTag(4, (short)14);
        TAG_GPS_TRACK = defineTag(4, (short)15);
        TAG_GPS_IMG_DIRECTION_REF = defineTag(4, (short)16);
        TAG_GPS_IMG_DIRECTION = defineTag(4, (short)17);
        TAG_GPS_MAP_DATUM = defineTag(4, (short)18);
        TAG_GPS_DEST_LATITUDE_REF = defineTag(4, (short)19);
        TAG_GPS_DEST_LATITUDE = defineTag(4, (short)20);
        TAG_GPS_DEST_LONGITUDE_REF = defineTag(4, (short)21);
        TAG_GPS_DEST_LONGITUDE = defineTag(4, (short)22);
        TAG_GPS_DEST_BEARING_REF = defineTag(4, (short)23);
        TAG_GPS_DEST_BEARING = defineTag(4, (short)24);
        TAG_GPS_DEST_DISTANCE_REF = defineTag(4, (short)25);
        TAG_GPS_DEST_DISTANCE = defineTag(4, (short)26);
        TAG_GPS_PROCESSING_METHOD = defineTag(4, (short)27);
        TAG_GPS_AREA_INFORMATION = defineTag(4, (short)28);
        TAG_GPS_DATE_STAMP = defineTag(4, (short)29);
        TAG_GPS_DIFFERENTIAL = defineTag(4, (short)30);
        TAG_INTEROPERABILITY_INDEX = defineTag(3, (short)1);
        DEFAULT_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
        b = new SimpleDateFormat("yyyy:MM:dd");
        c = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
        (ExifInterface.d = new HashSet<Short>()).add(getTrueTagKey(ExifInterface.TAG_GPS_IFD));
        ExifInterface.d.add(getTrueTagKey(ExifInterface.TAG_EXIF_IFD));
        ExifInterface.d.add(getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT));
        ExifInterface.d.add(getTrueTagKey(ExifInterface.TAG_INTEROPERABILITY_IFD));
        ExifInterface.d.add(getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS));
        (ExifInterface.sBannedDefines = new HashSet<Short>(ExifInterface.d)).add(getTrueTagKey(-1));
        ExifInterface.sBannedDefines.add(getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH));
        ExifInterface.sBannedDefines.add(getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS));
    }
    
    public interface Options
    {
        public static final int OPTION_IFD_0 = 1;
        public static final int OPTION_IFD_1 = 2;
        public static final int OPTION_IFD_EXIF = 4;
        public static final int OPTION_IFD_GPS = 8;
        public static final int OPTION_IFD_INTEROPERABILITY = 16;
        public static final int OPTION_THUMBNAIL = 32;
        public static final int OPTION_ALL = 63;
    }
    
    public interface SensitivityType
    {
        public static final short UNKNOWN = 0;
        public static final short SOS = 1;
        public static final short REI = 2;
        public static final short ISO = 3;
        public static final short SOS_REI = 4;
        public static final short SOS_ISO = 5;
        public static final short REI_ISO = 6;
        public static final short SOS_REI_ISO = 7;
    }
    
    public interface JpegProcess
    {
        public static final short BASELINE = -64;
        public static final short EXTENDED_SEQUENTIAL = -63;
        public static final short PROGRESSIVE = -62;
        public static final short LOSSLESS = -61;
        public static final short DIFFERENTIAL_SEQUENTIAL = -59;
        public static final short DIFFERENTIAL_PROGRESSIVE = -58;
        public static final short DIFFERENTIAL_LOSSLESS = -57;
        public static final short EXTENDED_SEQ_ARITHMETIC_CODING = -55;
        public static final short PROGRESSIVE_AIRTHMETIC_CODING = -54;
        public static final short LOSSLESS_AITHMETIC_CODING = -53;
        public static final short DIFFERENTIAL_SEQ_ARITHMETIC_CODING = -51;
        public static final short DIFFERENTIAL_PROGRESSIVE_ARITHMETIC_CODING = -50;
        public static final short DIFFERENTIAL_LOSSLESS_ARITHMETIC_CODING = -49;
    }
    
    public interface GpsDifferential
    {
        public static final short WITHOUT_DIFFERENTIAL_CORRECTION = 0;
        public static final short DIFFERENTIAL_CORRECTION_APPLIED = 1;
    }
    
    public interface GpsTrackRef
    {
        public static final String TRUE_DIRECTION = "T";
        public static final String MAGNETIC_DIRECTION = "M";
    }
    
    public interface GpsSpeedRef
    {
        public static final String KILOMETERS = "K";
        public static final String MILES = "M";
        public static final String KNOTS = "N";
    }
    
    public interface GpsMeasureMode
    {
        public static final String MODE_2_DIMENSIONAL = "2";
        public static final String MODE_3_DIMENSIONAL = "3";
    }
    
    public interface GpsStatus
    {
        public static final String IN_PROGRESS = "A";
        public static final String INTEROPERABILITY = "V";
    }
    
    public interface GpsAltitudeRef
    {
        public static final short SEA_LEVEL = 0;
        public static final short SEA_LEVEL_NEGATIVE = 1;
    }
    
    public interface GpsLongitudeRef
    {
        public static final String EAST = "E";
        public static final String WEST = "W";
    }
    
    public interface GpsLatitudeRef
    {
        public static final String NORTH = "N";
        public static final String SOUTH = "S";
    }
    
    public interface SubjectDistance
    {
        public static final short UNKNOWN = 0;
        public static final short MACRO = 1;
        public static final short CLOSE_VIEW = 2;
        public static final short DISTANT_VIEW = 3;
    }
    
    public interface Sharpness
    {
        public static final short NORMAL = 0;
        public static final short SOFT = 1;
        public static final short HARD = 2;
    }
    
    public interface Saturation
    {
        public static final short NORMAL = 0;
        public static final short LOW = 1;
        public static final short HIGH = 2;
    }
    
    public interface Contrast
    {
        public static final short NORMAL = 0;
        public static final short SOFT = 1;
        public static final short HARD = 2;
    }
    
    public interface GainControl
    {
        public static final short NONE = 0;
        public static final short LOW_UP = 1;
        public static final short HIGH_UP = 2;
        public static final short LOW_DOWN = 3;
        public static final short HIGH_DOWN = 4;
    }
    
    public interface SceneType
    {
        public static final short DIRECT_PHOTOGRAPHED = 1;
    }
    
    public interface FileSource
    {
        public static final short DSC = 3;
    }
    
    public interface SensingMethod
    {
        public static final short NOT_DEFINED = 1;
        public static final short ONE_CHIP_COLOR = 2;
        public static final short TWO_CHIP_COLOR = 3;
        public static final short THREE_CHIP_COLOR = 4;
        public static final short COLOR_SEQUENTIAL_AREA = 5;
        public static final short TRILINEAR = 7;
        public static final short COLOR_SEQUENTIAL_LINEAR = 8;
    }
    
    public interface LightSource
    {
        public static final short UNKNOWN = 0;
        public static final short DAYLIGHT = 1;
        public static final short FLUORESCENT = 2;
        public static final short TUNGSTEN = 3;
        public static final short FLASH = 4;
        public static final short FINE_WEATHER = 9;
        public static final short CLOUDY_WEATHER = 10;
        public static final short SHADE = 11;
        public static final short DAYLIGHT_FLUORESCENT = 12;
        public static final short DAY_WHITE_FLUORESCENT = 13;
        public static final short COOL_WHITE_FLUORESCENT = 14;
        public static final short WHITE_FLUORESCENT = 15;
        public static final short STANDARD_LIGHT_A = 17;
        public static final short STANDARD_LIGHT_B = 18;
        public static final short STANDARD_LIGHT_C = 19;
        public static final short D55 = 20;
        public static final short D65 = 21;
        public static final short D75 = 22;
        public static final short D50 = 23;
        public static final short ISO_STUDIO_TUNGSTEN = 24;
        public static final short OTHER = 255;
    }
    
    public interface ComponentsConfiguration
    {
        public static final short NOT_EXIST = 0;
        public static final short Y = 1;
        public static final short CB = 2;
        public static final short CR = 3;
        public static final short R = 4;
        public static final short G = 5;
        public static final short B = 6;
    }
    
    public interface SceneCapture
    {
        public static final short STANDARD = 0;
        public static final short LANDSCAPE = 1;
        public static final short PROTRAIT = 2;
        public static final short NIGHT_SCENE = 3;
    }
    
    public interface WhiteBalance
    {
        public static final short AUTO = 0;
        public static final short MANUAL = 1;
    }
    
    public interface ExposureMode
    {
        public static final short AUTO_EXPOSURE = 0;
        public static final short MANUAL_EXPOSURE = 1;
        public static final short AUTO_BRACKET = 2;
    }
    
    public interface ColorSpace
    {
        public static final short SRGB = 1;
        public static final short UNCALIBRATED = -1;
    }
    
    public interface Flash
    {
        public enum RedEyeMode
        {
            NONE, 
            SUPPORTED;
        }
        
        public enum FlashFunction
        {
            FUNCTION_PRESENT, 
            FUNCTION_NOR_PRESENT;
        }
        
        public enum CompulsoryMode
        {
            UNKNOWN, 
            FIRING, 
            SUPPRESSION, 
            AUTO;
        }
        
        public enum StrobeLightDetection
        {
            NO_DETECTION, 
            RESERVED, 
            LIGHT_NOT_DETECTED, 
            LIGHT_DETECTED;
        }
        
        public enum FlashFired
        {
            NO, 
            YES;
        }
    }
    
    public interface MeteringMode
    {
        public static final short UNKNOWN = 0;
        public static final short AVERAGE = 1;
        public static final short CENTER_WEIGHTED_AVERAGE = 2;
        public static final short SPOT = 3;
        public static final short MULTISPOT = 4;
        public static final short PATTERN = 5;
        public static final short PARTAIL = 6;
        public static final short OTHER = 255;
    }
    
    public interface ExposureProgram
    {
        public static final short NOT_DEFINED = 0;
        public static final short MANUAL = 1;
        public static final short NORMAL_PROGRAM = 2;
        public static final short APERTURE_PRIORITY = 3;
        public static final short SHUTTER_PRIORITY = 4;
        public static final short CREATIVE_PROGRAM = 5;
        public static final short ACTION_PROGRAM = 6;
        public static final short PROTRAIT_MODE = 7;
        public static final short LANDSCAPE_MODE = 8;
    }
    
    public interface PlanarConfiguration
    {
        public static final short CHUNKY = 1;
        public static final short PLANAR = 2;
    }
    
    public interface PhotometricInterpretation
    {
        public static final short RGB = 2;
        public static final short YCBCR = 6;
    }
    
    public interface ResolutionUnit
    {
        public static final short INCHES = 2;
        public static final short CENTIMETERS = 3;
        public static final short MILLIMETERS = 4;
        public static final short MICROMETERS = 5;
    }
    
    public interface Compression
    {
        public static final short UNCOMPRESSION = 1;
        public static final short JPEG = 6;
    }
    
    public interface YCbCrPositioning
    {
        public static final short CENTERED = 1;
        public static final short CO_SITED = 2;
    }
    
    public interface Orientation
    {
        public static final short TOP_LEFT = 1;
        public static final short TOP_RIGHT = 2;
        public static final short BOTTOM_RIGHT = 3;
        public static final short BOTTOM_LEFT = 4;
        public static final short LEFT_TOP = 5;
        public static final short RIGHT_TOP = 6;
        public static final short RIGHT_BOTTOM = 7;
        public static final short LEFT_BOTTOM = 8;
    }
}
