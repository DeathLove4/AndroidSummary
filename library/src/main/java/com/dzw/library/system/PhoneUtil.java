package com.dzw.library.system;

import java.util.regex.Pattern;

/**
 * @author Death丶Love
 * @date 2020-11-10 2:54 PM
 * @description 针对手机的号码方法
 */
public class PhoneUtil {
    /**
     * 正则验证是否是 400 号码
     * 支持类型
     * 400-8888-888
     * 4008-000-888
     * 400-666-8888
     * 4008886666
     *
     * @param number
     * @return
     */
    public static boolean is400Num(String number) {
        String regex = "^400-?\\d{3}-?\\d{4}$|^400-?\\d{4}-?\\d{3}$|^400[0-9]{7}$|^400[0-9]-?\\d{3}-?\\d{3}$";
        return Pattern.matches(regex, number);
    }

    /**
     * 处理联系号码，将号码中间部分隐藏
     *
     * @param phoneNum
     * @return
     */
    public static String encryptPhoneNum(String phoneNum) {
        if (phoneNum == null && phoneNum.length() < 10) {
            return phoneNum;
        } else {
            String head = phoneNum.substring(0, 3);
            String bottom = phoneNum.substring(phoneNum.length() - 4, phoneNum.length());
            return head + "****" + bottom;
        }
    }
}
