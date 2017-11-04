package spittr.repository.IJpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import spittr.model.Spitter;

/**
 * 名称：基于jpa接口的抽象接口
 * 功能：
 * 条件：
 * Created by wq on 2017/11/4.
 */
public interface JpaSpitterRepository extends JpaRepository<Spitter, Long> {

    Spitter findByUsername(String username);
}
