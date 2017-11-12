package spittr.model.mongo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Collection;
import java.util.LinkedList;

/**
 * 名称：Mongod使用的订单域对象
 * 功能：
 * 条件：
 * Created by wq on 2017/11/12.
 */
@Document    //和Entity的注解一样
public class Order {

    @Id
    private String id;

    @Field("client")
    private String customer;

    private String type;

    private Collection<Item> items = new LinkedList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<Item> getItems() {
        return items;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }
}
