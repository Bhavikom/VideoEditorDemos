// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite;

import android.database.Cursor;
import android.content.ContentResolver;
import android.content.Context;

public class CursorHelper
{
    public static ContentResolver getContentResolver(final Context context) {
        if (context == null) {
            return null;
        }
        return context.getContentResolver();
    }
    
    public static int getCursorIndex(final Cursor cursor, final String s) {
        if (cursor == null || s == null) {
            return -1;
        }
        return cursor.getColumnIndex(s);
    }
    
    public static String getCursorString(final Cursor cursor, final String s) {
        final int cursorIndex = getCursorIndex(cursor, s);
        if (cursorIndex == -1) {
            return null;
        }
        return cursor.getString(cursorIndex);
    }
    
    public static int getCursorInt(final Cursor cursor, final String s) {
        final int cursorIndex = getCursorIndex(cursor, s);
        if (cursorIndex == -1) {
            return 0;
        }
        return cursor.getInt(cursorIndex);
    }
    
    public static long getCursorLong(final Cursor cursor, final String s) {
        final int cursorIndex = getCursorIndex(cursor, s);
        if (cursorIndex == -1) {
            return 0L;
        }
        return cursor.getLong(cursorIndex);
    }
}
