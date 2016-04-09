package flyad.cx.mapper;

import java.util.Map;

import flyad.cx.entity.User;

public interface UserMapper {

	public void addUser(User user);
	
	public Integer countFindBy(Map<String, Object> parametersMap);
	
	public String getHasAwardByOpenId(Map<String, Object> parametersMap);
	
	public String getIsTakenByOpenId(Map<String, Object> parametersMap);
	
	public Integer updateUserStatus(Map<String, Object> parametersMap);

}
