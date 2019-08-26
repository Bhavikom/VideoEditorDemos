// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.TuSdkContext;
import android.os.Build;
import android.util.Range;
import android.graphics.RectF;
import android.graphics.Point;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.hardware.camera2.params.Face;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.hardware.camera2.params.MeteringRectangle;
import android.util.Size;
import android.graphics.Rect;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.PointF;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraAccessException;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.ContextUtils;
import android.hardware.camera2.CameraManager;
import android.content.Context;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;

@TargetApi(21)
public class Camera2Helper
{
    public static CameraManager cameraManager(final Context context) {
        return ContextUtils.getSystemService(context, "camera");
    }
    
    public static boolean canSupportCamera(final Context context) {
        return (boolean)(ContextUtils.hasSystemFeature(context, "android.hardware.camera") && cameraCounts(context) > 0);
    }
    
    public static String[] cameraIds(final Context context) {
        return cameraIds(cameraManager(context));
    }
    
    public static String[] cameraIds(final CameraManager cameraManager) {
        if (cameraManager == null) {
            return null;
        }
        try {
            return cameraManager.getCameraIdList();
        }
        catch (CameraAccessException ex) {
            TLog.e((Throwable)ex, "Get system all camera ids error", new Object[0]);
            return null;
        }
    }
    
    public static int cameraCounts(final Context context) {
        final String[] cameraIds = cameraIds(context);
        if (cameraIds == null) {
            return 0;
        }
        return cameraIds.length;
    }
    
    public static String firstBackCameraId(final Context context) {
        return firstCameraId(context, 1);
    }
    
    public static String firstFrontCameraId(final Context context) {
        return firstCameraId(context, 0);
    }
    
    public static String firstCameraId(final Context context, CameraConfigs.CameraFacing back) {
        if (back == null) {
            back = CameraConfigs.CameraFacing.Back;
        }
        int n = 1;
        switch (back.ordinal()) {
            case 1: {
                n = 0;
                break;
            }
        }
        return firstCameraId(context, n);
    }
    
    public static CameraConfigs.CameraFacing cameraPosition(final CameraCharacteristics cameraCharacteristics) {
        if (cameraCharacteristics == null) {
            return null;
        }
        switch ((int)cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)) {
            case 0: {
                return CameraConfigs.CameraFacing.Front;
            }
            default: {
                return CameraConfigs.CameraFacing.Back;
            }
        }
    }
    
    public static String firstCameraId(final Context context, final int n) {
        final CameraManager cameraManager = cameraManager(context);
        final String[] cameraIds = cameraIds(context);
        if (cameraIds == null) {
            return null;
        }
        String s = null;
        final String[] array = cameraIds;
        for (int length = array.length, n2 = 0; n2 < length && (int)cameraCharacter(cameraManager, s = array[n2]).get(CameraCharacteristics.LENS_FACING) != n; ++n2) {}
        return s;
    }
    
    public static CameraCharacteristics cameraCharacter(final CameraManager cameraManager, final String s) {
        if (cameraManager == null || s == null) {
            return null;
        }
        try {
            return cameraManager.getCameraCharacteristics(s);
        }
        catch (CameraAccessException ex) {
            TLog.e((Throwable)ex, "Get Camera Character error: %s", s);
            return null;
        }
    }
    
    public static StreamConfigurationMap streamConfigurationMap(final CameraCharacteristics cameraCharacteristics) {
        return (StreamConfigurationMap)cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
    }
    
    public static <T> void mergerBuilder(final CaptureRequest.Builder builder, final CaptureRequest.Builder builder2, final CaptureRequest.Key<T> key) {
        if (builder == null || builder2 == null || key == null) {
            return;
        }
        final Object value = builder.get((CaptureRequest.Key)key);
        if (value != null) {
            builder2.set((CaptureRequest.Key)key, value);
        }
    }
    
    public static int supportHardwareLevel(final CameraCharacteristics cameraCharacteristics) {
        if (cameraCharacteristics == null) {
            return -1;
        }
        final Integer n = (Integer)cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        if (n == null) {
            return -1;
        }
        return n;
    }
    
    public static boolean hardwareOnlySupportLegacy(final Context context) {
        return hardwareOnlySupportLegacy(cameraCharacter(cameraManager(context), firstBackCameraId(context)));
    }
    
    public static boolean hardwareOnlySupportLegacy(final CameraCharacteristics cameraCharacteristics) {
        final int supportHardwareLevel = supportHardwareLevel(cameraCharacteristics);
        return supportHardwareLevel < 0 || supportHardwareLevel == 2;
    }
    
    public static boolean canSupportFlash(final Context context) {
        return ContextUtils.hasSystemFeature(context, "android.hardware.camera.flash");
    }
    
    public static boolean supportFlash(final CameraCharacteristics cameraCharacteristics) {
        return cameraCharacteristics != null && (boolean)cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
    }
    
    public static CameraConfigs.CameraFlash getFlashMode(final CaptureRequest.Builder builder) {
        CameraConfigs.CameraFlash cameraFlash = CameraConfigs.CameraFlash.Off;
        if (builder == null) {
            return CameraConfigs.CameraFlash.Off;
        }
        final Integer n = (Integer)builder.get(CaptureRequest.CONTROL_AE_MODE);
        final Integer n2 = (Integer)builder.get(CaptureRequest.FLASH_MODE);
        if (n == null) {
            return cameraFlash;
        }
        if (n2 != null) {
            switch (n2) {
                case 1: {
                    cameraFlash = CameraConfigs.CameraFlash.On;
                    break;
                }
                case 2: {
                    cameraFlash = CameraConfigs.CameraFlash.Torch;
                    break;
                }
            }
        }
        switch (n) {
            case 2: {
                cameraFlash = CameraConfigs.CameraFlash.Auto;
                break;
            }
            case 4: {
                cameraFlash = CameraConfigs.CameraFlash.RedEye;
                break;
            }
            case 3: {
                cameraFlash = CameraConfigs.CameraFlash.Always;
                break;
            }
            case 0: {
                cameraFlash = CameraConfigs.CameraFlash.Off;
                break;
            }
        }
        return cameraFlash;
    }
    
    public static void setFlashMode(final CaptureRequest.Builder builder, final CameraConfigs.CameraFlash cameraFlash) {
        if (builder == null || cameraFlash == null) {
            return;
        }
        builder.set(CaptureRequest.CONTROL_MODE, 1);
        switch (cameraFlash.ordinal()) {
            case 1: {
                builder.set(CaptureRequest.CONTROL_AE_MODE, 1);
                builder.set(CaptureRequest.FLASH_MODE, 0);
                break;
            }
            case 2: {
                builder.set(CaptureRequest.CONTROL_AE_MODE, 2);
                break;
            }
            case 3: {
                builder.set(CaptureRequest.CONTROL_AE_MODE, 1);
                builder.set(CaptureRequest.FLASH_MODE, 1);
                break;
            }
            case 4: {
                builder.set(CaptureRequest.CONTROL_AE_MODE, 1);
                builder.set(CaptureRequest.FLASH_MODE, 2);
                break;
            }
            case 5: {
                builder.set(CaptureRequest.CONTROL_AE_MODE, 3);
                break;
            }
            case 6: {
                builder.set(CaptureRequest.CONTROL_AE_MODE, 4);
                break;
            }
        }
    }
    
    public static boolean canSupportAutofocus(final Context context) {
        return ContextUtils.hasSystemFeature(context, "android.hardware.camera.autofocus");
    }
    
    public static boolean canSupportAutofocus(final CameraCharacteristics cameraCharacteristics) {
        if (cameraCharacteristics == null) {
            return false;
        }
        final int[] array = (int[])cameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        return array != null && array.length != 0 && (array.length != 1 || array[0] != 0);
    }
    
    public static boolean canSupportAutofocus(final CameraCharacteristics cameraCharacteristics, final int n) {
        if (cameraCharacteristics == null) {
            return false;
        }
        final int[] array = (int[])cameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        if (array == null || array.length == 0) {
            return false;
        }
        final int[] array2 = array;
        for (int length = array2.length, i = 0; i < length; ++i) {
            if (n == array2[i]) {
                return true;
            }
        }
        return false;
    }
    
    public static int swichFocusMode(CameraConfigs.CameraAutoFocus off) {
        if (off == null) {
            off = CameraConfigs.CameraAutoFocus.Off;
        }
        switch (off.ordinal()) {
            case 1: {
                return 1;
            }
            case 2: {
                return 2;
            }
            case 3: {
                return 3;
            }
            case 4: {
                return 4;
            }
            case 5: {
                return 5;
            }
            default: {
                return 0;
            }
        }
    }
    
    public static CameraConfigs.CameraAutoFocus focusModeType(final CaptureRequest.Builder builder) {
        final CameraConfigs.CameraAutoFocus off = CameraConfigs.CameraAutoFocus.Off;
        if (builder == null) {
            return off;
        }
        final Integer n = (Integer)builder.get(CaptureRequest.CONTROL_AF_MODE);
        if (n == null) {
            return off;
        }
        switch (n) {
            case 1: {
                return CameraConfigs.CameraAutoFocus.Auto;
            }
            case 2: {
                return CameraConfigs.CameraAutoFocus.Macro;
            }
            case 3: {
                return CameraConfigs.CameraAutoFocus.ContinuousPicture;
            }
            case 4: {
                return CameraConfigs.CameraAutoFocus.ContinuousPicture;
            }
            case 5: {
                return CameraConfigs.CameraAutoFocus.EDOF;
            }
            default: {
                return off;
            }
        }
    }
    
    public static void setFocusMode(final CameraCharacteristics cameraCharacteristics, final CaptureRequest.Builder builder, final CameraConfigs.CameraAutoFocus cameraAutoFocus, final PointF pointF, final ImageOrientation imageOrientation) {
        if (cameraCharacteristics == null || builder == null || !canSupportAutofocus(cameraCharacteristics)) {
            return;
        }
        final int swichFocusMode = swichFocusMode(cameraAutoFocus);
        if (canSupportAutofocus(cameraCharacteristics, swichFocusMode)) {
            builder.set(CaptureRequest.CONTROL_AF_MODE, swichFocusMode);
        }
        setFocusPoint(cameraCharacteristics, builder, pointF, imageOrientation);
    }
    
    public static void setFocusPoint(final CameraCharacteristics cameraCharacteristics, final CaptureRequest.Builder builder, final PointF pointF, ImageOrientation up) {
        if (cameraCharacteristics == null || builder == null || pointF == null) {
            return;
        }
        final Rect rect = (Rect)cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        final Size size = (Size)cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);
        final PointF pointF2 = new PointF(pointF.x, pointF.y);
        if (up == null) {
            up = ImageOrientation.Up;
        }
        switch (up.ordinal()) {
            case 1: {
                pointF2.x = pointF.y;
                pointF2.y = 1.0f - pointF.x;
                break;
            }
            case 2: {
                pointF2.x = 1.0f - pointF.x;
                pointF2.y = 1.0f - pointF.y;
                break;
            }
            case 3: {
                pointF2.x = 1.0f - pointF.y;
                pointF2.y = pointF.x;
                break;
            }
            case 4: {
                pointF2.x = 1.0f - pointF.x;
                pointF2.y = pointF.y;
                break;
            }
            case 5: {
                pointF2.x = 1.0f - pointF.y;
                pointF2.y = 1.0f - pointF.x;
                break;
            }
            case 6: {
                pointF2.x = pointF.x;
                pointF2.y = 1.0f - pointF.y;
                break;
            }
            case 7: {
                pointF2.x = pointF.y;
                pointF2.y = pointF.x;
                break;
            }
        }
        final int n = (int)(Math.min(rect.width(), rect.height()) * 0.03f);
        final int n2 = rect.left + (int)(pointF2.x * rect.width()) + n;
        final int n3 = rect.top + (int)(pointF2.y * rect.width()) + n;
        final MeteringRectangle[] array = { new MeteringRectangle(fixFocusRange(new Rect(n2 - n * 2, n3 - n * 2, n2, n3), size), 500) };
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, 2);
        builder.set(CaptureRequest.CONTROL_AF_REGIONS, array);
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
        builder.set(CaptureRequest.CONTROL_AE_REGIONS, array);
        builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, 1);
    }
    
    public static Rect fixFocusRange(final Rect rect, final Size size) {
        final Rect rect2 = new Rect(rect);
        if (rect.left < 0) {
            rect2.left = 0;
            rect2.right = rect.width();
        }
        else if (rect.right > size.getWidth()) {
            rect2.right = size.getWidth();
            rect2.left = rect2.right - rect.width();
        }
        if (rect.top < 0) {
            rect2.top = 0;
            rect2.bottom = rect.height();
        }
        else if (rect.bottom > size.getHeight()) {
            rect2.bottom = size.getHeight();
            rect2.top = rect2.bottom - rect.height();
        }
        return rect2;
    }
    
    public static TuSdkSize createSize(final Size size) {
        if (size != null) {
            return new TuSdkSize(size.getWidth(), size.getHeight());
        }
        return null;
    }
    
    public static Size previewOptimalSize(final Context context, final Size[] a, int n, float n2) {
        if (a == null) {
            return null;
        }
        final TuSdkSize screenSize = ContextUtils.getScreenSize(context);
        if (screenSize == null) {
            return null;
        }
        final List<Size> sortSizeList = sortSizeList(Arrays.asList(a));
        if (sortSizeList == null || sortSizeList.isEmpty()) {
            return null;
        }
        Size size = null;
        if (n < 1) {
            return sortSizeList.get(0);
        }
        if (n2 <= 0.0f || n2 > 1.0f) {
            n2 = 1.0f;
        }
        final double floor = Math.floor(screenSize.maxSide() * n2);
        if (floor < n) {
            n = (int)floor;
        }
        final List<Size> matchRatioSizes = getMatchRatioSizes(screenSize.getRatio(), sortSizeList);
        if (!matchRatioSizes.isEmpty()) {
            size = matchRatioSizes.get(0);
            for (final Size size2 : matchRatioSizes) {
                if (size2.getWidth() != size2.getHeight() && size2.getWidth() <= n) {
                    if (size2.getHeight() > n) {
                        continue;
                    }
                    size = size2;
                    break;
                }
            }
        }
        if (size == null) {
            size = sortSizeList.get(sortSizeList.size() - 1);
            for (final Size size3 : sortSizeList) {
                if (size3.getWidth() <= n) {
                    if (size3.getHeight() > n) {
                        continue;
                    }
                    size = size3;
                    break;
                }
            }
        }
        return size;
    }
    
    public static Size pictureOptimalSize(final Context context, final Size[] a, TuSdkSize tuSdkSize) {
        if (a == null) {
            return null;
        }
        final TuSdkSize screenSize = ContextUtils.getScreenSize(context);
        if (screenSize == null) {
            return null;
        }
        final List<Size> sortSizeList = sortSizeList(Arrays.asList(a));
        if (sortSizeList == null || sortSizeList.isEmpty()) {
            return null;
        }
        if (tuSdkSize == null) {
            tuSdkSize = screenSize;
        }
        Size size = getNearestSize(getMatchRatioSizes(tuSdkSize.getRatio(), sortSizeList), tuSdkSize);
        if (size == null) {
            size = getNearestSize(sortSizeList, tuSdkSize);
        }
        if (size == null) {
            size = sortSizeList.get(sortSizeList.size() - 1);
        }
        return size;
    }
    
    public static List<Size> sortSizeList(final List<Size> list) {
        if (list == null || list.size() == 0) {
            return list;
        }
        Collections.sort(list, new Comparator<Size>() {
            @Override
            public int compare(final Size size, final Size size2) {
                if (size.getWidth() < size2.getWidth()) {
                    return 1;
                }
                if (size.getWidth() > size2.getWidth()) {
                    return -1;
                }
                if (size.getHeight() < size2.getHeight()) {
                    return 1;
                }
                if (size.getHeight() > size2.getHeight()) {
                    return -1;
                }
                return 0;
            }
        });
        return list;
    }
    
    public static List<Size> getMatchRatioSizes(final int n, final List<Size> list) {
        final ArrayList<Size> list2 = new ArrayList<Size>();
        if (n == 0 || list == null) {
            return list2;
        }
        for (final Size size : list) {
            if (matchRatio(n, size)) {
                list2.add(size);
            }
        }
        return list2;
    }
    
    public static boolean matchRatio(final int n, final Size size) {
        return Math.abs(n - getSizeRatio(size.getWidth(), size.getHeight())) < 2;
    }
    
    public static int getSizeRatio(final int n, final int n2) {
        return (int)Math.floor(Math.max(n, n2) / (float)Math.min(n, n2) * 10.0f);
    }
    
    public static Size getNearestSize(final List<Size> list, final TuSdkSize tuSdkSize) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Size size = null;
        final int size2 = TuSdkGPU.getGpuType().getSize();
        for (final Size size3 : list) {
            final TuSdkSize size4 = createSize(size3);
            if (size4.maxSide() > size2) {
                continue;
            }
            if (tuSdkSize == null) {
                size = size3;
                break;
            }
            if (size4.maxSide() > tuSdkSize.maxSide() && size4.minSide() > tuSdkSize.minSide()) {
                size = size3;
            }
            else {
                if (size4.maxSide() == tuSdkSize.maxSide() || size == null) {
                    size = size3;
                    break;
                }
                break;
            }
        }
        return size;
    }
    
    public static boolean canSupportFaceDetection(final CameraCharacteristics cameraCharacteristics) {
        if (cameraCharacteristics == null) {
            return false;
        }
        final Integer n = (Integer)cameraCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT);
        if (n == null || n == 0) {
            return false;
        }
        final int[] array = (int[])cameraCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);
        return array != null && array.length != 0 && (array.length != 1 || array[0] != 0);
    }
    
    public static List<TuSdkFace> transforFaces(final CameraCharacteristics cameraCharacteristics, final TuSdkSize tuSdkSize, final Face[] a, final ImageOrientation imageOrientation) {
        if (cameraCharacteristics == null || tuSdkSize == null || a == null || a.length == 0) {
            return null;
        }
        final Rect rect = (Rect)cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        final TuSdkSize create = TuSdkSize.create(rect.width(), rect.height());
        final ArrayList<TuSdkFace> list = new ArrayList<TuSdkFace>(a.length);
        final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(tuSdkSize, new Rect(0, 0, create.width, create.height));
        final Iterator<Face> iterator = a(Arrays.asList(a), create.center()).iterator();
        while (iterator.hasNext()) {
            list.add(transforFace(iterator.next(), rectWithAspectRatioInsideRect, imageOrientation));
        }
        return list;
    }
    
    private static List<Face> a(final List<Face> list, final Point point) {
        if (list == null || list.size() == 0) {
            return list;
        }
        Collections.sort(list, new Comparator<Face>() {
            @Override
            public int compare(final Face face, final Face face2) {
                if (face.getBounds() == null || face2.getBounds() == null || face.getBounds().equals(face2.getBounds())) {
                    return 0;
                }
                if (RectHelper.computerPotintDistance(new Point(face.getBounds().centerX(), face.getBounds().centerY()), point) > RectHelper.computerPotintDistance(new Point(face2.getBounds().centerX(), face2.getBounds().centerY()), point)) {
                    return 1;
                }
                return -1;
            }
        });
        return list;
    }
    
    public static TuSdkFace transforFace(final Face face, final Rect rect, final ImageOrientation imageOrientation) {
        if (face == null || rect == null) {
            return null;
        }
        final TuSdkFace tuSdkFace = new TuSdkFace();
        tuSdkFace.id = face.getId();
        tuSdkFace.score = face.getScore();
        if (face.getBounds() != null) {
            tuSdkFace.rect = new RectF();
            tuSdkFace.rect.left = (face.getBounds().left - rect.left) / (float)rect.width();
            tuSdkFace.rect.right = (face.getBounds().right - rect.left) / (float)rect.width();
            tuSdkFace.rect.top = (face.getBounds().top - rect.top) / (float)rect.height();
            tuSdkFace.rect.bottom = (face.getBounds().bottom - rect.top) / (float)rect.height();
        }
        tuSdkFace.leftEye = a(face.getLeftEyePosition(), rect);
        tuSdkFace.rightEye = a(face.getRightEyePosition(), rect);
        tuSdkFace.mouth = a(face.getMouthPosition(), rect);
        TuSdkFace.convertOrientation(tuSdkFace, imageOrientation);
        return tuSdkFace;
    }
    
    private static PointF a(final Point point, final Rect rect) {
        if (point == null || rect == null) {
            return null;
        }
        final PointF pointF = new PointF();
        pointF.x = (point.x - rect.left) / (float)rect.width();
        pointF.y = (point.y - rect.top) / (float)rect.height();
        return pointF;
    }
    
    public static void logCameraCharacter(final CameraCharacteristics cameraCharacteristics) {
        if (cameraCharacteristics == null) {
            return;
        }
        for (final CameraCharacteristics.Key key : cameraCharacteristics.getKeys()) {
            if (key.equals(CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES);
            }
            else if (key.equals(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES);
            }
            else if (key.equals(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
            }
            else if (key.equals(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS);
            }
            else if (key.equals(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES);
            }
            else if (key.equals(CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES);
            }
            else if (key.equals(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
            }
            else if (key.equals(CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES)) {
                final Size[] array = (Size[])cameraCharacteristics.get(CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES);
                if (array == null) {
                    continue;
                }
                final Size[] array2 = array;
                for (int length = array2.length, i = 0; i < length; ++i) {
                    TLog.d("Camera %s: %s", key.getName(), array2[i]);
                }
            }
            else if (key.equals(CameraCharacteristics.NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.NOISE_REDUCTION_AVAILABLE_NOISE_REDUCTION_MODES);
            }
            else if (key.equals(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
            }
            else if (key.equals(CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.SENSOR_AVAILABLE_TEST_PATTERN_MODES);
            }
            else if (key.equals(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);
            }
            else if (key.equals(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)) {
                final Range[] array3 = (Range[])cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
                if (array3 == null) {
                    continue;
                }
                for (final Range range : array3) {
                    TLog.d("Camera %s: [%s - %s]", key.getName(), range.getLower(), range.getUpper());
                }
            }
            else if (key.equals(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)) {
                final StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap)cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (streamConfigurationMap == null) {
                    continue;
                }
                for (final int l : streamConfigurationMap.getOutputFormats()) {
                    final Size[] outputSizes = streamConfigurationMap.getOutputSizes(l);
                    for (int length4 = outputSizes.length, n = 0; n < length4; ++n) {
                        TLog.d("Camera %s: [%s] %s", key.getName(), l, outputSizes[n]);
                    }
                }
            }
            else if (key.equals(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES)) {
                a(cameraCharacteristics, (CameraCharacteristics.Key<int[]>)CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
            }
            else {
                TLog.d("Camera %s: %s", key.getName(), cameraCharacteristics.get(key));
            }
        }
    }
    
    private static void a(final CameraCharacteristics cameraCharacteristics, final CameraCharacteristics.Key<int[]> key) {
        if (key == null) {
            return;
        }
        final int[] array = (int[])cameraCharacteristics.get((CameraCharacteristics.Key)key);
        if (array == null) {
            return;
        }
        final int[] array2 = array;
        for (int length = array2.length, i = 0; i < length; ++i) {
            TLog.d("Camera %s: [%s]", key.getName(), array2[i]);
        }
    }
    
    public static boolean showAlertIfNotSupportCamera(final Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return false;
        }
        if (context == null) {
            return true;
        }
        String s = null;
        if (cameraCounts(context) == 0) {
            s = TuSdkContext.getString("lsq_carema_no_device");
        }
        else if (!canSupportCamera(context)) {
            s = TuSdkContext.getString("lsq_carema_no_access", ContextUtils.getAppName(context));
        }
        if (s == null) {
            return false;
        }
        TuSdkViewHelper.alert(context, TuSdkContext.getString("lsq_carema_alert_title"), s, TuSdkContext.getString("lsq_button_done"));
        return true;
    }
}
