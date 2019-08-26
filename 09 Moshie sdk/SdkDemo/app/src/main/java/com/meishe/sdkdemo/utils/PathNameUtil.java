package com.meishe.sdkdemo.utils;

/**
 * Created by CaoZhiChao on 2018/12/12 11:56
 */
public class PathNameUtil {
    /**
     * 获取文件后缀名
     *
     * @param path
     */
    public static String getPathSuffix(String path) {
        if (!path.isEmpty()) {
            return path.substring(path.lastIndexOf(".") + 1);
        }
        return "";
    }

    /**
     * 获取文件除后缀名之外的全部
     *
     * @param path
     */
    public static String getOutOfPathSuffix(String path) {
        if (!path.isEmpty()) {
            return path.substring(0,path.lastIndexOf(".") + 1);
        }
        return "";
    }

    /**
     * 获取文件名，不带后缀
     *
     * @param path
     */
    public static String getPathNameNoSuffix(String path) {
        if (!path.isEmpty()) {
            return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
        }
        return "";
    }

    /**
     * 获取文件名，带后缀
     *
     * @param path
     */
    public static String getPathNameWithSuffix(String path) {
        if (!path.isEmpty()) {
            return path.substring(path.lastIndexOf("/") + 1);
        }
        return "";
    }
}
