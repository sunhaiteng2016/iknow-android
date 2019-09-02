package com.beyond.popscience.frame.util;

/**
 * 全局常量类
 */
public class VKConstants {
    /**
     * 缓存过期时间(毫秒) 1分钟
     */
    public final static long CACHE_EXPIRED_SECONDS = 1 * 60 * 1000;
    /**
     * 缓存过期时间(毫秒) 2天前
     */
    public final static long IMG_CACHE_EXPIRED_SECONDS = 2 * 24 * 60 * 60 * 1000;
    /**
     * 每页数据
     */
    public final static int PAGE_SIZE = 20;
    /**
     * 缓存根目录
     */
    public final static String CACHE_ROOT = FileUtil.getDataPath();
    /**
     * 图片缓存目录
     */
    public final static String CACHE_IMG = CACHE_ROOT + "images/";
    /**
     * 数据缓存目录
     */
    public final static String CACHE_DATA = CACHE_ROOT + "data/";
    /**
     * 系统相册/视频目录
     */
    public final static String SYSTEM_CAMERA_IMG_VIDEO = FileUtil.SDCard + "/DCIM/Camera";
    public final static String SYSTEM_DOC = FileUtil.SDCard + "/tongzhi";
    /**
     *
     */
    public final static String VIDEO_DOWNLOAD_PATH = FileUtil.getVideoDownPath();
    /**
     * drawable的资源图片
     */
    public final static String RESOURCE_DRAWABLE = "drawable://";
    /**
     * http
     */
    public final static String HTTP_PROTOCOL_PREFIX = "http://";
    /**
     * https
     */
    public final static String HTTPS_PROTOCOL_PREFIX = "https://";
    /**
     * file
     */
    public final static String FILE_PROTOCOL_PREFIX = "file:///";

}
