package com.dzw.library.utils

import android.os.Build
import android.text.SpannableStringBuilder
import androidx.annotation.RequiresApi
import java.util.*

/**
 * @author Death丶Love
 * @date 2022-03-18 13:55
 * @description String 相关的方法
 */
class StringUtils {
    /**
     * ClickSpan 的使用会造成内存泄漏
     * 因此在使用的时候，页面关闭时，需要将加载在 String 上面的 Span 全部 remove 掉
     * 此处使用 SpannableStringBuilder 进行示范
     * 在使用时，不能直接使用 Spannable，因为其 removeSpan(Object what) 并没有进行操作
     * 因此直接用 Spannable.removeSpan(Object what) 没有任何效果
     * 在使用时应该使用 Spannable 的实现类才有效果
     *
     * 该方法没有经过百分百测试，使用时需要关注是否会出现 内存泄漏
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun removeStringSpan(builder: SpannableStringBuilder) {
        val spans = builder.getSpans(0, builder.length, Objects::class.java)
        spans.forEach {
            builder.removeSpan(it)
        }
    }
}