package spittr.repository.IJpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spittr.model.Spitter;

import java.util.List;

/**
 * 名称：基于jpa接口的抽象接口
 * 功能：自定义的接口必须在这个接口内，简单的方法就是继承自定义的接口，spring jpa扫描会扫描待impl结尾的类，
 *           并实现自定义功能
 * 条件：
 * Created by wq on 2017/11/4.
 */
public interface JpaSpitterRepository extends JpaRepository<Spitter, Long>,JpaSpitterCustom  {

    List<Spitter> findByUsernameLike(String username);

    @Query("SELECT s FROM Spitter s where 1=2")
    List<Spitter> findABC();
}
