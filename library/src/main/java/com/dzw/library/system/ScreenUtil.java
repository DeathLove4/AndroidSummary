package com.dzw.library.system;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

/**
 * @author Death丶Love
 * @date 2020-10-29 10:37 AM
 * @description 获取手机屏幕的相关信息
 */
public class ScreenUtil {
    /**
     * DisplayMetrics,内部封装了手机屏幕的相关参数，如大小、密度、字体缩放等
     *
     * @param context
     * @return
     * @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
     */
    public static DisplayMetrics getDisplayMetrics(@NonNull Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getDisplay().getRealMetrics(metrics);
        return metrics;
    }

    /**
     * dpi:像素密度，指的是在系统软件上指定的单位尺寸的像素数量，它往往是写在系统出厂配置文件的一个固定值。
     * ppi:指的也是像素密度，但是这个是物理上的概念，它是客观存在的不会改变。
     * dpi是软件参考了物理像素密度后，人为指定的一个值，这样保证了某一个区间内的物理像素密度在软件上都使用同一个值。
     *
     * @param context
     * @return
     */
    public static int getDensityDpi(@NonNull Context context) {
        return getDisplayMetrics(context).densityDpi;
    }

    /**
     * 获取手机屏幕横向像素宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidthPx(@NonNull Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取手机屏幕纵向像素宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayHeightPx(@NonNull Context context) {
        return getDisplayMetrics(context).heightPixels;
    }
}
