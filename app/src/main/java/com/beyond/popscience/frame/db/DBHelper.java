package com.beyond.popscience.frame.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;

import com.beyond.library.util.L;
import com.beyond.popscience.frame.pojo.SearchHistoryV4;
import com.beyond.popscience.frame.util.FileUtil;
import com.beyond.popscience.frame.util.VKConstants;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * DBHelper
 *
 * @author linjinfa@126.com
 * @date 2013-4-22 下午1:18:08
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "pop_science";
    /**
     *
     */
    private static final int DATABASE_VERSION_1 = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION_1);
        getWritableDatabase().enableWriteAheadLogging();
        initTable(getWritableDatabase());
    }

    /**
     * 初始化表
     */
    private void initTable(SQLiteDatabase sqLiteDatabase) {
        try {
            createTableIfNotExists(sqLiteDatabase, SearchHistoryV4.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换敏感字符
     *
     * @param str
     * @return
     */
    public static String replaceSensitiveChar(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        return str.replace("'", "\"").replace("", "");
    }

    /**
     * 复制数据库到手机指定文件夹下
     *
     * @throws IOException
     */
    public static void copyDataBase() {
//        try {
//            File file = new File(DB_PATH);
//            file.mkdirs();
//            File dbFile = new File(file, DB_NAME);
//            if (dbFile.exists()) {
//                return;
//            }
//            FileOutputStream os = new FileOutputStream(dbFile);
//            InputStream is = VkerApplication_.getInstance().getResources().openRawResource(R.raw.ic_launcher);
//            byte[] buffer = new byte[1024];
//            int count = -1;
//            while ((count = is.read(buffer)) != -1) {
//                os.write(buffer, 0, count);
//                os.flush();
//            }
//            is.close();
//            os.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 复制数据库到SDCard
     */
    public static void copyDataBaseToSDCard(Context context) {
        try {
            String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + context.getPackageName() + "/databases/";
            File dbFile = new File(new File(DB_PATH), DB_NAME);
            FileInputStream fileInputStream = new FileInputStream(dbFile);
            FileUtil.writeFile(new File(FileUtil.getDirectory(VKConstants.CACHE_ROOT), DB_NAME).getAbsolutePath(), fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查表中某列是否存在
     *
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    private boolean checkColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(" select * from sqlite_master where name = ? and sql like ? ", new String[]{tableName, "%" + columnName + "%"});
            result = (cursor != null && cursor.moveToFirst());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 检查表是否存在
     *
     * @param tableName 表名
     * @return
     */
    private boolean checkTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) as tableCount from sqlite_master where type='table' and name= ? ", new String[]{tableName});
            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(cursor.getColumnIndex("tableCount"));
                return count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return false;
    }

    /**
     * 表不存在时 创建表
     *
     * @param sqLiteDatabase
     * @param dataClass
     * @throws Exception
     */
    public void createTableIfNotExists(SQLiteDatabase sqLiteDatabase, Class<?> dataClass) throws Exception {
        DatabaseTable databaseTable = dataClass.getAnnotation(DatabaseTable.class);
        if (databaseTable != null && !checkTableExists(sqLiteDatabase, databaseTable.tableName())) {
            TableUtils.createTableIfNotExists(getConnectionSource(), dataClass);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource arg1, int oldVersion, int newVersion) {
        L.v("DBHelper=================> oldVersion:" + oldVersion);
        L.v("DBHelper=================> newVersion:" + newVersion);
        initTable(sqLiteDatabase);
    }

}
