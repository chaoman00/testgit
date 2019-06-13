package util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 * 
 * @author Mike.Peng
 * @time Aug 26, 2015  5:57:47 PM
 */
public class StringUtil extends StringUtils {
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String parseString(Object obj) {
		if(null == obj || "".equals(obj))
			return "";
		return obj.toString();
	}
	
	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str){
		for (int i = str.length();--i>=0;){   
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
	public static Integer parseInteger(String str) {
		int v = -1;
		try {
			v = Integer.valueOf(str);
		} catch (Exception e) {
		}
		return v;
	}
	
	public static String getHashValue(String v, int length) {
		v = parseString(v);
		return String.valueOf(Math.abs(v.hashCode()) % length);
	}
	
	/**
	 * 根据下标移除数组中的一项 ，传入的参数必须合理且不为空
	 * 
	 * @param arr 数组
	 * @param index 下标
	 * @return 返回移除目标项后的数组
	 */
	public static String[] removeOfIndex(String[] arr, int index) {
		if(arr != null && arr.length > 0 && index > -1 && arr.length > index) {
			String[] tmpArr = new String[arr.length - 1];
			int j = 0;
			for(int i = 0;i < arr.length; i ++) {
				if(i != index) {
					tmpArr[j] = arr[i];
					j ++;
				}
			}
			return tmpArr;
		}
		return null;
	}
	
	/**
	 * 根据下标向数组中添加项
	 * 
	 * @param arr 原数组
	 * @param index 欲添加的下标位置
	 * @param obj 要添加的内容
	 * @return 返回添加目标项后的数组
	 */
	public static Object[] addByIndex(Object[] arr ,int index ,Object obj){
		List<Object> list = new ArrayList<Object>();  
        for (int i=0; i<arr.length; i++) {  
            list.add(arr[i]);  
        }  
        list.add(index, obj);
        String[] newArr =  list.toArray(new String[1]);
		return newArr;
	}
	
	/**
	 * 向数组后面添加参数
	 * @param questions
	 * @param StrAcs
	 * @return
	 */
	public static String[] insertStr(String[] questions ,String StrAcs){
		
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < questions.length; i++) {
			list.add(questions[i]);
		}
		list.add(questions.length, StrAcs); // list.add("ruby")
		String[] newStr = list.toArray(new String[1]); // 返回一个包含所有对象的指定类型的数组
		return newStr;
	}
	
	/**
	 * 按照标签如：[link][/link]截取标签中的内容
	 * 
	 * @param Str	需要截取的字符串
	 * @param tag	标签名
	 * @return
	 */
	public static List<String> getTag (String str ,String tag){
		List<String> items = new ArrayList<String>();
		String regex = "\\[" + tag + "\\](.+?)\\[/" + tag + "\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			String itemTemp = m.group();
			String item = itemTemp.substring(tag.length() + 2, itemTemp.length() - tag.length() - 3);
			items.add(item);
		}
		
		return items;
	}
	
	/**
	 * 按照标签如：[link][/link]移除标签及标签中的内容
	 * 
	 * @param Str	需要移除标签的字符串
	 * @param tag	标签名
	 * @return
	 */
	public static String removeTag (String str ,String tag){
		String regex = "\\[" + tag + "\\](.+?)\\[/" + tag + "\\]";
		String result = str.replaceAll(regex, "");
		return result;
	}
	
}
