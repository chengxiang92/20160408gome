package flyad.cx.service.innr;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import flyad.cx.entity.User;
import flyad.cx.mapper.UserMapper;

@Service
public class UserServiceInnr {
	
	private UserMapper userMapper;
	
	private static Logger logger = Logger.getLogger(UserServiceInnr.class);

	public Boolean addUser(User user){
		try{
			userMapper.addUser(user);
			return true;
		}catch(Exception e){
			logger.error("FAILED:[addUser] EXCEPTION:"+e.toString());
			return false;
		}
	}
	
	public Integer countFindBy(String openId){
		return countFindBy(openId, null);
	}
	
	public Integer countFindBy(String openId, String hasAward){
		Map<String , Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("openId", openId);
		parametersMap.put("hasAward", hasAward);
		try{
			return userMapper.countFindBy(parametersMap);
		}catch(Exception e){
			logger.error("FAILED:[countFindBy] EXCEPTION:"+e.toString());
			return null;
		}
	}
	
	public String getHasAwardByOpenId(String openId){
		Map<String , Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("openId", openId);
		try{
			return userMapper.getHasAwardByOpenId(parametersMap);
		}catch(Exception e){
			logger.error("FAILED:[getHasAwardByOpenId] EXCEPTION:"+e.toString());
			return null;
		}
	}
	
	public String getIsTakenByOpenId(String openId){
		Map<String , Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("openId", openId);
		try{
			return userMapper.getIsTakenByOpenId(parametersMap);
		}catch(Exception e){
			logger.error("FAILED:[getIsTakenByOpenId] EXCEPTION:"+e.toString());
			return null;
		}
	}
	
	public boolean updateUserStatus(String openId, String hasAward,String isToken){
		Map<String , Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("openId", openId);
		parametersMap.put("hasAward", hasAward);
		parametersMap.put("isToken", isToken);
		try{
			if(userMapper.updateUserStatus(parametersMap) > 0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			logger.error("FAILED:[updateUserStatus] EXCEPTION:"+e.toString());
			return false;
		}
	}
	
	@Resource
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
}
