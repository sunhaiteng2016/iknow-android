package com.beyond.popscience.frame.db.operation;


import com.beyond.library.util.DateUtil;
import com.beyond.popscience.frame.db.operation.base.BaseOperation;
import com.beyond.popscience.frame.pojo.SearchHistoryV4;

import java.util.List;

/**
 * Created by linjinfa 331710168@qq.com on 2016/3/7.
 */
public class SearchHistoryV4Operation extends BaseOperation<SearchHistoryV4> {

    /**
     *
     */
    private final int MAX_LIMIT = 30;

    /**
     * 模糊查询	只查看前20条
     * @param con
     * @return
     */
    public List<SearchHistoryV4> queryFuzzyByConType(String con, int type){
        String sql = "select * from "+getTableName()+" where type = "+type+" and content like ? order by createDateTime desc limit " + MAX_LIMIT + " ";
        return queryRawArguments(sql, "%"+(con==null?"":con)+"%");
    }

    /**
     * 模糊查询	只查看前20条
     * @return
     */
    public List<SearchHistoryV4> queryAllLimitByType(int type){
        String sql = "select * from "+getTableName()+" where type = "+type+" order by createDateTime desc limit "+MAX_LIMIT;
        return queryRawArguments(sql);
    }

    /**
     * 不存在时添加搜索记录
     * @param con
     */
    public boolean addIfNoExitsByType(String con, int type){
        if(!isExitsByCon(con, type)){
            addOrUpdateObj(new SearchHistoryV4(con, DateUtil.getNowString(), type));
            return true;
        }else{
            String sql = "update "+getTableName()+" set createDateTime = '"+ DateUtil.getNowString()+"' where content = ? ";
            executeRaw(sql, con==null?"":con);
        }
        return false;
    }

    /**
     * 根据con查询记录是否存在
     * @param con
     * @return
     */
    public boolean isExitsByCon(String con, int type){
        String sql = "select count(searchId) from "+getTableName()+" where content = ? and type = "+type;
        return getCountBySql(sql,con==null?"":con)>0;
    }

    /**
     * 根据type清空数据
     * @return
     */
    public boolean clearByType(int type){
        String sql = "delete from " + getTableName()+" where type = "+type;
        return executeRaw(sql);
    }

}
