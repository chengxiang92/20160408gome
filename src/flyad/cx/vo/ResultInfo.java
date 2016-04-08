package flyad.cx.vo;

public class ResultInfo {
	
	/**
	 * 状态码
	 */
	private String code;
	
	/**
	 * 状态描述
	 */
	private String desc;
	
	/**
	 * 返回数据
	 */
	private Object data;



	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ResultInfo() {}
	
	public ResultInfo(String code, String desc, Object data) {
		this.code = code;
		this.desc = desc;
		this.data = data;
	}

}
