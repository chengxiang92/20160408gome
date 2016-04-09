package flyad.cx.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import flyad.cx.entity.Coupon;
import flyad.cx.service.CouponService;
import flyad.cx.util.Config;
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
			) throws NoSuchAlgorithmException, UnsupportedEncodingException{
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
		return couponService.isTaken(openId)? "success":"failed";
	}
	/**
	 * 领卷状态写入接口
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping("/status")
	public @ResponseBody String status(
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="token", required=true)String token,
			@RequestParam(value="od", required=true)String od,
			@RequestParam(value="tms", required=true)String time,
			@RequestParam(value="result", required=true)String result
			) {
		Coupon coupon = new Coupon();
		coupon.setIsTaken(result);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			String openId = URLDecoder.decode(od, "utf-8");
			if(!couponService.checkToken(token, openId, time)){
				resultMap.put("msg", "check token failed");
				resultMap.put("code", "2");
			}else{
				coupon.setOpenId(openId);
				if(couponService.record(coupon)){
					resultMap.put("msg", "success");
					resultMap.put("code", "1");
				}else{
					resultMap.put("msg", "insert DB Failed");
					resultMap.put("code", "4");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			resultMap.put("msg", "od Decode Exception");
			resultMap.put("code","3");
		}finally {
			return MyJson.toJson(resultMap);
		}

	}

	@Resource
	public void setCouponService(CouponService couponService) {
		this.couponService = couponService;
	}

}
