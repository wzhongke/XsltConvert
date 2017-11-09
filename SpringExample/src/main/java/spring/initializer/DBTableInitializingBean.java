package spring.initializer;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * 实现 InitializingBean 接口，afterPropertiesSet方法会在Spring容器加载完成后执行
 */

@Service
public class DBTableInitializingBean implements InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
	}
}
