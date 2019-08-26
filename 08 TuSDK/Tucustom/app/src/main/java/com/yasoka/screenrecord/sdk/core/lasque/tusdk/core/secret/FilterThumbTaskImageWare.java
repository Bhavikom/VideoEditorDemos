// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret;

import android.widget.ImageView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.ImageViewTaskWare;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.FilterGroup;
//import org.lasque.tusdk.core.task.ImageViewTaskWare;

public class FilterThumbTaskImageWare extends ImageViewTaskWare
{
    public FilterGroup group;
    public FilterOption option;
    public FilterThumbTaskType taskType;
    
    public FilterThumbTaskImageWare(final ImageView imageView, final FilterThumbTaskType taskType, final FilterOption option, final FilterGroup group) {
        this.setImageView(imageView);
        this.taskType = taskType;
        this.option = option;
        this.group = group;
    }
    
    public enum FilterThumbTaskType
    {
        TypeGroupThumb, 
        TypeFilterThumb;
    }
}
