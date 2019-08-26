// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network;

//import org.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;

import java.util.Calendar;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class TuSdkConfigResult extends JsonBaseBean implements Serializable
{
    @DataBase("last_updated")
    public Calendar lastUpdatedDate;
    @DataBase("next_request")
    public Calendar nextCheckDate;
    @DataBase("master_key")
    public String masterKey;
}
