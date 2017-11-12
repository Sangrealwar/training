package spittr.repository.IMongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import spittr.model.mongo.Order;

/**
 * 名称：类似jpa的CURD接口
 * 功能：
 * 条件：
 * Created by wq on 2017/11/12.
 */
public interface OrderRepository extends MongoRepository<Order,String> {
}
