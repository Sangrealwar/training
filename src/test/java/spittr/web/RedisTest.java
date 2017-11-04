package spittr.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spittr.config.RedisCacheConfig;
import spittr.config.RootConfig;
import spittr.repository.IRepository.BaseRepository;

/**
 * 功能：
 * 条件：
 * Created by wq on 2017/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)     //测试需要运行在junit运行期中
@ContextConfiguration(classes={RootConfig.class, RedisCacheConfig.class})   //上下文配置
public class RedisTest {

//    @Autowired()
//    private RedisTemplate<String,String> template;
    @Autowired()
    private BaseRepository baseRepository ;

//    @Resource(name="redisTemplate")
//    private ListOperations<String, String> listOps;

    @Test
    public void test()
    {

//        List Thread2 = baseRepository.find(Spitter2.class,2,1);
//        List b = baseRepository.find(Spitter2.class,2,1);


//        String userId= "12";
//        URL url = null;
//        try {
//            url = new URL("127.0.0.1");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        listOps.leftPush(userId, url.toExternalForm());
//        template.boundListOps(userId).leftPush(url.toExternalForm());
    }
}
