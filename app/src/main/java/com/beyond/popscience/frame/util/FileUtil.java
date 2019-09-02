package com.beyond.popscience.frame.util;

import android.graphics.Bitmap;
import android.os.Environment;

import com.beyond.popscience.frame.application.BeyondApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件操作类
 * Created by danxiang.feng on 2017/4/13.
 */
public class FileUtil {

    public static String SDCard = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * 检测SDCard是否可用
     */
    public static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 返回数据目录
     */
    public static String getDataPath() {
        if (checkSDCard()) {
            return SDCard + "/Android/data/" + BeyondApplication.getInstance().getPackageName() + "/";
        } else {
            return "/data/data/" + BeyondApplication.getInstance().getPackageName() + "/";
        }
    }
    /**
     * 返回视频下载目录
     */
    public static String getVideoDownPath() {
        if (checkSDCard()) {
            return SDCard + "/IKnow/Videos/";
        } else {
            return "/data/data/" + "IKnow/Videos/";
        }
    }


    /**
     * 返回File 如果没有就创建
     */
    public static File getDirectory(String path) {
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }

    /**
     * 创建文件夹
     *
     * @param filePath
     * @return 1 :创建成功 0 :已存在 -1 :创建失败
     */
    public static int createFloder(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return 0;
        }
        if (file.mkdirs()) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * 删除文件夹
     */
    public static boolean deleteDirectory(String sPath) {
        boolean flag = false;
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param sPath 路径
     * @return
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 读取文件
     *
     * @return
     */
    public static String readerFile(String filePath) {
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, "utf-8");// 文件编码Unicode,UTF-8,ASCII,GB2312,Big5
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char) ch);
            }
            in.close();
        } catch (Exception e) {
            return null;
        }
        return buffer.toString();
    }

    /**
     * 写入文件
     *
     * @param path
     * @param content
     * @return 1: 写入成功 0: 写入失败
     */
    public static int writeFile(String path, String content) {
        try {
            File f = new File(path);
            if (f.exists()) {
                f.delete();
            }
            if (f.createNewFile()) {
                FileOutputStream utput = new FileOutputStream(f);
                utput.write(content.getBytes());
                utput.close();
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 写入文件
     *
     * @param path
     * @param in
     * @return 1: 写入成功 0: 写入失败
     */
    public static int writeFile(String path, InputStream in) {
        try {
            if (in == null)
                return 0;
            File f = new File(path);
            if (f.exists()) {
                f.delete();
            }
            if (f.createNewFile()) {
                FileOutputStream utput = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int count = -1;
                while ((count = in.read(buffer)) != -1) {
                    utput.write(buffer, 0, count);
                    utput.flush();
                }
                utput.close();
                in.close();
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 复制文件
     *
     * @param is
     * @param os
     * @return 1: 写入成功 0: 写入失败
     * @throws IOException
     */
    public static int copyStream(InputStream is, OutputStream os) {
        try {
            final int buffer_size = 1024;
            byte[] bytes = new byte[buffer_size];
            while (true) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1) {
                    break;
                }
                os.write(bytes, 0, count);
            }
            return 1;
        } catch (IOException e) {
            return 0;
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 读取序列化对象
     *
     * @param filePath
     * @return
     */
    public static Object readerObject(String filePath) {
        Object oo;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fis);
            oo = objectIn.readObject();
            objectIn.close();
            fis.close();
        } catch (Exception e) {
            return null;
        }
        return oo;
    }

    /**
     * 写入序列化对象
     *
     * @param path
     * @param object
     * @return
     */
    public static int writeObject(String path, Object object) {
        try {
            File f = new File(path);
            if (f.exists()) {
                f.delete();
            }
            if (f.createNewFile()) {
                FileOutputStream utput = new FileOutputStream(f);
                ObjectOutputStream objOut = new ObjectOutputStream(utput);
                objOut.writeObject(object);
                objOut.close();
                utput.close();
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     *
     * @param rootPath
     * @param file
     */
    public static void unzip(String rootPath, File file) {
        int BUFFER = 4 * 1024; // 这里缓冲区我们使用4KB，
        try {
            BufferedOutputStream dest = null; // 缓冲输出流
            FileInputStream fis = new FileInputStream(file);
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(fis));
            ZipEntry entry; // 每个zip条目的实例

            while ((entry = zis.getNextEntry()) != null) {
                try {
                    int count;
                    byte data[] = new byte[BUFFER];
                    String strEntry[] = entry.getName().split("/"); // 保存每个zip的条目名称
                    File entryFile = new File(rootPath + strEntry[1]);// 解压后的文件名用原文件名
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
        } catch (Exception cwj) {
            cwj.printStackTrace();
        }
    }


    /**
     * 解压
     *
     * @param rootPath 解压的根目录
     * @param fileIn   要解压的ZIP、rar文件流
     * @throws Exception
     */
    public static void unzip(String rootPath, InputStream fileIn) {
        try {
            /* 创建根文件夹 */
            File rootFile = new File(rootPath);
            rootFile.mkdir();
            rootFile = new File(rootPath + "resource/");
            rootFile.mkdir();
            ZipInputStream in = new ZipInputStream(new BufferedInputStream(
                    fileIn, 2048));
            ZipEntry entry = null;// 读取的压缩条目
            /* 解压缩开始 */
            while ((entry = in.getNextEntry()) != null) {
                decompression(entry, rootPath, in);// 解压
            }
            in.close();// 关闭输入流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：解压
     *
     * @param entry    解压条目
     *                 数量
     * @param rootPath 根目录
     * @throws Exception
     */
    private static void decompression(ZipEntry entry, String rootPath, ZipInputStream in) throws Exception {
        /* 如果是文件夹 */
        if ((entry.isDirectory() || -1 == entry.getName().lastIndexOf("."))) {
            File file = new File(rootPath + entry.getName().substring(0, entry.getName().length() - 1));
            file.mkdir();
        } else {
            File file = new File(rootPath + entry.getName());
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file), 2048);
            int b;
            while ((b = in.read()) != -1) {
                bos.write(b);
            }
            bos.close();
        }
    }

    /**
     * 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 保存图片
     * @param bitmap
     * @param filePath
     * @return
     */
    public static String saveBitmap(Bitmap bitmap, String filePath) {

        if (bitmap == null || bitmap.isRecycled() || filePath == null || "".equals(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }

}
