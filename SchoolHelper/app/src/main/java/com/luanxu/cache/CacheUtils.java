package com.luanxu.cache;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 栾煦 on 2016/12/4.
 */
public class CacheUtils {
    public static int msg = 11111;
    /**
     * 修改用户信息
     * @param context 上下文对象
     */
    public static void updateUserMsg(Context context){
        ArrayList<ContentProviderOperation> contactOps = new ArrayList<ContentProviderOperation>();
        ContentValues recorder = new ContentValues();
        recorder.put(CacheProvider.LoginMessage.LOGIN_NAME, "luanxu");
        recorder.put(CacheProvider.LoginMessage.LOGIN_PASSWARD, msg+"");
        if (msg == 11111){
            msg = 22222;
        }else{
            msg = 11111;
        }

        if (isExist(context,CacheProvider.LOGIN_MESSAGE_URL,CacheProvider.LoginMessage.LOGIN_NAME,"luanxu")){
            contactOps.add(ContentProviderOperation.newUpdate(CacheProvider.LOGIN_MESSAGE_URL)
                    .withValues(recorder)
                    .withSelection(CacheProvider.LoginMessage.LOGIN_NAME+"=?",new String[]{"luanxu"}).build());
        }else{
            contactOps.add(ContentProviderOperation.newInsert(CacheProvider.LOGIN_MESSAGE_URL).withValues(recorder).build());
        }
        try {
            ContentProviderResult[] resultArr = context.getContentResolver().applyBatch(CacheProvider.authority,contactOps);//处理事务
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户信息
     * @param context
     */
    public static void queryUserMsg(Context context){
        String result = null;
        Cursor cussor = null;
        try {
            cussor = context.getContentResolver().query(CacheProvider.LOGIN_MESSAGE_URL, null, CacheProvider.LoginMessage.LOGIN_NAME + "=?", new String[]{"luanxu"}, null);
            if (cussor != null && cussor.moveToFirst()) {
                result = cussor.getString(cussor.getColumnIndex(CacheProvider.LoginMessage.LOGIN_PASSWARD));
            }
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        } finally {
            if (null != cussor){
                cussor.close();
            }
        }
    }

    /**
     * 判断该数据在数据库表中是否存在
     * @param context 上下文
     * @param uri Uri
     * @param selection 查询的字段
     * @param selectionArg 查询字段对应的值
     * @return true：数据库存在数据 false：不存在
     */
    public static boolean isExist(Context context, Uri uri, String selection, String selectionArg){
        if (TextUtils.isEmpty(selection) || TextUtils.isEmpty(selectionArg)){
            return false;
        }
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, null, selection+"= ?", new String[]{selectionArg}, "");
        } finally {
            if (null != cursor && cursor.getCount() > 0){
                result = true;
                cursor.close();
            }
        }
        return result;
    }
}
