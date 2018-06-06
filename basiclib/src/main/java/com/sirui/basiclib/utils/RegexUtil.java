package com.sirui.basiclib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 *  Create by xiepc on 2016/11/18 17:36
 */
public class RegexUtil {

	/**
	 * 车牌号码Pattern
	 */
	public static final Pattern PLATE_NUMBER_PATTERN = Pattern
			.compile("^[\u0391-\uFFE5]{1}[a-zA-Z0-9]{6}$");

	/**
	 * 证件号码Pattern
	 */
	public static final Pattern ID_CODE_PATTERN = Pattern
			.compile("^[a-zA-Z0-9]+$");

	/**
	 * 编码Pattern
	 */
	public static final Pattern CODE_PATTERN = Pattern
			.compile("^[a-zA-Z0-9]+$");

	/**
	 * 固定电话编码Pattern
	 */
	public static final Pattern PHONE_NUMBER_PATTERN = Pattern
			.compile("0\\d{2,3}-[0-9]+");

	/**
	 * 邮政编码Pattern
	 */
	public static final Pattern POST_CODE_PATTERN = Pattern.compile("\\d{6}");

	/**
	 * 面积Pattern
	 */
	public static final Pattern AREA_PATTERN = Pattern.compile("\\d*.?\\d*");

	/**
	 * 手机号码Pattern
	 */
	public static final Pattern MOBILE_NUMBER_PATTERN = Pattern
			.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))(\\d{8})$");

	/**
	 * 银行帐号Pattern
	 */
	public static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern
			.compile("\\d{16,21}");

	/**
	 * QQ号Pattern
	 */
	public static final Pattern QQ_NUMBER_PATTERN = Pattern
			.compile("[1-9][0-9]{4,}");
	/**
	 * 英文邮箱@Pattern
	 */
	public static final Pattern EMAIL_ENGLISH_NUMBER_PATTERN = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	/**
	 * 中文邮箱＠Pattern
	 */
	public static final Pattern EMAIL_CHINA_NUMBER_PATTERN = Pattern
			.compile("\\w+([-+.]\\w+)*＠\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	/**
	 * 非零正整数
	 */
	public static final Pattern INTEGER_PATTERN = Pattern
			.compile("^[1-9]\\d*$");

    /** 就诊人姓名 */
	public static final Pattern USER_NAME_PATTERN = Pattern.compile("[a-zA-Z·\\p{Han}]{2,}");
//	public static final Pattern USER_NAME_PATTERN = Pattern.compile("[a-zA-Z·\\u4E00-\\u9FFF]{2,}");

	/** 就诊人年龄 */
	public static final Pattern USER_AGE_PATTERN = Pattern.compile("^[1-9][0-9]?$");

	/**
	 * 车牌号码是否正确
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isPlateNumber(String s) {
		Matcher m = PLATE_NUMBER_PATTERN.matcher(s);
		return m.matches();
	}

	/**
	 * 证件号码是否正确
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isIDCode(String s) {
		Matcher m = ID_CODE_PATTERN.matcher(s);
		return m.matches();
	}

	/**
	 * 编码是否正确
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isCode(String s) {
		Matcher m = CODE_PATTERN.matcher(s);
		return m.matches();
	}

	/**
	 * 固话编码是否正确
	 * 
	 * @param s
	 * @return
	 */
	public static boolean checkTelephoneNo(String s) {
		Matcher m = PHONE_NUMBER_PATTERN.matcher(s);
		return m.matches();
	}

	/**
	 * 邮政编码是否正确
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isPostCode(String s) {
		Matcher m = POST_CODE_PATTERN.matcher(s);
		return m.matches();
	}

	/**
	 * 面积是否正确
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isArea(String s) {
		Matcher m = AREA_PATTERN.matcher(s);
		return m.matches();
	}

	/**
	 * 手机号码否正确 （十一位数）
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isMobileNumber(String s) {
		Matcher m = MOBILE_NUMBER_PATTERN.matcher(s);
		return m.matches();
	}

	/**
	 * 银行账号否正确
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isAccountNumber(String s) {
		Matcher m = ACCOUNT_NUMBER_PATTERN.matcher(s);
		return m.matches();
	}

	/**
	 * QQ号验证
	 * @param qq
	 * @return
	 */
	public static boolean checkQQ(String qq) {
		Matcher m = QQ_NUMBER_PATTERN.matcher(qq);
		return m.matches();
	}

	/**
	 * 验证邮箱(@)
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		Matcher m = EMAIL_ENGLISH_NUMBER_PATTERN.matcher(email);
		return m.matches();
	}

	/**
	 * 验证邮箱(＠)
	 */
	public static boolean checkEmailHuaweiInput(String email) {
		Matcher m = EMAIL_CHINA_NUMBER_PATTERN.matcher(email);
		return m.matches();
	}

	/**
	 * 是否是字母
	 * 
	 * @param str
	 * @return
	 */
	public static String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}
   
	/**
	 * 判断是否为数字
	 * @param content
	 * @return
	 */
   public static boolean isNumber(String content){
	   boolean isNum = content.matches("[0-9]+");
	   return isNum;
   }

	/**
	 * 判断是否为非零正整数
	 * @param content
	 * @return
	 */
   public static boolean isInteger(String content){
	   Matcher m = INTEGER_PATTERN.matcher(content);
	   return m.matches();
   }

	/**
	 * 判断是否符合就诊人姓名要求
	 * @param content 待检查内容
	 * @return 是否符合
	 */
	public static boolean checkName(String content) {
		return USER_NAME_PATTERN.matcher(content).matches();
	}

	/**
	 * 判断是否符合就诊人年龄要求
	 * @param content 待检查内容
	 * @return 是否符合
	 */
	public static boolean checkAge(String content) {
		return USER_AGE_PATTERN.matcher(content).matches();
	}
}
