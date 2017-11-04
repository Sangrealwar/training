package spittr.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.Registration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * 功能：提供一个StatViewServlet，用于展示Druid信息
 * 条件：
 * Created by wq on 2017/5/7.
 */
public class DruidStatViewServlet implements WebApplicationInitializer {
    public void onStartup(ServletContext servletContext) throws ServletException {

        servletContext.setInitParameter("resetEnable","true");
        servletContext.setInitParameter("loginUsername","druid");
        servletContext.setInitParameter("loginPassword","druid");
        //添加Druid的配置Servlet
        ServletRegistration.Dynamic registration = servletContext.addServlet("DruidStatView",new StatViewServlet());
        registration.setLoadOnStartup(1);
        registration.addMapping("/druid/*");
    }
}
