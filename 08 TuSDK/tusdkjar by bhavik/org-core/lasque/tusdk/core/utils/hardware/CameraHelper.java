package org.lasque.tusdk.core.utils.hardware;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build.VERSION;
import android.view.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkDate;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.view.TuSdkViewHelper;

public class CameraHelper
{
  public static final long AutoFocusDelay = 5000L;
  public static final long FocusResetDelay = 5000L;
  public static final String FOCUS_MODE_CONTINUOUS_PICTURE = "continuous-picture";
  public static final String DENOISE_KEY = "denoise";
  public static final String DENOISE_VALUES = "denoise-values";
  public static final String DENOISE_VALUES_OFF = "denoise-off";
  public static final String DENOISE_VALUES_ON = "denoise-on";
  public static final String SHARPNESS_KEY = "sharpness";
  public static final String SHARPNESS_MAX_VALUE = "max-sharpness";
  public static final String SHARPNESS_MIN_VALUE = "min-sharpness";
  public static final String[] focusModes = { "continuous-picture", "continuous-video", "infinity", "macro", "auto" };
  public static final String[] videoFocusModes = { "continuous-video", "macro", "auto" };
  public static final ArrayList<String> autoFocusModes = new ArrayList();
  
  public static boolean canSupportCamera(Context paramContext)
  {
    Boolean localBoolean = Boolean.valueOf((ContextUtils.hasSystemFeature(paramContext, "android.hardware.camera")) && (cameraCounts() > 0));
    if (localBoolean.booleanValue())
    {
      TuSdkDate localTuSdkDate = TuSdkDate.create();
      Camera.CameraInfo localCameraInfo = firstCameraInfo(0);
      Camera localCamera = getCamera(localCameraInfo);
      if (localCamera == null) {
        localBoolean = Boolean.valueOf(false);
      } else {
        try
        {
          localCamera.getParameters();
          localCamera.release();
          localCamera = null;
        }
        catch (RuntimeException localRuntimeException)
        {
          TLog.e("fail to access camera", new Object[0]);
          localBoolean = Boolean.valueOf(false);
        }
      }
      TLog.d("time for checking camera access: %s ms", new Object[] { Long.valueOf(localTuSdkDate.diffOfMillis()) });
    }
    return localBoolean.booleanValue();
  }
  
  public static int cameraCounts()
  {
    return Camera.getNumberOfCameras();
  }
  
  public static boolean canSupportBackFace()
  {
    Camera.CameraInfo localCameraInfo = firstBackCameraInfo();
    return (localCameraInfo != null) && (localCameraInfo.facing == 0);
  }
  
  public static boolean canSupportFlash(Context paramContext)
  {
    return ContextUtils.hasSystemFeature(paramContext, "android.hardware.camera.flash");
  }
  
  public static boolean canSupportAutofocus(Context paramContext)
  {
    return ContextUtils.hasSystemFeature(paramContext, "android.hardware.camera.autofocus");
  }
  
  public static Camera getCamera(Camera.CameraInfo paramCameraInfo)
  {
    if (paramCameraInfo == null) {
      return null;
    }
    try
    {
      return Camera.open(paramCameraInfo.facing);
    }
    catch (Exception localException)
    {
      TLog.e("open Camera: %s", new Object[] { localException });
    }
    return null;
  }
  
  public static Camera.CameraInfo firstBackCameraInfo()
  {
    return firstCameraInfo(0);
  }
  
  public static Camera.CameraInfo firstFrontCameraInfo()
  {
    return firstCameraInfo(1);
  }
  
  public static CameraConfigs.CameraFacing getCameraFacing(Camera.CameraInfo paramCameraInfo)
  {
    if (paramCameraInfo == null) {
      return CameraConfigs.CameraFacing.Back;
    }
    switch (paramCameraInfo.facing)
    {
    case 1: 
      return CameraConfigs.CameraFacing.Front;
    }
    return CameraConfigs.CameraFacing.Back;
  }
  
  public static Camera.CameraInfo firstCameraInfo(CameraConfigs.CameraFacing paramCameraFacing)
  {
    if (paramCameraFacing == null) {
      paramCameraFacing = CameraConfigs.CameraFacing.Back;
    }
    int i = 0;
    switch (5.a[paramCameraFacing.ordinal()])
    {
    case 1: 
      i = 1;
      break;
    }
    return firstCameraInfo(i);
  }
  
  public static Camera.CameraInfo firstCameraInfo(int paramInt)
  {
    int i = cameraCounts();
    if (i == 0) {
      return null;
    }
    Object localObject = null;
    for (int j = 0; j < i; j++)
    {
      Camera.CameraInfo localCameraInfo = new Camera.CameraInfo();
      Camera.getCameraInfo(j, localCameraInfo);
      if (localCameraInfo.facing == paramInt) {
        return localCameraInfo;
      }
      localObject = localCameraInfo;
    }
    return (Camera.CameraInfo)localObject;
  }
  
  public static CameraConfigs.CameraFacing cameraPosition(Camera.CameraInfo paramCameraInfo)
  {
    if (paramCameraInfo == null) {
      return null;
    }
    switch (paramCameraInfo.facing)
    {
    case 1: 
      return CameraConfigs.CameraFacing.Front;
    }
    return CameraConfigs.CameraFacing.Back;
  }
  
  public static TuSdkSize createSize(Camera.Size paramSize)
  {
    if (paramSize != null) {
      return new TuSdkSize(paramSize.width, paramSize.height);
    }
    return null;
  }
  
  public static void unifiedParameters(Camera.Parameters paramParameters)
  {
    if (paramParameters == null) {
      return;
    }
    setDenoise(paramParameters, false);
    setSharpness(paramParameters, 20);
  }
  
  public static String findSettableValue(Collection<String> paramCollection, String... paramVarArgs)
  {
    Object localObject = null;
    if ((paramCollection == null) || (paramVarArgs == null)) {
      return (String)localObject;
    }
    for (String str : paramVarArgs) {
      if (paramCollection.contains(str))
      {
        localObject = str;
        break;
      }
    }
    return (String)localObject;
  }
  
  public static void setColorEffect(Camera.Parameters paramParameters, String... paramVarArgs)
  {
    String str = findSettableValue(paramParameters.getSupportedColorEffects(), paramVarArgs);
    if (str != null) {
      paramParameters.setColorEffect(str);
    }
  }
  
  public static void setSceneMode(Camera.Parameters paramParameters, String... paramVarArgs)
  {
    String str = findSettableValue(paramParameters.getSupportedSceneModes(), paramVarArgs);
    if (str != null) {
      paramParameters.setSceneMode(str);
    }
  }
  
  public static void setWhiteBalance(Camera.Parameters paramParameters, String... paramVarArgs)
  {
    String str = findSettableValue(paramParameters.getSupportedWhiteBalance(), paramVarArgs);
    if (str != null) {
      paramParameters.setWhiteBalance(str);
    }
  }
  
  public static void setWhiteBalance(Camera.Parameters paramParameters, CameraConfigs.CameraWhiteBalance paramCameraWhiteBalance)
  {
    if (paramCameraWhiteBalance == null) {
      paramCameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Auto;
    }
    String str = "auto";
    switch (5.b[paramCameraWhiteBalance.ordinal()])
    {
    case 1: 
      str = "incandescent";
      break;
    case 2: 
      str = "fluorescent";
      break;
    case 3: 
      str = "warm-fluorescent";
      break;
    case 4: 
      str = "daylight";
      break;
    case 5: 
      str = "cloudy-daylight";
      break;
    case 6: 
      str = "twilight";
      break;
    case 7: 
      str = "shade";
      break;
    }
    setWhiteBalance(paramParameters, new String[] { str });
  }
  
  public static CameraConfigs.CameraWhiteBalance whiteBalance(String paramString)
  {
    CameraConfigs.CameraWhiteBalance localCameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Auto;
    if (paramString == null) {
      return localCameraWhiteBalance;
    }
    if (paramString.equalsIgnoreCase("incandescent")) {
      localCameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Incandescent;
    } else if (paramString.equalsIgnoreCase("fluorescent")) {
      localCameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Fluorescent;
    } else if (paramString.equalsIgnoreCase("warm-fluorescent")) {
      localCameraWhiteBalance = CameraConfigs.CameraWhiteBalance.WarmFluorescent;
    } else if (paramString.equalsIgnoreCase("daylight")) {
      localCameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Daylight;
    } else if (paramString.equalsIgnoreCase("twilight")) {
      localCameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Twilight;
    } else if (paramString.equalsIgnoreCase("shade")) {
      localCameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Shade;
    }
    return localCameraWhiteBalance;
  }
  
  public static void setAntibanding(Camera.Parameters paramParameters, String... paramVarArgs)
  {
    String str = findSettableValue(paramParameters.getSupportedAntibanding(), paramVarArgs);
    if (str != null) {
      paramParameters.setAntibanding(str);
    }
  }
  
  public static void setAntibanding(Camera.Parameters paramParameters, CameraConfigs.CameraAntibanding paramCameraAntibanding)
  {
    if (paramCameraAntibanding == null) {
      paramCameraAntibanding = CameraConfigs.CameraAntibanding.Off;
    }
    String str = "off";
    switch (5.c[paramCameraAntibanding.ordinal()])
    {
    case 1: 
      str = "auto";
      break;
    case 2: 
      str = "50hz";
      break;
    case 3: 
      str = "60hz";
      break;
    default: 
      str = "off";
    }
    setAntibanding(paramParameters, new String[] { str });
  }
  
  public static CameraConfigs.CameraAntibanding antiBandingType(String paramString)
  {
    CameraConfigs.CameraAntibanding localCameraAntibanding = CameraConfigs.CameraAntibanding.Off;
    if (paramString == null) {
      return localCameraAntibanding;
    }
    if (paramString.equalsIgnoreCase("auto")) {
      localCameraAntibanding = CameraConfigs.CameraAntibanding.Auto;
    } else if (paramString.equalsIgnoreCase("50hz")) {
      localCameraAntibanding = CameraConfigs.CameraAntibanding.RATE_50HZ;
    } else if (paramString.equalsIgnoreCase("60hz")) {
      localCameraAntibanding = CameraConfigs.CameraAntibanding.RATE_60HZ;
    } else if (paramString.equalsIgnoreCase("off")) {
      localCameraAntibanding = CameraConfigs.CameraAntibanding.Off;
    }
    return localCameraAntibanding;
  }
  
  public static boolean canSupportDenoise(Camera.Parameters paramParameters)
  {
    if (paramParameters == null) {
      return false;
    }
    return paramParameters.get("denoise") != null;
  }
  
  public static boolean getDenoise(Camera.Parameters paramParameters)
  {
    if (!canSupportDenoise(paramParameters)) {
      return false;
    }
    String str = paramParameters.get("denoise");
    return (str != null) && (!str.equalsIgnoreCase("denoise-off"));
  }
  
  public static boolean setDenoise(Camera.Parameters paramParameters, boolean paramBoolean)
  {
    if (!canSupportDenoise(paramParameters)) {
      return false;
    }
    paramParameters.set("denoise", paramBoolean ? "denoise-on" : "denoise-off");
    return true;
  }
  
  public static boolean canSupportSharpness(Camera.Parameters paramParameters)
  {
    if (paramParameters == null) {
      return false;
    }
    return paramParameters.get("sharpness") != null;
  }
  
  public static int getSharpness(Camera.Parameters paramParameters)
  {
    if (!canSupportSharpness(paramParameters)) {
      return 0;
    }
    return paramParameters.getInt("sharpness");
  }
  
  public static boolean setSharpness(Camera.Parameters paramParameters, int paramInt)
  {
    if (!canSupportSharpness(paramParameters)) {
      return false;
    }
    int i = paramParameters.getInt("max-sharpness");
    int j = paramParameters.getInt("min-sharpness");
    paramParameters.set("sharpness", Math.max(j, Math.min(paramInt, i)));
    return true;
  }
  
  public static boolean canSupportAutofocus(Context paramContext, Camera.Parameters paramParameters)
  {
    if ((!canSupportAutofocus(paramContext)) || (paramParameters == null) || (paramParameters.getFocusMode() == null)) {
      return false;
    }
    return autoFocusModes.contains(paramParameters.getFocusMode());
  }
  
  public static boolean isContinuous(Camera.Parameters paramParameters)
  {
    if (paramParameters == null) {
      return false;
    }
    String str = paramParameters.getFocusMode();
    if (str == null) {
      return false;
    }
    return str.equalsIgnoreCase("continuous-picture");
  }
  
  public static void setFocusMode(Camera.Parameters paramParameters, String... paramVarArgs)
  {
    if (paramParameters == null) {
      return;
    }
    String str = findSettableValue(paramParameters.getSupportedFocusModes(), paramVarArgs);
    if (str != null) {
      paramParameters.setFocusMode(str);
    }
  }
  
  public static String swichFocusMode(CameraConfigs.CameraAutoFocus paramCameraAutoFocus)
  {
    if (paramCameraAutoFocus == null) {
      paramCameraAutoFocus = CameraConfigs.CameraAutoFocus.Off;
    }
    switch (5.d[paramCameraAutoFocus.ordinal()])
    {
    case 1: 
      return "auto";
    case 2: 
      return "macro";
    case 3: 
      return "continuous-video";
    case 4: 
      return "continuous-picture";
    case 5: 
      return "edof";
    }
    return "infinity";
  }
  
  public static CameraConfigs.CameraAutoFocus focusModeType(String paramString)
  {
    CameraConfigs.CameraAutoFocus localCameraAutoFocus = CameraConfigs.CameraAutoFocus.Off;
    if (paramString == null) {
      return localCameraAutoFocus;
    }
    if (paramString.equalsIgnoreCase("auto")) {
      localCameraAutoFocus = CameraConfigs.CameraAutoFocus.Auto;
    } else if (paramString.equalsIgnoreCase("macro")) {
      localCameraAutoFocus = CameraConfigs.CameraAutoFocus.Macro;
    } else if (paramString.equalsIgnoreCase("continuous-video")) {
      localCameraAutoFocus = CameraConfigs.CameraAutoFocus.ContinuousVideo;
    } else if (paramString.equalsIgnoreCase("continuous-picture")) {
      localCameraAutoFocus = CameraConfigs.CameraAutoFocus.ContinuousPicture;
    } else if (paramString.equalsIgnoreCase("edof")) {
      localCameraAutoFocus = CameraConfigs.CameraAutoFocus.EDOF;
    }
    return localCameraAutoFocus;
  }
  
  public static void setFocusMode(Camera.Parameters paramParameters, CameraConfigs.CameraAutoFocus paramCameraAutoFocus, PointF paramPointF, ImageOrientation paramImageOrientation)
  {
    if (paramParameters == null) {
      return;
    }
    String str = swichFocusMode(paramCameraAutoFocus);
    if ((paramParameters.getSupportedFocusModes() != null) && (paramParameters.getSupportedFocusModes().contains(str))) {
      paramParameters.setFocusMode(str);
    }
    setFocusPoint(paramParameters, paramPointF, paramImageOrientation);
  }
  
  public static void setFocusPoint(Camera.Parameters paramParameters, PointF paramPointF, ImageOrientation paramImageOrientation)
  {
    if ((paramPointF == null) || (paramParameters == null) || (Build.VERSION.SDK_INT < 14)) {
      return;
    }
    setFocusArea(paramParameters, paramPointF, paramImageOrientation);
  }
  
  @TargetApi(14)
  public static void setFocusArea(Camera.Parameters paramParameters, PointF paramPointF, ImageOrientation paramImageOrientation)
  {
    setFocusArea(paramParameters, paramPointF, paramImageOrientation, true);
  }
  
  @TargetApi(14)
  public static void setFocusArea(Camera.Parameters paramParameters, PointF paramPointF, ImageOrientation paramImageOrientation, boolean paramBoolean)
  {
    Camera.Area localArea = convertToCameraArea(paramPointF, paramImageOrientation, 1000);
    setFocusArea(paramParameters, localArea);
    if (paramBoolean) {
      setMeteringArea(paramParameters, localArea);
    }
  }
  
  @TargetApi(14)
  public static void setFocusArea(Camera.Parameters paramParameters, Camera.Area paramArea)
  {
    if ((paramParameters == null) || (paramArea == null) || (paramParameters.getMaxNumFocusAreas() < 1)) {
      return;
    }
    ArrayList localArrayList = new ArrayList(1);
    localArrayList.add(paramArea);
    paramParameters.setFocusAreas(localArrayList);
  }
  
  @TargetApi(14)
  public static void setMeteringArea(Camera.Parameters paramParameters, Camera.Area paramArea)
  {
    if ((paramParameters == null) || (paramArea == null) || (paramParameters.getMaxNumMeteringAreas() < 1)) {
      return;
    }
    ArrayList localArrayList = new ArrayList(1);
    localArrayList.add(paramArea);
    paramParameters.setMeteringAreas(localArrayList);
  }
  
  @TargetApi(14)
  public static Camera.Area convertToCameraArea(PointF paramPointF, ImageOrientation paramImageOrientation, int paramInt)
  {
    if (paramPointF == null) {
      return new Camera.Area(new Rect(0, 0, 0, 0), 1000);
    }
    if (paramImageOrientation == null) {
      paramImageOrientation = ImageOrientation.Up;
    }
    PointF localPointF = new PointF(paramPointF.x, paramPointF.y);
    switch (5.e[paramImageOrientation.ordinal()])
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
    int i = (int)(localPointF.x * 2000.0F) - 1000;
    int j = (int)(localPointF.y * 2000.0F) - 1000;
    Rect localRect = getFocusRect(i, j, 100, 100);
    Camera.Area localArea = new Camera.Area(localRect, paramInt);
    return localArea;
  }
  
  public static Rect getFocusRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = (int)(paramInt3 * 0.5D);
    int j = (int)(paramInt4 * 0.5D);
    Rect localRect = new Rect(paramInt1 - i, paramInt2 - j, paramInt1 + i, paramInt2 + j);
    if (localRect.left < 64536)
    {
      localRect.left = 64536;
      localRect.right = (localRect.left + paramInt3);
    }
    else if (localRect.right > 1000)
    {
      localRect.right = 1000;
      localRect.left = (localRect.right - paramInt3);
    }
    if (localRect.top < 64536)
    {
      localRect.top = 64536;
      localRect.bottom = (localRect.top + paramInt4);
    }
    else if (localRect.bottom > 1000)
    {
      localRect.bottom = 1000;
      localRect.top = (localRect.bottom - paramInt4);
    }
    return localRect;
  }
  
  public static boolean supportFlash(Camera.Parameters paramParameters)
  {
    if (paramParameters == null) {
      return false;
    }
    List localList = paramParameters.getSupportedFlashModes();
    if ((localList == null) || (localList.size() == 0)) {
      return false;
    }
    return (localList.size() != 1) || (!localList.contains("off"));
  }
  
  public static CameraConfigs.CameraFlash getFlashMode(Camera.Parameters paramParameters)
  {
    if (paramParameters == null) {
      return CameraConfigs.CameraFlash.Off;
    }
    String str = paramParameters.getFlashMode();
    if (str == null) {
      return CameraConfigs.CameraFlash.Off;
    }
    if (str.equalsIgnoreCase("on")) {
      return CameraConfigs.CameraFlash.On;
    }
    if (str.equalsIgnoreCase("auto")) {
      return CameraConfigs.CameraFlash.Auto;
    }
    if (str.equalsIgnoreCase("torch")) {
      return CameraConfigs.CameraFlash.Torch;
    }
    if (str.equalsIgnoreCase("red-eye")) {
      return CameraConfigs.CameraFlash.RedEye;
    }
    return CameraConfigs.CameraFlash.Off;
  }
  
  public static void setFlashMode(Camera.Parameters paramParameters, CameraConfigs.CameraFlash paramCameraFlash)
  {
    if (paramCameraFlash == null) {
      paramCameraFlash = CameraConfigs.CameraFlash.Off;
    }
    String str = "off";
    switch (5.f[paramCameraFlash.ordinal()])
    {
    case 1: 
      str = "on";
      break;
    case 2: 
      str = "auto";
      break;
    case 3: 
      str = "torch";
      break;
    case 4: 
      str = "on";
      break;
    case 5: 
      str = "red-eye";
      break;
    }
    setFlashMode(paramParameters, str);
  }
  
  public static void setFlashMode(Camera.Parameters paramParameters, String paramString)
  {
    if (paramParameters == null) {
      return;
    }
    List localList = paramParameters.getSupportedFlashModes();
    if ((localList == null) || (paramString == null)) {
      return;
    }
    if (localList.contains(paramString)) {
      paramParameters.setFlashMode(paramString);
    }
  }
  
  public static void setPreviewSize(Context paramContext, Camera.Parameters paramParameters, int paramInt, float paramFloat)
  {
    setPreviewSize(paramContext, paramParameters, paramInt, paramFloat, 0.0F);
  }
  
  public static void setPreviewSize(Context paramContext, Camera.Parameters paramParameters, int paramInt, float paramFloat1, float paramFloat2)
  {
    if (paramParameters == null) {
      return;
    }
    TuSdkSize localTuSdkSize1 = ContextUtils.getScreenSize(paramContext);
    if (localTuSdkSize1 == null) {
      return;
    }
    if ((paramFloat1 <= 0.0F) || (paramFloat1 > 1.0F)) {
      paramFloat1 = 1.0F;
    }
    double d = Math.floor(localTuSdkSize1.maxSide() * paramFloat1);
    if (d < paramInt) {
      paramInt = (int)d;
    }
    if (paramFloat2 <= 0.0F) {
      paramFloat2 = localTuSdkSize1.maxMinRatio();
    } else if (paramFloat2 < 1.0F) {
      paramFloat2 = 1.0F / paramFloat2;
    }
    TuSdkSize localTuSdkSize2 = TuSdkSize.create(paramInt, (int)(paramInt / paramFloat2));
    setPreviewSize(paramParameters, localTuSdkSize2);
  }
  
  public static void setMaxPreviewSize(Camera.Parameters paramParameters, TuSdkSize paramTuSdkSize)
  {
    if ((paramParameters == null) || (paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    List localList = sortMaxSizeList(paramParameters.getSupportedPreviewSizes());
    if ((localList == null) || (localList.isEmpty())) {
      return;
    }
    paramTuSdkSize = TuSdkSize.create(paramTuSdkSize.maxSide(), paramTuSdkSize.minSide());
    Camera.Size localSize = (Camera.Size)localList.get(0);
    TLog.d("matched max previewSize found for (%d, %d)", new Object[] { Integer.valueOf(localSize.width), Integer.valueOf(localSize.height) });
    paramParameters.setPreviewSize(localSize.width, localSize.height);
  }
  
  public static void setPreviewSize(Camera.Parameters paramParameters, TuSdkSize paramTuSdkSize)
  {
    if ((paramParameters == null) || (paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    List localList = sortSizeList(paramParameters.getSupportedPreviewSizes());
    if ((localList == null) || (localList.isEmpty())) {
      return;
    }
    paramTuSdkSize = TuSdkSize.create(paramTuSdkSize.maxSide(), paramTuSdkSize.minSide());
    Object localObject = null;
    Iterator localIterator1 = localList.iterator();
    while (localIterator1.hasNext())
    {
      Camera.Size localSize1 = (Camera.Size)localIterator1.next();
      if ((Math.max(localSize1.width, localSize1.height) == paramTuSdkSize.width) && (Math.min(localSize1.width, localSize1.height) == paramTuSdkSize.height)) {
        localObject = localSize1;
      }
    }
    if (localObject == null)
    {
      int i = paramTuSdkSize.width * paramTuSdkSize.height;
      float f1 = paramTuSdkSize.width / paramTuSdkSize.height;
      int j = Integer.MAX_VALUE;
      float f2 = Float.MAX_VALUE;
      Iterator localIterator2 = localList.iterator();
      while (localIterator2.hasNext())
      {
        Camera.Size localSize2 = (Camera.Size)localIterator2.next();
        int k = Math.max(localSize2.width, localSize2.height);
        int m = Math.min(localSize2.width, localSize2.height);
        Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(paramTuSdkSize, new Rect(0, 0, k, m));
        int n = localRect.width() * localRect.height();
        float f3 = k / m;
        int i1 = Math.abs(n - i);
        float f4 = Math.abs(f3 - f1);
        if ((i1 < j) || ((i1 == j) && (f4 < f2)))
        {
          j = i1;
          f2 = f4;
          localObject = localSize2;
        }
      }
    }
    TLog.d("matched previewSize found for (%d, %d) : (%d, %d)", new Object[] { Integer.valueOf(paramTuSdkSize.width), Integer.valueOf(paramTuSdkSize.height), Integer.valueOf(((Camera.Size)localObject).width), Integer.valueOf(((Camera.Size)localObject).height) });
    paramParameters.setPreviewSize(((Camera.Size)localObject).width, ((Camera.Size)localObject).height);
  }
  
  public static boolean setPreviewFpsRange(Camera.Parameters paramParameters, int paramInt)
  {
    List localList = paramParameters.getSupportedPreviewFpsRange();
    Iterator localIterator1 = localList.iterator();
    while (localIterator1.hasNext())
    {
      int[] arrayOfInt1 = (int[])localIterator1.next();
      if ((arrayOfInt1[0] == arrayOfInt1[1]) && (arrayOfInt1[1] == paramInt))
      {
        paramParameters.setPreviewFpsRange(arrayOfInt1[0], arrayOfInt1[1]);
        return true;
      }
    }
    int i = 0;
    int j = 0;
    Iterator localIterator2 = localList.iterator();
    while (localIterator2.hasNext())
    {
      int[] arrayOfInt2 = (int[])localIterator2.next();
      if ((Math.min(arrayOfInt2[0], arrayOfInt2[1]) > i) && (Math.max(arrayOfInt2[0], arrayOfInt2[1]) >= paramInt))
      {
        i = Math.min(arrayOfInt2[0], arrayOfInt2[1]);
        j = Math.max(arrayOfInt2[0], arrayOfInt2[1]);
      }
    }
    if (i > 0)
    {
      paramParameters.setPreviewFpsRange(i, j);
      return true;
    }
    TLog.d("Couldn't find matched Fps range for [" + paramInt + "]", new Object[0]);
    return false;
  }
  
  public static List<Camera.Size> getMatchRatioSizes(int paramInt, List<Camera.Size> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramInt == 0) || (paramList == null)) {
      return localArrayList;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Camera.Size localSize = (Camera.Size)localIterator.next();
      if (matchRatio(paramInt, localSize)) {
        localArrayList.add(localSize);
      }
    }
    return localArrayList;
  }
  
  public static void setPictureSize(Context paramContext, Camera.Parameters paramParameters, TuSdkSize paramTuSdkSize)
  {
    setMaxPictureSize(paramContext, paramParameters, paramTuSdkSize, 0.0F);
  }
  
  public static void setMaxPictureSize(Context paramContext, Camera.Parameters paramParameters, TuSdkSize paramTuSdkSize, float paramFloat)
  {
    if (paramParameters == null) {
      return;
    }
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(paramContext);
    if (localTuSdkSize == null) {
      return;
    }
    if (paramTuSdkSize == null) {
      paramTuSdkSize = localTuSdkSize;
    }
    List localList = sortMaxSizeList(paramParameters.getSupportedPictureSizes());
    if ((localList == null) || (localList.isEmpty())) {
      return;
    }
    Camera.Size localSize = (Camera.Size)localList.get(0);
    TLog.d("matched max pictureSize found for (%d, %d)", new Object[] { Integer.valueOf(localSize.width), Integer.valueOf(localSize.height) });
    paramParameters.setPictureSize(localSize.width, localSize.height);
  }
  
  public static void setPictureSize(Context paramContext, Camera.Parameters paramParameters, TuSdkSize paramTuSdkSize, float paramFloat)
  {
    if (paramParameters == null) {
      return;
    }
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(paramContext);
    if (localTuSdkSize == null) {
      return;
    }
    if (paramTuSdkSize == null) {
      paramTuSdkSize = localTuSdkSize;
    }
    List localList1 = sortSizeList(paramParameters.getSupportedPictureSizes());
    if ((localList1 == null) || (localList1.isEmpty())) {
      return;
    }
    Camera.Size localSize = null;
    int i = paramTuSdkSize.getRatio();
    if (paramFloat > 0.0F) {
      i = (int)Math.floor(paramFloat * 10.0F);
    }
    List localList2 = getMatchRatioSizes(i, localList1);
    localSize = getNearestSize(localList2, paramTuSdkSize);
    if (localSize == null) {
      localSize = getNearestSize(localList1, TuSdkSize.create(paramParameters.getPreviewSize().width, paramParameters.getPreviewSize().height));
    }
    if (localSize == null) {
      localSize = (Camera.Size)localList1.get(localList1.size() - 1);
    }
    TLog.d("matched pictureSize found for (%d, %d) : (%d, %d)", new Object[] { Integer.valueOf(paramTuSdkSize.width), Integer.valueOf(paramTuSdkSize.height), Integer.valueOf(localSize.width), Integer.valueOf(localSize.height) });
    paramParameters.setPictureSize(localSize.width, localSize.height);
  }
  
  public static Camera.Size getNearestSize(List<Camera.Size> paramList, TuSdkSize paramTuSdkSize)
  {
    if ((paramList == null) || (paramList.isEmpty())) {
      return null;
    }
    Object localObject = null;
    int i = TuSdkGPU.getGpuType().getSize();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Camera.Size localSize = (Camera.Size)localIterator.next();
      TuSdkSize localTuSdkSize = createSize(localSize);
      if (localTuSdkSize.maxSide() <= i) {
        if (paramTuSdkSize == null) {
          localObject = localSize;
        } else if ((localTuSdkSize.maxSide() > paramTuSdkSize.maxSide()) && (localTuSdkSize.minSide() > paramTuSdkSize.minSide()) && (localTuSdkSize.maxSide() != localTuSdkSize.minSide())) {
          localObject = localSize;
        } else if ((localTuSdkSize.maxSide() == paramTuSdkSize.maxSide()) || (localObject == null)) {
          localObject = localSize;
        }
      }
    }
    return (Camera.Size)localObject;
  }
  
  public static List<Camera.Size> sortSizeList(List<Camera.Size> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return paramList;
    }
    Collections.sort(paramList, new Comparator()
    {
      public int compare(Camera.Size paramAnonymousSize1, Camera.Size paramAnonymousSize2)
      {
        if (paramAnonymousSize1.width < paramAnonymousSize2.width) {
          return 1;
        }
        if (paramAnonymousSize1.width > paramAnonymousSize2.width) {
          return -1;
        }
        if (paramAnonymousSize1.height < paramAnonymousSize2.height) {
          return 1;
        }
        if (paramAnonymousSize1.height > paramAnonymousSize2.height) {
          return -1;
        }
        return 0;
      }
    });
    return paramList;
  }
  
  public static List<Camera.Size> sortMaxSizeList(List<Camera.Size> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return paramList;
    }
    Collections.sort(paramList, new Comparator()
    {
      public int compare(Camera.Size paramAnonymousSize1, Camera.Size paramAnonymousSize2)
      {
        if (paramAnonymousSize1.width * paramAnonymousSize1.height < paramAnonymousSize2.width * paramAnonymousSize2.height) {
          return 1;
        }
        if (paramAnonymousSize1.width * paramAnonymousSize1.height > paramAnonymousSize2.width * paramAnonymousSize2.height) {
          return -1;
        }
        return 0;
      }
    });
    return paramList;
  }
  
  public static boolean matchRatio(int paramInt, Camera.Size paramSize)
  {
    return Math.abs(paramInt - getSizeRatio(paramSize.width, paramSize.height)) < 2;
  }
  
  public static int getSizeRatio(int paramInt1, int paramInt2)
  {
    int i = Math.max(paramInt1, paramInt2);
    int j = Math.min(paramInt1, paramInt2);
    return (int)Math.floor(i / j * 10.0F);
  }
  
  public static int cameraDisplayOrientation(Context paramContext, Camera.CameraInfo paramCameraInfo)
  {
    if (paramCameraInfo == null) {
      return 0;
    }
    int i = ContextUtils.getRotation(paramContext);
    int j = 0;
    switch (i)
    {
    case 0: 
      j = 0;
      break;
    case 1: 
      j = 90;
      break;
    case 2: 
      j = 180;
      break;
    case 3: 
      j = 270;
    }
    int k;
    if (paramCameraInfo.facing == 1) {
      k = (paramCameraInfo.orientation + j) % 360;
    } else {
      k = (paramCameraInfo.orientation - j + 360) % 360;
    }
    return k;
  }
  
  public static int captureOrientation(Context paramContext, Camera.CameraInfo paramCameraInfo, int paramInt)
  {
    int i = cameraDisplayOrientation(paramContext, paramCameraInfo);
    int j;
    if (paramCameraInfo.facing == 1) {
      j = (i - paramInt + 360) % 360;
    } else {
      j = (i + paramInt) % 360;
    }
    return j;
  }
  
  public static boolean canSupportFaceDetection(Camera.Parameters paramParameters)
  {
    if (Build.VERSION.SDK_INT < 14) {
      return false;
    }
    return a(paramParameters);
  }
  
  @TargetApi(14)
  private static boolean a(Camera.Parameters paramParameters)
  {
    if (paramParameters == null) {
      return false;
    }
    return paramParameters.getMaxNumDetectedFaces() > 0;
  }
  
  public static List<TuSdkFace> transforFaces(Camera.Face[] paramArrayOfFace, ImageOrientation paramImageOrientation)
  {
    if ((paramArrayOfFace == null) || (paramArrayOfFace.length == 0)) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(paramArrayOfFace.length);
    List localList = sortFaceWithCenterList(Arrays.asList(paramArrayOfFace));
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      Camera.Face localFace = (Camera.Face)localIterator.next();
      localArrayList.add(transforFace(localFace, paramImageOrientation));
    }
    return localArrayList;
  }
  
  public static List<Camera.Face> sortFaceWithCenterList(List<Camera.Face> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      return paramList;
    }
    Collections.sort(paramList, new Comparator()
    {
      @TargetApi(14)
      public int compare(Camera.Face paramAnonymousFace1, Camera.Face paramAnonymousFace2)
      {
        if ((paramAnonymousFace1.rect == null) || (paramAnonymousFace2.rect == null) || (paramAnonymousFace1.rect.equals(paramAnonymousFace2.rect))) {
          return 0;
        }
        Point localPoint1 = new Point(paramAnonymousFace1.rect.centerX(), paramAnonymousFace1.rect.centerY());
        Point localPoint2 = new Point(paramAnonymousFace2.rect.centerX(), paramAnonymousFace2.rect.centerY());
        Point localPoint3 = new Point(0, 0);
        if (RectHelper.computerPotintDistance(localPoint1, localPoint3) > RectHelper.computerPotintDistance(localPoint2, localPoint3)) {
          return 1;
        }
        return -1;
      }
    });
    return paramList;
  }
  
  @TargetApi(14)
  public static TuSdkFace transforFace(Camera.Face paramFace, ImageOrientation paramImageOrientation)
  {
    if (paramFace == null) {
      return null;
    }
    TuSdkFace localTuSdkFace = new TuSdkFace();
    localTuSdkFace.id = paramFace.id;
    localTuSdkFace.score = paramFace.score;
    if (paramFace.rect != null)
    {
      Rect localRect = new Rect(paramFace.rect);
      localTuSdkFace.rect = new RectF();
      localTuSdkFace.rect.left = ((localRect.left + 1000) / 2000.0F);
      localTuSdkFace.rect.right = ((localRect.right + 1000) / 2000.0F);
      localTuSdkFace.rect.top = ((localRect.top + 1000) / 2000.0F);
      localTuSdkFace.rect.bottom = ((localRect.bottom + 1000) / 2000.0F);
    }
    localTuSdkFace.leftEye = a(paramFace.leftEye);
    localTuSdkFace.rightEye = a(paramFace.rightEye);
    localTuSdkFace.mouth = a(paramFace.mouth);
    TuSdkFace.convertOrientation(localTuSdkFace, paramImageOrientation);
    return localTuSdkFace;
  }
  
  private static PointF a(Point paramPoint)
  {
    if (paramPoint == null) {
      return null;
    }
    PointF localPointF = new PointF();
    localPointF.x = ((paramPoint.x + 1000) / 2000.0F);
    localPointF.y = ((paramPoint.y + 1000) / 2000.0F);
    return localPointF;
  }
  
  public static void logSize(Camera.Parameters paramParameters)
  {
    Camera.Size localSize1 = paramParameters.getPictureSize();
    Camera.Size localSize2 = paramParameters.getPreviewSize();
    TLog.i("logSize: [preview: %sx%s - picture: %sx%s]", new Object[] { Integer.valueOf(localSize2.width), Integer.valueOf(localSize2.height), Integer.valueOf(localSize1.width), Integer.valueOf(localSize1.height) });
  }
  
  public static void logParameters(Camera.Parameters paramParameters)
  {
    if (paramParameters == null) {
      return;
    }
    String[] arrayOfString1 = paramParameters.flatten().split(";");
    HashMap localHashMap = new HashMap(arrayOfString1.length);
    for (Object localObject2 : arrayOfString1)
    {
      String[] arrayOfString2 = ((String)localObject2).split("=");
      if (arrayOfString2.length == 2) {
        localHashMap.put(arrayOfString2[0], arrayOfString2[1]);
      }
    }
    ??? = new ArrayList(localHashMap.keySet());
    Collections.sort((List)???, new Comparator()
    {
      public int compare(String paramAnonymousString1, String paramAnonymousString2)
      {
        int i = 0;
        while ((i < paramAnonymousString1.length()) && (i < paramAnonymousString2.length()))
        {
          int j = paramAnonymousString1.charAt(i);
          int k = paramAnonymousString2.charAt(i);
          i++;
          int m = j - k;
          if (m != 0) {
            return m > 0 ? 1 : -1;
          }
        }
        return 0;
      }
    });
    Iterator localIterator = ((ArrayList)???).iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      TLog.i("log: %s = %s", new Object[] { str, localHashMap.get(str) });
    }
  }
  
  public static Rect computerCameraViewRect(Context paramContext, View paramView1, View paramView2, float paramFloat)
  {
    if ((paramContext == null) || (paramView1 == null) || (paramView2 == null)) {
      return null;
    }
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(paramContext);
    Rect localRect = new Rect(0, 0, localTuSdkSize.width, localTuSdkSize.height);
    paramFloat = Math.min(1.0F, Math.max(0.0F, paramFloat));
    if (paramFloat == 0.0F) {
      return localRect;
    }
    localRect.bottom = ((int)Math.floor(localRect.right / paramFloat));
    if (localRect.bottom + paramView1.getHeight() <= localTuSdkSize.height)
    {
      localRect.top = paramView1.getHeight();
      localRect.bottom += localRect.top;
    }
    int i = localTuSdkSize.height - localRect.bottom;
    if (i > paramView2.getHeight()) {
      TuSdkViewHelper.setViewHeight(paramView2, i);
    }
    return localRect;
  }
  
  public static boolean showAlertIfNotSupportCamera(Context paramContext)
  {
    return showAlertIfNotSupportCamera(paramContext, false);
  }
  
  public static boolean showAlertIfNotSupportCamera(Context paramContext, boolean paramBoolean)
  {
    if ((!paramBoolean) && (Build.VERSION.SDK_INT >= 23)) {
      return false;
    }
    if (paramContext == null) {
      return true;
    }
    String str = null;
    if (cameraCounts() == 0) {
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
  
  static
  {
    if (Build.VERSION.SDK_INT > 13)
    {
      autoFocusModes.add("continuous-picture");
      autoFocusModes.add("continuous-video");
    }
    autoFocusModes.add("auto");
    autoFocusModes.add("macro");
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\CameraHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */