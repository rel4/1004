package com.hongbaogou.utils;

/**
 * Created by Administrator on 2015/12/8.
 */

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件存储数据方式工具类
 *
 * @author zuolongsnail
 */
public class FilesUtils {
    /**
     * 保存文件内容
     *
     * @param c
     * @param fileName 文件名称
     * @param content  内容
     */
    public static void writeFiles(Context c, String fileName, String content, int mode)
            throws Exception {
        // 打开文件获取输出流，文件不存在则自动创建
        FileOutputStream fos = c.openFileOutput(fileName, mode);
        fos.write(content.getBytes());
        fos.close();
    }

    /**
     * 读取文件内容
     *
     * @param c
     * @param fileName 文件名称
     * @return 返回文件内容
     */
    public static String readFiles(Context c, String fileName) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = c.openFileInput(fileName);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        String content = baos.toString();
        fis.close();
        baos.close();
        return content;
    }
}
