package flyad.cx.service.innr;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import flyad.cx.entity.Coupon;
import flyad.cx.mapper.CouponMapper;

@Service
public class CouPonServiceInnr {
	
	private CouponMapper couponMapper;
	
	private static Logger logger = Logger.getLogger(CouPonServiceInnr.class);

	public Boolean addCoupon(Coupon coupon){
		try{
			couponMapper.addCoupon(coupon);
			return true;
		}catch(Exception e){
			logger.error("FAILED:[addCoupon] EXCEPTION:"+e.toString());
			return false;
		}
	}

	@Resource
	public void setCouponMapper(CouponMapper couponMapper) {
		this.couponMapper = couponMapper;
	}

}
