package flyad.cx.util;

/**
 * 微信url处理
 * @author chengxiang
 * 
 */
public class WeChatUrl {

	//获取微信openId的url
	private static String getOpenidUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?";
	
	private static String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?";
	
	public static String getOpenIdUrl(String code, String grant_typ){
		getOpenidUrl+="appid="+WeChatConfig.getAppid()+"&secret="+WeChatConfig.getAppsecret()+"&code="+code+"&grant_type="+grant_typ;
		return getOpenidUrl;
	}
	
	public static String getUserInfo(String access_token, String openid){
		getUserInfo+="access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
		return getUserInfo;
	}
}
