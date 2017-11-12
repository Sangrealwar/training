package spittr.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 名称：Mongo的配置
 * 功能：
 * 条件：
 * Created by wq on 2017/11/12.
 */
@Configuration
@EnableMongoRepositories(basePackages = "spittr.repository.IMongo")
public class MongoConfig {

    @Bean
    public MongoFactoryBean mongo() {
        //用于注入MongoClient的客户端
        MongoFactoryBean mongo = new MongoFactoryBean();
        mongo.setHost("localhost");
        return mongo;
    }

    @Bean
    public MongoOperations mongoTemplate(Mongo mongo) {
        return new MongoTemplate(mongo,"linlin");
    }
}
