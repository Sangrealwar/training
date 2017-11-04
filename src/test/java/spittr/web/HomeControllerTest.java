package spittr.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spittr.config.RootConfig;
import spittr.controller.HomeController;

/**
 * 单元测试，用来HomeController
 *
 * @author wq
 * repository：2016-11-19
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)     //测试需要运行在junit运行期中
@ContextConfiguration(classes={RootConfig.class})   //上下文配置
public class HomeControllerTest {
	/**
	 * 测试针对"/"的请求
	 * @throws Exception
	 */
	@Test
	public void testHomePage() throws Exception
	{
//		HomeController controller = new HomeController();
//
//		//搭建MockMvc
//		MockMvc mockMvc = standaloneSetup(controller).build();
//
//		//执行get请求，匹配"/"的请求，期望返回的视图名为Home
//		mockMvc.perform(get("/")).andExpect(view().name("home"));
//		mockMvc.perform(get("/homepage")).andExpect(view().name("home"));
	}
}
