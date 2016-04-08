package flyad.cx.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 微信配置
 * @author chengxiang
 *
 */
public class WeChatConfig {

	private static String appid;
	
	private static String appsecret;
	
	private static String token;
	
	private static Properties properties;
	
	private static String filePath = "wechat.properties";

	
    public synchronized static void init() {
    	
    	if(properties != null){
    		return;
    	}
    	InputStream inputStream = WeChatConfig.class.getClassLoader().getResourceAsStream(filePath);
    	try{
    		properties = new Properties();
    		properties.load(inputStream);
    	} catch (IOException e1){
    		e1.printStackTrace();
    	}
    	appid = properties.getProperty("appid");
    	
    	appsecret = properties.getProperty("appsecret");
    	
    	token = properties.getProperty("token");
    }


	public static String getAppid() {
		return appid;
	}


	public static String getAppsecret() {
		return appsecret;
	}


	public static String getToken() {
		return token;
	}
    
}
