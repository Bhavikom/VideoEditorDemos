// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis;

//import org.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;

import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class ImageMarkFaceResult extends JsonBaseBean implements Serializable
{
    @DataBase("count")
    public int count;
    @DataBase("items")
    public ImageMark5FaceArgument<ImageMark5FaceArgument.ImageItems> items;
}
