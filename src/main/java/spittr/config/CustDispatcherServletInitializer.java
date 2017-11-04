package spittr.config;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.util.Log4jConfigListener;
import spittr.service.task.HelloJob;
import spittr.web.Filter.LoginFilter;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import java.util.Date;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 功能：注册一个Servlet，可配置web.xml相似的功能，如Filter,Lister
 *          这里注册Log4j监听
 * 条件：
 * Created by wq on 2017/3/4.
 */
public class CustDispatcherServletInitializer implements WebApplicationInitializer {
    public void onStartup(ServletContext servletContext) throws ServletException {
        //Log4jConfigListener
        servletContext.setInitParameter("log4jConfigLocation", "classpath:/log4j.properties");
        servletContext.addListener(Log4jConfigListener.class);

//        javax.servlet.FilterRegistration.Dynamic filter =        //过滤器的另一种配置方法
//                servletContext.addFilter("loginFilter", LoginFilter.class);
//        filter.addMappingForUrlPatterns(null,false,"/");    //filter mapping
    }
}
