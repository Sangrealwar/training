package spittr.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.inject.Inject;

/**
 * 安全配置
 * 用来配置Security的Bean，且Spring Security必须配置在
 * 实现了WebSecurityConfigurer或
 * 继承了WebSecurityConfigurerAdapter中
 * @author wq
 * repository：2016-12-12
 *
 */
@Configuration
/**
 * 启用安全配置，配置了一个参数解析器，可获取认证用户和添加跨站请求伪造 token域
 */
@EnableWebSecurity
@ComponentScan
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	/**
	 * 自定义登陆的Bean
	 * @return
	 */
	@Bean
	public AuthenticationSuccessHandler successHandle()
	{
		return new LoginSuccessHandle();
	}

	@Autowired
	private AuthenticationSuccessHandler successHandle;

	/**
	 * 配置拦截规则，优先于RequestMapping
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
			.formLogin() 			// 启用登陆页
				.loginPage("/login")           //自定义登陆页
				.defaultSuccessUrl("/")   //登陆默认页
//				.successHandler(successHandle)   //成功后的处理类
			.and()
			.rememberMe()
				.tokenValiditySeconds(2419200)
				.key("spitterKey")
			.and()
				.logout()
				.logoutSuccessUrl("/")
				.logoutUrl("/logout")       //logout虚拟url，该url通过security的LogoutFilter拦截请求后操作
			.and()
				.authorizeRequests()
				.antMatchers("/spitter/register").hasRole("User")
				.antMatchers(HttpMethod.POST, "/spittles").authenticated()
//				.anyRequest().permitAll()
			.and()
				.csrf().disable();        //关闭csrf，因为csrf不支持ajax post提交
		// .and()
		// .requiresChannel() //启用安全通道，未成功
		// .antMatchers("/spitter/register").requiresSecure();
		// .antMatchers("/").requiresInsecure(); //首页不要安全通道
	}

	//2.自定义数据库查询用户及用户权限
	//	@Autowired
	//	private DataSource dataSource;

	//3.自定义服务查询
	//	@Autowired
	//	private SpitterRepository spitterRepository;
	/**
	 * 配置用户存储的服务
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		//1.基于内存存储
		auth.inMemoryAuthentication()
		.withUser("user").password("1").roles("User").and()    //匹配有优先顺序
		.withUser("admin").password("1").authorities("ROLE_User","ROLE_Admin");

		//2.自定义数据库查询用户及用户权限
//		auth.jdbcAuthentication()
//		.dataSource(dataSource)
//		.usersByUsernameQuery("select username,`password`,true from user where username=?")
//		.authoritiesByUsernameQuery("select u.username,auth.authority from authorities auth inner join `user` u on auth.pk_user = u.pk_user where u.username=?");
//		.passwordEncoder(new StandardPasswordEncoder("1"));   密码转码

		//3.自定义服务查询
//		auth.userDetailsService(new SpitterUserService(spitterRepository));

	}

	/**
	 *
	 * @param web
	 * @throws Exception
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

}
