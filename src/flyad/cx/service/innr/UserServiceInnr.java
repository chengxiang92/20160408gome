package flyad.cx.service.innr;

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
	
	@Resource
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
}
