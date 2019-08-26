// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class ArrayHelper
{
    public static <T> ArrayGroup<T> splitForGroupsize(final List<T> list, final int n) {
        if (list == null) {
            return null;
        }
        final int n2 = (int)Math.ceil(list.size() / (float)n);
        final ArrayGroup arrayGroup = new ArrayGroup<ArrayList<Object>>(n2);
        int n3 = n;
        int i = 0;
        int n4 = 0;
        final int size = list.size();
        while (i < n2) {
            final int n5 = n4 + n3;
            if (n5 > size) {
                n3 = size - n4;
            }
            arrayGroup.add(new ArrayList<Object>(list.subList(n4, n4 + n3)));
            n4 = n5;
            ++i;
        }
        return (ArrayGroup<T>)arrayGroup;
    }
    
    public static String join(final List<String> list, final String s) {
        return join(list, s, null, null);
    }
    
    public static String join(final List<String> list, final String str, final String str2, final String str3) {
        if (list == null || list.size() < 1) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (final String str4 : list) {
            if (str2 != null) {
                sb.append(str2);
            }
            sb.append(str4);
            if (str3 != null) {
                sb.append(str3);
            }
            sb.append(str);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    public static boolean contains(final int[] array, final int n) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    public static int[] toIntArray(final List<Integer> list) {
        if (list == null) {
            return null;
        }
        final int[] array = new int[list.size()];
        for (int i = 0; i < array.length; ++i) {
            if (list != null && list.get(i) != null) {
                array[i] = list.get(i);
            }
        }
        return array;
    }

    public static <T> T[] toArray(Collection<T> var0, Class<?> var1) {
        if (var0 == null) {
            return null;
        } else {
            Object[] var2 = ReflectUtils.arrayInstance(var1, var0.size());
            return var2 == null ? null : (T[]) var0.toArray(var2);
        }
    }
    
    public static <T> List<T> toList(final T[] array, final int[] array2) {
        if (array == null || array2 == null) {
            return null;
        }
        final ArrayList<T> list = new ArrayList<T>(array2.length);
        for (int i = 0; i < array2.length; ++i) {
            list.add(array[array2[i]]);
        }
        return list;
    }
    
    public static class ArrayGroup<T> extends ArrayList<List<T>>
    {
        public ArrayGroup() {
        }
        
        public ArrayGroup(final Collection<? extends List<T>> c) {
            super(c);
        }
        
        public ArrayGroup(final int initialCapacity) {
            super(initialCapacity);
        }
    }
}
