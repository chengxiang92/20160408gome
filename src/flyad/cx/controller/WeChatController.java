package flyad.cx.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import flyad.cx.util.MyHttpClient;
import flyad.cx.util.MyJson;
import flyad.cx.util.SHA1;
import flyad.cx.util.WeChatConfig;
import flyad.cx.util.WeChatUrl;

@Controller
@RequestMapping("weixin")
public class WeChatController {
    
	private static Logger logger = Logger.getLogger(WeChatController.class);
	
    /**
     * 微信Token验证
     * @param request
     * @param response
     */
    @RequestMapping(value="verify",method=RequestMethod.GET)
	public void index(
			HttpServletRequest request,
			HttpServletResponse response
			){
		// 微信加密签名
        String signature = request.getParameter("signature");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");

        String[] str = {WeChatConfig.getToken(), timestamp, nonce };
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2];
        // SHA1加密
        String digest = new SHA1().getDigestOfString(bigStr.getBytes()).toLowerCase();

        // 确认请求来至微信
        if (digest.equals(signature)) {
            try {
				response.getWriter().print(echostr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
    
    @RequestMapping(value="verify",method=RequestMethod.POST)
    public @ResponseBody String getMessage(
    		HttpServletRequest request,
    		HttpServletResponse response
    		) throws UnsupportedEncodingException{
    	 // 将请求、响应的编码均设置为UTF-8（防止中文乱码） 
        request.setCharacterEncoding("UTF-8"); 
        response.setCharacterEncoding("UTF-8"); 
 
        // 调用核心业务类接收消息、处理消息 
        ServletInputStream in;
		try {
			in = request.getInputStream();
			logger.debug(in.toString());
			return in.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        // 响应消息 
    	return null;
    }
    
    /**
     * 微信用户授权+获取用户详基本信息
     * @param request
     * @param response
     * @param code
     * @param state
     * @return
     */
    @RequestMapping(value="redirect")
    public @ResponseBody String redirect(
    		HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="code", required=false)String code,
			@RequestParam(value="state", required=false)String state
    		){
    	logger.info("ENTER[weixin/redirect]");
    	logger.info("PRAMETERS:{code:"+code+"state:"+state+"}");
    	
    	String getOpenIdUrl = WeChatUrl.getOpenIdUrl(code, "authorization_code");
    	String getOpenIdResult = MyHttpClient.get(getOpenIdUrl);
    	
    	logger.info("PRAMETERS:"+getOpenIdResult);
    	
    	Map<String, Object> map = MyJson.fromJson(getOpenIdResult, HashMap.class);
    	String accessToken = map.get("access_token").toString();
    	String openId = map.get("openid").toString();
    	String getUserInfoUrl = WeChatUrl.getUserInfo(accessToken, openId);
    	String getUserInfoResult = MyHttpClient.get(getUserInfoUrl);
    	
    	logger.info("PRAMETERS:"+getUserInfoResult);
    	return getUserInfoResult;
    }
}
