package com.beyond.popscience.frame.db.operation.base;

import java.util.List;

/**
 * @author linjinfa@126.com
 * @date 2013-7-26 下午1:49:24
 */
public interface IBaseOperation<T> {

    /**
     * 清空表
     *
     * @return
     */
    boolean clearTable();

    /**
     * 清空表并将自增主键归零
     *
     * @return
     */
    boolean clearTableZero();

    /**
     * 查询此hql总记录数
     *
     * @return
     */
    Long getCount();

    /**
     * 添加/更新对象信息
     *
     */
    boolean addOrUpdateObj(T t);

    /**
     * 批量添加/更新对象信息
     *
     * @param list
     * @return
     */
    boolean addOrUpdateList(List<T> list);

    /**
     * 删除对象
     *
     * @param t
     */
    boolean delete(T t);

    /**
     * 根据主键值获取一个对象
     *
     * @return
     */
    T queryObj(String id);

    /**
     * 查询所有数据
     *
     * @return
     */
    List<T> queryAll();

}
