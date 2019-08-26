package org.lasque.tusdk.core.utils.hardware;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraCharacteristics.Key;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build.VERSION;
import android.util.Range;
import android.util.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.view.TuSdkViewHelper;

@TargetApi(21)
public class Camera2Helper
{
  public static CameraManager cameraManager(Context paramContext)
  {
    CameraManager localCameraManager = (CameraManager)ContextUtils.getSystemService(paramContext, "camera");
    return localCameraManager;
  }
  
  public static boolean canSupportCamera(Context paramContext)
  {
    Boolean localBoolean = Boolean.valueOf((ContextUtils.hasSystemFeature(paramContext, "android.hardware.camera")) && (cameraCounts(paramContext) > 0));
    return localBoolean.booleanValue();
  }
  
  public static String[] cameraIds(Context paramContext)
  {
    return cameraIds(cameraManager(paramContext));
  }
  
  public static String[] cameraIds(CameraManager paramCameraManager)
  {
    if (paramCameraManager == null) {
      return null;
    }
    try
    {
      String[] arrayOfString = paramCameraManager.getCameraIdList();
      return arrayOfString;
    }
    catch (CameraAccessException localCameraAccessException)
    {
      TLog.e(localCameraAccessException, "Get system all camera ids error", new Object[0]);
    }
    return null;
  }
  
  public static int cameraCounts(Context paramContext)
  {
    String[] arrayOfString = cameraIds(paramContext);
    if (arrayOfString == null) {
      return 0;
    }
    return arrayOfString.length;
  }
  
  public static String firstBackCameraId(Context paramContext)
  {
    return firstCameraId(paramContext, 1);
  }
  
  public static String firstFrontCameraId(Context paramContext)
  {
    return firstCameraId(paramContext, 0);
  }
  
  public static String firstCameraId(Context paramContext, CameraConfigs.CameraFacing paramCameraFacing)
  {
    if (paramCameraFacing == null) {
      paramCameraFacing = CameraConfigs.CameraFacing.Back;
    }
    int i = 1;
    switch (3.a[paramCameraFacing.ordinal()])
    {
    case 1: 
      i = 0;
      break;
    }
    return firstCameraId(paramContext, i);
  }
  
  public static CameraConfigs.CameraFacing cameraPosition(CameraCharacteristics paramCameraCharacteristics)
  {
    if (paramCameraCharacteristics == null) {
      return null;
    }
    int i = ((Integer)paramCameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue();
    switch (i)
    {
    case 0: 
      return CameraConfigs.CameraFacing.Front;
    }
    return CameraConfigs.CameraFacing.Back;
  }
  
  public static String firstCameraId(Context paramContext, int paramInt)
  {
    CameraManager localCameraManager = cameraManager(paramContext);
    String[] arrayOfString1 = cameraIds(paramContext);
    if (arrayOfString1 == null) {
      return null;
    }
    Object localObject = null;
    for (String str : arrayOfString1)
    {
      localObject = str;
      CameraCharacteristics localCameraCharacteristics = cameraCharacter(localCameraManager, str);
      if (((Integer)localCameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == paramInt) {
        break;
      }
    }
    return (String)localObject;
  }
  
  public static CameraCharacteristics cameraCharacter(CameraManager paramCameraManager, String paramString)
  {
    if ((paramCameraManager == null) || (paramString == null)) {
      return null;
    }
    try
    {
      CameraCharacteristics localCameraCharacteristics = paramCameraManager.getCameraCharacteristics(paramString);
      return localCameraCharacteristics;
    }
    catch (CameraAccessException localCameraAccessException)
    {
      TLog.e(localCameraAccessException, "Get Camera Character error: %s", new Object[] { paramString });
    }
    return null;
  }
  
  public static StreamConfigurationMap streamConfigurationMap(CameraCharacteristics paramCameraCharacteristics)
  {
    return (StreamConfigurationMap)paramCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
  }
  
  public static <T> void mergerBuilder(CaptureRequest.Builder paramBuilder1, CaptureRequest.Builder paramBuilder2, CaptureRequest.Key<T> paramKey)
  {
    if ((paramBuilder1 == null) || (paramBuilder2 == null) || (paramKey == null)) {
      return;
    }
    Object localObject = paramBuilder1.get(paramKey);
    if (localObject != null) {
      paramBuilder2.set(paramKey, localObject);
    }
  }
  
  public static int supportHardwareLevel(CameraCharacteristics paramCameraCharacteristics)
  {
    if (paramCameraCharacteristics == null) {
      return -1;
    }
    Integer localInteger = (Integer)paramCameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
    if (localInteger == null) {
      return -1;
    }
    return localInteger.intValue();
  }
  
  public static boolean hardwareOnlySupportLegacy(Context paramContext)
  {
    CameraManager localCameraManager = cameraManager(paramContext);
    String str = firstBackCameraId(paramContext);
    CameraCharacteristics localCameraCharacteristics = cameraCharacter(localCameraManager, str);
    return hardwareOnlySupportLegacy(localCameraCharacteristics);
  }
  
  public static boolean hardwareOnlySupportLegacy(CameraCharacteristics paramCameraCharacteristics)
  {
    int i = supportHardwareLevel(paramCameraCharacteristics);
    return (i < 0) || (i == 2);
  }
  
  public static boolean canSupportFlash(Context paramContext)
  {
    return ContextUtils.hasSystemFeature(paramContext, "android.hardware.camera.flash");
  }
  
  public static boolean supportFlash(CameraCharacteristics paramCameraCharacteristics)
  {
    if (paramCameraCharacteristics == null) {
      return false;
    }
    return ((Boolean)paramCameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)).booleanValue();
  }
  
  public static CameraConfigs.CameraFlash getFlashMode(CaptureRequest.Builder paramBuilder)
  {
    CameraConfigs.CameraFlash localCameraFlash = CameraConfigs.CameraFlash.Off;
    if (paramBuilder == null) {
      return CameraConfigs.CameraFlash.Off;
    }
    Integer localInteger1 = (Integer)paramBuilder.get(CaptureRequest.CONTROL_AE_MODE);
    Integer localInteger2 = (Integer)paramBuilder.get(CaptureRequest.FLASH_MODE);
    if (localInteger1 == null) {
      return localCameraFlash;
    }
    if (localInteger2 != null) {
      switch (localInteger2.intValue())
      {
      case 1: 
        localCameraFlash = CameraConfigs.CameraFlash.On;
        break;
      case 2: 
        localCameraFlash = CameraConfigs.CameraFlash.Torch;
        break;
      }
    }
    switch (localInteger1.intValue())
    {
    case 2: 
      localCameraFlash = CameraConfigs.CameraFlash.Auto;
      break;
    case 4: 
      localCameraFlash = CameraConfigs.CameraFlash.RedEye;
      break;
    case 3: 
      localCameraFlash = CameraConfigs.CameraFlash.Always;
      break;
    case 0: 
      localCameraFlash = CameraConfigs.CameraFlash.Off;
      break;
    }
    return localCameraFlash;
  }
  
  public static void setFlashMode(CaptureRequest.Builder paramBuilder, CameraConfigs.CameraFlash paramCameraFlash)
  {
    if ((paramBuilder == null) || (paramCameraFlash == null)) {
      return;
    }
    paramBuilder.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(1));
    switch (3.b[paramCameraFlash.ordinal()])
    {
    case 1: 
      paramBuilder.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(1));
      paramBuilder.set(CaptureRequest.FLASH_MODE, Integer.valueOf(0));
      break;
    case 2: 
      paramBuilder.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(2));
      break;
    case 3: 
      paramBuilder.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(1));
      paramBuilder.set(CaptureRequest.FLASH_MODE, Integer.valueOf(1));
      break;
    case 4: 
      paramBuilder.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(1));
      paramBuilder.set(CaptureRequest.FLASH_MODE, Integer.valueOf(2));
      break;
    case 5: 
      paramBuilder.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(3));
      break;
    case 6: 
      paramBuilder.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(4));
      break;
    }
  }
  
  public static boolean canSupportAutofocus(Context paramContext)
  {
    return ContextUtils.hasSystemFeature(paramContext, "android.hardware.camera.autofocus");
  }
  
  public static boolean canSupportAutofocus(CameraCharacteristics paramCameraCharacteristics)
  {
    if (paramCameraCharacteristics == null) {
      return false;
    }
    int[] arrayOfInt = (int[])paramCameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
    if ((arrayOfInt == null) || (arrayOfInt.length == 0)) {
      return false;
    }
    return (arrayOfInt.length != 1) || (arrayOfInt[0] != 0);
  }
  
  public static boolean canSupportAutofocus(CameraCharacteristics paramCameraCharacteristics, int paramInt)
  {
    if (paramCameraCharacteristics == null) {
      return false;
    }
    int[] arrayOfInt1 = (int[])paramCameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
    if ((arrayOfInt1 == null) || (arrayOfInt1.length == 0)) {
      return false;
    }
    for (int k : arrayOfInt1) {
      if (paramInt == k) {
        return true;
      }
    }
    return false;
  }
  
  public static int swichFocusMode(CameraConfigs.CameraAutoFocus paramCameraAutoFocus)
  {
    if (paramCameraAutoFocus == null) {
      paramCameraAutoFocus = CameraConfigs.CameraAutoFocus.Off;
    }
    switch (3.c[paramCameraAutoFocus.ordinal()])
    {
    case 1: 
      return 1;
    case 2: 
      return 2;
    case 3: 
      return 3;
    case 4: 
      return 4;
    case 5: 
      return 5;
    }
    return 0;
  }
  
  public static CameraConfigs.CameraAutoFocus focusModeType(CaptureRequest.Builder paramBuilder)
  {
    CameraConfigs.CameraAutoFocus localCameraAutoFocus = CameraConfigs.CameraAutoFocus.Off;
    if (paramBuilder == null) {
      return localCameraAutoFocus;
    }
    Integer localInteger = (Integer)paramBuilder.get(CaptureRequest.CONTROL_AF_MODE);
    if (localInteger == null) {
      return localCameraAutoFocus;
    }
    switch (localInteger.intValue())
    {
    case 1: 
      return CameraConfigs.CameraAutoFocus.Auto;
    case 2: 
      return CameraConfigs.CameraAutoFocus.Macro;
    case 3: 
      return CameraConfigs.CameraAutoFocus.ContinuousPicture;
    case 4: 
      return CameraConfigs.CameraAutoFocus.ContinuousPicture;
    case 5: 
      return CameraConfigs.CameraAutoFocus.EDOF;
    }
    return localCameraAutoFocus;
  }
  
  public static void setFocusMode(CameraCharacteristics paramCameraCharacteristics, CaptureRequest.Builder paramBuilder, CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF, ImageOrientation paramImageOrientation)
  {
    if ((paramCameraCharacteristics == null) || (paramBuilder == null) || (!canSupportAutofocus(paramCameraCharacteristics))) {
      return;
    }
    int i = swichFocusMode(paramCameraAutoFocus);
    if (canSupportAutofocus(paramCameraCharacteristics, i)) {
      paramBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(i));
    }
    setFocusPoint(paramCameraCharacteristics, paramBuilder, paramPointF, paramImageOrientation);
  }
  
  public static void setFocusPoint(CameraCharacteristics paramCameraCharacteristics, CaptureRequest.Builder paramBuilder, PointF paramPointF, ImageOrientation paramImageOrientation)
  {
    if ((paramCameraCharacteristics == null) || (paramBuilder == null) || (paramPointF == null)) {
      return;
    }
    Rect localRect1 = (Rect)paramCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
    Size localSize = (Size)paramCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);
    PointF localPointF = new PointF(paramPointF.x, paramPointF.y);
    if (paramImageOrientation == null) {
      paramImageOrientation = ImageOrientation.Up;
    }
    switch (3.d[paramImageOrientation.ordinal()])
    {
    case 1: 
      localPointF.x = paramPointF.y;
      localPointF.y = (1.0F - paramPointF.x);
      break;
    case 2: 
      localPointF.x = (1.0F - paramPointF.x);
      localPointF.y = (1.0F - paramPointF.y);
      break;
    case 3: 
      localPointF.x = (1.0F - paramPointF.y);
      localPointF.y = paramPointF.x;
      break;
    case 4: 
      localPointF.x = (1.0F - paramPointF.x);
      localPointF.y = paramPointF.y;
      break;
    case 5: 
      localPointF.x = (1.0F - paramPointF.y);
      localPointF.y = (1.0F - paramPointF.x);
      break;
    case 6: 
      localPointF.x = paramPointF.x;
      localPointF.y = (1.0F - paramPointF.y);
      break;
    case 7: 
      localPointF.x = paramPointF.y;
      localPointF.y = paramPointF.x;
      break;
    }
    int i = (int)(Math.min(localRect1.width(), localRect1.height()) * 0.03F);
    int j = localRect1.left + (int)(localPointF.x * localRect1.width()) + i;
    int k = localRect1.top + (int)(localPointF.y * localRect1.width()) + i;
    Rect localRect2 = new Rect(j - i * 2, k - i * 2, j, k);
    localRect2 = fixFocusRange(localRect2, localSize);
    MeteringRectangle localMeteringRectangle = new MeteringRectangle(localRect2, 500);
    MeteringRectangle[] arrayOfMeteringRectangle = { localMeteringRectangle };
    paramBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(2));
    paramBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, arrayOfMeteringRectangle);
    paramBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(1));
    paramBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, arrayOfMeteringRectangle);
    paramBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, Integer.valueOf(1));
  }
  
  public static Rect fixFocusRange(Rect paramRect, Size paramSize)
  {
    Rect localRect = new Rect(paramRect);
    if (paramRect.left < 0)
    {
      localRect.left = 0;
      localRect.right = paramRect.width();
    }
    else if (paramRect.right > paramSize.getWidth())
    {
      localRect.right = paramSize.getWidth();
      localRect.left = (localRect.right - paramRect.width());
    }
    if (paramRect.top < 0)
    {
      localRect.top = 0;
      localRect.bottom = paramRect.height();
    }
    else if (paramRect.bottom > paramSize.getHeight())
    {
      localRect.bottom = paramSize.getHeight();
      localRect.top = (localRect.bottom - paramRect.height());
    }
    return localRect;
  }
  
  public static TuSdkSize createSize(Size paramSize)
  {
    if (paramSize != null) {
      return new TuSdkSize(paramSize.getWidth(), paramSize.getHeight());
    }
    return null;
  }
  
  public static Size previewOptimalSize(Context paramContext, Size[] paramArrayOfSize, int paramInt, float paramFloat)
  {
    if (paramArrayOfSize == null) {
      return null;
    }
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(paramContext);
    if (localTuSdkSize == null) {
      return null;
    }
    List localList1 = sortSizeList(Arrays.asList(paramArrayOfSize));
    if ((localList1 == null) || (localList1.isEmpty())) {
      return null;
    }
    Object localObject = null;
    if (paramInt < 1) {
      return (Size)localList1.get(0);
    }
    if ((paramFloat <= 0.0F) || (paramFloat > 1.0F)) {
      paramFloat = 1.0F;
    }
    double d = Math.floor(localTuSdkSize.maxSide() * paramFloat);
    if (d < paramInt) {
      paramInt = (int)d;
    }
    List localList2 = getMatchRatioSizes(localTuSdkSize.getRatio(), localList1);
    Iterator localIterator;
    Size localSize;
    if (!localList2.isEmpty())
    {
      localObject = (Size)localList2.get(0);
      localIterator = localList2.iterator();
      while (localIterator.hasNext())
      {
        localSize = (Size)localIterator.next();
        if ((localSize.getWidth() != localSize.getHeight()) && (localSize.getWidth() <= paramInt) && (localSize.getHeight() <= paramInt)) {
          localObject = localSize;
        }
      }
    }
    if (localObject == null)
    {
      localObject = (Size)localList1.get(localList1.size() - 1);
      localIterator = localList1.iterator();
      while (localIterator.hasNext())
      {
        localSize = (Size)localIterator.next();
        if ((localSize.getWidth() <= paramInt) && (localSize.getHeight() <= paramInt)) {
          localObject = localSize;
        }
      }
    }
    return (Size)localObject;
  }
  
  public static Size pictureOptimalSize(Context paramContext, Size[] paramArrayOfSize, TuSdkSize paramTuSdkSize)
  {
    if (paramArrayOfSize == null) {
      return null;
    }
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(paramContext);
    if (localTuSdkSize == null) {
      return null;
    }
    List localList1 = sortSizeList(Arrays.asList(paramArrayOfSize));
    if ((localList1 == null) || (localList1.isEmpty())) {
      return null;
    }
    Size localSize = null;
    if (paramTuSdkSize == null) {
      paramTuSdkSize = localTuSdkSize;
    }
    List localList2 = getMatchRatioSizes(paramTuSdkSize.getRatio(), localList1);
    localSize = getNearestSize(localList2, paramTuSdkSize);
    if (localSize == null) {
      localSize = getNearestSize(localList1, paramTuSdkSize);
    }
    if (localSize == null) {
      localSize = (Size)localList1.get(localList1.size() - 1);
    }
    return localSize;
  }
  
  public static List<Size> sortSizeList(List<Size> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return paramList;
    }
    Collections.sort(paramList, new Comparator()
    {
      public int compare(Size paramAnonymousSize1, Size paramAnonymousSize2)
      {
        if (paramAnonymousSize1.getWidth() < paramAnonymousSize2.getWidth()) {
          return 1;
        }
        if (paramAnonymousSize1.getWidth() > paramAnonymousSize2.getWidth()) {
          return -1;
        }
        if (paramAnonymousSize1.getHeight() < paramAnonymousSize2.getHeight()) {
          return 1;
        }
        if (paramAnonymousSize1.getHeight() > paramAnonymousSize2.getHeight()) {
          return -1;
        }
        return 0;
      }
    });
    return paramList;
  }
  
  public static List<Size> getMatchRatioSizes(int paramInt, List<Size> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramInt == 0) || (paramList == null)) {
      return localArrayList;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Size localSize = (Size)localIterator.next();
      if (matchRatio(paramInt, localSize)) {
        localArrayList.add(localSize);
      }
    }
    return localArrayList;
  }
  
  public static boolean matchRatio(int paramInt, Size paramSize)
  {
    return Math.abs(paramInt - getSizeRatio(paramSize.getWidth(), paramSize.getHeight())) < 2;
  }
  
  public static int getSizeRatio(int paramInt1, int paramInt2)
  {
    int i = Math.max(paramInt1, paramInt2);
    int j = Math.min(paramInt1, paramInt2);
    return (int)Math.floor(i / j * 10.0F);
  }
  
  public static Size getNearestSize(List<Size> paramList, TuSdkSize paramTuSdkSize)
  {
    if ((paramList == null) || (paramList.isEmpty())) {
      return null;
    }
    Object localObject = null;
    int i = TuSdkGPU.getGpuType().getSize();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Size localSize = (Size)localIterator.next();
      TuSdkSize localTuSdkSize = createSize(localSize);
      if (localTuSdkSize.maxSide() <= i) {
        if (paramTuSdkSize == null) {
          localObject = localSize;
        } else if ((localTuSdkSize.maxSide() > paramTuSdkSize.maxSide()) && (localTuSdkSize.minSide() > paramTuSdkSize.minSide())) {
          localObject = localSize;
        } else if ((localTuSdkSize.maxSide() == paramTuSdkSize.maxSide()) || (localObject == null)) {
          localObject = localSize;
        }
      }
    }
    return (Size)localObject;
  }
  
  public static boolean canSupportFaceDetection(CameraCharacteristics paramCameraCharacteristics)
  {
    if (paramCameraCharacteristics == null) {
      return false;
    }
    Integer localInteger = (Integer)paramCameraCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT);
    if ((localInteger == null) || (localInteger.intValue() == 0)) {
      return false;
    }
    int[] arrayOfInt = (int[])paramCameraCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);
    if ((arrayOfInt == null) || (arrayOfInt.length == 0)) {
      return false;
    }
    return (arrayOfInt.length != 1) || (arrayOfInt[0] != 0);
  }
  
  public static List<TuSdkFace> transforFaces(CameraCharacteristics paramCameraCharacteristics, TuSdkSize paramTuSdkSize, Face[] paramArrayOfFace, ImageOrientation paramImageOrientation)
  {
    if ((paramCameraCharacteristics == null) || (paramTuSdkSize == null) || (paramArrayOfFace == null) || (paramArrayOfFace.length == 0)) {
      return null;
    }
    Rect localRect1 = (Rect)paramCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
    TuSdkSize localTuSdkSize = TuSdkSize.create(localRect1.width(), localRect1.height());
    ArrayList localArrayList = new ArrayList(paramArrayOfFace.length);
    Rect localRect2 = RectHelper.makeRectWithAspectRatioInsideRect(paramTuSdkSize, new Rect(0, 0, localTuSdkSize.width, localTuSdkSize.height));
    List localList = a(Arrays.asList(paramArrayOfFace), localTuSdkSize.center());
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      Face localFace = (Face)localIterator.next();
      localArrayList.add(transforFace(localFace, localRect2, paramImageOrientation));
    }
    return localArrayList;
  }
  
  private static List<Face> a(List<Face> paramList, Point paramPoint)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return paramList;
    }
    Collections.sort(paramList, new Comparator()
    {
      public int compare(Face paramAnonymousFace1, Face paramAnonymousFace2)
      {
        if ((paramAnonymousFace1.getBounds() == null) || (paramAnonymousFace2.getBounds() == null) || (paramAnonymousFace1.getBounds().equals(paramAnonymousFace2.getBounds()))) {
          return 0;
        }
        Point localPoint1 = new Point(paramAnonymousFace1.getBounds().centerX(), paramAnonymousFace1.getBounds().centerY());
        Point localPoint2 = new Point(paramAnonymousFace2.getBounds().centerX(), paramAnonymousFace2.getBounds().centerY());
        if (RectHelper.computerPotintDistance(localPoint1, this.a) > RectHelper.computerPotintDistance(localPoint2, this.a)) {
          return 1;
        }
        return -1;
      }
    });
    return paramList;
  }
  
  public static TuSdkFace transforFace(Face paramFace, Rect paramRect, ImageOrientation paramImageOrientation)
  {
    if ((paramFace == null) || (paramRect == null)) {
      return null;
    }
    TuSdkFace localTuSdkFace = new TuSdkFace();
    localTuSdkFace.id = paramFace.getId();
    localTuSdkFace.score = paramFace.getScore();
    if (paramFace.getBounds() != null)
    {
      localTuSdkFace.rect = new RectF();
      localTuSdkFace.rect.left = ((paramFace.getBounds().left - paramRect.left) / paramRect.width());
      localTuSdkFace.rect.right = ((paramFace.getBounds().right - paramRect.left) / paramRect.width());
      localTuSdkFace.rect.top = ((paramFace.getBounds().top - paramRect.top) / paramRect.height());
      localTuSdkFace.rect.bottom = ((paramFace.getBounds().bottom - paramRect.top) / paramRect.height());
    }
    localTuSdkFace.leftEye = a(paramFace.getLeftEyePosition(), paramRect);
    localTuSdkFace.rightEye = a(paramFace.getRightEyePosition(), paramRect);
    localTuSdkFace.mouth = a(paramFace.getMouthPosition(), paramRect);
    TuSdkFace.convertOrientation(localTuSdkFace, paramImageOrientation);
    return localTuSdkFace;
  }
  
  private static PointF a(Point paramPoint, Rect paramRect)
  {
    if ((paramPoint == null) || (paramRect == null)) {
      return null;
    }
    PointF localPointF = new PointF();
    localPointF.x = ((paramPoint.x - paramRect.left) / paramRect.width());
    localPointF.y = ((paramPoint.y - paramRect.top) / paramRect.height());
    return localPointF;
  }
  
  public static void logCameraCharacter(CameraCharacteristics paramCameraCharacteristics)
  {
    if (paramCameraCharacteristics == null) {
      return;
    }
    Iterator localIterator = paramCameraCharacteristics.getKeys().iterator();
    while (localIterator.hasNext())
    {
      CameraCharacteristics.Key localKey = (CameraCharacteristics.Key)localIterator.next();
      if (localKey.equals(CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES))
      {
        a(paramCameraCharacteristics, CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES);
      }
      else if (localKey.equals(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES))
      {
        a(paramCameraCharacteristics, CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES);
      }
      else if (localKey.equals(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES))
      {
        a(paramCameraCharacteristics, CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
      }
      else if (localKey.equals(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS))
      {
        a(paramCameraCharacteristics, CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS);
      }
      else if (localKey.equals(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES))
      {
        a(paramCameraCharacteristics, CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES);
      }
      else if (localKey.equals(CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES))
      {
        a(paramCameraCharacteristics, CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES);
      }
      else if (localKey.equals(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES))
      {
        a(paramCameraCharacteristics, CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
      }
      else
      {
        Object localObject1;
        Object localObject4;
        if (localKey.equals(CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES))
        {
          localObject1 = (Size[])paramCameraCharacteristics.get(CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES);
          if (localObject1 != null) {
            for (localObject4 : localObject1) {
              TLog.d("Camera %s: %s", new Object[] { localKey.getName(), localObject4 });
            }
          }
        }
        else if (localKey.equals(CameraCharacteristics.NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES))
        {
          a(paramCameraCharacteristics, CameraCharacteristics.NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES);
        }
        else if (localKey.equals(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES))
        {
          a(paramCameraCharacteristics, CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        }
        else if (localKey.equals(CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES))
        {
          a(paramCameraCharacteristics, CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES);
        }
        else if (localKey.equals(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES))
        {
          a(paramCameraCharacteristics, CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);
        }
        else if (localKey.equals(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES))
        {
          localObject1 = (Range[])paramCameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
          if (localObject1 != null) {
            for (localObject4 : localObject1) {
              TLog.d("Camera %s: [%s - %s]", new Object[] { localKey.getName(), ((Range)localObject4).getLower(), ((Range)localObject4).getUpper() });
            }
          }
        }
        else if (localKey.equals(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP))
        {
          localObject1 = (StreamConfigurationMap)paramCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
          if (localObject1 != null)
          {
            ??? = ((StreamConfigurationMap)localObject1).getOutputFormats();
            for (int m : ???)
            {
              Size[] arrayOfSize1 = ((StreamConfigurationMap)localObject1).getOutputSizes(m);
              for (Size localSize : arrayOfSize1) {
                TLog.d("Camera %s: [%s] %s", new Object[] { localKey.getName(), Integer.valueOf(m), localSize });
              }
            }
          }
        }
        else if (localKey.equals(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES))
        {
          a(paramCameraCharacteristics, CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        }
        else
        {
          TLog.d("Camera %s: %s", new Object[] { localKey.getName(), paramCameraCharacteristics.get(localKey) });
        }
      }
    }
  }
  
  private static void a(CameraCharacteristics paramCameraCharacteristics, CameraCharacteristics.Key<int[]> paramKey)
  {
    if (paramKey == null) {
      return;
    }
    int[] arrayOfInt1 = (int[])paramCameraCharacteristics.get(paramKey);
    if (arrayOfInt1 == null) {
      return;
    }
    for (int k : arrayOfInt1) {
      TLog.d("Camera %s: [%s]", new Object[] { paramKey.getName(), Integer.valueOf(k) });
    }
  }
  
  public static boolean showAlertIfNotSupportCamera(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 23) {
      return false;
    }
    if (paramContext == null) {
      return true;
    }
    String str = null;
    if (cameraCounts(paramContext) == 0) {
      str = TuSdkContext.getString("lsq_carema_no_device");
    } else if (!canSupportCamera(paramContext)) {
      str = TuSdkContext.getString("lsq_carema_no_access", new Object[] { ContextUtils.getAppName(paramContext) });
    }
    if (str == null) {
      return false;
    }
    TuSdkViewHelper.alert(paramContext, TuSdkContext.getString("lsq_carema_alert_title"), str, TuSdkContext.getString("lsq_button_done"));
    return true;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\Camera2Helper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */