package flyad.cx.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import flyad.cx.entity.Coupon;
import flyad.cx.entity.User;
import flyad.cx.service.innr.CouPonServiceInnr;
import flyad.cx.service.innr.UserServiceInnr;
import flyad.cx.util.Config;
import flyad.cx.util.MD5;
import sun.misc.BASE64Encoder;

@Service
public class CouponService {
	
	private static SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	private static Logger logger = Logger.getLogger(CouponService.class);

	private UserServiceInnr userServiceInnr;
	
	private CouPonServiceInnr couPonServiceInnr;
	
	public boolean regist(
			String openId,
			String nickName,
			String headImgUrl
			){
		User user = new User();
		user.setOpenId(openId);
		user.setNickName(nickName);
		user.setHeadImgUrl(headImgUrl);
		user.setCreateDate(simpleDateFormatFull.format(new Date()));
		return userServiceInnr.addUser(user);
	}
	
	public String getRedirectUrl(String openId){
		String couponTakeUrl = Config.getCouponTakeUrl();
		String redirectUrl = Config.getRedirectUrl();
		String privateKey = Config.getPrivateKey();
		String from = Config.getFrom();
		String tms =  new Date().getTime()+"";
		if(couponTakeUrl == null || redirectUrl ==null || privateKey == null || from== null || openId == null){
			return null;
		}
		String token = MD5.encrypt(privateKey+MD5.encrypt(openId+"gome"+tms));
		redirectUrl += "?openId="+openId;
		try {
			redirectUrl = new BASE64Encoder().encode(redirectUrl.getBytes("UTF-8"));
			couponTakeUrl += "?token="+token+"&return_url="+redirectUrl+"&od="+URLEncoder.encode(openId,"utf-8")+"&tms="+tms+"&from="+from;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		return couponTakeUrl;
	}

	public void record(Coupon coupon){
		coupon.setCreateDate(simpleDateFormatFull.format(new Date()));
		couPonServiceInnr.addCoupon(coupon);
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Config.init();
		System.out.println(new CouponService().getRedirectUrl("123456"));
	}
}
