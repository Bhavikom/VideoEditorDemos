// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.edit;

import java.util.ArrayList;
import java.util.List;

public enum TuEditActionType
{
    TypeUnknow, 
    TypeCuter, 
    TypeFilter, 
    TypeSticker, 
    TypeSkin, 
    TypeText, 
    TypeSmudge, 
    TypeAdjust, 
    TypeWipeFilter, 
    TypeSharpness, 
    TypeVignette, 
    TypeAperture, 
    TypeHolyLight, 
    TypeHDR, 
    TypeTurn, 
    TypeEdit, 
    TypePaint;
    
    public static List<TuEditActionType> entryActionTypes() {
        final ArrayList<TuEditActionType> list = new ArrayList<TuEditActionType>();
        list.add(TuEditActionType.TypeCuter);
        list.add(TuEditActionType.TypeFilter);
        list.add(TuEditActionType.TypeSticker);
        return list;
    }
    
    public static List<TuEditActionType> multipleActionTypes() {
        final ArrayList<TuEditActionType> list = new ArrayList<TuEditActionType>();
        list.add(TuEditActionType.TypeSticker);
        list.add(TuEditActionType.TypeText);
        list.add(TuEditActionType.TypeFilter);
        list.add(TuEditActionType.TypeSkin);
        list.add(TuEditActionType.TypeCuter);
        list.add(TuEditActionType.TypeSmudge);
        list.add(TuEditActionType.TypePaint);
        list.add(TuEditActionType.TypeAdjust);
        list.add(TuEditActionType.TypeWipeFilter);
        list.add(TuEditActionType.TypeAperture);
        list.add(TuEditActionType.TypeHDR);
        list.add(TuEditActionType.TypeHolyLight);
        list.add(TuEditActionType.TypeVignette);
        list.add(TuEditActionType.TypeSharpness);
        return list;
    }
}
