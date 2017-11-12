package spittr.repository.IJpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spittr.model.Spitter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/11/12.
 */
@Repository
@Transactional
public class JpaSpitterRepositoryImpl implements JpaSpitterCustom {

    /**
     * 因为EntityManager不是线程安全的，所以该注解注入一个EntityManager的代理，这个注解不是spring注解，需要注入
     */
    @PersistenceContext
    private EntityManager em;

    @Override
    public int updateOne(Spitter updateSpitter) {
        String update="update Spitter set username='xiaozhu' where id=25";
        return em.createQuery(update).executeUpdate();
    }
}
