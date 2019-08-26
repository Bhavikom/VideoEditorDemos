// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image;

//import org.lasque.tusdk.modules.components.ComponentActType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;

import java.util.ArrayList;

public class RatioType
{
    public static final int ratio_orgin = 1;
    public static final int ratio_1_1 = 2;
    public static final int ratio_2_3 = 4;
    public static final int ratio_3_4 = 8;
    public static final int ratio_9_16 = 16;
    public static final int ratio_3_2 = 32;
    public static final int ratio_4_3 = 64;
    public static final int ratio_16_9 = 128;
    public static final int ratio_all = 255;
    public static final int ratio_default = 31;
    public static final int[] ratioTypes;
    public static final int[] defaultRatioTypes;
    
    public static float ratio(final int n) {
        switch (n) {
            case 2: {
                return 1.0f;
            }
            case 4: {
                return 0.6666667f;
            }
            case 8: {
                return 0.75f;
            }
            case 16: {
                return 0.5625f;
            }
            case 32: {
                return 1.5f;
            }
            case 64: {
                return 1.3333334f;
            }
            case 128: {
                return 1.7777778f;
            }
            default: {
                return 0.0f;
            }
        }
    }
    
    public static int radioType(final float n) {
        final int n2 = (int)Math.floor(n * 100.0f);
        for (final int n3 : RatioType.ratioTypes) {
            if ((int)Math.floor(ratio(n3) * 100.0f) == n2) {
                return n3;
            }
        }
        return 1;
    }
    
    public static int[] getRatioTypesByValue(final int n) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (final int j : RatioType.ratioTypes) {
            if (j == (j & n)) {
                list.add(j);
            }
        }
        return a(list);
    }
    
    public static int[] validRatioTypes(final int[] array) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < array.length; ++i) {
            final int j = array[i];
            if (j == 1 || ratio(j) != 0.0f) {
                list.add(j);
            }
        }
        int[] array2;
        if (list.size() == 0) {
            array2 = RatioType.ratioTypes;
        }
        else {
            array2 = a(list);
        }
        return array2;
    }
    
    private static int[] a(final ArrayList<Integer> list) {
        int[] array = null;
        if (list != null && list.size() > 0) {
            array = new int[list.size()];
            for (int i = 0; i < list.size(); ++i) {
                array[i] = list.get(i);
            }
        }
        return array;
    }
    
    public static int firstRatioType(final int n) {
        if (n <= 0 || n == 255) {
            return 1;
        }
        int n2 = 1;
        for (final int n3 : RatioType.ratioTypes) {
            if (n3 == (n3 & n)) {
                n2 = n3;
                break;
            }
        }
        return n2;
    }
    
    public static float firstRatio(final int n) {
        return ratio(firstRatioType(n));
    }
    
    public static int ratioCount(final int n) {
        if (n <= 0) {
            return 1;
        }
        int n2 = 0;
        for (final int n3 : RatioType.ratioTypes) {
            if (n3 == (n3 & n)) {
                ++n2;
            }
        }
        return n2;
    }
    
    public static int nextRatioType(final int n, final int n2, final int n3) {
        final int nextRatioType = nextRatioType(n, n2);
        if (n3 > 0 && nextRatioType == n3) {
            return nextRatioType(n, nextRatioType);
        }
        return nextRatioType;
    }
    
    public static int nextRatioType(final int n, final int n2) {
        final int ratioCount = ratioCount(n);
        if (ratioCount < 2) {
            return n2;
        }
        final int[] array = new int[ratioCount];
        int n3 = 0;
        int n4 = 0;
        for (final int n5 : RatioType.ratioTypes) {
            if (n5 == (n5 & n)) {
                if (n2 == (array[n3] = n5)) {
                    n4 = n3;
                }
                ++n3;
            }
        }
        if (n4 + 1 < ratioCount) {
            return array[n4 + 1];
        }
        return array[0];
    }
    
    public static long ratioActionType(final int n) {
        switch (n) {
            case 2: {
                return ComponentActType.editCuter_action_ratio_1_1;
            }
            case 4: {
                return ComponentActType.editCuter_action_ratio_2_3;
            }
            case 8: {
                return ComponentActType.editCuter_action_ratio_3_4;
            }
            case 16: {
                return ComponentActType.editCuter_action_ratio_9_16;
            }
            case 32: {
                return ComponentActType.editCuter_action_ratio_3_2;
            }
            case 64: {
                return ComponentActType.editCuter_action_ratio_4_3;
            }
            case 128: {
                return ComponentActType.editCuter_action_ratio_16_9;
            }
            default: {
                return ComponentActType.editCuter_action_ratio_orgin;
            }
        }
    }
    
    static {
        ratioTypes = new int[] { 1, 2, 4, 8, 16, 32, 64, 128 };
        defaultRatioTypes = new int[] { 1, 2, 4, 8, 16 };
    }
}
