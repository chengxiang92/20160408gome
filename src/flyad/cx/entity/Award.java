package flyad.cx.entity;

import java.io.Serializable;

public class Award implements Serializable{

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
	 * 是否拿到
	 * 1是 0否
	 */
	private String isTaken;
	/**
	 * 操作日期
	 */
	private String createDate;

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

	public String getIsTaken() {
		return isTaken;
	}

	public void setIsTaken(String isToken) {
		this.isTaken = isToken;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
