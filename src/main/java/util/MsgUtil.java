package util;

import com.eastrobot.robotface.RobotMessageResource;
import com.eastrobot.robotface.RobotMessageResourceProxy;

public class MsgUtil {

	private static final RobotMessageResource robotMessageResource = new RobotMessageResourceProxy();

	
	public static String get(String key, String platform) {
		return robotMessageResource.get(key, platform);
	}
	
	public static String getWithDefValue(String key, String platform, String defValue) {
		String value = robotMessageResource.get(key, platform);
		if(value == null) {
			value = defValue;
		}
		return value;
	}

	/**
	 * 获取消息资源配置参数，为斜杠和美元符号转义
	 * 
	 * @param key
	 *            参数标识
	 * @param params
	 *            占位符表示参数
	 * @return
	 */
	public static String get(String key, String platform, String... params) {
		String result = null;
		if (key != null && !"".equals(key.trim())) {
			String[] newParams = new String[params.length];
			for (int i = 0; i < params.length; i++) {
				newParams[i] = params[i].replace("\\", "\\\\\\\\").replace("$",
						"\\$");
			}
			result = robotMessageResource.get(key, platform, newParams);
		}
		return result;
	}

	/**
	 * 获取消息资源配置参数 ,如果有$(ID),用OpenId替换它
	 * 
	 * @param userId
	 *            OpenId
	 * @param key
	 *            参数标识
	 * @param params
	 *            占位符表示的参数
	 * @return
	 */
	public static String getWithUserId(String userId, String platform,
			String key, String... params) {
		String result = get(key, platform, params);
		if (result != null) {
			result = result.replace("$(ID)", userId);
		}
		return result;
	}

}
