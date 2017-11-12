package spittr.config;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import spittr.web.Filter.LoginFilter;

/**
 * SpringMVC 基本环境
 * 适用于Tomcat7.0及以上，需部署到Servlet3.0服务器
 * 
 * @author wq
 * repository  2016-11-17
 *
 */
public class SpittrWebAppInitilizer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * 创建其他bean的应用上下文配置
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{RootConfig.class};
	}

	/**
	 * 获得Servlet的应用服务器上下文配置
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{WebConfig.class};
	}

	/**
	 * 获得DispatchServlet的映射，这里默认是"/"
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

	/**
	 * 获得过滤器列表
	 * @return
	 */
	@Override
	protected Filter[] getServletFilters() {
		return new Filter[]{new LoginFilter()};
	}

	/**
	 * 在DispatchServlet注册到Servlet容器后，调用该方法
	 * 通过配置Dynamic对DispatchServlet进行额外的配置
	 */
	@Override
	protected void customizeRegistration(Dynamic registration) {
		//设置上传文件的临时存放目录		
		registration.setMultipartConfig(
				/**
				 * 参数分别为（容量以字节为单位）：
				 * 临时文件位置
				 * 上传文件最大容量，默认没有限制
				 * 整个请求的最大容量，默认无限制
				 * 上传过程中，达到指定容量写入临时文件，默认指定容量为0
				 */				
				new MultipartConfigElement("/tmp/spittr/uploads", 2097151,4194304, 0));    
	}
}
