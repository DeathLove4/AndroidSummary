package com.dzw.library.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Death丶Love
 * @date 2021-02-20 11:39 AM
 * @description 封装一些共用的方法
 */
public class Common {

    /**
     * 深拷贝对象
     */
    public static <T> T copyObject(T t) {
        Gson gson = new Gson();
        return (T) gson.fromJson(gson.toJson(t), t.getClass());
    }

    /**
     * 深拷贝对象list
     */
    public static <T> List<T> copyList(List<T> list, Class<T> cls) {
        if (list.size() == 0) {
            return new ArrayList<T>();
        }
        List<T> newList = new ArrayList<>();
        Gson gson = new Gson();
        JsonArray array = JsonParser.parseString(gson.toJson(list)).getAsJsonArray();
        for (JsonElement element : array) {
            newList.add(gson.fromJson(element, cls));
        }
        return newList;
    }
}
