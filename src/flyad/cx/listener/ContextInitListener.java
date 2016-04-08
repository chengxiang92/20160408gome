package flyad.cx.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import flyad.cx.util.Config;
import flyad.cx.util.WeChatConfig;

/**
 * web启动创建context监听器
 * @author chengxiang
 *
 */
public class ContextInitListener implements ServletContextListener{
	private static Logger log = Logger.getLogger(ContextInitListener.class);
	private ApplicationContext app; 

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		log.debug("contextDestroyed");
		//获取spring上下文！  
		app = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.debug("contextInitialized");
		//获取spring上下文！
		app = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());  
		//初始化微信配置
		WeChatConfig.init();
		//初始配置文件
		Config.init();
	}

}
