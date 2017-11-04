package spittr.web;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import spittr.config.RootConfig;
import spittr.controller.SpittleController;
import spittr.repository.IRepository.SpittleRepository;
import spittr.model.Spittle;

/**
 * 单元测试，用来HomeController

 *
 * @author wq
 * repository：2016-11-19
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)     //测试需要运行在junit运行期中
@ContextConfiguration(classes={RootConfig.class})   //上下文配置
public class SpittleControllerTest {
	
	
	/**
	 * 
	 * @throws Exception 
	 * 
	 */
//	@Test
	public void shouldShowRecentSpittles() throws Exception
	{
		List<Spittle> expectedSpittles = createSpittleList(50);
		
		//模拟一个信息 仓库
		SpittleRepository mockRepository = mock(SpittleRepository.class);
		
		//定义一个规则
		when(mockRepository.findSpittles(1,20))
		.thenReturn(expectedSpittles); 

		//模拟一个mvc控制器，在setSingleView中设置了视图名
		SpittleController controller =new SpittleController(mockRepository);		
		MockMvc mockMvc = standaloneSetup(controller)
		.setSingleView(
				new InternalResourceView("/WEB-INF/views/spittles.jsp"))
				.build();

		//设置预定返回的结果
		mockMvc.perform(get("/spittles"))
		.andExpect(view().name("spittles"))
		.andExpect(model().attributeExists("spittleList"))
		.andExpect(model().attribute("spittleList",hasItems(expectedSpittles.toArray())));
	}
	
//	@Test
	public void testSpittle() throws Exception{
		Spittle expectedSpittle = new Spittle("Hello",new Date());

		//模拟一个信息 仓库
		SpittleRepository mockRepository = mock(SpittleRepository.class);

		//定义一个规则
		when(mockRepository.findOne(12345)).thenReturn(expectedSpittle); 
		
		//模拟一个mvc控制器，在setSingleView中设置了视图名
		SpittleController controller =new SpittleController(mockRepository);		
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		//设置预定返回的结果
		mockMvc.perform(get("/spittles/12345"))
		.andExpect(view().name("spittle"))
		.andExpect(model().attributeExists("spittle"))
		.andExpect(model().attribute("spittle",expectedSpittle));	
	}
	
	/**
	 * 创建待测试的更新信息数据
	 * @param count
	 * @return
	 */
	private List<Spittle> createSpittleList(int count)
	{
		List<Spittle> spittles =new ArrayList<Spittle>();
		for(int i=0;i<count;i++)
		{
			spittles.add(
					new Spittle("Spittle "+i,new Date()));
		}
		return spittles;
	}
	

	@Inject
	private SpittleRepository repository;
	
	@Test
	public void executeSus()
	{
//		Spittle spittle =  new Spittle("xx123",new Date(),new Double(111),new Double(222));
//
//		Assert.assertNotNull(repository.findSpittles(1,10));
//		long Thread2 = repository.add(spittle);
//		Assert.assertEquals((long)1,Thread2);
	}
}
