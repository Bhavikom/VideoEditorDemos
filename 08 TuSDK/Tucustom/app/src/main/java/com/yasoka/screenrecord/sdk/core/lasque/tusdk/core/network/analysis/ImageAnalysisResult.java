// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.analysis;

//import org.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;

import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class ImageAnalysisResult extends JsonBaseBean implements Serializable
{
    @DataBase("color")
    public ImageColorArgument color;
}
