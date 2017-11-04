package spittr.config;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import spittr.service.task.HelloJob;

/**
 * 功能：Quartz框架配置
 * 条件：
 * Created by wq on 2017/5/4.
 */
//@Configuration
public class QuartzConfig {

        /**
     * 任务调度器 FactoryBean 默认注入的是getObject()返回的Bean
     * @param simpleTrigger
     * @return
     */
    @Bean
    public SchedulerFactoryBean schedule(SimpleTrigger simpleTrigger)
    {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setTriggers(simpleTrigger);
        return bean;
    }

    /**
     * 简单的任务触发器FactoryBean
     * @param exampleJob
     * @return
     */
    @Bean
	public SimpleTriggerFactoryBean simpleTrigger(JobDetail exampleJob)
	{
        SimpleTriggerFactoryBean bean = new SimpleTriggerFactoryBean();
		bean.setJobDetail(exampleJob);
        bean.setStartDelay(10000);    //延迟10秒后执行
        bean.setRepeatInterval(5000);   //5秒的重复间隔
        return bean;
	}

    /**
     * JobDetail FactoryBean 传递了过期时间和job定义
     * @return
     */
	@Bean
	public JobDetailFactoryBean exampleJob()
	{
		JobDetailFactoryBean bean = new JobDetailFactoryBean();
		bean.setJobClass(HelloJob.class);
		JobDataMap map = new JobDataMap();
		map.put("timeout",5);
		bean.setJobDataMap(map);
		return bean;
	}
}
