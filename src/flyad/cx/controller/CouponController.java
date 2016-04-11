package flyad.cx.controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import flyad.cx.entity.Coupon;
import flyad.cx.service.CouponService;
import flyad.cx.util.MyJson;
/**
 * 
 * @author chengxiang
 *
 */
@Controller
@RequestMapping(value = "/coupon")
public class CouponController {

	private CouponService couponService;
	
	private static Logger logger = Logger.getLogger(CouponController.class);
	
	
	/**
	 * 首页
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public String index(
			Model model,
			HttpServletRequest request
			){
		couponService.filter(request);
		logger.debug("index===========");
		return "/html/index.html";
	}
	/**
	 * 抽奖
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/probability")
	public @ResponseBody String probability(
			HttpServletRequest request
			){
		String openId = (String)(request.getSession().getAttribute("openid"));
		return couponService.probability(openId);
	}
	/**
	 * 领卷跳转
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/get")
	public String get(
			HttpServletRequest request,
			HttpServletResponse response
			) {
		couponService.filter(request);
		String openId = (String)(request.getSession().getAttribute("openid"));
		String couponUrl = couponService.getRedirectUrl(openId);
		if(couponUrl == null){
			return "redirect:/coupon/index";
		}
		return "redirect:"+couponUrl;
	}
	/**
	 * 领卷回调
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/redirect")
	public String redirect(
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="openId", required=true)String openId
			) {
		return couponService.isTaken(openId)? "/html/gongxi.html":"/html/shibai.html";
	}
	/**
	 * 领卷状态写入接口
	 * @param model
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/status")
	public @ResponseBody String status(
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="token", required=false)String token,
			@RequestParam(value="od", required=false)String od,
			@RequestParam(value="tms", required=false)String time,
			@RequestParam(value="result", required=false)String result
			) throws IOException {
/*		logger.debug("parameters["+token+","+od+","+time+","+result+"]");
		token = request.getHeader("token");
		od = request.getHeader("od");
		time = request.getHeader("tms");
		result = request.getHeader("result");*/
		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuffer buffer = new StringBuffer();
        String msg = null;
        while((msg=in.readLine()) != null){
            System.out.println(msg);
            logger.debug("["+msg+"]");
            buffer.append(msg);
        }
        logger.debug("buffer:["+buffer+"]");
		logger.debug("parameters["+token+","+od+","+time+","+result+"]");
		Enumeration b = request.getHeaders("Accept-Encoding");  
        while(b.hasMoreElements()){  
            String headValue = (String) b.nextElement();  
            String value = request.getHeader(headValue);  
            System.out.println(headValue+"="+value);  
        }  
        System.out.println("-------------------------------------------------");  
         b = request.getHeaderNames();  
         while(b.hasMoreElements()){  
             String name = (String) b.nextElement();  
             String value = request.getHeader(name);  
             System.out.println(name+" = "+value);  
               
         }  
		Coupon coupon = new Coupon();
		coupon.setIsTaken(result);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(token == null || od == null || time ==null || result == null){
			resultMap.put("msg", "arquired parameter missing arquired");
			resultMap.put("code", "40004");
			logger.debug("arquired parameter missing arquired");
			return MyJson.toJson(resultMap);
		}
		try{
			String openId = URLDecoder.decode(od, "utf-8");
			if(!couponService.checkOpenId(openId)){
				resultMap.put("msg", "openId not exist");
				resultMap.put("code", "40002");
				logger.debug("openId not exist");
			}else if(couponService.isTaken(openId)){
				resultMap.put("msg", "already take");
				resultMap.put("code", "40005");
				logger.debug("already take");
			}
			else{
				if(!couponService.checkToken(token, openId, time)){
					resultMap.put("msg", "check token failed");
					resultMap.put("code", "40001");
					logger.debug("check token failed");
				}else{
					coupon.setOpenId(openId);
					if(couponService.record(coupon)){
						resultMap.put("msg", "success");
						resultMap.put("code", "1");
						logger.debug("success");
					}else{
						resultMap.put("msg", "insert DB Failed");
						resultMap.put("code", "40003");
						logger.debug("insert DB Failed");
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			resultMap.put("msg", "od Decode Exception");
			resultMap.put("code","40004");
			logger.debug("od Decode Exception"+e);
		}finally {
			return MyJson.toJson(resultMap);
		}

	}

	@Resource
	public void setCouponService(CouponService couponService) {
		this.couponService = couponService;
	}

}
