// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network;

//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;

import java.util.Calendar;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class TuSdkAuthInfo extends JsonBaseBean implements Serializable
{
    @DataBase("last_updated")
    public Calendar lastUpdatedDate;
    @DataBase("next_request")
    public Calendar nextCheckDate;
    @DataBase("service_expire")
    public Calendar service_expire;
    @DataBase("master_key")
    public String masterKey;
    
    public boolean isValid() {
        return StringHelper.isNotBlank(this.masterKey) && this.masterKey.trim().length() > 11;
    }
    
    @Override
    public String toString() {
        return "masterKey : " + this.masterKey + " nextCheckDate :" + this.nextCheckDate + " lastUpdatedDate :" + this.lastUpdatedDate;
    }
}
