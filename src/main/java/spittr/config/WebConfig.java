package spittr.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
import spittr.web.Interceptor.CommonInterceptor;

/**
 * SpringMvc容器
 * SpringMvc容器和Spring容器存在父子关系，WebConfig仅为SpringMvc容器
 * EnableWebMvc启用SpringMvc容器，扫描Controller搭配RequestMapping使用
 * 要扫描的包仅限于Controller，若包含事务，会重复扫描，导致事务无法在同一线程
 *
 *  <mvc:annotation-driven conversion-service="conversionService"/>
 * @author wq
 * repository  2016-11-17
 *
 */
@Configuration
@EnableWebMvc             //启用Springmvc
@ComponentScan(basePackages={"spittr.controller"})        //启用组件扫描
public class WebConfig extends WebMvcConfigurerAdapter{


	/**
	 * 返回视图解析类，注入模板引擎，解析逻辑视图
	 */
	@Bean
	public ViewResolver viewResolver(SpringTemplateEngine templateEngine){
//		InternalResourceViewResolver resolver=
//			new InternalResourceViewResolver();    //默认处理jsp的视图解析器
//		resolver.setPrefix("/WEB-INF/views/activemq/");  //前缀
//		resolver.setSuffix(".jsp");              //后缀
//		resolver.setExposeContextBeansAsAttributes(true);
		
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine);
		resolver.setCharacterEncoding("UTF-8");    //设置字符集
		return resolver;
	}
	
	/**
	 * 模板引擎，注入模板解析类
	 * @param templateResolver
	 * @return
	 */
	@Bean
	public SpringTemplateEngine templateEngine(TemplateResolver templateResolver){
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		return templateEngine;
	}
	
	/**
	 * 模板解析类，处理模板响应，解析模板，定位
	 * @return
	 */
	@Bean
	public TemplateResolver templateResolver(){
		TemplateResolver templateResolver = new ServletContextTemplateResolver();
		
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");	
		templateResolver.setCharacterEncoding("UTF-8");    //设置字符集
		return templateResolver;		
	}
	
	@Bean
	public MultipartResolver multipartResolver() throws IOException{
		/**
		 * 标准解析器，依赖于Servlet3.0容器
		 */
//		return new StandardServletMultipartResolver();   
		
		/**
		 * 基于Jakarta 的解析器，不会强制要求设置临时文件路径，默认servlet容器临时目录
		 */
		CommonsMultipartResolver multipartResolver= new CommonsMultipartResolver();	
		multipartResolver.setUploadTempDir(
				new FileSystemResource("/tmp/spittr/uploads")); //设置临时文件路径
		multipartResolver.setMaxUploadSize(2097152);    //上传的文件最大为2MB
		multipartResolver.setMaxInMemorySize(0);  //存入临时文件的指定大小
		multipartResolver.setDefaultEncoding("UTF-8");  //设置文件流字符集	
		//无法设置请求的最大大小
		return multipartResolver;	
	}

	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();   //静态资源处理，好像没起作用
	}

	/**
	 * 静态资源获取  <mvc:resources  />
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//访问/view/**的请求会定位到/view 下查找
		registry.addResourceHandler("/view/**").addResourceLocations("/view/");
		registry.addResourceHandler("/login/**").addResourceLocations("/login/");
		super.addResourceHandlers(registry);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CommonInterceptor());
		super.addInterceptors(registry);
	}
}
