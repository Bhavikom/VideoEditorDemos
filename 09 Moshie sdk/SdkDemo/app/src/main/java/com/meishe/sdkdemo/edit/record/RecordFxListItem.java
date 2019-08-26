package com.meishe.sdkdemo.edit.record;

import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * Created by ms on 2018/8/9.
 */
@Keep
public class RecordFxListItem implements Serializable {
    public String fxID;
    public String fxName;
    public int index;
    public boolean selected;
    public Drawable image_drawable;

    public RecordFxListItem() {
        selected = false;
    }
}
