package flyad.cx.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import flyad.cx.entity.Coupon;
import flyad.cx.service.CouponService;
import flyad.cx.util.Config;
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
	
	@RequestMapping("/index")
	public String index(
			Model model,
			HttpServletRequest request
			){
		if(null == request.getSession().getAttribute("openid")){
			if(null == request.getParameter("openid")){
				return "redirect:"+Config.getAuthUrl();
			}else{
				request.getSession().setAttribute("openid", request.getParameter("openid"));
			}
		}
		String openid = (String)request.getSession().getAttribute("openid");
		model.addAttribute("openid", openid);
		return "index";
	}
	
	@RequestMapping("/get")
	public String get(
			HttpServletRequest request,
			HttpServletResponse response
			) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String openId = (String)(request.getSession().getAttribute("openid"));
		String couponUrl = couponService.getRedirectUrl(openId);
		if(couponUrl == null){
			return "redirect:/coupon/index";
		}
		return "redirect:"+couponUrl;
	}
	
	@RequestMapping("/redirect")
	public String redirect(
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="token", required=true)String token,
			@RequestParam(value="od", required=true)String od,
			@RequestParam(value="tms", required=true)String sttmsate,
			@RequestParam(value="result", required=true)String result
			) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		Coupon coupon = new Coupon();
		coupon.setIsTaken(result);
		coupon.setOpenId(URLDecoder.decode(od, "utf-8"));
		couponService.record(coupon);
		if(null != request && "1".equals(result)){
			return "success";
		}else{
			return "failed";
		}
	}

	@Resource
	public void setCouponService(CouponService couponService) {
		this.couponService = couponService;
	}

}
