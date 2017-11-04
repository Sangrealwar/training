package spittr.repository.MysqlRepository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spittr.repository.IRepository.BaseRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * 功能：仓储基类
 * 条件：
 * Created by wq on 2017/3/1.
 */
@Repository()
@Transactional()
public class BaseRepository4Mysql implements BaseRepository{

    private SessionFactory sessionFactory;

    @Inject
    public BaseRepository4Mysql(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 获得当前的Session对象
     * @return
     */
    private Session currentSession()
    {
        Session session = null;
        try
        {
            //绑定当前线程
            //创建的线程会再事务回滚或事务提交后自动关闭，如果设置的是 HibernateTransactionManager
            session= sessionFactory.getCurrentSession();
        }
        catch(HibernateException e)
        {
            throw e;
            //不推荐，为每一个线程创建session，需要手动处理session
            //session= sessionFactory.openSession();
        }
        return session;
    }

    /**
     * 查询某个模型类的方法
     * @param type
     * @return
     */
    //开启缓存的注解（缓存的名称；缓存的key，默认以方法加方法参数；是否缓存的条件，可用SpEL）
//    @Cacheable(value = "baseEntity")
    public List find(Class type, int page, int pageSize)
    {
        return currentSession().createCriteria(type)
                .setFirstResult((page-1))
                .setMaxResults((page)*pageSize).list();
    }
}


