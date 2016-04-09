package flyad.cx.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import flyad.cx.entity.Coupon;
import flyad.cx.entity.User;
import flyad.cx.service.innr.CouPonServiceInnr;
import flyad.cx.service.innr.UserServiceInnr;
import flyad.cx.util.Config;
import flyad.cx.util.MD5;
import sun.misc.BASE64Encoder;
/**
 * 
 * @author chengxiang
 *
 */
@Service
public class CouponService {
	
	private static SimpleDateFormat simpleDateFormatFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static Logger logger = Logger.getLogger(CouponService.class);

	private UserServiceInnr userServiceInnr;
	
	private CouPonServiceInnr couPonServiceInnr;
	
	public void filter(
			HttpServletRequest request
			){
		if(null != request.getAttribute("regist") && (boolean)request.getAttribute("regist")){
			String openId = request.getParameter("openid");
			String nickName = request.getParameter("nickname");
			String headImgUrl = request.getParameter("headimgurl");
			String sex = request.getParameter("sex");
			String province = request.getParameter("province");
			String city = request.getParameter("city");
			regist(openId, nickName, headImgUrl, sex, province, city);
		}
	}
	
	public boolean regist(
			String openId,
			String nickName,
			String headImgUrl,
			String sex,
			String province,
			String city
			){
		Integer count = userServiceInnr.countFindBy(openId);
		if(null == count || count == 0){
			User user = new User();
			user.setOpenId(openId);
			user.setNickName(nickName);
			user.setHeadImgUrl(headImgUrl);
			user.setSex(sex);
			user.setProvince(province);
			user.setCity(city);
			user.setCreateDate(simpleDateFormatFull.format(new Date()));
			return userServiceInnr.addUser(user);
		}else{
			return true;
		}
	}
	
	public String probability(String openId){
		String hasAward = userServiceInnr.getHasAwardByOpenId(openId);
		if(null != hasAward && "1".equals(hasAward)){
			String isTaken = userServiceInnr.getIsTakenByOpenId(openId);
			if(null != isTaken && "1".equals(isTaken)){
				return "3";
			}else{
				return "1";
			}
		}else{
			Double probability = Double.parseDouble(Config.getProbability());
			double randomNumber = Math.random();
			if(randomNumber < probability){
				return userServiceInnr.updateUserStatus(openId, "1", null)?"1":"2";
			}else{
				return "2";
			}
		}
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
			logger.error("FAILED:[getRedirectUrl] EXCEPTION:"+e.toString());
			return null;
		}
		return couponTakeUrl;
	}

	public boolean checkToken(String token, String openId, String time){
		if(token == null || openId == null || time ==null){
			return false;
		}
		String checkKey = Config.getCheckKey();
		String tokenCheck = MD5.encrypt(checkKey + MD5.encrypt(openId + checkKey + time));
		return token.equals(tokenCheck);
	}
	
	public boolean isTaken(String openId){
		String isTaken = userServiceInnr.getIsTakenByOpenId(openId);
		return (null != isTaken && "1".equals(isTaken))?true : false;
	}
	
	public boolean hasAward(String openId){
		String isTaken = userServiceInnr.getHasAwardByOpenId(openId);
		return (null != isTaken && "1".equals(isTaken))?true : false;
	}
	
	public boolean record(Coupon coupon){
		boolean status = true;
		if(null != coupon.getIsTaken()&& "1".equals(coupon.getIsTaken())){
			status = userServiceInnr.updateUserStatus(coupon.getOpenId(), null, coupon.getIsTaken());
		}
		coupon.setCreateDate(simpleDateFormatFull.format(new Date()));
		return status ? couPonServiceInnr.addCoupon(coupon) : status;
	}
	
	@Resource
	public void setUserServiceInnr(UserServiceInnr userServiceInnr) {
		this.userServiceInnr = userServiceInnr;
	}

	@Resource
	public void setCouPonServiceInnr(CouPonServiceInnr couPonServiceInnr) {
		this.couPonServiceInnr = couPonServiceInnr;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Config.init();
		System.out.println(new CouponService().checkToken("sdf", "123456qwerty", new Date().getTime()+""));
	}
}
