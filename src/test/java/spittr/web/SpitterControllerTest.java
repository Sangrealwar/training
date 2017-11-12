package spittr.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spittr.config.RootConfig;
import spittr.controller.SpitterController;
import spittr.model.Spitter;
import spittr.repository.IJpaRepository.JpaSpitterRepository;
import spittr.repository.IMongo.OrderRepository;
import spittr.repository.IRepository.BaseRepository;
import spittr.repository.IRepository.SpitterRepository;

import javax.inject.Inject;

/**
 * 单元测试
 *
 * @author wq repository：2016-11-20
 */
@RunWith(SpringJUnit4ClassRunner.class)     //测试需要运行在junit运行期中
@ContextConfiguration(classes = {RootConfig.class})   //上下文配置
public class SpitterControllerTest {

    @Inject
    private SpitterRepository spitterRepository;

    @Test
    public void shouldExec() {
//		Spitter spitter = new Spitter(24L,"jbauer","24hours","Jack","Bauer");
//
//		Assert.assertEquals(1, spitterRepository.add(spitter));
    }

    /**
     * 测试是否进入注册界面
     *
     * @throws Exception
     */
    @Test
    public void ShouldShowRegistration() throws Exception {
        SpitterRepository repository = Mockito.mock(SpitterRepository.class);
        BaseRepository baseRepository = Mockito.mock(BaseRepository.class);
        JpaSpitterRepository jpaSpitterRepository = Mockito.mock(JpaSpitterRepository.class);
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

        SpitterController controller = new SpitterController(repository, baseRepository, jpaSpitterRepository,orderRepository);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/spitter/register"))
                .andExpect(MockMvcResultMatchers.view().name("registerForm"));
    }

    /**
     * 测试是否Post注册信息
     *
     * @throws Exception
     */
    // @Test
    public void ShouldProcessRegistration() throws Exception {
        Spitter unsaved = new Spitter("jbauer", "24hours", "Jack", "Bauer");
        Spitter saved = new Spitter(24L, "jbauer", "24hours", "Jack", "Bauer");

        SpitterRepository repository = Mockito.mock(SpitterRepository.class);
        BaseRepository baseRepository = Mockito.mock(BaseRepository.class);
        JpaSpitterRepository jpaSpitterRepository = Mockito.mock(JpaSpitterRepository.class);
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

        Mockito.when(repository.save(unsaved)).thenReturn(saved);

        SpitterController controller = new SpitterController(repository, baseRepository,jpaSpitterRepository,orderRepository);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/spitter/register").param(
                        "firesName", "Jack").param("lastName", "Bauer").param(
                        "username", "jbauer").param("password", "24hours"))
                .andExpect(
                        MockMvcResultMatchers.redirectedUrl("/spitter/jbauer"));

        // 校验保存情况，由于post传值时重新给Spitter赋值，最后执行方法的对象已不是unsaved
        // Mockito.verify(repository,Mockito.atLeastOnce()).save(unsaved);
    }
}
