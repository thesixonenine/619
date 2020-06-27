/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.github.thesixonenine.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import io.github.thesixonenine.common.es.SkuModel;
import org.apache.http.HttpStatus;

import java.util.*;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public R setData(Object data) {
        if (Objects.isNull(data)) {
            return this;
        }
        this.put("data", data);
        return this;
    }

    public <T> T getData(TypeToken<T> type) {
        Object data = this.get("data");
        if (Objects.isNull(data)) {
            return null;
        }
        Gson gson = new Gson();
        // 默认data是map
        JsonElement tree = gson.toJsonTree(data, type.getType());
        return gson.fromJson(tree, type.getType());
    }

    public int getCode() {
        return (int) this.get("code");
    }

    public static void main(String[] args) {
        List<SkuModel> list = new ArrayList<>();
        SkuModel m1 = new SkuModel();
        m1.setSpuId(1L);
        m1.setSkuId(1L);
        list.add(m1);

        SkuModel m2 = new SkuModel();
        m2.setSpuId(2L);
        m2.setSkuId(2L);
        list.add(m2);

        R r = R.ok();
        r.setData(list);

        List<SkuModel> modelList = r.getData(new TypeToken<List<SkuModel>>() {
        });
        System.out.println(modelList);
    }
}
