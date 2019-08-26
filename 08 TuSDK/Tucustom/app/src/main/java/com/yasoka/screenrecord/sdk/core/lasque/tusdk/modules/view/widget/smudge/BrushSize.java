// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge;

import java.util.ArrayList;
import java.util.List;

public class BrushSize
{
    public static List<SizeType> getAllBrushSizes() {
        final ArrayList<SizeType> list = new ArrayList<SizeType>();
        list.add(SizeType.SmallBrush);
        list.add(SizeType.MediumBrush);
        list.add(SizeType.LargeBrush);
        return list;
    }
    
    public static SizeType nextBrushSize(final SizeType sizeType) {
        List<SizeType> allBrushSizes;
        int n;
        for (allBrushSizes = getAllBrushSizes(), n = 0; n < allBrushSizes.size() && sizeType != allBrushSizes.get(n); ++n) {}
        if (++n >= allBrushSizes.size()) {
            n = 0;
        }
        return allBrushSizes.get(n);
    }
    
    public static String nameForSize(final SizeType sizeType) {
        final List<SizeType> allBrushSizes = getAllBrushSizes();
        final String[] array = { "small", "medium", "large" };
        for (int i = 0; i < allBrushSizes.size(); ++i) {
            if (sizeType == allBrushSizes.get(i)) {
                return array[i];
            }
        }
        return null;
    }
    
    public static double getBrushValue(final SizeType sizeType) {
        switch (sizeType.ordinal()) {
            case 1: {
                return 0.2;
            }
            case 2: {
                return 0.15;
            }
            case 3: {
                return 0.1;
            }
            case 4: {
                return sizeType.getCustomizeBrushValue() * 0.2;
            }
            default: {
                return 0.15;
            }
        }
    }
    
    public enum SizeType
    {
        SmallBrush(1), 
        MediumBrush(2), 
        LargeBrush(3), 
        CustomizeBrush(0);
        
        private int a;
        private float b;
        
        private SizeType(final int a) {
            this.b = 0.1f;
            this.a = a;
        }
        
        public int getValue() {
            return this.a;
        }
        
        public SizeType setCustomizeBrushValue(final float b) {
            if (b > 1.0f || b < 0.0f) {
                return this;
            }
            this.b = b;
            return this;
        }
        
        public float getCustomizeBrushValue() {
            return this.b;
        }
    }
}
