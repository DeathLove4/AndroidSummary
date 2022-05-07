package com.dzw.library.utils

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import androidx.annotation.RequiresApi

/**
 * @author Death丶Love
 * @date 2022-05-07 16:23
 * @description 关于手机屏幕相关数据获取的工具类
 */
class ScreenUtils {

    /**
     * android L ~ R 之间获取 Diaplay 方式
     * @param context Context
     * @return Display
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun getDisplayApiL(context: Context): Display {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return windowManager.defaultDisplay
    }

    /**
     * android  R 及以上获取 Diaplay 方式
     * @param context Context
     * @return Display
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    fun getDisplayApiR(context: Context): Display? = context.display

    /**
     * 获取屏幕宽度
     * @param context Context
     * @return Int
     */
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.width()
        } else {
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            dm.widthPixels
        }
    }

    /**
     * 获取屏幕高度
     * @param context Context
     * @return Int
     */
    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.height()
        } else {
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            dm.heightPixels
        }
    }
}