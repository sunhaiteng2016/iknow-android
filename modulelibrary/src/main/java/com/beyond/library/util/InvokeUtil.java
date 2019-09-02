package com.beyond.library.util;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 *
 * @author linjinfa 331710168@qq.com
 * @date 2013-12-29 下午1:59:10
 */
public class InvokeUtil {

    /**
     * 设置字段值
     *
     * @param fieldName
     * @param value
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            if (fieldName == null || value == null) {
                return;
            }
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (field.getType() == Integer.class) {
                field.set(obj, new Integer(Integer.parseInt(value.toString())));
            } else if (field.getType() == int.class) {
                field.setInt(obj, Integer.parseInt(value.toString()));
            } else if (field.getType() == Long.class) {
                field.set(obj, new Long(Long.parseLong(value.toString())));
            } else if (field.getType() == long.class) {
                field.setLong(obj, Long.parseLong(value.toString()));
            } else {
                field.set(obj, value);
            }
        } catch (Exception e) {
            L.e("setFieldValue======>  " + fieldName + "  " + value);
            e.printStackTrace();
        }
    }

    /**
     * 反射设置值
     *
     * @param obj
     * @param methodName
     * @param value
     */
    public static void setValue(Object obj, String methodName, Object value,
                                Class<?>... parameterTypes) {
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            method.invoke(obj, value);
        } catch (Exception e) {
            L.e("(可忽略异常)反射设置值异常" + e.getMessage() + "  " + methodName);
        }
    }

    /**
     * 反射设置值 参数是Integer类型 (单个参数)
     *
     * @param obj
     * @param methodName
     * @param value
     */
    public static void setIntegerValue(Object obj, String methodName, int value) {
        setValue(obj, methodName, value, Integer.class);
    }

    /**
     * 反射设置值 参数是String类型(单个参数)
     *
     * @param obj
     * @param methodName
     * @param value
     */
    public static void setStringValue(Object obj, String methodName, String value) {
        setValue(obj, methodName, value, String.class);
    }

    /**
     * 调用静态方法
     *
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @param args
     */
    public static Object invokeStaticMethod(Object obj, String methodName, Class[] parameterTypes, Object... args) {
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(null, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射获取值
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static Object getValue(Object obj, String methodName) {
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(obj);
        } catch (Exception e) {
            L.e("(可忽略异常)反射获取值异常" + e.getMessage() + "  " + methodName);
        }
        return null;
    }

}
