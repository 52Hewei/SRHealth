package com.sirui.inquiry.hospital.util.string;

import java.util.regex.Pattern;

/**
 * 身份证工具类
 *
 * Created by yellow on 17/4/13.
 */

public class IDCardUtil {
/*
 * 身份证号码验证
 * 1、号码的结构
 * 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。
 * 2、地址码(前六位数）
 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
 * 3、出生日期码（第七位至十四位）
 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
 * 4、顺序码（第十五位至十七位）
 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
 * 顺序码的奇数分配给男性，偶数分配给女性。
 * 5、校验码（第十八位数）
 * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
 * （2）计算模 Y = mod(S, 11)
 * （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
 */
//    private static SimpleDateFormat sf = new SimpleDateFormat("YYYYMMdd", Locale.CHINA);

    /**
     * 验证身份证是否合法
     *
     * @param idCard 身份证号
     * @return 是否合法
     */

    public static boolean validate(String idCard) {
        String upperIdCard = idCard.toUpperCase();
        if (!Pattern.matches("[\\d]{17}[\\dX]", upperIdCard)) {
            return false;
        }
        int sigma = 0;
        Integer[] a = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        String[] w = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        for (int i = 0; i < 17; i++) {
            int ai = Integer.parseInt(upperIdCard.substring(i, i + 1));
            int wi = a[i];
            sigma += ai * wi;
        }
        int number = sigma % 11;
        String checkNumber = w[number];
        return upperIdCard.substring(17).equals(checkNumber);
    }

//    public static Date getBirth(@NonNull String idCard) {
//        Date birthDate = null;
//        if (validate(idCard)) {
//            // 120413/19890412/0417
//            String birthStr = idCard.substring(6, 14);
//            try {
//                birthDate = sf.parse(birthStr);
//            } catch (ParseException e) {
//                MyLog.e("IDCard getBirth ParseException");
//            }
//        }
//        return birthDate;
//    }
}
