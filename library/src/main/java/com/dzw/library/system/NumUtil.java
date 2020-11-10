package com.dzw.library.system;

/**
 * @author Death丶Love
 * @date 2020-11-10 2:43 PM
 * @description 针对数字的一些方法
 */
public class NumUtil {
    /**
     * 在某些手机上（尤其是华为手机），在输入￥符号后，在手机只能显示一行
     * 通过ASCII码转义可以显示完整的字符
     *
     * @return
     */
    public static String getRMBSymbol() {
        int rmb = 165;
        return String.valueOf(rmb);
    }
}
