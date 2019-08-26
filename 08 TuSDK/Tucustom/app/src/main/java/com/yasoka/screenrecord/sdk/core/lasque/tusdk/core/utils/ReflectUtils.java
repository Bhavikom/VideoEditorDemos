// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.io.StreamCorruptedException;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import android.util.Base64;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ResourceType;

import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
//import org.lasque.tusdk.core.type.ResourceType;

public class ReflectUtils
{
    public static int getResource(final Class<?> clazz, final ResourceType resourceType, final String s) {
        if (clazz == null || resourceType == null || s == null) {
            return 0;
        }
        final Class<?> subClass = subClass(clazz, resourceType.getKey());
        if (subClass == null) {
            return 0;
        }
        return getResourceFieldValue(subClass, s);
    }
    
    public static int getResourceFieldValue(final Class<?> obj, final String s) {
        final Field field = getField(obj, s);
        if (field == null) {
            return 0;
        }
        try {
            return (int)field.get(obj);
        }
        catch (Exception ex) {
            TLog.e(ex, "getResourceFieldValue: %s | %s", obj, s);
            return 0;
        }
    }
    
    public static Field getField(final Class<?> clazz, final String anotherString) {
        if (clazz == null || anotherString == null) {
            return null;
        }
        final Field[] fields = clazz.getFields();
        if (fields == null) {
            return null;
        }
        for (final Field field : fields) {
            if (field.getName().equalsIgnoreCase(anotherString)) {
                return field;
            }
        }
        return null;
    }
    
    public static Class<?> subClass(final Class<?> clazz, final String anotherString) {
        final Class[] declaredClasses = clazz.getDeclaredClasses();
        if (anotherString == null || declaredClasses == null || declaredClasses.length == 0) {
            return null;
        }
        for (final Class clazz2 : declaredClasses) {
            if (clazz2.getSimpleName().equalsIgnoreCase(anotherString)) {
                return (Class<?>)clazz2;
            }
        }
        return null;
    }
    
    public static Class<?> reflectClass(final String className) {
        if (className == null) {
            return null;
        }
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException ex) {
            TLog.e(ex);
            return null;
        }
    }
    
    public static <T> T classInstance(final String s) {
        return classInstance(reflectClass(s));
    }
    
    public static <T> T classInstance(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        try {
            return (T)clazz.newInstance();
        }
        catch (InstantiationException ex) {
            TLog.e(ex, "classInstance", new Object[0]);
        }
        catch (IllegalAccessException ex2) {
            TLog.e(ex2, "classInstance", new Object[0]);
        }
        return null;
    }
    
    public static <T> T[] arrayInstance(final Class<?> componentType, final int length) {
        if (componentType == null || length < 0) {
            return null;
        }
        try {
            return (T[])Array.newInstance(componentType, length);
        }
        catch (Exception ex) {
            TLog.e(ex, "arrayInstance", new Object[0]);
            return null;
        }
    }
    
    public static StringBuilder trace(final Object o) {
        if (o == null) {
            return null;
        }
        final StringBuilder append = new StringBuilder(o.getClass().getName()).append(":{\n");
        for (final Field field : o.getClass().getFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                append.append(field.getName()).append(" : ");
                final Object fieldValue = getFieldValue(field, o);
                if (fieldValue == null) {
                    append.append("null");
                }
                else {
                    append.append(fieldValue.toString());
                }
                append.append(", \n");
            }
        }
        append.append("};");
        return append;
    }
    
    public static Object getFieldValue(final Field field, final Object obj) {
        Object value = null;
        try {
            value = field.get(obj);
        }
        catch (IllegalArgumentException ex) {
            TLog.e(ex);
        }
        catch (IllegalAccessException ex2) {
            TLog.e(ex2);
        }
        return value;
    }
    
    public static Field reflectField(final Class<?> clazz, final String name) {
        if (clazz == null || name == null) {
            return null;
        }
        Field field = null;
        try {
            field = clazz.getField(name);
        }
        catch (NoSuchFieldException ex) {
            TLog.e(ex);
        }
        return field;
    }
    
    public static void setFieldValue(final Field field, final Object obj, final Object value) {
        if (field == null || obj == null || value == null) {
            return;
        }
        try {
            field.set(obj, value);
        }
        catch (IllegalArgumentException ex) {
            TLog.e(ex);
        }
        catch (IllegalAccessException ex2) {
            TLog.e(ex2);
        }
    }
    
    public static Method getMethod(final Class<?> clazz, final String name, final Class<?>... parameterTypes) {
        if (clazz == null || name == null) {
            return null;
        }
        Method method = null;
        try {
            method = clazz.getMethod(name, (Class[])parameterTypes);
        }
        catch (NoSuchMethodException ex) {
            TLog.e(ex);
        }
        return method;
    }
    
    public static Object reflectMethod(final Method method, final Object obj, final Object... args) {
        if (method == null || obj == null) {
            return null;
        }
        try {
            return method.invoke(obj, args);
        }
        catch (IllegalArgumentException ex) {
            TLog.e(ex);
        }
        catch (IllegalAccessException ex2) {
            TLog.e(ex2);
        }
        catch (InvocationTargetException ex3) {
            TLog.e(ex3);
        }
        return null;
    }
    
    public static Class<?> genericCollectionType(final Type type) {
        final List<Class<?>> genericCollectionTypes = genericCollectionTypes(type);
        if (genericCollectionTypes == null || genericCollectionTypes.isEmpty()) {
            return null;
        }
        return genericCollectionTypes.get(0);
    }
    
    public static List<Class<?>> genericCollectionTypes(final Type type) {
        if (type == null || !(type instanceof ParameterizedType)) {
            return null;
        }
        final Type[] actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length < 1) {
            return null;
        }
        final ArrayList list = new ArrayList<Class<?>>(actualTypeArguments.length);
        final Type[] array = actualTypeArguments;
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add((Class<?>)array[i]);
        }
        return (List<Class<?>>)list;
    }
    
    public static String serialize(final Object obj) {
        if (obj == null) {
            return null;
        }
        String encodeToString = null;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(out).writeObject(obj);
            encodeToString = Base64.encodeToString(out.toByteArray(), 0);
        }
        catch (IOException ex) {
            TLog.e(ex, "serialize: %s", obj);
        }
        return encodeToString;
    }
    
    public static <T> T deserialize(final String s) {
        if (s == null) {
            return null;
        }
        final ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(s, 0));
        Object object = null;
        try {
            object = new ObjectInputStream(in).readObject();
        }
        catch (StreamCorruptedException ex) {
            TLog.e(ex, "deserialize error", new Object[0]);
        }
        catch (IOException ex2) {
            TLog.e(ex2, "deserialize error", new Object[0]);
        }
        catch (ClassNotFoundException ex3) {
            TLog.e(ex3, "deserialize error", new Object[0]);
        }
        return (T)object;
    }
    
    public static void asserts(final boolean b, final String detailMessage) {
        if (!b) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    public static <T> T notNull(final T t, final String str) {
        if (t == null) {
            throw new IllegalArgumentException(str + " should not be null!");
        }
        return t;
    }
}
