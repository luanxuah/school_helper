package com.luanxu.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author: 范建海
 * @createTime: 2016/10/24 10:32
 * @className:  GsonUtil
 * @description: Gson解析相关工具类
 * @changed by:
 */
public class GsonUtil {
    // Gson实体
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS").create();

    @Deprecated
    public static <T> T json2Object(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken.getType());
    }

    @Deprecated
    public static <T> List<T> json2List(String json, TypeToken<List<T>> typeToken){
        return gson.fromJson(json, typeToken.getType());
    }

    /**
     *  JSON串解析成实体Bean
     * @param json  待解析的json串
     * @param cls   对应实体Bean的class类型
     * @param <T>   泛型
     * @return      实体Bean
     */
    public static <T> T json2Object(String json, Class<T> cls) {
        return gson.fromJson(json,cls);
    }

    /**
     * 实体(对象/集合)转换成JSON串
     * @param obj 带转换的对象
     * @return  JSON串
     */
    public static String object2Json(Object obj) {
        return gson.toJson(obj);
    }

    public static String map2Json2(Map<String ,String> map, String token, String pid) {
        if (null == map || map.isEmpty()){
            return "";
        }
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject bodyJsonObject = new JSONObject();
            if (map!=null){
                for (Map.Entry<String,String> entry : map.entrySet()) {
                    Object val = entry.getValue();
                    if (null == val)
                        val = "null";
                    bodyJsonObject.put(entry.getKey(),val.toString());
                }
            }
            jsonObject.put("body", bodyJsonObject);

            JSONObject headJsonObject = new JSONObject();
            if (!TextUtils.isEmpty(token)){
                headJsonObject.put("token", token);
            }
            if (!TextUtils.isEmpty(pid)){
                headJsonObject.put("pid", pid);
            }
            jsonObject.put("head", headJsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        String aesJson = AESUtils.encryptParameters(json);
        String md5Json = MD5Utils.getMD5String(aesJson);

        return aesJson+"."+md5Json;
    }

}
