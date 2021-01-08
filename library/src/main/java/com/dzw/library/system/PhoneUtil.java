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

    /**
     * 去除字符串首、尾多余的0
     * 如果最后一位是 . ,也去掉
     * 022 -> 22
     * 022.0 -> 22
     * 022.2300 -> 22.23
     */
    public static String subZeroAndDot(String s) {
        if (s.startsWith("0")) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != '0') {
                    s = s.substring(i, s.length());
                    break;
                }
            }
        }
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     */
    public static boolean isNumeric(String str) {
        return str.matches("-?[0-9]+.*[0-9]*");
    }
}
