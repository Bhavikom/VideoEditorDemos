// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.view.View;
import java.util.HashMap;
import android.graphics.RectF;
import android.graphics.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
import java.util.List;
import android.annotation.TargetApi;
import android.os.Build;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.PointF;
import java.util.Collection;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.hardware.Camera;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.TuSdkDate;
//import org.lasque.tusdk.core.utils.ContextUtils;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkDate;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;

import java.util.ArrayList;

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
    public static final String[] focusModes;
    public static final String[] videoFocusModes;
    public static final ArrayList<String> autoFocusModes;
    
    public static boolean canSupportCamera(final Context context) {
        Boolean b = ContextUtils.hasSystemFeature(context, "android.hardware.camera") && cameraCounts() > 0;
        if (b) {
            final TuSdkDate create = TuSdkDate.create();
            final Camera camera = getCamera(firstCameraInfo(0));
            if (camera == null) {
                b = false;
            }
            else {
                try {
                    camera.getParameters();
                    camera.release();
                }
                catch (RuntimeException ex) {
                    TLog.e("fail to access camera", new Object[0]);
                    b = false;
                }
            }
            TLog.d("time for checking camera access: %s ms", create.diffOfMillis());
        }
        return b;
    }
    
    public static int cameraCounts() {
        return Camera.getNumberOfCameras();
    }
    
    public static boolean canSupportBackFace() {
        final Camera.CameraInfo firstBackCameraInfo = firstBackCameraInfo();
        return firstBackCameraInfo != null && firstBackCameraInfo.facing == 0;
    }
    
    public static boolean canSupportFlash(final Context context) {
        return ContextUtils.hasSystemFeature(context, "android.hardware.camera.flash");
    }
    
    public static boolean canSupportAutofocus(final Context context) {
        return ContextUtils.hasSystemFeature(context, "android.hardware.camera.autofocus");
    }
    
    public static Camera getCamera(final Camera.CameraInfo cameraInfo) {
        if (cameraInfo == null) {
            return null;
        }
        try {
            return Camera.open(cameraInfo.facing);
        }
        catch (Exception ex) {
            TLog.e("open Camera: %s", ex);
            return null;
        }
    }
    
    public static Camera.CameraInfo firstBackCameraInfo() {
        return firstCameraInfo(0);
    }
    
    public static Camera.CameraInfo firstFrontCameraInfo() {
        return firstCameraInfo(1);
    }
    
    public static CameraConfigs.CameraFacing getCameraFacing(final Camera.CameraInfo cameraInfo) {
        if (cameraInfo == null) {
            return CameraConfigs.CameraFacing.Back;
        }
        switch (cameraInfo.facing) {
            case 1: {
                return CameraConfigs.CameraFacing.Front;
            }
            default: {
                return CameraConfigs.CameraFacing.Back;
            }
        }
    }
    
    public static Camera.CameraInfo firstCameraInfo(CameraConfigs.CameraFacing back) {
        if (back == null) {
            back = CameraConfigs.CameraFacing.Back;
        }
        int n = 0;
        switch (back.ordinal()) {
            case 1: {
                n = 1;
                break;
            }
        }
        return firstCameraInfo(n);
    }
    
    public static Camera.CameraInfo firstCameraInfo(final int n) {
        final int cameraCounts = cameraCounts();
        if (cameraCounts == 0) {
            return null;
        }
        Camera.CameraInfo cameraInfo = null;
        for (int i = 0; i < cameraCounts; ++i) {
            final Camera.CameraInfo cameraInfo2 = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo2);
            if (cameraInfo2.facing == n) {
                return cameraInfo2;
            }
            cameraInfo = cameraInfo2;
        }
        return cameraInfo;
    }
    
    public static CameraConfigs.CameraFacing cameraPosition(final Camera.CameraInfo cameraInfo) {
        if (cameraInfo == null) {
            return null;
        }
        switch (cameraInfo.facing) {
            case 1: {
                return CameraConfigs.CameraFacing.Front;
            }
            default: {
                return CameraConfigs.CameraFacing.Back;
            }
        }
    }
    
    public static TuSdkSize createSize(final Camera.Size size) {
        if (size != null) {
            return new TuSdkSize(size.width, size.height);
        }
        return null;
    }
    
    public static void unifiedParameters(final Camera.Parameters parameters) {
        if (parameters == null) {
            return;
        }
        setDenoise(parameters, false);
        setSharpness(parameters, 20);
    }
    
    public static String findSettableValue(final Collection<String> collection, final String... array) {
        String s = null;
        if (collection == null || array == null) {
            return s;
        }
        for (final String s2 : array) {
            if (collection.contains(s2)) {
                s = s2;
                break;
            }
        }
        return s;
    }
    
    public static void setColorEffect(final Camera.Parameters parameters, final String... array) {
        final String settableValue = findSettableValue(parameters.getSupportedColorEffects(), array);
        if (settableValue != null) {
            parameters.setColorEffect(settableValue);
        }
    }
    
    public static void setSceneMode(final Camera.Parameters parameters, final String... array) {
        final String settableValue = findSettableValue(parameters.getSupportedSceneModes(), array);
        if (settableValue != null) {
            parameters.setSceneMode(settableValue);
        }
    }
    
    public static void setWhiteBalance(final Camera.Parameters parameters, final String... array) {
        final String settableValue = findSettableValue(parameters.getSupportedWhiteBalance(), array);
        if (settableValue != null) {
            parameters.setWhiteBalance(settableValue);
        }
    }
    
    public static void setWhiteBalance(final Camera.Parameters parameters, CameraConfigs.CameraWhiteBalance auto) {
        if (auto == null) {
            auto = CameraConfigs.CameraWhiteBalance.Auto;
        }
        String s = "auto";
        switch (auto.ordinal()) {
            case 1: {
                s = "incandescent";
                break;
            }
            case 2: {
                s = "fluorescent";
                break;
            }
            case 3: {
                s = "warm-fluorescent";
                break;
            }
            case 4: {
                s = "daylight";
                break;
            }
            case 5: {
                s = "cloudy-daylight";
                break;
            }
            case 6: {
                s = "twilight";
                break;
            }
            case 7: {
                s = "shade";
                break;
            }
        }
        setWhiteBalance(parameters, s);
    }
    
    public static CameraConfigs.CameraWhiteBalance whiteBalance(final String s) {
        CameraConfigs.CameraWhiteBalance cameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Auto;
        if (s == null) {
            return cameraWhiteBalance;
        }
        if (s.equalsIgnoreCase("incandescent")) {
            cameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Incandescent;
        }
        else if (s.equalsIgnoreCase("fluorescent")) {
            cameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Fluorescent;
        }
        else if (s.equalsIgnoreCase("warm-fluorescent")) {
            cameraWhiteBalance = CameraConfigs.CameraWhiteBalance.WarmFluorescent;
        }
        else if (s.equalsIgnoreCase("daylight")) {
            cameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Daylight;
        }
        else if (s.equalsIgnoreCase("twilight")) {
            cameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Twilight;
        }
        else if (s.equalsIgnoreCase("shade")) {
            cameraWhiteBalance = CameraConfigs.CameraWhiteBalance.Shade;
        }
        return cameraWhiteBalance;
    }
    
    public static void setAntibanding(final Camera.Parameters parameters, final String... array) {
        final String settableValue = findSettableValue(parameters.getSupportedAntibanding(), array);
        if (settableValue != null) {
            parameters.setAntibanding(settableValue);
        }
    }
    
    public static void setAntibanding(final Camera.Parameters parameters, CameraConfigs.CameraAntibanding off) {
        if (off == null) {
            off = CameraConfigs.CameraAntibanding.Off;
        }
        String s = null;
        switch (off.ordinal()) {
            case 1: {
                s = "auto";
                break;
            }
            case 2: {
                s = "50hz";
                break;
            }
            case 3: {
                s = "60hz";
                break;
            }
            default: {
                s = "off";
                break;
            }
        }
        setAntibanding(parameters, s);
    }
    
    public static CameraConfigs.CameraAntibanding antiBandingType(final String s) {
        CameraConfigs.CameraAntibanding cameraAntibanding = CameraConfigs.CameraAntibanding.Off;
        if (s == null) {
            return cameraAntibanding;
        }
        if (s.equalsIgnoreCase("auto")) {
            cameraAntibanding = CameraConfigs.CameraAntibanding.Auto;
        }
        else if (s.equalsIgnoreCase("50hz")) {
            cameraAntibanding = CameraConfigs.CameraAntibanding.RATE_50HZ;
        }
        else if (s.equalsIgnoreCase("60hz")) {
            cameraAntibanding = CameraConfigs.CameraAntibanding.RATE_60HZ;
        }
        else if (s.equalsIgnoreCase("off")) {
            cameraAntibanding = CameraConfigs.CameraAntibanding.Off;
        }
        return cameraAntibanding;
    }
    
    public static boolean canSupportDenoise(final Camera.Parameters parameters) {
        return parameters != null && parameters.get("denoise") != null;
    }
    
    public static boolean getDenoise(final Camera.Parameters parameters) {
        if (!canSupportDenoise(parameters)) {
            return false;
        }
        final String value = parameters.get("denoise");
        return value != null && !value.equalsIgnoreCase("denoise-off");
    }
    
    public static boolean setDenoise(final Camera.Parameters parameters, final boolean b) {
        if (!canSupportDenoise(parameters)) {
            return false;
        }
        parameters.set("denoise", b ? "denoise-on" : "denoise-off");
        return true;
    }
    
    public static boolean canSupportSharpness(final Camera.Parameters parameters) {
        return parameters != null && parameters.get("sharpness") != null;
    }
    
    public static int getSharpness(final Camera.Parameters parameters) {
        if (!canSupportSharpness(parameters)) {
            return 0;
        }
        return parameters.getInt("sharpness");
    }
    
    public static boolean setSharpness(final Camera.Parameters parameters, final int a) {
        if (!canSupportSharpness(parameters)) {
            return false;
        }
        parameters.set("sharpness", Math.max(parameters.getInt("min-sharpness"), Math.min(a, parameters.getInt("max-sharpness"))));
        return true;
    }
    
    public static boolean canSupportAutofocus(final Context context, final Camera.Parameters parameters) {
        return canSupportAutofocus(context) && parameters != null && parameters.getFocusMode() != null && CameraHelper.autoFocusModes.contains(parameters.getFocusMode());
    }
    
    public static boolean isContinuous(final Camera.Parameters parameters) {
        if (parameters == null) {
            return false;
        }
        final String focusMode = parameters.getFocusMode();
        return focusMode != null && focusMode.equalsIgnoreCase("continuous-picture");
    }
    
    public static void setFocusMode(final Camera.Parameters parameters, final String... array) {
        if (parameters == null) {
            return;
        }
        final String settableValue = findSettableValue(parameters.getSupportedFocusModes(), array);
        if (settableValue != null) {
            parameters.setFocusMode(settableValue);
        }
    }
    
    public static String swichFocusMode(CameraConfigs.CameraAutoFocus off) {
        if (off == null) {
            off = CameraConfigs.CameraAutoFocus.Off;
        }
        switch (off.ordinal()) {
            case 1: {
                return "auto";
            }
            case 2: {
                return "macro";
            }
            case 3: {
                return "continuous-video";
            }
            case 4: {
                return "continuous-picture";
            }
            case 5: {
                return "edof";
            }
            default: {
                return "infinity";
            }
        }
    }
    
    public static CameraConfigs.CameraAutoFocus focusModeType(final String s) {
        CameraConfigs.CameraAutoFocus cameraAutoFocus = CameraConfigs.CameraAutoFocus.Off;
        if (s == null) {
            return cameraAutoFocus;
        }
        if (s.equalsIgnoreCase("auto")) {
            cameraAutoFocus = CameraConfigs.CameraAutoFocus.Auto;
        }
        else if (s.equalsIgnoreCase("macro")) {
            cameraAutoFocus = CameraConfigs.CameraAutoFocus.Macro;
        }
        else if (s.equalsIgnoreCase("continuous-video")) {
            cameraAutoFocus = CameraConfigs.CameraAutoFocus.ContinuousVideo;
        }
        else if (s.equalsIgnoreCase("continuous-picture")) {
            cameraAutoFocus = CameraConfigs.CameraAutoFocus.ContinuousPicture;
        }
        else if (s.equalsIgnoreCase("edof")) {
            cameraAutoFocus = CameraConfigs.CameraAutoFocus.EDOF;
        }
        return cameraAutoFocus;
    }
    
    public static void setFocusMode(final Camera.Parameters parameters, final CameraConfigs.CameraAutoFocus cameraAutoFocus, final PointF pointF, final ImageOrientation imageOrientation) {
        if (parameters == null) {
            return;
        }
        final String swichFocusMode = swichFocusMode(cameraAutoFocus);
        if (parameters.getSupportedFocusModes() != null && parameters.getSupportedFocusModes().contains(swichFocusMode)) {
            parameters.setFocusMode(swichFocusMode);
        }
        setFocusPoint(parameters, pointF, imageOrientation);
    }
    
    public static void setFocusPoint(final Camera.Parameters parameters, final PointF pointF, final ImageOrientation imageOrientation) {
        if (pointF == null || parameters == null || Build.VERSION.SDK_INT < 14) {
            return;
        }
        setFocusArea(parameters, pointF, imageOrientation);
    }
    
    @TargetApi(14)
    public static void setFocusArea(final Camera.Parameters parameters, final PointF pointF, final ImageOrientation imageOrientation) {
        setFocusArea(parameters, pointF, imageOrientation, true);
    }
    
    @TargetApi(14)
    public static void setFocusArea(final Camera.Parameters parameters, final PointF pointF, final ImageOrientation imageOrientation, final boolean b) {
        final Camera.Area convertToCameraArea = convertToCameraArea(pointF, imageOrientation, 1000);
        setFocusArea(parameters, convertToCameraArea);
        if (b) {
            setMeteringArea(parameters, convertToCameraArea);
        }
    }
    
    @TargetApi(14)
    public static void setFocusArea(final Camera.Parameters parameters, final Camera.Area e) {
        if (parameters == null || e == null || parameters.getMaxNumFocusAreas() < 1) {
            return;
        }
        final ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>(1);
        focusAreas.add(e);
        parameters.setFocusAreas((List)focusAreas);
    }
    
    @TargetApi(14)
    public static void setMeteringArea(final Camera.Parameters parameters, final Camera.Area e) {
        if (parameters == null || e == null || parameters.getMaxNumMeteringAreas() < 1) {
            return;
        }
        final ArrayList<Camera.Area> meteringAreas = new ArrayList<Camera.Area>(1);
        meteringAreas.add(e);
        parameters.setMeteringAreas((List)meteringAreas);
    }
    
    @TargetApi(14)
    public static Camera.Area convertToCameraArea(final PointF pointF, ImageOrientation up, final int n) {
        if (pointF == null) {
            return new Camera.Area(new Rect(0, 0, 0, 0), 1000);
        }
        if (up == null) {
            up = ImageOrientation.Up;
        }
        final PointF pointF2 = new PointF(pointF.x, pointF.y);
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
        return new Camera.Area(getFocusRect((int)(pointF2.x * 2000.0f) - 1000, (int)(pointF2.y * 2000.0f) - 1000, 100, 100), n);
    }
    
    public static Rect getFocusRect(final int n, final int n2, final int n3, final int n4) {
        final int n5 = (int)(n3 * 0.5);
        final int n6 = (int)(n4 * 0.5);
        final Rect rect = new Rect(n - n5, n2 - n6, n + n5, n2 + n6);
        if (rect.left < -1000) {
            rect.left = -1000;
            rect.right = rect.left + n3;
        }
        else if (rect.right > 1000) {
            rect.right = 1000;
            rect.left = rect.right - n3;
        }
        if (rect.top < -1000) {
            rect.top = -1000;
            rect.bottom = rect.top + n4;
        }
        else if (rect.bottom > 1000) {
            rect.bottom = 1000;
            rect.top = rect.bottom - n4;
        }
        return rect;
    }
    
    public static boolean supportFlash(final Camera.Parameters parameters) {
        if (parameters == null) {
            return false;
        }
        final List supportedFlashModes = parameters.getSupportedFlashModes();
        return supportedFlashModes != null && supportedFlashModes.size() != 0 && (supportedFlashModes.size() != 1 || !supportedFlashModes.contains("off"));
    }
    
    public static CameraConfigs.CameraFlash getFlashMode(final Camera.Parameters parameters) {
        if (parameters == null) {
            return CameraConfigs.CameraFlash.Off;
        }
        final String flashMode = parameters.getFlashMode();
        if (flashMode == null) {
            return CameraConfigs.CameraFlash.Off;
        }
        if (flashMode.equalsIgnoreCase("on")) {
            return CameraConfigs.CameraFlash.On;
        }
        if (flashMode.equalsIgnoreCase("auto")) {
            return CameraConfigs.CameraFlash.Auto;
        }
        if (flashMode.equalsIgnoreCase("torch")) {
            return CameraConfigs.CameraFlash.Torch;
        }
        if (flashMode.equalsIgnoreCase("red-eye")) {
            return CameraConfigs.CameraFlash.RedEye;
        }
        return CameraConfigs.CameraFlash.Off;
    }
    
    public static void setFlashMode(final Camera.Parameters parameters, CameraConfigs.CameraFlash off) {
        if (off == null) {
            off = CameraConfigs.CameraFlash.Off;
        }
        String s = "off";
        switch (off.ordinal()) {
            case 1: {
                s = "on";
                break;
            }
            case 2: {
                s = "auto";
                break;
            }
            case 3: {
                s = "torch";
                break;
            }
            case 4: {
                s = "on";
                break;
            }
            case 5: {
                s = "red-eye";
                break;
            }
        }
        setFlashMode(parameters, s);
    }
    
    public static void setFlashMode(final Camera.Parameters parameters, final String flashMode) {
        if (parameters == null) {
            return;
        }
        final List supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || flashMode == null) {
            return;
        }
        if (supportedFlashModes.contains(flashMode)) {
            parameters.setFlashMode(flashMode);
        }
    }
    
    public static void setPreviewSize(final Context context, final Camera.Parameters parameters, final int n, final float n2) {
        setPreviewSize(context, parameters, n, n2, 0.0f);
    }
    
    public static void setPreviewSize(final Context context, final Camera.Parameters parameters, int n, float n2, float maxMinRatio) {
        if (parameters == null) {
            return;
        }
        final TuSdkSize screenSize = ContextUtils.getScreenSize(context);
        if (screenSize == null) {
            return;
        }
        if (n2 <= 0.0f || n2 > 1.0f) {
            n2 = 1.0f;
        }
        final double floor = Math.floor(screenSize.maxSide() * n2);
        if (floor < n) {
            n = (int)floor;
        }
        if (maxMinRatio <= 0.0f) {
            maxMinRatio = screenSize.maxMinRatio();
        }
        else if (maxMinRatio < 1.0f) {
            maxMinRatio = 1.0f / maxMinRatio;
        }
        setPreviewSize(parameters, TuSdkSize.create(n, (int)(n / maxMinRatio)));
    }
    
    public static void setMaxPreviewSize(final Camera.Parameters parameters, TuSdkSize create) {
        if (parameters == null || create == null || !create.isSize()) {
            return;
        }
        final List<Camera.Size> sortMaxSizeList = sortMaxSizeList(parameters.getSupportedPreviewSizes());
        if (sortMaxSizeList == null || sortMaxSizeList.isEmpty()) {
            return;
        }
        create = TuSdkSize.create(create.maxSide(), create.minSide());
        final Camera.Size size = sortMaxSizeList.get(0);
        TLog.d("matched max previewSize found for (%d, %d)", size.width, size.height);
        parameters.setPreviewSize(size.width, size.height);
    }
    
    public static void setPreviewSize(final Camera.Parameters parameters, TuSdkSize create) {
        if (parameters == null || create == null || !create.isSize()) {
            return;
        }
        final List<Camera.Size> sortSizeList = sortSizeList(parameters.getSupportedPreviewSizes());
        if (sortSizeList == null || sortSizeList.isEmpty()) {
            return;
        }
        create = TuSdkSize.create(create.maxSide(), create.minSide());
        Camera.Size size = null;
        for (final Camera.Size size2 : sortSizeList) {
            if (Math.max(size2.width, size2.height) == create.width) {
                if (Math.min(size2.width, size2.height) != create.height) {
                    continue;
                }
                size = size2;
                break;
            }
        }
        if (size == null) {
            final int n = create.width * create.height;
            final float n2 = create.width / (float)create.height;
            int n3 = Integer.MAX_VALUE;
            float n4 = Float.MAX_VALUE;
            for (final Camera.Size size3 : sortSizeList) {
                final int max = Math.max(size3.width, size3.height);
                final int min = Math.min(size3.width, size3.height);
                final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(create, new Rect(0, 0, max, min));
                final int n5 = rectWithAspectRatioInsideRect.width() * rectWithAspectRatioInsideRect.height();
                final float n6 = max / (float)min;
                final int abs = Math.abs(n5 - n);
                final float abs2 = Math.abs(n6 - n2);
                if (abs < n3 || (abs == n3 && abs2 < n4)) {
                    n3 = abs;
                    n4 = abs2;
                    size = size3;
                }
            }
        }
        TLog.d("matched previewSize found for (%d, %d) : (%d, %d)", create.width, create.height, size.width, size.height);
        parameters.setPreviewSize(size.width, size.height);
    }

    public static boolean setPreviewFpsRange(Camera.Parameters var0, int var1) {
        List var2 = var0.getSupportedPreviewFpsRange();
        Iterator var3 = var2.iterator();

        int[] var4;
        do {
            if (!var3.hasNext()) {
                int var7 = 0;
                int var8 = 0;
                Iterator var5 = var2.iterator();

                while(var5.hasNext()) {
                    int[] var6 = (int[])var5.next();
                    if (Math.min(var6[0], var6[1]) > var7 && Math.max(var6[0], var6[1]) >= var1) {
                        var7 = Math.min(var6[0], var6[1]);
                        var8 = Math.max(var6[0], var6[1]);
                    }
                }

                if (var7 > 0) {
                    var0.setPreviewFpsRange(var7, var8);
                    return true;
                }

                TLog.d("Couldn't find matched Fps range for [" + var1 + "]", new Object[0]);
                return false;
            }

            var4 = (int[])var3.next();
        } while(var4[0] != var4[1] || var4[1] != var1);

        var0.setPreviewFpsRange(var4[0], var4[1]);
        return true;
    }
    
    public static List<Camera.Size> getMatchRatioSizes(final int n, final List<Camera.Size> list) {
        final ArrayList<Camera.Size> list2 = new ArrayList<Camera.Size>();
        if (n == 0 || list == null) {
            return list2;
        }
        for (final Camera.Size size : list) {
            if (matchRatio(n, size)) {
                list2.add(size);
            }
        }
        return list2;
    }
    
    public static void setPictureSize(final Context context, final Camera.Parameters parameters, final TuSdkSize tuSdkSize) {
        setMaxPictureSize(context, parameters, tuSdkSize, 0.0f);
    }
    
    public static void setMaxPictureSize(final Context context, final Camera.Parameters parameters, TuSdkSize tuSdkSize, final float n) {
        if (parameters == null) {
            return;
        }
        final TuSdkSize screenSize = ContextUtils.getScreenSize(context);
        if (screenSize == null) {
            return;
        }
        if (tuSdkSize == null) {
            tuSdkSize = screenSize;
        }
        final List<Camera.Size> sortMaxSizeList = sortMaxSizeList(parameters.getSupportedPictureSizes());
        if (sortMaxSizeList == null || sortMaxSizeList.isEmpty()) {
            return;
        }
        final Camera.Size size = sortMaxSizeList.get(0);
        TLog.d("matched max pictureSize found for (%d, %d)", size.width, size.height);
        parameters.setPictureSize(size.width, size.height);
    }
    
    public static void setPictureSize(final Context context, final Camera.Parameters parameters, TuSdkSize tuSdkSize, final float n) {
        if (parameters == null) {
            return;
        }
        final TuSdkSize screenSize = ContextUtils.getScreenSize(context);
        if (screenSize == null) {
            return;
        }
        if (tuSdkSize == null) {
            tuSdkSize = screenSize;
        }
        final List<Camera.Size> sortSizeList = sortSizeList(parameters.getSupportedPictureSizes());
        if (sortSizeList == null || sortSizeList.isEmpty()) {
            return;
        }
        int ratio = tuSdkSize.getRatio();
        if (n > 0.0f) {
            ratio = (int)Math.floor(n * 10.0f);
        }
        Camera.Size size = getNearestSize(getMatchRatioSizes(ratio, sortSizeList), tuSdkSize);
        if (size == null) {
            size = getNearestSize(sortSizeList, TuSdkSize.create(parameters.getPreviewSize().width, parameters.getPreviewSize().height));
        }
        if (size == null) {
            size = sortSizeList.get(sortSizeList.size() - 1);
        }
        TLog.d("matched pictureSize found for (%d, %d) : (%d, %d)", tuSdkSize.width, tuSdkSize.height, size.width, size.height);
        parameters.setPictureSize(size.width, size.height);
    }
    
    public static Camera.Size getNearestSize(final List<Camera.Size> list, final TuSdkSize tuSdkSize) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Camera.Size size = null;
        final int size2 = TuSdkGPU.getGpuType().getSize();
        for (final Camera.Size size3 : list) {
            final TuSdkSize size4 = createSize(size3);
            if (size4.maxSide() > size2) {
                continue;
            }
            if (tuSdkSize == null) {
                size = size3;
                break;
            }
            if (size4.maxSide() > tuSdkSize.maxSide() && size4.minSide() > tuSdkSize.minSide() && size4.maxSide() != size4.minSide()) {
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
    
    public static List<Camera.Size> sortSizeList(final List<Camera.Size> list) {
        if (list == null || list.size() == 0) {
            return list;
        }
        Collections.sort(list, new Comparator<Camera.Size>() {
            @Override
            public int compare(final Camera.Size size, final Camera.Size size2) {
                if (size.width < size2.width) {
                    return 1;
                }
                if (size.width > size2.width) {
                    return -1;
                }
                if (size.height < size2.height) {
                    return 1;
                }
                if (size.height > size2.height) {
                    return -1;
                }
                return 0;
            }
        });
        return list;
    }
    
    public static List<Camera.Size> sortMaxSizeList(final List<Camera.Size> list) {
        if (list == null || list.size() == 0) {
            return list;
        }
        Collections.sort(list, new Comparator<Camera.Size>() {
            @Override
            public int compare(final Camera.Size size, final Camera.Size size2) {
                if (size.width * size.height < size2.width * size2.height) {
                    return 1;
                }
                if (size.width * size.height > size2.width * size2.height) {
                    return -1;
                }
                return 0;
            }
        });
        return list;
    }
    
    public static boolean matchRatio(final int n, final Camera.Size size) {
        return Math.abs(n - getSizeRatio(size.width, size.height)) < 2;
    }
    
    public static int getSizeRatio(final int n, final int n2) {
        return (int)Math.floor(Math.max(n, n2) / (float)Math.min(n, n2) * 10.0f);
    }
    
    public static int cameraDisplayOrientation(final Context context, final Camera.CameraInfo cameraInfo) {
        if (cameraInfo == null) {
            return 0;
        }
        final int rotation = ContextUtils.getRotation(context);
        int n = 0;
        switch (rotation) {
            case 0: {
                n = 0;
                break;
            }
            case 1: {
                n = 90;
                break;
            }
            case 2: {
                n = 180;
                break;
            }
            case 3: {
                n = 270;
                break;
            }
        }
        int n2;
        if (cameraInfo.facing == 1) {
            n2 = (cameraInfo.orientation + n) % 360;
        }
        else {
            n2 = (cameraInfo.orientation - n + 360) % 360;
        }
        return n2;
    }
    
    public static int captureOrientation(final Context context, final Camera.CameraInfo cameraInfo, final int n) {
        final int cameraDisplayOrientation = cameraDisplayOrientation(context, cameraInfo);
        int n2;
        if (cameraInfo.facing == 1) {
            n2 = (cameraDisplayOrientation - n + 360) % 360;
        }
        else {
            n2 = (cameraDisplayOrientation + n) % 360;
        }
        return n2;
    }
    
    public static boolean canSupportFaceDetection(final Camera.Parameters parameters) {
        return Build.VERSION.SDK_INT >= 14 && a(parameters);
    }
    
    @TargetApi(14)
    private static boolean a(final Camera.Parameters parameters) {
        return parameters != null && parameters.getMaxNumDetectedFaces() > 0;
    }
    
    public static List<TuSdkFace> transforFaces(final Camera.Face[] a, final ImageOrientation imageOrientation) {
        if (a == null || a.length == 0) {
            return null;
        }
        final ArrayList<TuSdkFace> list = new ArrayList<TuSdkFace>(a.length);
        final Iterator<Camera.Face> iterator = sortFaceWithCenterList(Arrays.asList(a)).iterator();
        while (iterator.hasNext()) {
            list.add(transforFace(iterator.next(), imageOrientation));
        }
        return list;
    }

    public static List<Camera.Face> sortFaceWithCenterList(List<Camera.Face> var0) {
        if (var0 != null && var0.size() != 0) {
            Collections.sort(var0, new Comparator<Camera.Face>() {
                @TargetApi(14)
                public int compare(Camera.Face var1, Camera.Face var2) {
                    if (var1.rect != null && var2.rect != null && !var1.rect.equals(var2.rect)) {
                        Point var3 = new Point(var1.rect.centerX(), var1.rect.centerY());
                        Point var4 = new Point(var2.rect.centerX(), var2.rect.centerY());
                        Point var5 = new Point(0, 0);
                        return RectHelper.computerPotintDistance(var3, var5) > RectHelper.computerPotintDistance(var4, var5) ? 1 : -1;
                    } else {
                        return 0;
                    }
                }
            });
            return var0;
        } else {
            return var0;
        }
    }
    
    @TargetApi(14)
    public static TuSdkFace transforFace(final Camera.Face face, final ImageOrientation imageOrientation) {
        if (face == null) {
            return null;
        }
        final TuSdkFace tuSdkFace = new TuSdkFace();
        tuSdkFace.id = face.id;
        tuSdkFace.score = face.score;
        if (face.rect != null) {
            final Rect rect = new Rect(face.rect);
            tuSdkFace.rect = new RectF();
            tuSdkFace.rect.left = (rect.left + 1000) / 2000.0f;
            tuSdkFace.rect.right = (rect.right + 1000) / 2000.0f;
            tuSdkFace.rect.top = (rect.top + 1000) / 2000.0f;
            tuSdkFace.rect.bottom = (rect.bottom + 1000) / 2000.0f;
        }
        tuSdkFace.leftEye = a(face.leftEye);
        tuSdkFace.rightEye = a(face.rightEye);
        tuSdkFace.mouth = a(face.mouth);
        TuSdkFace.convertOrientation(tuSdkFace, imageOrientation);
        return tuSdkFace;
    }
    
    private static PointF a(final Point point) {
        if (point == null) {
            return null;
        }
        final PointF pointF = new PointF();
        pointF.x = (point.x + 1000) / 2000.0f;
        pointF.y = (point.y + 1000) / 2000.0f;
        return pointF;
    }
    
    public static void logSize(final Camera.Parameters parameters) {
        final Camera.Size pictureSize = parameters.getPictureSize();
        final Camera.Size previewSize = parameters.getPreviewSize();
        TLog.i("logSize: [preview: %sx%s - picture: %sx%s]", previewSize.width, previewSize.height, pictureSize.width, pictureSize.height);
    }

    public static void logParameters(Camera.Parameters var0) {
        if (var0 != null) {
            String[] var1 = var0.flatten().split(";");
            HashMap var2 = new HashMap(var1.length);
            String[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String var6 = var3[var5];
                String[] var7 = var6.split("=");
                if (var7.length == 2) {
                    var2.put(var7[0], var7[1]);
                }
            }

            ArrayList var8 = new ArrayList(var2.keySet());
            Collections.sort(var8, new Comparator<String>() {
                public int compare(String var1, String var2) {
                    int var3 = 0;

                    while(var3 < var1.length() && var3 < var2.length()) {
                        char var4 = var1.charAt(var3);
                        char var5 = var2.charAt(var3);
                        ++var3;
                        int var6 = var4 - var5;
                        if (var6 != 0) {
                            return var6 > 0 ? 1 : -1;
                        }
                    }

                    return 0;
                }
            });
            Iterator var9 = var8.iterator();

            while(var9.hasNext()) {
                String var10 = (String)var9.next();
                TLog.i("log: %s = %s", new Object[]{var10, var2.get(var10)});
            }

        }
    }
    
    public static Rect computerCameraViewRect(final Context context, final View view, final View view2, float min) {
        if (context == null || view == null || view2 == null) {
            return null;
        }
        final TuSdkSize screenSize = ContextUtils.getScreenSize(context);
        final Rect rect = new Rect(0, 0, screenSize.width, screenSize.height);
        min = Math.min(1.0f, Math.max(0.0f, min));
        if (min == 0.0f) {
            return rect;
        }
        rect.bottom = (int)Math.floor(rect.right / min);
        if (rect.bottom + view.getHeight() <= screenSize.height) {
            rect.top = view.getHeight();
            final Rect rect2 = rect;
            rect2.bottom += rect.top;
        }
        final int n = screenSize.height - rect.bottom;
        if (n > view2.getHeight()) {
            TuSdkViewHelper.setViewHeight(view2, n);
        }
        return rect;
    }
    
    public static boolean showAlertIfNotSupportCamera(final Context context) {
        return showAlertIfNotSupportCamera(context, false);
    }
    
    public static boolean showAlertIfNotSupportCamera(final Context context, final boolean b) {
        if (!b && Build.VERSION.SDK_INT >= 23) {
            return false;
        }
        if (context == null) {
            return true;
        }
        String s = null;
        if (cameraCounts() == 0) {
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
    
    static {
        focusModes = new String[] { "continuous-picture", "continuous-video", "infinity", "macro", "auto" };
        videoFocusModes = new String[] { "continuous-video", "macro", "auto" };
        autoFocusModes = new ArrayList<String>();
        if (Build.VERSION.SDK_INT > 13) {
            CameraHelper.autoFocusModes.add("continuous-picture");
            CameraHelper.autoFocusModes.add("continuous-video");
        }
        CameraHelper.autoFocusModes.add("auto");
        CameraHelper.autoFocusModes.add("macro");
    }
}
