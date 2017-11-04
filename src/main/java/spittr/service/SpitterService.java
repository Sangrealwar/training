package spittr.service;

import spittr.model.Spittle;

import java.util.List;

/**
 * function: 远程调用用户的服务接口类
 * condition:
 * Created by wq on 2016/12/27.
 */
public interface SpitterService {
    List<Spittle> getRecentSpittles(int count);
}
