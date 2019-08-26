// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite;

import android.database.Cursor;
import java.io.Serializable;

public class SqlLiteInfo implements Serializable
{
    public SqlLiteInfo() {
    }
    
    public SqlLiteInfo(final Cursor infoWithCursor) {
        this.setInfoWithCursor(infoWithCursor);
    }
    
    public void setInfoWithCursor(final Cursor cursor) {
        if (cursor == null) {
            return;
        }
    }
    
    public String getCursorString(final Cursor cursor, final String s) {
        return CursorHelper.getCursorString(cursor, s);
    }
    
    public int getCursorInt(final Cursor cursor, final String s) {
        return CursorHelper.getCursorInt(cursor, s);
    }
    
    public long getCursorLong(final Cursor cursor, final String s) {
        return CursorHelper.getCursorLong(cursor, s);
    }
}
