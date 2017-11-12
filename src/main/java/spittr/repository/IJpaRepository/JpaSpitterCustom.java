package spittr.repository.IJpaRepository;

import spittr.model.Spitter;

/**
 * 名称：自定义jpa接口
 * 功能：
 * 条件：
 * Created by wq on 2017/11/12.
 */
public interface JpaSpitterCustom {

    int updateOne(Spitter updateSpitter);
}
