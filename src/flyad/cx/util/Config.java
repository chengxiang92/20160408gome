package flyad.cx.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 微信配置
 * @author chengxiang
 *
 */
public class Config {

	private static String authUrl;
	
	private static String couponTakeUrl;
	
	private static String privateKey;
	
	private static String from;
	
	private static String redirectUrl;
	
	private static String probability;
	
	private static String checkKey;
	
	private static Properties properties;
	
	private static String filePath = "config.properties";

    public synchronized static void init() {
    	
    	InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(filePath);
    	try{
    		properties = new Properties();
    		properties.load(inputStream);
    	} catch (IOException e1){
    		e1.printStackTrace();
    	}
    	authUrl = properties.getProperty("authUrl");
    	couponTakeUrl = properties.getProperty("couponTakeUrl");
    	privateKey = properties.getProperty("privateKey");
    	from = properties.getProperty("from");
    	redirectUrl = properties.getProperty("redirectUrl");
    	probability = properties.getProperty("probability");
    	checkKey = properties.getProperty("checkKey");
    }

    public static void reload(){
    	
    }
    
	public static String getCouponTakeUrl() {
		return couponTakeUrl;
	}

	public static String getPrivateKey() {
		return privateKey;
	}

	public static String getFrom() {
		return from;
	}

	public static String getRedirectUrl() {
		return redirectUrl;
	}

	public static String getAuthUrl() {
		return authUrl;
	}

	public static String getProbability() {
		return probability;
	}

	public static String getCheckKey() {
		return checkKey;
	}

}
