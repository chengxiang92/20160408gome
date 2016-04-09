package flyad.cx.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import flyad.cx.util.Config;
import flyad.cx.util.MyJson;

@Controller
@RequestMapping("config")
public class ConfigController {
    
	private static Logger logger = Logger.getLogger(ConfigController.class);
	
    /**
     * config reload
     * @param request
     * @param response
     */
    @RequestMapping(value="reload",method=RequestMethod.GET)
	public @ResponseBody String index(){
    	Config.init();
    	Map<String, String> config= new HashMap<String, String>();
//    	config.put("authUrl", Config.getAuthUrl());
    	config.put("couponTakeUrl", Config.getCouponTakeUrl());
//    	config.put("privateKey", Config.getPrivateKey());
    	config.put("from", Config.getFrom());
    	config.put("redirectUrl", Config.getRedirectUrl());
    	config.put("probability", Config.getProbability());
//    	config.put("checkKey", Config.getCheckKey());
    	return MyJson.toJson(config);
	}
}
