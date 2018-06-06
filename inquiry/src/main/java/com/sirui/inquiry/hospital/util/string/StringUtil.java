package com.sirui.inquiry.hospital.util.string;

import android.text.TextUtils;

import java.util.Locale;
import java.util.UUID;

public class StringUtil {
    /**
     * String.format("我叫%s,她叫%s", "小明","小方"); // 我叫小明,她叫小方
     * String.format("我叫%2$s,她叫%1$s", "小明","小方"); // 我叫小方,她叫小明

     */
    public static String getPercentString(float percent) {
        return String.format(Locale.US, "%d%%", (int) (percent * 100));
    }

    /**
     * 删除字符串中的空白符
     *
     * @param content
     * @return String
     */
    public static String removeBlanks(String content) {
        if (content == null) {
            return null;
        }
        StringBuilder buff = new StringBuilder();
        buff.append(content);
        for (int i = buff.length() - 1; i >= 0; i--) {
            if (' ' == buff.charAt(i) || ('\n' == buff.charAt(i)) || ('\t' == buff.charAt(i))
                    || ('\r' == buff.charAt(i))) {
                buff.deleteCharAt(i);
            }
        }
        return buff.toString();
    }

    /**
     * 获取32位uuid
     *
     * @return
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean isEmpty(String input) {
        return TextUtils.isEmpty(input);
    }

    /**
     * 生成唯一号
     *
     * @return
     */
    public static String get36UUID() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }

    public static String makeMd5(String source) {
        return MD5.getStringMD5(source);
    }

    public static final String filterUCS4(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        if (str.codePointCount(0, str.length()) == str.length()) {
            return str;
        }

        StringBuilder sb = new StringBuilder();

        int index = 0;
        while (index < str.length()) {
            int codePoint = str.codePointAt(index);
            index += Character.charCount(codePoint);
            if (Character.isSupplementaryCodePoint(codePoint)) {
                continue;
            }

            sb.appendCodePoint(codePoint);
        }

        return sb.toString();
    }

    /**
     * counter ASCII character as one, otherwise two
     *
     * @param str
     * @return count
     */
    public static int counterChars(String str) {
        // return
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            int tmp = (int) str.charAt(i);
            if (tmp > 0 && tmp < 127) {
                count += 1;
            } else {
                count += 2;
            }
        }
        return count;
    }

    /**
     * 身份证号掩码处理
     * 长度需大于 17 位，否则原样返回
     *
     * @param string 需要处理的信息字符串
     * @return 处理后的信息
     */
    public static String hideIdCardNumber(String string) {
        if (string != null && string.length() > 17) {
            string = string.substring(0, string.length() - 4)
                    + "****";
        }
        return string;
    }

    /**
     * 手机号掩码处理
     * 长度需大于 10 位，否则原样返回
     *
     * @param string 需要处理的信息字符串
     * @return 处理后的信息
     */
    public static String hideMobileNumber(String string) {
        if (string != null && string.length() > 10) {
            string = string.substring(0, 3)
                    + "****"
                    + string.substring(7, string.length());
        }
        return string;
    }

    /**
     * 姓名掩码处理
     * 长度需大于 1 位，否则原样返回
     *
     * @param string 需要处理的信息字符串
     * @return 处理后的信息
     */
//    public static String hideRealName(String string) {
//        if (string != null && string.length() > 1) {
//            string = string.substring(0, string.length() - 1)
//                    + "*";
//        }
//        return string;
//    }

    /**
     * 去除字符串中的换行符
     *
     * @param string 需要处理的字符串
     * @return 去除换行符(\r \n)的字符串
     */
    public static String removeLineBreak(String string) {
        if (string != null && !isEmpty(string)) {
            string = string.replace("\n", "");
            string = string.replace("\r", "");
        }
        return string;
    }
}
