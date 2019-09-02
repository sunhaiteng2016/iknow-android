package com.beyond.popscience.module.news.task;

import com.beyond.library.network.enity.MSG;
import com.beyond.library.network.task.ITask;
import com.beyond.popscience.frame.db.operation.SearchHistoryV4Operation;
import com.beyond.popscience.frame.pojo.SearchHistoryV4;

import java.util.List;

/**
 * 搜索历史记录
 * Created by linjinfa on 2017/6/9.
 * email 331710168@qq.com
 */
public class LoadHistoryTask extends ITask {

    private SearchHistoryV4Operation searchHistoryV4Operation = new SearchHistoryV4Operation();

    public LoadHistoryTask(int taskId) {
        super(taskId);
    }

    @Override
    public MSG doTask() {
        List<SearchHistoryV4> searchHistoryV4List = searchHistoryV4Operation.queryAllLimitByType(SearchHistoryV4.SearchHistoryType.NEWS_TYPE);
        return new MSG(true, searchHistoryV4List);
    }

}
