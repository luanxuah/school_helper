package com.luanxu.cache;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.luanxu.application.SchoolHelperApplication;
import com.luanxu.utils.CommonConstant;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 栾煦 on 2016/10/20.
 */
public class CacheProvider extends ContentProvider{

    // Canonical全名
    public static final String authority = CacheProvider.class.getCanonicalName();

    //表
    public static final String TABLE_LOGIN_MESSAGE = "table_login_message";//登录信息

    public static String sql_login_message = "create table TABLE_LOGIN_MESSAGE (_id integer auto increment, loginName text primary key, loginPassward text)";

    public static Uri LOGIN_MESSAGE_URL = Uri.parse("content://"+ authority + "/" +TABLE_LOGIN_MESSAGE);

    private CacheHelper mHelper;
    private SQLiteDatabase db = null;

    private static final int CONTACT = 0;
    private static UriMatcher uris = null;

    public static class LoginMessage{
        public static final String _ID = "_id";
        public static final String LOGIN_NAME = "loginName";
        public static final String LOGIN_PASSWARD = "loginPassward";
    }

    static {
        uris = new UriMatcher(UriMatcher.NO_MATCH);
        uris.addURI(authority, TABLE_LOGIN_MESSAGE, CONTACT);
    }

    @Override
    public boolean onCreate() {
        return db == null ? false:true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    public String getTableNameFromUri(Uri uri) {
        String currentTab = "";
        String uriStr = uri.toString();
        if (uriStr.contains("/")) {
            currentTab = uriStr.substring(uriStr.lastIndexOf("/") + 1);
        }
        return currentTab;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = -1;
        switch (uris.match(uri)){
            case CONTACT:
                String tableName = getTableNameFromUri(uri);
                if (tableName == null && tableName.length()==0){
                    return uri;
                }
                getOrCreateDb();
                if (db!=null){
                    id = db.insert(tableName, "", contentValues);
                }
                if (id != -1) {
                    uri = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(uri, null);//操作成功发出信号
                }
                break;
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int count = 0;
        switch (uris.match(uri)){
            case CONTACT:
                String tableName = getTableNameFromUri(uri);
                if (tableName == null && tableName.length()==0){
                    return count;
                }
                getOrCreateDb();
                if (db!=null){
                    count = db.delete(tableName, s, strings);
                }
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int count = 0;
        switch (uris.match(uri)){
            case CONTACT:
                String tableName = getTableNameFromUri(uri);
                if (tableName == null && tableName.length()==0){
                    return count;
                }
                getOrCreateDb();
                if (db == null){
                    Log.i("doctorlog", "db为空");
                }
                if (db != null) {
                    count = db.update(tableName, contentValues, s, strings);
                }
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
        }
        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (uris.match(uri)){
            case CONTACT:
                String tableName = getTableNameFromUri(uri);
                if (tableName == null && tableName.length()==0){
                    return cursor;
                }
                getOrCreateDb();
                // c = db.query(TABLE, 返回列 null 全部返回, where条件, 占位符参数, 分组 , 组条件,排序字段);
                if (db != null){
                    cursor = db.query(tableName, null, selection, selectionArgs, null, null, sortOrder);
                }else{
                    return null;
                }
                break;
        }
        return cursor;
    }

    /**
     * 添加事务处理
     * @param operations
     * @return
     * @throws OperationApplicationException
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        getOrCreateDb();
        if (db != null){
            db.beginTransaction();//开始事务  

            try {
                ContentProviderResult[] results = super.applyBatch(operations);
                db.setTransactionSuccessful();//Commit 设置事务标记为successful  
                return results;
            } finally{
                db.endTransaction();//结束事务  
            }
        }else{
            return null;
        }
    }

    /**
     * 批处理
     * @param uri
     * @param values
     * @return
     */
    public int bulkInsert(Uri uri, ContentValues[] values){
        int numValues = 0;
        db.beginTransaction();//开始事务  
        try{
            numValues = values.length;
            for(int i = 0; i < numValues; i++) {
                int count = update(uri ,values[i],null,null);
                if (count < 1){
                    insert(uri,values[i]);
                }
            }
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
        return numValues;
    }

    /**
     * 获取DB的名称（包含路径信息）
     */
        private static String getMyDatabaseName() {
            String DB_NAME = "school_helper.sqlite";
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {//SDCard是否插入
                String dbPath = CommonConstant.CACHE_PATH;
                Log.i("doctorlog", dbPath);
                File dbp = new File(dbPath);
                if (!dbp.exists()) {//确保存放路径存在，否则报异常
                    boolean judge = dbp.mkdirs();
                    if (judge){
                        Log.i("doctorlog", "成功");
                    }else{
                        Log.i("doctorlog", "失败");
                    }
                }
                DB_NAME = dbPath + DB_NAME;
            }
            return DB_NAME;
        }

    /**
     * provider 获取或者创建DB
     */
    public void getOrCreateDb(){
       String db_name = getMyDatabaseName();
        try {
            mHelper = CacheHelper.getInstance(db_name);
            if (db == null /*|| !db.isOpen()*/){
                db = mHelper.getWritableDatabase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class CacheHelper extends SQLiteOpenHelper {
        public static final int DB_VERSION = 1;
        public static CacheHelper instance = null;

        public CacheHelper(Context context, String name) {
            super(context, name, null, DB_VERSION);
        }

        /**
         * 获取CacheHelper 实例
         */
        public static CacheHelper getInstance(String name) {
            if (instance == null) {
                //第一次创建
                synchronized (CacheHelper.class) {
                    instance = new CacheHelper(SchoolHelperApplication.getInstance(), name);
                }
            }
            return instance;
        }

        @Override
        public void onCreate(SQLiteDatabase arg0) {
            arg0.execSQL(sql_login_message);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
