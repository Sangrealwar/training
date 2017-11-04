package spittr.config;

import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import spittr.model.Spitter;
import spittr.model.Spittle;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 其他spring的Bean的上下文环境
 *
 * FactoryBean 是包装初始化逻辑较为复杂的Bean，一般常用于集成第三方插件
 * 常用的getBean("mybean")返回的是FactoryBean的getObject()
 * 如果想返回FactoryBean 则需要getBean("&mybean")
 *
 * @author wq
 * repository：2016-11-19
 *
 */
@Configuration
@EnableTransactionManagement         //启用事务
@ComponentScan(basePackages={"spittr"},
		excludeFilters={@Filter(type=FilterType.ANNOTATION,value=EnableWebMvc.class)})
public class RootConfig {

	@Bean
	public DataSource dataSource() throws SQLException {
		DruidDataSource ds = new DruidDataSource();
		ds.setName("myDatasource");
		//连接Mysql 中文转码需要加上characterEncoding;
//		ds.setUrl("jdbc:mysql://192.168.0.125:3306/studydatabase?useUnicode=true&characterEncoding=utf-8");
		ds.setUrl("jdbc:mysql://127.0.0.1:3306/studydatabase?useUnicode=true&characterEncoding=utf-8");
		ds.setUsername("linlin");
		ds.setPassword("1");
//		ds.setDriverClassName("com.mysql.jdbc.Driver");   默认根据url自动识别
		ds.setInitialSize(0);  //初始化时建立物理连接的个数
		ds.setMaxActive(8);   //最大连接池数量
		ds.setMinIdle(0);   //最小连接池数量
		ds.setMaxWait(3000); //获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
		ds.setPoolPreparedStatements(false);   //是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
		ds.setMaxPoolPreparedStatementPerConnectionSize(-1); //要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100

		ds.setMinEvictableIdleTimeMillis(300000); //连接保持空闲而不被驱逐的最长时间

		// 1) Destroy线程会检测连接的间隔时间，如果连接空闲时间大于等于minEvictableIdleTimeMillis则关闭物理连接。
		// 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
		ds.setTimeBetweenEvictionRunsMillis(60000);

		ds.setValidationQuery("select 'x'"); //用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。
		ds.setTestWhileIdle(true);   //申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
		ds.setTestOnBorrow(false);  //申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
		ds.setTestOnReturn(false);  //归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能

		ds.setFilters("stat,log4j"); //配置监控统计拦截的filters 监控统计用的filter:stat  日志用的filter:log4j  防御sql注入的filter:wall

		//配置log4j过滤器
//		List<com.alibaba.druid.filter.Filter> list = new ArrayList<com.alibaba.druid.filter.Filter>();
//		Log4jFilter log4g = new Log4jFilter();
//		log4g.setResultSetLogEnabled(false);
//		log4g.setStatementExecutableSqlLogEnable(true);
//		list.add(log4g);
//		ds.setProxyFilters(list);
 		return ds;
	}


//	/**
//	 * 简单的数据源连接，并不是一个标准的数据连接池
//	 * @return
//	 */
//	@Bean
//	public DataSource dataSource()
//	{
//		DriverManagerDataSource ds = new DriverManagerDataSource();
//		ds.setDriverClassName("com.mysql.jdbc.Driver");
//		//连接Mysql 中文转码需要加上characterEncoding;
////		ds.setUrl("jdbc:mysql://192.168.0.125:3306/studydatabase?useUnicode=true&characterEncoding=utf-8");
//		ds.setUrl("jdbc:mysql://127.0.0.1:3306/studydatabase?useUnicode=true&characterEncoding=utf-8");
//		ds.setUsername("linlin");
//		ds.setPassword("1");
//		return ds;
//	}

	/**
	 * 数据库连接
	 * 1：spring-JdbcTemplate
	 * @param dataSource
	 * @return
	 */
	@Bean
	public NamedParameterJdbcOperations jdcbTemlate(DataSource dataSource)
	{
		return new NamedParameterJdbcTemplate(dataSource);
	}

	/**
	 * 数据库连接：
	 * 2 hibernate4
	 * SessionFactory 支持xml和注解配置实体类映射
	 *
	 * @param dataSource
	 * @return
	 */
	@Bean
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource)
	{
		LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
		sfb.setDataSource(dataSource);
//		sfb.setPackagesToScan("spitter.model");   //注解扫描包
		sfb.setAnnotatedClasses( new Class<?>[]{Spitter.class,Spittle.class});

		Properties props = new Properties();
		//数据库方言  连接mysql
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		sfb.setHibernateProperties(props);
		return sfb;
	}

	/**
	 * 事务管理器，供SessionFactory使用
	 * 在每次SessionFacroty 调用getCurrentSession()返回同一实例
	 * @param sessionFactory
	 * @return
	 */
	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory)
	{
		HibernateTransactionManager mng = new HibernateTransactionManager();
		mng.setSessionFactory(sessionFactory);
		return mng;
	}

	//<editor-fold desc="mybatis连接配置，缺少sqlsessionfactory的bean，报错">
//    /**
//     * 事务管理器- 查看mysql引擎 是否为 InnoDB，否则不能启用事务
//     * @param dataSource
//     * @return
//     */
//	@Bean
//	public DataSourceTransactionManager transactionManager(DataSource dataSource)
//    {
//        DataSourceTransactionManager mng = new DataSourceTransactionManager();
//        mng.setDataSource(dataSource);
//        return mng;
//    }
//
//    @Bean("sqlSessionFactory")
//    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource)
//	{
//		SqlSessionFactoryBean ssf = new SqlSessionFactoryBean();
//		ssf.setDataSource(dataSource);
//
//		int count = 1;
//		Resource[] resourceses = new Resource[count];
//		for(int i=0;i<count;i++)
//		{
//			ClassPathResource classPathResource= new ClassPathResource("/spittr/repository/mapping/spitterMapping.xml");
//			resourceses[i] = classPathResource;
//		}
//		ssf.setMapperLocations(resourceses);
//
//		return ssf;
//	}
//
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer()
//	{
//		MapperScannerConfigurer msc = new MapperScannerConfigurer();
//		msc.setBasePackage("spittr.repository.dao");
//		msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
//		return msc;
//	}
	//</editor-fold>

	//<editor-fold desc="远程调用Tomcat服务器运行报错">
//    /**
//	 * Rmi服务实现类
//	 * @return
//	 */
//	@Bean
//	public SpitterService spitterService()
//	{
//		return new SpitterServiceImpl();
//	}


//	/**
//	 * 服务端配置Rmi服务
//	 * @param spitterService
//	 * @return
//	 */
//	@Bean
//	public RmiServiceExporter rmiExporter(SpitterService spitterService)
//	{
//		RmiServiceExporter rmiExporter=new RmiServiceExporter();
//		rmiExporter.setService(spitterService);
//		rmiExporter.setServiceName("SpitterService");
//		rmiExporter.setServiceInterface(SpitterService.class);
//		rmiExporter.setRegistryPort(1099);
//		return rmiExporter;
//	}

//	/**
//	 * 客户端装配Rmi服务
//	 * @return
//	 */
//	@Bean(name = "client")
//	public RmiProxyFactoryBean spitterService_C()
//	{
//		RmiProxyFactoryBean rmiProxy = new RmiProxyFactoryBean();
//		rmiProxy.setServiceUrl("rmi://127.0.0.1:1099/SpitterService");
//		rmiProxy.setServiceInterface(SpitterService.class);
//		return  rmiProxy;
//	}
	//</editor-fold>
}
