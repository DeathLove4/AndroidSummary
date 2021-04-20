package com.dzw.library.utils;

import android.app.Application;

import java.io.File;
import java.util.Objects;

/**
 * @author Death丶Love
 * @date 2021-04-20 9:24 AM
 * @description 对手机文件一些操作方法
 */
public class FileUtil {
    /**
     * 获取手机文件夹根目录
     * Environment.getExternalStorageDirectory().getAbsolutePath() 已过时
     * <p>
     * 通过递归形式逐渐获取到 Android 层级
     * 正常来说搭载 Android 系统的手机肯定会存在 Android 目录
     * <p>
     * 使用时要传入 Application application
     *
     * @return
     */
    public static String getExternalStorageDirectory() {
        Application application = new Application();
        File externalFileRootDir = application.getExternalFilesDir(null);
        do {
            externalFileRootDir = Objects.requireNonNull(externalFileRootDir).getParentFile();
        } while (Objects.requireNonNull(externalFileRootDir).getAbsolutePath().contains("/Android"));
        return Objects.requireNonNull(externalFileRootDir).getAbsolutePath();
    }
}
