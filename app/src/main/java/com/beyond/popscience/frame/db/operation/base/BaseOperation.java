package com.beyond.popscience.frame.db.operation.base;

import android.database.SQLException;
import android.text.TextUtils;

import com.beyond.library.util.DateUtil;
import com.beyond.library.util.InvokeUtil;
import com.beyond.library.util.L;
import com.beyond.popscience.frame.application.BeyondApplication;
import com.beyond.popscience.frame.db.DBHelper;
import com.beyond.popscience.frame.util.VKConstants;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author linjinfa@126.com
 * @date 2013-7-26 下午1:51:53
 */
public class BaseOperation<T> implements IBaseOperation<T> {

    /**
     * 写锁
     */
    protected final static byte[] _writeLock = new byte[0];

    protected static DBHelper dbHelper;
    /**
     * 主键为String的Dao
     */
    protected Dao<T, String> dao;
    /**
     * 主键为int的Dao
     */
    protected Dao<T, Integer> daoInt;
    /**
     * 实体类的Class
     */
    private Class<T> entityClass;
    /**
     * 表名
     */
    protected String tableName;

    public BaseOperation() {
        super();
        initDao();
    }

    /**
     * 初始化DBHelper
     */
    private synchronized void initDBHelper(){
        if(dbHelper==null){
            synchronized (DBHelper.class){
                if(dbHelper==null){
                    dbHelper = OpenHelperManager.getHelper(BeyondApplication.getInstance(), DBHelper.class);
                    dbHelper.getWritableDatabase().enableWriteAheadLogging();
                }
            }
        }
    }

    /**
     * 初始化Dao
     */
    protected void initDao() {
        try {
            dbHelper = OpenHelperManager.getHelper(BeyondApplication.getInstance(), DBHelper.class);
//            initDBHelper();
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            entityClass = (Class<T>) params[0];
            dao = (Dao<T, String>) dbHelper.getDao(entityClass);
            daoInt = (Dao<T, Integer>) dbHelper.getDao(entityClass);
            if(daoInt!=null && !daoInt.isTableExists()){
                TableUtils.createTableIfNotExists(dbHelper.getConnectionSource(), entityClass);
            }
            DatabaseTable databaseTable = entityClass.getAnnotation(DatabaseTable.class);
            tableName = databaseTable.tableName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果null 重新初始化
     */
    private void initDaoIfNull(){
        if(dao==null || daoInt==null || tableName==null){
            initDao();
        }
    }

    @Override
    public boolean clearTable() {
        if (TextUtils.isEmpty(tableName))
            return false;
        try {
            initDaoIfNull();
            dao.executeRaw("delete from " + tableName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean clearTableZero() {
        if (TextUtils.isEmpty(tableName))
            return false;
        try {
            initDaoIfNull();
            dao.executeRaw("delete from " + tableName);
            dao.executeRaw("update sqlite_sequence set seq=0 where name = '" + tableName + "'");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long getCount() {
        try {
            initDaoIfNull();
            QueryBuilder<T, String> qb = dao.queryBuilder();
            qb.setCountOf(true);
            return dao.countOf(qb.prepare());
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 根据Sql统计
     *
     * @param sql
     * @return
     */
    protected Long getCountBySql(String sql) {
        L.vSql("getCountBySql========> "+sql);
        try {
            initDaoIfNull();
            String countStr = daoInt.queryRaw(sql).getResults().get(0)[0];
            if(countStr==null){
                return 0L;
            }
            return Long.parseLong(countStr);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 根据Sql统计
     *
     * @param sql
     * @return
     */
    protected Long getCountBySql(String sql, String... arguments) {
        L.vSql("getCountBySql=========>  "+sql+"  "+arguments);
        try {
            initDaoIfNull();
            String countStr = daoInt.queryRaw(sql,arguments).getResults().get(0)[0];
            if(countStr==null){
                return 0L;
            }
            return Long.parseLong(countStr);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public synchronized boolean addOrUpdateObj(T t) {
        initDaoIfNull();
        synchronized (_writeLock){
            try {
                dao.createOrUpdate(t);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public boolean addOrUpdateList(List<T> list) {
        if (list == null)
            return false;
        initDaoIfNull();
        synchronized (_writeLock) {
            try {
                dbHelper.getWritableDatabase().beginTransaction();
                for (T t : list) {
                    daoInt.createOrUpdate(t);
                }
                dbHelper.getWritableDatabase().setTransactionSuccessful();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                dbHelper.getWritableDatabase().endTransaction();
            }
        }
    }

    @Override
    public boolean delete(T t) {
        initDaoIfNull();
        try {
            synchronized (_writeLock){
                dao.delete(t);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public T queryObj(String id) {
        initDaoIfNull();
        try {
            return dao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> queryAll() {
        initDaoIfNull();
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据Sql查询
     *
     * @param sql
     * @return
     */
    protected List<T> queryRaw(String sql) {
        return queryRawArguments(sql);
    }

    /**
     * 根据Sql查询
     * @param sql
     * @return
     */
    protected List<T> queryRaw(String sql, List<String> argumentList) {
        try {
            String arguments[] = null;
            if(argumentList!=null){
                arguments = new String[argumentList.size()];
                for(int i=0;i<argumentList.size();i++){
                    arguments[i] = argumentList.get(i);
                }
            }
            return queryRawArguments(sql, arguments);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据预编译的Sql查询
     * @param sql
     * @return
     */
    protected List<T> queryRawArguments(String sql, String... arguments) {
        try {
            GenericRawResults<T> genericRawResults = queryRaw(sql, arguments);
            if(genericRawResults!=null){
                return genericRawResults.getResults();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询一条数据
     * @param sql
     * @param arguments
     * @return
     */
    protected T queryObjArguments(String sql, String... arguments){
        try {
            GenericRawResults<T> genericRawResults = queryRaw(sql, arguments);
            if(genericRawResults!=null){
                return genericRawResults.getFirstResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询
     * @param sql
     * @param arguments
     * @return
     */
    private GenericRawResults<T> queryRaw(String sql, String... arguments){
        L.vSql("queryRaw=======> "+sql);
        try {
            if (sql == null || entityClass == null)
                return null;
            initDaoIfNull();

            RawRowMapper<T> rawRowMapper = new RawRowMapper<T>() {
                @Override
                public T mapRow(String[] columns, String[] results) throws SQLException {
                    T t = null;
                    try {
                        t = entityClass.newInstance();
                        for (int i = 0; i < columns.length; i++) {
                            InvokeUtil.setFieldValue(t, columns[i], results[i]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return t;
                }
            };
            return getDaoInt().queryRaw(sql, rawRowMapper, arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据预编译的Sql查询 一个字段 集合
     * @param sql
     * @return
     */
    protected List<String> queryRawArgumentsOneField(String sql, String... arguments) {
        L.vSql("queryRawArgumentsOneField========> "+sql+"  "+arguments);
        if (sql == null)
            return null;
        initDaoIfNull();
        try {
            RawRowMapper<String> rawRowMapper = new RawRowMapper<String>() {
                @Override
                public String mapRow(String[] columns, String[] results) throws SQLException {
                    if(results!=null && results.length!=0){
                        return results[0];
                    }
                    return "";
                }
            };
            return dao.queryRaw(sql, rawRowMapper, arguments).getResults();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行预编译的sql
     * @param statementSql
     * @param argumentList
     * @return
     */
    public boolean executeRaw(String statementSql, List<String> argumentList){
        L.vSql("executeRaw========> "+statementSql+"  "+argumentList);
        initDaoIfNull();
        synchronized (_writeLock){
            try {
                String arguments[] = null;
                if(argumentList!=null){
                    arguments = new String[argumentList.size()];
                    for(int i=0;i<argumentList.size();i++){
                        arguments[i] = argumentList.get(i);
                    }
                }
                dao.executeRaw(statementSql, arguments);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 执行预编译的sql
     * @param statementSql
     * @return
     */
    public boolean executeRaw(String statementSql, String... arguments){
        L.vSql("executeRaw=======>  "+statementSql+"  "+arguments);
        initDaoIfNull();
        synchronized (_writeLock){
            try {
                dao.executeRaw(statementSql, arguments);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 反射
     * @param columns
     * @param results
     * @param obj
     */
    protected void invokeObj(String[] columns, String[] results, Object obj){
        String methodName;
        for (int i = 0; i < columns.length; i++) {
//            methodName = "set" + (columns[i].charAt(0) + "").toUpperCase() + columns[i].substring(1, columns[i].length());
//            if ("id".equalsIgnoreCase(columns[i])) {
//                int id = 0;
//                try {
//                    id = Integer.parseInt(results[i]);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                InvokeUtil.setIntegerValue(obj, methodName, id);
//            } else {
//                InvokeUtil.setStringValue(obj, methodName, results[i]);
//            }
            InvokeUtil.setFieldValue(obj, columns[i], results[i]);
        }
    }

    /**
     * 缓存是否过期 true：过期 false：未过期
     *
     * @return
     */
    protected boolean isExpired(String where) {
        try {
            initDaoIfNull();
            String sql = "select cache_date_time from " + tableName + " %s limit 0,1";
            if (TextUtils.isEmpty(where)) {
                where = "";
            }
            sql = String.format(sql, where);
            List<String[]> resultList = dao.queryRaw(sql).getResults();
            if (resultList != null && resultList.size() != 0) {
                String results[] = resultList.get(0);
                if (results != null && results.length != 0) {
                    String cacheDateTime = results[0];
                    if (!TextUtils.isEmpty(cacheDateTime)) {
                        long times = Math.abs(DateUtil.getTimesBetween(cacheDateTime, DateUtil.getNowString()));
                        return times >= VKConstants.CACHE_EXPIRED_SECONDS;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 缓存是否过期 true：过期 false：未过期
     * @return
     */
    public boolean isExpired() {
        return isExpired("");
    }

    /**
     *
     * @return
     */
    protected Dao<T, Integer> getDaoInt() {
        initDaoIfNull();
        return daoInt;
    }

    /**
     *
     * @return
     */
    protected Dao<T, String> getDao() {
        initDaoIfNull();
        return dao;
    }

    /**
     *
     * @return
     */
    protected String getTableName() {
        initDaoIfNull();
        return tableName;
    }

}
