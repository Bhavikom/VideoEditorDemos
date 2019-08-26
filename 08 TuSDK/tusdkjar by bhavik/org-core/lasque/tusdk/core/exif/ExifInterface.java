package org.lasque.tusdk.core.exif;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.SparseIntArray;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;

public class ExifInterface
{
  public static final int TAG_NULL = -1;
  public static final int IFD_NULL = -1;
  public static final int DEFINITION_NULL = 0;
  public static final int TAG_IMAGE_WIDTH = defineTag(0, (short)256);
  public static final int TAG_IMAGE_LENGTH = defineTag(0, (short)257);
  public static final int TAG_BITS_PER_SAMPLE = defineTag(0, (short)258);
  public static final int TAG_COMPRESSION = defineTag(0, (short)259);
  public static final int TAG_PHOTOMETRIC_INTERPRETATION = defineTag(0, (short)262);
  public static final int TAG_IMAGE_DESCRIPTION = defineTag(0, (short)270);
  public static final int TAG_MAKE = defineTag(0, (short)271);
  public static final int TAG_MODEL = defineTag(0, (short)272);
  public static final int TAG_STRIP_OFFSETS = defineTag(0, (short)273);
  public static final int TAG_ORIENTATION = defineTag(0, (short)274);
  public static final int TAG_SAMPLES_PER_PIXEL = defineTag(0, (short)277);
  public static final int TAG_ROWS_PER_STRIP = defineTag(0, (short)278);
  public static final int TAG_STRIP_BYTE_COUNTS = defineTag(0, (short)279);
  public static final int TAG_INTEROP_VERSION = defineTag(3, (short)2);
  public static final int TAG_X_RESOLUTION = defineTag(0, (short)282);
  public static final int TAG_Y_RESOLUTION = defineTag(0, (short)283);
  public static final int TAG_PLANAR_CONFIGURATION = defineTag(0, (short)284);
  public static final int TAG_RESOLUTION_UNIT = defineTag(0, (short)296);
  public static final int TAG_TRANSFER_FUNCTION = defineTag(0, (short)301);
  public static final int TAG_SOFTWARE = defineTag(0, (short)305);
  public static final int TAG_DATE_TIME = defineTag(0, (short)306);
  public static final int TAG_ARTIST = defineTag(0, (short)315);
  public static final int TAG_WHITE_POINT = defineTag(0, (short)318);
  public static final int TAG_PRIMARY_CHROMATICITIES = defineTag(0, (short)319);
  public static final int TAG_Y_CB_CR_COEFFICIENTS = defineTag(0, (short)529);
  public static final int TAG_Y_CB_CR_SUB_SAMPLING = defineTag(0, (short)530);
  public static final int TAG_Y_CB_CR_POSITIONING = defineTag(0, (short)531);
  public static final int TAG_REFERENCE_BLACK_WHITE = defineTag(0, (short)532);
  public static final int TAG_COPYRIGHT = defineTag(0, (short)33432);
  public static final int TAG_EXIF_IFD = defineTag(0, (short)34665);
  public static final int TAG_GPS_IFD = defineTag(0, (short)34853);
  public static final int TAG_JPEG_INTERCHANGE_FORMAT = defineTag(1, (short)513);
  public static final int TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = defineTag(1, (short)514);
  public static final int TAG_EXPOSURE_TIME = defineTag(2, (short)33434);
  public static final int TAG_F_NUMBER = defineTag(2, (short)33437);
  public static final int TAG_EXPOSURE_PROGRAM = defineTag(2, (short)34850);
  public static final int TAG_SPECTRAL_SENSITIVITY = defineTag(2, (short)34852);
  public static final int TAG_ISO_SPEED_RATINGS = defineTag(2, (short)34855);
  public static final int TAG_OECF = defineTag(2, (short)34856);
  public static final int TAG_EXIF_VERSION = defineTag(2, (short)36864);
  public static final int TAG_DATE_TIME_ORIGINAL = defineTag(2, (short)36867);
  public static final int TAG_DATE_TIME_DIGITIZED = defineTag(2, (short)36868);
  public static final int TAG_COMPONENTS_CONFIGURATION = defineTag(2, (short)37121);
  public static final int TAG_COMPRESSED_BITS_PER_PIXEL = defineTag(2, (short)37122);
  public static final int TAG_SHUTTER_SPEED_VALUE = defineTag(2, (short)37377);
  public static final int TAG_APERTURE_VALUE = defineTag(2, (short)37378);
  public static final int TAG_BRIGHTNESS_VALUE = defineTag(2, (short)37379);
  public static final int TAG_EXPOSURE_BIAS_VALUE = defineTag(2, (short)37380);
  public static final int TAG_MAX_APERTURE_VALUE = defineTag(2, (short)37381);
  public static final int TAG_SUBJECT_DISTANCE = defineTag(2, (short)37382);
  public static final int TAG_METERING_MODE = defineTag(2, (short)37383);
  public static final int TAG_LIGHT_SOURCE = defineTag(2, (short)37384);
  public static final int TAG_FLASH = defineTag(2, (short)37385);
  public static final int TAG_FOCAL_LENGTH = defineTag(2, (short)37386);
  public static final int TAG_SUBJECT_AREA = defineTag(2, (short)37396);
  public static final int TAG_MAKER_NOTE = defineTag(2, (short)37500);
  public static final int TAG_USER_COMMENT = defineTag(2, (short)37510);
  public static final int TAG_SUB_SEC_TIME = defineTag(2, (short)37520);
  public static final int TAG_SUB_SEC_TIME_ORIGINAL = defineTag(2, (short)37521);
  public static final int TAG_SUB_SEC_TIME_DIGITIZED = defineTag(2, (short)37522);
  public static final int TAG_FLASHPIX_VERSION = defineTag(2, (short)40960);
  public static final int TAG_COLOR_SPACE = defineTag(2, (short)40961);
  public static final int TAG_PIXEL_X_DIMENSION = defineTag(2, (short)40962);
  public static final int TAG_PIXEL_Y_DIMENSION = defineTag(2, (short)40963);
  public static final int TAG_RELATED_SOUND_FILE = defineTag(2, (short)40964);
  public static final int TAG_INTEROPERABILITY_IFD = defineTag(2, (short)40965);
  public static final int TAG_FLASH_ENERGY = defineTag(2, (short)41483);
  public static final int TAG_SPATIAL_FREQUENCY_RESPONSE = defineTag(2, (short)41484);
  public static final int TAG_FOCAL_PLANE_X_RESOLUTION = defineTag(2, (short)41486);
  public static final int TAG_FOCAL_PLANE_Y_RESOLUTION = defineTag(2, (short)41487);
  public static final int TAG_FOCAL_PLANE_RESOLUTION_UNIT = defineTag(2, (short)41488);
  public static final int TAG_SUBJECT_LOCATION = defineTag(2, (short)41492);
  public static final int TAG_EXPOSURE_INDEX = defineTag(2, (short)41493);
  public static final int TAG_SENSING_METHOD = defineTag(2, (short)41495);
  public static final int TAG_FILE_SOURCE = defineTag(2, (short)41728);
  public static final int TAG_SCENE_TYPE = defineTag(2, (short)41729);
  public static final int TAG_CFA_PATTERN = defineTag(2, (short)41730);
  public static final int TAG_CUSTOM_RENDERED = defineTag(2, (short)41985);
  public static final int TAG_EXPOSURE_MODE = defineTag(2, (short)41986);
  public static final int TAG_WHITE_BALANCE = defineTag(2, (short)41987);
  public static final int TAG_DIGITAL_ZOOM_RATIO = defineTag(2, (short)41988);
  public static final int TAG_FOCAL_LENGTH_IN_35_MM_FILE = defineTag(2, (short)41989);
  public static final int TAG_SCENE_CAPTURE_TYPE = defineTag(2, (short)41990);
  public static final int TAG_GAIN_CONTROL = defineTag(2, (short)41991);
  public static final int TAG_CONTRAST = defineTag(2, (short)41992);
  public static final int TAG_SATURATION = defineTag(2, (short)41993);
  public static final int TAG_SHARPNESS = defineTag(2, (short)41994);
  public static final int TAG_DEVICE_SETTING_DESCRIPTION = defineTag(2, (short)41995);
  public static final int TAG_SUBJECT_DISTANCE_RANGE = defineTag(2, (short)41996);
  public static final int TAG_IMAGE_UNIQUE_ID = defineTag(2, (short)42016);
  public static final int TAG_LENS_SPECS = defineTag(2, (short)42034);
  public static final int TAG_LENS_MAKE = defineTag(2, (short)42035);
  public static final int TAG_LENS_MODEL = defineTag(2, (short)42036);
  public static final int TAG_SENSITIVITY_TYPE = defineTag(2, (short)34864);
  public static final int TAG_GPS_VERSION_ID = defineTag(4, (short)0);
  public static final int TAG_GPS_LATITUDE_REF = defineTag(4, (short)1);
  public static final int TAG_GPS_LATITUDE = defineTag(4, (short)2);
  public static final int TAG_GPS_LONGITUDE_REF = defineTag(4, (short)3);
  public static final int TAG_GPS_LONGITUDE = defineTag(4, (short)4);
  public static final int TAG_GPS_ALTITUDE_REF = defineTag(4, (short)5);
  public static final int TAG_GPS_ALTITUDE = defineTag(4, (short)6);
  public static final int TAG_GPS_TIME_STAMP = defineTag(4, (short)7);
  public static final int TAG_GPS_SATTELLITES = defineTag(4, (short)8);
  public static final int TAG_GPS_STATUS = defineTag(4, (short)9);
  public static final int TAG_GPS_MEASURE_MODE = defineTag(4, (short)10);
  public static final int TAG_GPS_DOP = defineTag(4, (short)11);
  public static final int TAG_GPS_SPEED_REF = defineTag(4, (short)12);
  public static final int TAG_GPS_SPEED = defineTag(4, (short)13);
  public static final int TAG_GPS_TRACK_REF = defineTag(4, (short)14);
  public static final int TAG_GPS_TRACK = defineTag(4, (short)15);
  public static final int TAG_GPS_IMG_DIRECTION_REF = defineTag(4, (short)16);
  public static final int TAG_GPS_IMG_DIRECTION = defineTag(4, (short)17);
  public static final int TAG_GPS_MAP_DATUM = defineTag(4, (short)18);
  public static final int TAG_GPS_DEST_LATITUDE_REF = defineTag(4, (short)19);
  public static final int TAG_GPS_DEST_LATITUDE = defineTag(4, (short)20);
  public static final int TAG_GPS_DEST_LONGITUDE_REF = defineTag(4, (short)21);
  public static final int TAG_GPS_DEST_LONGITUDE = defineTag(4, (short)22);
  public static final int TAG_GPS_DEST_BEARING_REF = defineTag(4, (short)23);
  public static final int TAG_GPS_DEST_BEARING = defineTag(4, (short)24);
  public static final int TAG_GPS_DEST_DISTANCE_REF = defineTag(4, (short)25);
  public static final int TAG_GPS_DEST_DISTANCE = defineTag(4, (short)26);
  public static final int TAG_GPS_PROCESSING_METHOD = defineTag(4, (short)27);
  public static final int TAG_GPS_AREA_INFORMATION = defineTag(4, (short)28);
  public static final int TAG_GPS_DATE_STAMP = defineTag(4, (short)29);
  public static final int TAG_GPS_DIFFERENTIAL = defineTag(4, (short)30);
  public static final int TAG_INTEROPERABILITY_INDEX = defineTag(3, (short)1);
  public static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
  private ExifData a = new ExifData(DEFAULT_BYTE_ORDER);
  @SuppressLint({"SimpleDateFormat"})
  private static final DateFormat b = new SimpleDateFormat("yyyy:MM:dd");
  @SuppressLint({"SimpleDateFormat"})
  private static final DateFormat c = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
  private static HashSet<Short> d = new HashSet();
  protected static HashSet<Short> sBannedDefines;
  private final Calendar e = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
  private SparseIntArray f = null;
  
  public ExifInterface()
  {
    b.setTimeZone(TimeZone.getTimeZone("UTC"));
  }
  
  protected static boolean isOffsetTag(short paramShort)
  {
    return d.contains(Short.valueOf(paramShort));
  }
  
  public static short getOrientationValueForRotation(int paramInt)
  {
    paramInt %= 360;
    if (paramInt < 0) {
      paramInt += 360;
    }
    if (paramInt < 90) {
      return 1;
    }
    if (paramInt < 180) {
      return 6;
    }
    if (paramInt < 270) {
      return 4;
    }
    return 7;
  }
  
  public static int getRotationForOrientationValue(short paramShort)
  {
    switch (paramShort)
    {
    case 1: 
      return 0;
    case 6: 
      return 90;
    case 4: 
      return 180;
    case 7: 
      return 270;
    }
    return 0;
  }
  
  public double getResolutionUnit(int paramInt)
  {
    switch (paramInt)
    {
    case 1: 
    case 2: 
      return 25.4D;
    case 3: 
      return 10.0D;
    case 4: 
      return 1.0D;
    case 5: 
      return 0.001D;
    }
    return 25.4D;
  }
  
  public static double convertLatOrLongToDouble(Rational[] paramArrayOfRational, String paramString)
  {
    try
    {
      double d1 = paramArrayOfRational[0].toDouble();
      double d2 = paramArrayOfRational[1].toDouble();
      double d3 = paramArrayOfRational[2].toDouble();
      double d4 = d1 + d2 / 60.0D + d3 / 3600.0D;
      if ((paramString.startsWith("S")) || (paramString.startsWith("W"))) {
        return -d4;
      }
      return d4;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      throw new IllegalArgumentException();
    }
  }
  
  protected static int[] getAllowedIfdsFromInfo(int paramInt)
  {
    int i = getAllowedIfdFlagsFromInfo(paramInt);
    int[] arrayOfInt1 = IfdData.getIfds();
    ArrayList localArrayList = new ArrayList();
    for (int j = 0; j < 5; j++)
    {
      k = i >> j & 0x1;
      if (k == 1) {
        localArrayList.add(Integer.valueOf(arrayOfInt1[j]));
      }
    }
    if (localArrayList.size() <= 0) {
      return null;
    }
    int[] arrayOfInt2 = new int[localArrayList.size()];
    int k = 0;
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      int m = ((Integer)localIterator.next()).intValue();
      arrayOfInt2[(k++)] = m;
    }
    return arrayOfInt2;
  }
  
  public void readExif(String paramString, int paramInt)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Argument is null");
    }
    BufferedInputStream localBufferedInputStream = null;
    try
    {
      localBufferedInputStream = new BufferedInputStream(new FileInputStream(paramString));
      readExif(localBufferedInputStream, paramInt);
    }
    catch (IOException localIOException)
    {
      closeSilently(localBufferedInputStream);
      throw localIOException;
    }
    localBufferedInputStream.close();
  }
  
  public void readExif(InputStream paramInputStream, int paramInt)
  {
    if (paramInputStream == null) {
      throw new IllegalArgumentException("Argument is null");
    }
    ExifData localExifData;
    try
    {
      localExifData = new ExifReader(this).read(paramInputStream, paramInt);
    }
    catch (ExifInvalidFormatException localExifInvalidFormatException)
    {
      throw new IOException("Invalid exif format : " + localExifInvalidFormatException);
    }
    this.a = localExifData;
  }
  
  protected static void closeSilently(Closeable paramCloseable)
  {
    if (paramCloseable != null) {
      try
      {
        paramCloseable.close();
      }
      catch (Throwable localThrowable) {}
    }
  }
  
  public void setExif(Collection<ExifTag> paramCollection)
  {
    clearExif();
    setTags(paramCollection);
  }
  
  public void clearExif()
  {
    this.a = new ExifData(DEFAULT_BYTE_ORDER);
  }
  
  public void setTags(Collection<ExifTag> paramCollection)
  {
    if (null == paramCollection) {
      return;
    }
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      ExifTag localExifTag = (ExifTag)localIterator.next();
      setTag(localExifTag);
    }
  }
  
  public ExifTag setTag(ExifTag paramExifTag)
  {
    return this.a.addTag(paramExifTag);
  }
  
  public void writeExif(String paramString)
  {
    TLog.i("%s writeExif: %s", new Object[] { "ExifInterface", paramString });
    File localFile1 = new File(paramString);
    File localFile2 = new File(paramString + ".t");
    localFile2.delete();
    try
    {
      if (!writeExif(localFile1.getAbsolutePath(), localFile2.getAbsolutePath())) {
        return;
      }
      localFile2.renameTo(localFile1);
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    finally
    {
      localFile2.delete();
    }
  }
  
  public boolean writeExif(String paramString1, String paramString2)
  {
    TLog.i(" %s writeExif: %s", new Object[] { "ExifInterface", paramString2 });
    if (paramString1.equals(paramString2)) {
      return false;
    }
    FileInputStream localFileInputStream = new FileInputStream(paramString1);
    FileChannel localFileChannel1 = localFileInputStream.getChannel();
    if ((localFileChannel1 == null) || (localFileChannel1.size() <= 0L))
    {
      FileHelper.safeClose(localFileInputStream);
      return false;
    }
    FileOutputStream localFileOutputStream = new FileOutputStream(paramString2);
    int i = a(localFileInputStream, localFileOutputStream, this.a);
    FileChannel localFileChannel2 = localFileOutputStream.getChannel();
    if ((i >= 0) && (localFileChannel1.size() - i >= 0L)) {
      localFileChannel1.transferTo(i, localFileChannel1.size() - i, localFileChannel2);
    } else {
      TLog.e("try to call FileChannel.transferTo() with negative number.", new Object[0]);
    }
    localFileOutputStream.flush();
    FileHelper.safeClose(localFileInputStream);
    FileHelper.safeClose(localFileOutputStream);
    return true;
  }
  
  public void writeExif(InputStream paramInputStream, String paramString)
  {
    TLog.i(" %s writeExif: %s", new Object[] { "ExifInterface", paramString });
    FileOutputStream localFileOutputStream = new FileOutputStream(paramString);
    a(paramInputStream, localFileOutputStream, this.a);
    FileHelper.copy(paramInputStream, localFileOutputStream);
    localFileOutputStream.flush();
    localFileOutputStream.close();
  }
  
  public void writeExif(Bitmap paramBitmap, String paramString, int paramInt)
  {
    TLog.i(" %s writeExif: %s", new Object[] { "ExifInterface", paramString });
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, paramInt, localByteArrayOutputStream);
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(localByteArrayOutputStream.toByteArray());
    localByteArrayOutputStream.close();
    writeExif(localByteArrayInputStream, paramString);
  }
  
  private static int a(InputStream paramInputStream, OutputStream paramOutputStream, ExifData paramExifData)
  {
    ExifInterface localExifInterface = new ExifInterface();
    localExifInterface.readExif(paramInputStream, 0);
    paramOutputStream.write(255);
    paramOutputStream.write(216);
    List localList = localExifInterface.a.getSections();
    if (((ExifParser.Section)localList.get(0)).b != 224)
    {
      TLog.w("%s first section is not a JFIF or EXIF tag", new Object[] { "ExifInterface" });
      paramOutputStream.write(JpegHeader.JFIF_HEADER);
    }
    ExifOutputStream localExifOutputStream = new ExifOutputStream(localExifInterface);
    localExifOutputStream.setExifData(paramExifData);
    localExifOutputStream.writeExifData(paramOutputStream);
    for (int i = 0; i < localList.size() - 1; i++)
    {
      ExifParser.Section localSection2 = (ExifParser.Section)localList.get(i);
      paramOutputStream.write(255);
      paramOutputStream.write(localSection2.b);
      paramOutputStream.write(localSection2.c);
    }
    ExifParser.Section localSection1 = (ExifParser.Section)localList.get(localList.size() - 1);
    paramOutputStream.write(255);
    paramOutputStream.write(localSection1.b);
    paramOutputStream.write(localSection1.c);
    return localExifInterface.a.mUncompressedDataPosition;
  }
  
  public List<ExifTag> getAllTags()
  {
    return this.a.getAllTags();
  }
  
  public void readExif(byte[] paramArrayOfByte, int paramInt)
  {
    readExif(new ByteArrayInputStream(paramArrayOfByte), paramInt);
  }
  
  public List<ExifTag> getTagsForTagId(short paramShort)
  {
    return this.a.getAllTagsForTagId(paramShort);
  }
  
  public List<ExifTag> getTagsForIfdId(int paramInt)
  {
    return this.a.getAllTagsForIfd(paramInt);
  }
  
  public ExifTag getTag(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTag(paramInt, i);
  }
  
  public int getDefinedTagDefaultIfd(int paramInt)
  {
    int i = getTagInfo().get(paramInt);
    if (i == 0) {
      return -1;
    }
    return getTrueIfd(paramInt);
  }
  
  public ExifTag getTag(int paramInt1, int paramInt2)
  {
    if (!ExifTag.isValidIfd(paramInt2)) {
      return null;
    }
    return this.a.getTag(getTrueTagKey(paramInt1), paramInt2);
  }
  
  protected SparseIntArray getTagInfo()
  {
    if (this.f == null)
    {
      this.f = new SparseIntArray();
      a();
    }
    return this.f;
  }
  
  public static int getTrueIfd(int paramInt)
  {
    return paramInt >>> 16;
  }
  
  public static short getTrueTagKey(int paramInt)
  {
    return (short)paramInt;
  }
  
  private void a()
  {
    int[] arrayOfInt1 = { 0, 1 };
    int i = getFlagsFromAllowedIfds(arrayOfInt1) << 24;
    this.f.put(TAG_MAKE, i | 0x20000);
    this.f.put(TAG_IMAGE_WIDTH, i | 0x40000 | 0x1);
    this.f.put(TAG_IMAGE_LENGTH, i | 0x40000 | 0x1);
    this.f.put(TAG_BITS_PER_SAMPLE, i | 0x30000 | 0x3);
    this.f.put(TAG_COMPRESSION, i | 0x30000 | 0x1);
    this.f.put(TAG_PHOTOMETRIC_INTERPRETATION, i | 0x30000 | 0x1);
    this.f.put(TAG_ORIENTATION, i | 0x30000 | 0x1);
    this.f.put(TAG_SAMPLES_PER_PIXEL, i | 0x30000 | 0x1);
    this.f.put(TAG_PLANAR_CONFIGURATION, i | 0x30000 | 0x1);
    this.f.put(TAG_Y_CB_CR_SUB_SAMPLING, i | 0x30000 | 0x2);
    this.f.put(TAG_Y_CB_CR_POSITIONING, i | 0x30000 | 0x1);
    this.f.put(TAG_X_RESOLUTION, i | 0x50000 | 0x1);
    this.f.put(TAG_Y_RESOLUTION, i | 0x50000 | 0x1);
    this.f.put(TAG_RESOLUTION_UNIT, i | 0x30000 | 0x1);
    this.f.put(TAG_STRIP_OFFSETS, i | 0x40000);
    this.f.put(TAG_ROWS_PER_STRIP, i | 0x40000 | 0x1);
    this.f.put(TAG_STRIP_BYTE_COUNTS, i | 0x40000);
    this.f.put(TAG_TRANSFER_FUNCTION, i | 0x30000 | 0x300);
    this.f.put(TAG_WHITE_POINT, i | 0x50000 | 0x2);
    this.f.put(TAG_PRIMARY_CHROMATICITIES, i | 0x50000 | 0x6);
    this.f.put(TAG_Y_CB_CR_COEFFICIENTS, i | 0x50000 | 0x3);
    this.f.put(TAG_REFERENCE_BLACK_WHITE, i | 0x50000 | 0x6);
    this.f.put(TAG_DATE_TIME, i | 0x20000 | 0x14);
    this.f.put(TAG_IMAGE_DESCRIPTION, i | 0x20000);
    this.f.put(TAG_MODEL, i | 0x20000);
    this.f.put(TAG_SOFTWARE, i | 0x20000);
    this.f.put(TAG_ARTIST, i | 0x20000);
    this.f.put(TAG_COPYRIGHT, i | 0x20000);
    this.f.put(TAG_EXIF_IFD, i | 0x40000 | 0x1);
    this.f.put(TAG_GPS_IFD, i | 0x40000 | 0x1);
    int[] arrayOfInt2 = { 1 };
    int j = getFlagsFromAllowedIfds(arrayOfInt2) << 24;
    this.f.put(TAG_JPEG_INTERCHANGE_FORMAT, j | 0x40000 | 0x1);
    this.f.put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, j | 0x40000 | 0x1);
    int[] arrayOfInt3 = { 2 };
    int k = getFlagsFromAllowedIfds(arrayOfInt3) << 24;
    this.f.put(TAG_EXIF_VERSION, k | 0x70000 | 0x4);
    this.f.put(TAG_FLASHPIX_VERSION, k | 0x70000 | 0x4);
    this.f.put(TAG_COLOR_SPACE, k | 0x30000 | 0x1);
    this.f.put(TAG_COMPONENTS_CONFIGURATION, k | 0x70000 | 0x4);
    this.f.put(TAG_COMPRESSED_BITS_PER_PIXEL, k | 0x50000 | 0x1);
    this.f.put(TAG_PIXEL_X_DIMENSION, k | 0x40000 | 0x1);
    this.f.put(TAG_PIXEL_Y_DIMENSION, k | 0x40000 | 0x1);
    this.f.put(TAG_MAKER_NOTE, k | 0x70000);
    this.f.put(TAG_USER_COMMENT, k | 0x70000);
    this.f.put(TAG_RELATED_SOUND_FILE, k | 0x20000 | 0xD);
    this.f.put(TAG_DATE_TIME_ORIGINAL, k | 0x20000 | 0x14);
    this.f.put(TAG_DATE_TIME_DIGITIZED, k | 0x20000 | 0x14);
    this.f.put(TAG_SUB_SEC_TIME, k | 0x20000);
    this.f.put(TAG_SUB_SEC_TIME_ORIGINAL, k | 0x20000);
    this.f.put(TAG_SUB_SEC_TIME_DIGITIZED, k | 0x20000);
    this.f.put(TAG_IMAGE_UNIQUE_ID, k | 0x20000 | 0x21);
    this.f.put(TAG_LENS_SPECS, k | 0xA0000 | 0x4);
    this.f.put(TAG_LENS_MAKE, k | 0x20000);
    this.f.put(TAG_LENS_MODEL, k | 0x20000);
    this.f.put(TAG_SENSITIVITY_TYPE, k | 0x30000 | 0x1);
    this.f.put(TAG_EXPOSURE_TIME, k | 0x50000 | 0x1);
    this.f.put(TAG_F_NUMBER, k | 0x50000 | 0x1);
    this.f.put(TAG_EXPOSURE_PROGRAM, k | 0x30000 | 0x1);
    this.f.put(TAG_SPECTRAL_SENSITIVITY, k | 0x20000);
    this.f.put(TAG_ISO_SPEED_RATINGS, k | 0x30000);
    this.f.put(TAG_OECF, k | 0x70000);
    this.f.put(TAG_SHUTTER_SPEED_VALUE, k | 0xA0000 | 0x1);
    this.f.put(TAG_APERTURE_VALUE, k | 0x50000 | 0x1);
    this.f.put(TAG_BRIGHTNESS_VALUE, k | 0xA0000 | 0x1);
    this.f.put(TAG_EXPOSURE_BIAS_VALUE, k | 0xA0000 | 0x1);
    this.f.put(TAG_MAX_APERTURE_VALUE, k | 0x50000 | 0x1);
    this.f.put(TAG_SUBJECT_DISTANCE, k | 0x50000 | 0x1);
    this.f.put(TAG_METERING_MODE, k | 0x30000 | 0x1);
    this.f.put(TAG_LIGHT_SOURCE, k | 0x30000 | 0x1);
    this.f.put(TAG_FLASH, k | 0x30000 | 0x1);
    this.f.put(TAG_FOCAL_LENGTH, k | 0x50000 | 0x1);
    this.f.put(TAG_SUBJECT_AREA, k | 0x30000);
    this.f.put(TAG_FLASH_ENERGY, k | 0x50000 | 0x1);
    this.f.put(TAG_SPATIAL_FREQUENCY_RESPONSE, k | 0x70000);
    this.f.put(TAG_FOCAL_PLANE_X_RESOLUTION, k | 0x50000 | 0x1);
    this.f.put(TAG_FOCAL_PLANE_Y_RESOLUTION, k | 0x50000 | 0x1);
    this.f.put(TAG_FOCAL_PLANE_RESOLUTION_UNIT, k | 0x30000 | 0x1);
    this.f.put(TAG_SUBJECT_LOCATION, k | 0x30000 | 0x2);
    this.f.put(TAG_EXPOSURE_INDEX, k | 0x50000 | 0x1);
    this.f.put(TAG_SENSING_METHOD, k | 0x30000 | 0x1);
    this.f.put(TAG_FILE_SOURCE, k | 0x70000 | 0x1);
    this.f.put(TAG_SCENE_TYPE, k | 0x70000 | 0x1);
    this.f.put(TAG_CFA_PATTERN, k | 0x70000);
    this.f.put(TAG_CUSTOM_RENDERED, k | 0x30000 | 0x1);
    this.f.put(TAG_EXPOSURE_MODE, k | 0x30000 | 0x1);
    this.f.put(TAG_WHITE_BALANCE, k | 0x30000 | 0x1);
    this.f.put(TAG_DIGITAL_ZOOM_RATIO, k | 0x50000 | 0x1);
    this.f.put(TAG_FOCAL_LENGTH_IN_35_MM_FILE, k | 0x30000 | 0x1);
    this.f.put(TAG_SCENE_CAPTURE_TYPE, k | 0x30000 | 0x1);
    this.f.put(TAG_GAIN_CONTROL, k | 0x50000 | 0x1);
    this.f.put(TAG_CONTRAST, k | 0x30000 | 0x1);
    this.f.put(TAG_SATURATION, k | 0x30000 | 0x1);
    this.f.put(TAG_SHARPNESS, k | 0x30000 | 0x1);
    this.f.put(TAG_DEVICE_SETTING_DESCRIPTION, k | 0x70000);
    this.f.put(TAG_SUBJECT_DISTANCE_RANGE, k | 0x30000 | 0x1);
    this.f.put(TAG_INTEROPERABILITY_IFD, k | 0x40000 | 0x1);
    int[] arrayOfInt4 = { 4 };
    int m = getFlagsFromAllowedIfds(arrayOfInt4) << 24;
    this.f.put(TAG_GPS_VERSION_ID, m | 0x10000 | 0x4);
    this.f.put(TAG_GPS_LATITUDE_REF, m | 0x20000 | 0x2);
    this.f.put(TAG_GPS_LONGITUDE_REF, m | 0x20000 | 0x2);
    this.f.put(TAG_GPS_LATITUDE, m | 0xA0000 | 0x3);
    this.f.put(TAG_GPS_LONGITUDE, m | 0xA0000 | 0x3);
    this.f.put(TAG_GPS_ALTITUDE_REF, m | 0x10000 | 0x1);
    this.f.put(TAG_GPS_ALTITUDE, m | 0x50000 | 0x1);
    this.f.put(TAG_GPS_TIME_STAMP, m | 0x50000 | 0x3);
    this.f.put(TAG_GPS_SATTELLITES, m | 0x20000);
    this.f.put(TAG_GPS_STATUS, m | 0x20000 | 0x2);
    this.f.put(TAG_GPS_MEASURE_MODE, m | 0x20000 | 0x2);
    this.f.put(TAG_GPS_DOP, m | 0x50000 | 0x1);
    this.f.put(TAG_GPS_SPEED_REF, m | 0x20000 | 0x2);
    this.f.put(TAG_GPS_SPEED, m | 0x50000 | 0x1);
    this.f.put(TAG_GPS_TRACK_REF, m | 0x20000 | 0x2);
    this.f.put(TAG_GPS_TRACK, m | 0x50000 | 0x1);
    this.f.put(TAG_GPS_IMG_DIRECTION_REF, m | 0x20000 | 0x2);
    this.f.put(TAG_GPS_IMG_DIRECTION, m | 0x50000 | 0x1);
    this.f.put(TAG_GPS_MAP_DATUM, m | 0x20000);
    this.f.put(TAG_GPS_DEST_LATITUDE_REF, m | 0x20000 | 0x2);
    this.f.put(TAG_GPS_DEST_LATITUDE, m | 0x50000 | 0x1);
    this.f.put(TAG_GPS_DEST_BEARING_REF, m | 0x20000 | 0x2);
    this.f.put(TAG_GPS_DEST_BEARING, m | 0x50000 | 0x1);
    this.f.put(TAG_GPS_DEST_DISTANCE_REF, m | 0x20000 | 0x2);
    this.f.put(TAG_GPS_DEST_DISTANCE, m | 0x50000 | 0x1);
    this.f.put(TAG_GPS_PROCESSING_METHOD, m | 0x70000);
    this.f.put(TAG_GPS_AREA_INFORMATION, m | 0x70000);
    this.f.put(TAG_GPS_DATE_STAMP, m | 0x20000 | 0xB);
    this.f.put(TAG_GPS_DIFFERENTIAL, m | 0x30000 | 0xB);
    int[] arrayOfInt5 = { 3 };
    int n = getFlagsFromAllowedIfds(arrayOfInt5) << 24;
    this.f.put(TAG_INTEROPERABILITY_INDEX, n | 0x20000);
    this.f.put(TAG_INTEROP_VERSION, n | 0x70000 | 0x4);
  }
  
  protected static int getFlagsFromAllowedIfds(int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt == null) || (paramArrayOfInt.length == 0)) {
      return 0;
    }
    int i = 0;
    int[] arrayOfInt1 = IfdData.getIfds();
    for (int j = 0; j < 5; j++) {
      for (int n : paramArrayOfInt) {
        if (arrayOfInt1[j] == n)
        {
          i |= 1 << j;
          break;
        }
      }
    }
    return i;
  }
  
  public Object getTagValue(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTagValue(paramInt, i);
  }
  
  public Object getTagValue(int paramInt1, int paramInt2)
  {
    ExifTag localExifTag = getTag(paramInt1, paramInt2);
    return localExifTag == null ? null : localExifTag.getValue();
  }
  
  public String getTagStringValue(int paramInt1, int paramInt2)
  {
    ExifTag localExifTag = getTag(paramInt1, paramInt2);
    if (localExifTag == null) {
      return null;
    }
    return localExifTag.getValueAsString();
  }
  
  public String getTagStringValue(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTagStringValue(paramInt, i);
  }
  
  public Long getTagLongValue(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTagLongValue(paramInt, i);
  }
  
  @SuppressLint({"UseValueOf"})
  public Long getTagLongValue(int paramInt1, int paramInt2)
  {
    long[] arrayOfLong = getTagLongValues(paramInt1, paramInt2);
    if ((arrayOfLong == null) || (arrayOfLong.length <= 0)) {
      return null;
    }
    return new Long(arrayOfLong[0]);
  }
  
  @SuppressLint({"UseValueOf"})
  public long[] getTagLongValues(int paramInt1, int paramInt2)
  {
    ExifTag localExifTag = getTag(paramInt1, paramInt2);
    if (localExifTag == null) {
      return null;
    }
    return localExifTag.getValueAsLongs();
  }
  
  public Integer getTagIntValue(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTagIntValue(paramInt, i);
  }
  
  @SuppressLint({"UseValueOf"})
  public Integer getTagIntValue(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = getTagIntValues(paramInt1, paramInt2);
    if ((arrayOfInt == null) || (arrayOfInt.length <= 0)) {
      return null;
    }
    return new Integer(arrayOfInt[0]);
  }
  
  public int[] getTagIntValues(int paramInt1, int paramInt2)
  {
    ExifTag localExifTag = getTag(paramInt1, paramInt2);
    if (localExifTag == null) {
      return null;
    }
    return localExifTag.getValueAsInts();
  }
  
  public Byte getTagByteValue(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTagByteValue(paramInt, i);
  }
  
  @SuppressLint({"UseValueOf"})
  public Byte getTagByteValue(int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = getTagByteValues(paramInt1, paramInt2);
    if ((arrayOfByte == null) || (arrayOfByte.length <= 0)) {
      return null;
    }
    return new Byte(arrayOfByte[0]);
  }
  
  public byte[] getTagByteValues(int paramInt1, int paramInt2)
  {
    ExifTag localExifTag = getTag(paramInt1, paramInt2);
    if (localExifTag == null) {
      return null;
    }
    return localExifTag.getValueAsBytes();
  }
  
  public Rational getTagRationalValue(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTagRationalValue(paramInt, i);
  }
  
  public Rational getTagRationalValue(int paramInt1, int paramInt2)
  {
    Rational[] arrayOfRational = getTagRationalValues(paramInt1, paramInt2);
    if ((arrayOfRational == null) || (arrayOfRational.length == 0)) {
      return null;
    }
    return new Rational(arrayOfRational[0]);
  }
  
  public Rational[] getTagRationalValues(int paramInt1, int paramInt2)
  {
    ExifTag localExifTag = getTag(paramInt1, paramInt2);
    if (localExifTag == null) {
      return null;
    }
    return localExifTag.getValueAsRationals();
  }
  
  public long[] getTagLongValues(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTagLongValues(paramInt, i);
  }
  
  public int[] getTagIntValues(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTagIntValues(paramInt, i);
  }
  
  public byte[] getTagByteValues(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTagByteValues(paramInt, i);
  }
  
  public Rational[] getTagRationalValues(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return getTagRationalValues(paramInt, i);
  }
  
  public boolean isTagCountDefined(int paramInt)
  {
    int i = getTagInfo().get(paramInt);
    return (i != 0) && (getComponentCountFromInfo(i) != 0);
  }
  
  protected static int getComponentCountFromInfo(int paramInt)
  {
    return paramInt & 0xFFFF;
  }
  
  public int getDefinedTagCount(int paramInt)
  {
    int i = getTagInfo().get(paramInt);
    if (i == 0) {
      return 0;
    }
    return getComponentCountFromInfo(i);
  }
  
  public int getActualTagCount(int paramInt1, int paramInt2)
  {
    ExifTag localExifTag = getTag(paramInt1, paramInt2);
    if (localExifTag == null) {
      return 0;
    }
    return localExifTag.getComponentCount();
  }
  
  public short getDefinedTagType(int paramInt)
  {
    int i = getTagInfo().get(paramInt);
    if (i == 0) {
      return -1;
    }
    return getTypeFromInfo(i);
  }
  
  protected static short getTypeFromInfo(int paramInt)
  {
    return (short)(paramInt >> 16 & 0xFF);
  }
  
  protected ExifTag buildUninitializedTag(int paramInt)
  {
    int i = getTagInfo().get(paramInt);
    if (i == 0) {
      return null;
    }
    short s = getTypeFromInfo(i);
    int j = getComponentCountFromInfo(i);
    boolean bool = j != 0;
    int k = getTrueIfd(paramInt);
    return new ExifTag(getTrueTagKey(paramInt), s, j, k, bool);
  }
  
  public boolean setTagValue(int paramInt, Object paramObject)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    return setTagValue(paramInt, i, paramObject);
  }
  
  public boolean setTagValue(int paramInt1, int paramInt2, Object paramObject)
  {
    ExifTag localExifTag = getTag(paramInt1, paramInt2);
    return (localExifTag != null) && (localExifTag.setValue(paramObject));
  }
  
  public void deleteTag(int paramInt)
  {
    int i = getDefinedTagDefaultIfd(paramInt);
    deleteTag(paramInt, i);
  }
  
  public void deleteTag(int paramInt1, int paramInt2)
  {
    this.a.removeTag(getTrueTagKey(paramInt1), paramInt2);
  }
  
  public int setTagDefinition(short paramShort1, int paramInt, short paramShort2, short paramShort3, int[] paramArrayOfInt)
  {
    if (sBannedDefines.contains(Short.valueOf(paramShort1))) {
      return -1;
    }
    if ((ExifTag.isValidType(paramShort2)) && (ExifTag.isValidIfd(paramInt)))
    {
      int i = defineTag(paramInt, paramShort1);
      if (i == -1) {
        return -1;
      }
      int[] arrayOfInt1 = getTagDefinitionsForTagId(paramShort1);
      SparseIntArray localSparseIntArray = getTagInfo();
      int j = 0;
      int i1;
      for (i1 : paramArrayOfInt)
      {
        if (paramInt == i1) {
          j = 1;
        }
        if (!ExifTag.isValidIfd(i1)) {
          return -1;
        }
      }
      if (j == 0) {
        return -1;
      }
      int k = getFlagsFromAllowedIfds(paramArrayOfInt);
      if (arrayOfInt1 != null) {
        for (int i2 : arrayOfInt1)
        {
          int i3 = localSparseIntArray.get(i2);
          int i4 = getAllowedIfdFlagsFromInfo(i3);
          if ((k & i4) != 0) {
            return -1;
          }
        }
      }
      getTagInfo().put(i, k << 24 | paramShort2 << 16 | paramShort3);
      return i;
    }
    return -1;
  }
  
  protected int getTagDefinition(short paramShort, int paramInt)
  {
    return getTagInfo().get(defineTag(paramInt, paramShort));
  }
  
  public static int defineTag(int paramInt, short paramShort)
  {
    return paramShort & 0xFFFF | paramInt << 16;
  }
  
  protected int[] getTagDefinitionsForTagId(short paramShort)
  {
    int[] arrayOfInt1 = IfdData.getIfds();
    int[] arrayOfInt2 = new int[arrayOfInt1.length];
    int i = 0;
    SparseIntArray localSparseIntArray = getTagInfo();
    for (int m : arrayOfInt1)
    {
      int n = defineTag(m, paramShort);
      if (localSparseIntArray.get(n) != 0) {
        arrayOfInt2[(i++)] = n;
      }
    }
    if (i == 0) {
      return null;
    }
    return Arrays.copyOfRange(arrayOfInt2, 0, i);
  }
  
  protected int getTagDefinitionForTag(ExifTag paramExifTag)
  {
    short s = paramExifTag.getDataType();
    int i = paramExifTag.getComponentCount();
    int j = paramExifTag.getIfd();
    return getTagDefinitionForTag(paramExifTag.getTagId(), s, i, j);
  }
  
  protected int getTagDefinitionForTag(short paramShort1, short paramShort2, int paramInt1, int paramInt2)
  {
    int[] arrayOfInt1 = getTagDefinitionsForTagId(paramShort1);
    if (arrayOfInt1 == null) {
      return -1;
    }
    SparseIntArray localSparseIntArray = getTagInfo();
    int i = -1;
    for (int m : arrayOfInt1)
    {
      int n = localSparseIntArray.get(m);
      short s = getTypeFromInfo(n);
      int i1 = getComponentCountFromInfo(n);
      int[] arrayOfInt3 = getAllowedIfdsFromInfo(n);
      int i2 = 0;
      for (int i5 : arrayOfInt3) {
        if (i5 == paramInt2)
        {
          i2 = 1;
          break;
        }
      }
      if ((i2 != 0) && (paramShort2 == s) && ((paramInt1 == i1) || (i1 == 0)))
      {
        i = m;
        break;
      }
    }
    return i;
  }
  
  public void removeTagDefinition(int paramInt)
  {
    getTagInfo().delete(paramInt);
  }
  
  public void resetTagDefinitions()
  {
    this.f = null;
  }
  
  public Bitmap getThumbnailBitmap()
  {
    if (this.a.hasCompressedThumbnail())
    {
      byte[] arrayOfByte = this.a.getCompressedThumbnail();
      return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
    }
    if (this.a.hasUncompressedStrip()) {}
    return null;
  }
  
  public byte[] getThumbnailBytes()
  {
    if (this.a.hasCompressedThumbnail()) {
      return this.a.getCompressedThumbnail();
    }
    if (this.a.hasUncompressedStrip()) {}
    return null;
  }
  
  public byte[] getThumbnail()
  {
    return this.a.getCompressedThumbnail();
  }
  
  public int getQualityGuess()
  {
    return this.a.getQualityGuess();
  }
  
  public short getJpegProcess()
  {
    return this.a.getJpegProcess();
  }
  
  public int[] getImageSize()
  {
    return this.a.getImageSize();
  }
  
  public boolean isThumbnailCompressed()
  {
    return this.a.hasCompressedThumbnail();
  }
  
  public boolean hasThumbnail()
  {
    return this.a.hasCompressedThumbnail();
  }
  
  public boolean setCompressedThumbnail(Bitmap paramBitmap)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    if (!paramBitmap.compress(Bitmap.CompressFormat.JPEG, 90, localByteArrayOutputStream)) {
      return false;
    }
    return setCompressedThumbnail(localByteArrayOutputStream.toByteArray());
  }
  
  public boolean setCompressedThumbnail(byte[] paramArrayOfByte)
  {
    this.a.clearThumbnailAndStrips();
    this.a.setCompressedThumbnail(paramArrayOfByte);
    return true;
  }
  
  public void removeCompressedThumbnail()
  {
    this.a.setCompressedThumbnail(null);
  }
  
  public String getUserComment()
  {
    return this.a.getUserComment();
  }
  
  public double getAltitude(double paramDouble)
  {
    Byte localByte = getTagByteValue(TAG_GPS_ALTITUDE_REF);
    Rational localRational = getTagRationalValue(TAG_GPS_ALTITUDE);
    int i = 1;
    if (null != localByte) {
      i = localByte.intValue() == 1 ? -1 : 1;
    }
    if (localRational != null) {
      return localRational.toDouble() * i;
    }
    return paramDouble;
  }
  
  public double[] getLatLongAsDoubles()
  {
    Rational[] arrayOfRational1 = getTagRationalValues(TAG_GPS_LATITUDE);
    String str1 = getTagStringValue(TAG_GPS_LATITUDE_REF);
    Rational[] arrayOfRational2 = getTagRationalValues(TAG_GPS_LONGITUDE);
    String str2 = getTagStringValue(TAG_GPS_LONGITUDE_REF);
    if ((arrayOfRational1 == null) || (arrayOfRational2 == null) || (str1 == null) || (str2 == null) || (arrayOfRational1.length < 3) || (arrayOfRational2.length < 3)) {
      return null;
    }
    double[] arrayOfDouble = new double[2];
    arrayOfDouble[0] = convertLatOrLongToDouble(arrayOfRational1, str1);
    arrayOfDouble[1] = convertLatOrLongToDouble(arrayOfRational2, str2);
    return arrayOfDouble;
  }
  
  public String getLatitude()
  {
    Rational[] arrayOfRational = getTagRationalValues(TAG_GPS_LATITUDE);
    String str = getTagStringValue(TAG_GPS_LATITUDE_REF);
    if ((null == arrayOfRational) || (null == str)) {
      return null;
    }
    return a(arrayOfRational, str);
  }
  
  public String getLongitude()
  {
    Rational[] arrayOfRational = getTagRationalValues(TAG_GPS_LONGITUDE);
    String str = getTagStringValue(TAG_GPS_LONGITUDE_REF);
    if ((null == arrayOfRational) || (null == str)) {
      return null;
    }
    return a(arrayOfRational, str);
  }
  
  private static String a(Rational[] paramArrayOfRational, String paramString)
  {
    try
    {
      double d1 = paramArrayOfRational[0].toDouble();
      double d2 = paramArrayOfRational[1].toDouble();
      double d3 = paramArrayOfRational[2].toDouble();
      paramString = paramString.substring(0, 1);
      return String.format(Locale.getDefault(), "%1$.0f° %2$.0f' %3$.0f\" %4$s", new Object[] { Double.valueOf(d1), Double.valueOf(d2), Double.valueOf(d3), paramString.toUpperCase(Locale.getDefault()) });
    }
    catch (NumberFormatException localNumberFormatException)
    {
      localNumberFormatException.printStackTrace();
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      localArrayIndexOutOfBoundsException.printStackTrace();
    }
    return null;
  }
  
  @SuppressLint({"SimpleDateFormat"})
  public static Date getDateTime(String paramString, TimeZone paramTimeZone)
  {
    if (paramString == null) {
      return null;
    }
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
    localSimpleDateFormat.setTimeZone(paramTimeZone);
    try
    {
      return localSimpleDateFormat.parse(paramString);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
    }
    catch (ParseException localParseException)
    {
      localParseException.printStackTrace();
    }
    return null;
  }
  
  public boolean addDateTimeStampTag(int paramInt, long paramLong, TimeZone paramTimeZone)
  {
    if ((paramInt == TAG_DATE_TIME) || (paramInt == TAG_DATE_TIME_DIGITIZED) || (paramInt == TAG_DATE_TIME_ORIGINAL))
    {
      c.setTimeZone(paramTimeZone);
      ExifTag localExifTag = buildTag(paramInt, c.format(Long.valueOf(paramLong)));
      if (localExifTag == null) {
        return false;
      }
      setTag(localExifTag);
    }
    else
    {
      return false;
    }
    return true;
  }
  
  public ExifTag buildTag(int paramInt, Object paramObject)
  {
    int i = getTrueIfd(paramInt);
    return buildTag(paramInt, i, paramObject);
  }
  
  public ExifTag buildTag(int paramInt1, int paramInt2, Object paramObject)
  {
    int i = getTagInfo().get(paramInt1);
    if ((i == 0) || (paramObject == null)) {
      return null;
    }
    short s = getTypeFromInfo(i);
    int j = getComponentCountFromInfo(i);
    boolean bool = j != 0;
    if (!isIfdAllowed(i, paramInt2)) {
      return null;
    }
    ExifTag localExifTag = new ExifTag(getTrueTagKey(paramInt1), s, j, paramInt2, bool);
    if (!localExifTag.setValue(paramObject)) {
      return null;
    }
    return localExifTag;
  }
  
  protected static boolean isIfdAllowed(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = IfdData.getIfds();
    int i = getAllowedIfdFlagsFromInfo(paramInt1);
    for (int j = 0; j < arrayOfInt.length; j++) {
      if ((paramInt2 == arrayOfInt[j]) && ((i >> j & 0x1) == 1)) {
        return true;
      }
    }
    return false;
  }
  
  protected static int getAllowedIfdFlagsFromInfo(int paramInt)
  {
    return paramInt >>> 24;
  }
  
  public boolean addGpsTags(double paramDouble1, double paramDouble2)
  {
    ExifTag localExifTag1 = buildTag(TAG_GPS_LATITUDE, a(paramDouble1));
    ExifTag localExifTag2 = buildTag(TAG_GPS_LONGITUDE, a(paramDouble2));
    ExifTag localExifTag3 = buildTag(TAG_GPS_LATITUDE_REF, paramDouble1 >= 0.0D ? "N" : "S");
    ExifTag localExifTag4 = buildTag(TAG_GPS_LONGITUDE_REF, paramDouble2 >= 0.0D ? "E" : "W");
    if ((localExifTag1 == null) || (localExifTag2 == null) || (localExifTag3 == null) || (localExifTag4 == null)) {
      return false;
    }
    setTag(localExifTag1);
    setTag(localExifTag2);
    setTag(localExifTag3);
    setTag(localExifTag4);
    return true;
  }
  
  private static Rational[] a(double paramDouble)
  {
    paramDouble = Math.abs(paramDouble);
    int i = (int)paramDouble;
    paramDouble = (paramDouble - i) * 60.0D;
    int j = (int)paramDouble;
    paramDouble = (paramDouble - j) * 6000.0D;
    int k = (int)paramDouble;
    return new Rational[] { new Rational(i, 1L), new Rational(j, 1L), new Rational(k, 100L) };
  }
  
  public boolean addGpsDateTimeStampTag(long paramLong)
  {
    ExifTag localExifTag = buildTag(TAG_GPS_DATE_STAMP, b.format(Long.valueOf(paramLong)));
    if (localExifTag == null) {
      return false;
    }
    setTag(localExifTag);
    this.e.setTimeInMillis(paramLong);
    localExifTag = buildTag(TAG_GPS_TIME_STAMP, new Rational[] { new Rational(this.e.get(11), 1L), new Rational(this.e.get(12), 1L), new Rational(this.e.get(13), 1L) });
    if (localExifTag == null) {
      return false;
    }
    setTag(localExifTag);
    return true;
  }
  
  public double getApertureSize()
  {
    Rational localRational = getTagRationalValue(TAG_F_NUMBER);
    if ((null != localRational) && (localRational.toDouble() > 0.0D)) {
      return localRational.toDouble();
    }
    localRational = getTagRationalValue(TAG_APERTURE_VALUE);
    if ((null != localRational) && (localRational.toDouble() > 0.0D)) {
      return Math.exp(localRational.toDouble() * Math.log(2.0D) * 0.5D);
    }
    return 0.0D;
  }
  
  public String getLensModelDescription()
  {
    String str = getTagStringValue(TAG_LENS_MODEL);
    if (null != str) {
      return str;
    }
    Rational[] arrayOfRational = getTagRationalValues(TAG_LENS_SPECS);
    if (null != arrayOfRational) {
      return ExifUtil.processLensSpecifications(arrayOfRational);
    }
    return null;
  }
  
  public static byte[] toBitArray(short paramShort)
  {
    byte[] arrayOfByte = new byte[16];
    for (short s = 0; s < 16; s++) {
      arrayOfByte[(15 - s)] = ((byte)(paramShort >> s & 0x1));
    }
    return arrayOfByte;
  }
  
  static
  {
    d.add(Short.valueOf(getTrueTagKey(TAG_GPS_IFD)));
    d.add(Short.valueOf(getTrueTagKey(TAG_EXIF_IFD)));
    d.add(Short.valueOf(getTrueTagKey(TAG_JPEG_INTERCHANGE_FORMAT)));
    d.add(Short.valueOf(getTrueTagKey(TAG_INTEROPERABILITY_IFD)));
    d.add(Short.valueOf(getTrueTagKey(TAG_STRIP_OFFSETS)));
    sBannedDefines = new HashSet(d);
    sBannedDefines.add(Short.valueOf(getTrueTagKey(-1)));
    sBannedDefines.add(Short.valueOf(getTrueTagKey(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH)));
    sBannedDefines.add(Short.valueOf(getTrueTagKey(TAG_STRIP_BYTE_COUNTS)));
  }
  
  public static abstract interface Options
  {
    public static final int OPTION_IFD_0 = 1;
    public static final int OPTION_IFD_1 = 2;
    public static final int OPTION_IFD_EXIF = 4;
    public static final int OPTION_IFD_GPS = 8;
    public static final int OPTION_IFD_INTEROPERABILITY = 16;
    public static final int OPTION_THUMBNAIL = 32;
    public static final int OPTION_ALL = 63;
  }
  
  public static abstract interface SensitivityType
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
  
  public static abstract interface JpegProcess
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
  
  public static abstract interface GpsDifferential
  {
    public static final short WITHOUT_DIFFERENTIAL_CORRECTION = 0;
    public static final short DIFFERENTIAL_CORRECTION_APPLIED = 1;
  }
  
  public static abstract interface GpsTrackRef
  {
    public static final String TRUE_DIRECTION = "T";
    public static final String MAGNETIC_DIRECTION = "M";
  }
  
  public static abstract interface GpsSpeedRef
  {
    public static final String KILOMETERS = "K";
    public static final String MILES = "M";
    public static final String KNOTS = "N";
  }
  
  public static abstract interface GpsMeasureMode
  {
    public static final String MODE_2_DIMENSIONAL = "2";
    public static final String MODE_3_DIMENSIONAL = "3";
  }
  
  public static abstract interface GpsStatus
  {
    public static final String IN_PROGRESS = "A";
    public static final String INTEROPERABILITY = "V";
  }
  
  public static abstract interface GpsAltitudeRef
  {
    public static final short SEA_LEVEL = 0;
    public static final short SEA_LEVEL_NEGATIVE = 1;
  }
  
  public static abstract interface GpsLongitudeRef
  {
    public static final String EAST = "E";
    public static final String WEST = "W";
  }
  
  public static abstract interface GpsLatitudeRef
  {
    public static final String NORTH = "N";
    public static final String SOUTH = "S";
  }
  
  public static abstract interface SubjectDistance
  {
    public static final short UNKNOWN = 0;
    public static final short MACRO = 1;
    public static final short CLOSE_VIEW = 2;
    public static final short DISTANT_VIEW = 3;
  }
  
  public static abstract interface Sharpness
  {
    public static final short NORMAL = 0;
    public static final short SOFT = 1;
    public static final short HARD = 2;
  }
  
  public static abstract interface Saturation
  {
    public static final short NORMAL = 0;
    public static final short LOW = 1;
    public static final short HIGH = 2;
  }
  
  public static abstract interface Contrast
  {
    public static final short NORMAL = 0;
    public static final short SOFT = 1;
    public static final short HARD = 2;
  }
  
  public static abstract interface GainControl
  {
    public static final short NONE = 0;
    public static final short LOW_UP = 1;
    public static final short HIGH_UP = 2;
    public static final short LOW_DOWN = 3;
    public static final short HIGH_DOWN = 4;
  }
  
  public static abstract interface SceneType
  {
    public static final short DIRECT_PHOTOGRAPHED = 1;
  }
  
  public static abstract interface FileSource
  {
    public static final short DSC = 3;
  }
  
  public static abstract interface SensingMethod
  {
    public static final short NOT_DEFINED = 1;
    public static final short ONE_CHIP_COLOR = 2;
    public static final short TWO_CHIP_COLOR = 3;
    public static final short THREE_CHIP_COLOR = 4;
    public static final short COLOR_SEQUENTIAL_AREA = 5;
    public static final short TRILINEAR = 7;
    public static final short COLOR_SEQUENTIAL_LINEAR = 8;
  }
  
  public static abstract interface LightSource
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
  
  public static abstract interface ComponentsConfiguration
  {
    public static final short NOT_EXIST = 0;
    public static final short Y = 1;
    public static final short CB = 2;
    public static final short CR = 3;
    public static final short R = 4;
    public static final short G = 5;
    public static final short B = 6;
  }
  
  public static abstract interface SceneCapture
  {
    public static final short STANDARD = 0;
    public static final short LANDSCAPE = 1;
    public static final short PROTRAIT = 2;
    public static final short NIGHT_SCENE = 3;
  }
  
  public static abstract interface WhiteBalance
  {
    public static final short AUTO = 0;
    public static final short MANUAL = 1;
  }
  
  public static abstract interface ExposureMode
  {
    public static final short AUTO_EXPOSURE = 0;
    public static final short MANUAL_EXPOSURE = 1;
    public static final short AUTO_BRACKET = 2;
  }
  
  public static abstract interface ColorSpace
  {
    public static final short SRGB = 1;
    public static final short UNCALIBRATED = -1;
  }
  
  public static abstract interface Flash
  {
    public static enum RedEyeMode
    {
      private RedEyeMode() {}
    }
    
    public static enum FlashFunction
    {
      private FlashFunction() {}
    }
    
    public static enum CompulsoryMode
    {
      private CompulsoryMode() {}
    }
    
    public static enum StrobeLightDetection
    {
      private StrobeLightDetection() {}
    }
    
    public static enum FlashFired
    {
      private FlashFired() {}
    }
  }
  
  public static abstract interface MeteringMode
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
  
  public static abstract interface ExposureProgram
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
  
  public static abstract interface PlanarConfiguration
  {
    public static final short CHUNKY = 1;
    public static final short PLANAR = 2;
  }
  
  public static abstract interface PhotometricInterpretation
  {
    public static final short RGB = 2;
    public static final short YCBCR = 6;
  }
  
  public static abstract interface ResolutionUnit
  {
    public static final short INCHES = 2;
    public static final short CENTIMETERS = 3;
    public static final short MILLIMETERS = 4;
    public static final short MICROMETERS = 5;
  }
  
  public static abstract interface Compression
  {
    public static final short UNCOMPRESSION = 1;
    public static final short JPEG = 6;
  }
  
  public static abstract interface YCbCrPositioning
  {
    public static final short CENTERED = 1;
    public static final short CO_SITED = 2;
  }
  
  public static abstract interface Orientation
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


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\ExifInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */