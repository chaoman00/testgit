package util;

import function.Constants;

import java.io.*;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符工具类
 * 
 * @author yale
 * 
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

	/** 所有表情符号正则表达式 */
	public static final String REGEX_EMOJI = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)";
	/** 所有中文正则表达式 */
	public static final String REGEX_ZH_CN = "[\u4e00-\u9fa5]+";

	private StringUtils() {
	}

	/**
	 * 去除字符串中的所有空格
	 * 
	 * @param str
	 *            字符串
	 * @return 去除空格后的字符串
	 */
	public static String removeSpaces(String str) {
		if (str == null)
			return null;
		return str.replaceAll(" ", "");
	}

	/**
	 * 获取去除"-"的32位UUID字符串
	 * 
	 * @return 32字符串
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 判断字符串是否符合正则表达式
	 * 
	 * @param str
	 *            字符串
	 * @param regex
	 *            正则
	 * @return true or false
	 */
	public static boolean matches(String str, String regex) {
		if (regex == null || str == null)
			return Boolean.FALSE;
		return str.matches(regex);
	}

	/**
	 * 提取字符串中满足正则表达式的字符（多个满足条件字符串之间默认用空格分隔）
	 * 
	 * @param str
	 *            字符串
	 * @param regex
	 *            正则表达式，例：
	 *            <p/>
	 *            <small>[\u4e00-\u9fa5]+：获取所有中文字符</small><br/>
	 *            <small>\\d{11}：获取所有11位数字(模拟手机号)</small><br/>
	 *            <small>\\w+：获取英文、数字、下划线</small>
	 * @param splitStr
	 *            多个满足正则表达式的字符按split字符分隔（默认为空格）
	 * @return 满足条件的字符串，若没有则返回null
	 */
	public static String getStrByRegex(String str, String regex, String splitStr) {
		StringBuffer buf = new StringBuffer("");
		if (isBlank(splitStr))
			splitStr = " ";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			if (matcher.group().length() > 0) {
				buf.append(matcher.group() + splitStr);
			}
		}
		String result = buf.toString();
		if (isBlank(result)) {
			return null;
		}
		if (result.endsWith(splitStr)) {
			result = result.substring(0, result.length() - splitStr.length());
		}
		return result;
	}

	/**
	 * 根据正则表达式，替换字符串中满足条件的字符
	 * 
	 * @param str
	 *            字符串
	 * @param regex
	 *            正则表达式，例：
	 *            <p/>
	 *            <small>StringUtils.REGEX_EMOJI：去除所有表情符号</small><br/>
	 *            <small>StringUtils.REGEX_ZH_CN：去除所有中文</small>
	 * @param replacement
	 *            替换的字符（默认为*号）
	 * @return 替换后的字符串
	 */
	public static String replaceStrByRegex(String str, String regex,
			String replacement) {
		if (isBlank(replacement))
			replacement = "*";
		return str.replaceAll(regex, replacement);
	}

	/**
	 * 根据输入流得到字符串（默认utf-8编码）
	 * 
	 * @param is
	 *            输入流
	 * @return 输入流中获取的字符
	 */
	public static String getString(InputStream is) {
		return getString(is, Constants.CHARSET);
	}

	/**
	 * 根据输入流得到字符串
	 * 
	 * @param is
	 *            输入流
	 * @param charset
	 *            字符集
	 * @return 输入流中获取的字符
	 */
	public static String getString(InputStream is, String charset) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, charset));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line + "\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return sb.toString();
	}

}
