package com.beyond.popscience.frame.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by linjinfa 331710168@qq.com on 2016/3/7.
 */
@DatabaseTable(tableName = "search_history_v4")
public class SearchHistoryV4 implements Serializable {

    /**
     * 搜索类别
     */
    public final class SearchHistoryType{
        /**
         * 所有  不区分模块
         */
        public static final int ALL_TYPE = 100;
        /**
         * 新闻
         */
        public static final int NEWS_TYPE = 101;
    }


    @DatabaseField(generatedId = true)
    private Integer searchId;
    @DatabaseField
    private String content;
    @DatabaseField
    private String createDateTime;
    @DatabaseField
    private int type;

    public SearchHistoryV4() {
    }

    public SearchHistoryV4(String content) {
        this.content = content;
    }

    public SearchHistoryV4(String content, String createDateTime, int type) {
        this.content = content;
        this.createDateTime = createDateTime;
        this.type = type;
    }

    public Integer getSearchId() {
        return searchId;
    }

    public void setSearchId(Integer searchId) {
        this.searchId = searchId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
