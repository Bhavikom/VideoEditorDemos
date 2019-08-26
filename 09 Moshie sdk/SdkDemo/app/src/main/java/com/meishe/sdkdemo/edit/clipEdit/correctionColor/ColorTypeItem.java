package com.meishe.sdkdemo.edit.clipEdit.correctionColor;

/**
 * Created by Administrator on 2018/10/11 0011.
 */

public class ColorTypeItem {
    private String colorAtrubuteText;
    private String colorTypeName;
    private String fxName;
    private boolean selected;

    public ColorTypeItem() {
        selected = false;
    }

    public String getColorAtrubuteText() {
        return colorAtrubuteText;
    }

    public void setColorAtrubuteText(String colorAtrubuteText) {
        this.colorAtrubuteText = colorAtrubuteText;
    }

    public String getColorTypeName() {
        return colorTypeName;
    }

    public void setColorTypeName(String colorTypeName) {
        this.colorTypeName = colorTypeName;
    }

    public String getFxName() {
        return fxName;
    }

    public void setFxName(String fxName) {
        this.fxName = fxName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
