package spittr.config.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * 配置DelegatingFilterProxy的初始化类
 * 会拦截请求，并将请求委托给springSecurityFilterChain处理
 *
 * @author wq
 * repository：2016-12-12
 *
 */
public class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer{
	
}
