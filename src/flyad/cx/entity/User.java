package flyad.cx.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 用户信息表
 * @author chengxiang
 *
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * openId
	 */
	private String openId;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 头像地址
	 */
	private String sex;
	
	private String province;
	
	private String city;
	
	private String headImgUrl;
	
	private String createDate;
	
	private String hasAward;
	
	private String isTaken;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getHasAward() {
		return hasAward;
	}
	public void setHasAward(String hasAward) {
		this.hasAward = hasAward;
	}
	public String getIsTaken() {
		return isTaken;
	}
	public void setIsTaken(String isTaken) {
		this.isTaken = isTaken;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

}
