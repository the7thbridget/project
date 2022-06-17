package edu.gatech.cs6310.groceryexpress.common;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class R extends HashMap<String, Object> {
    public R() {
        put("code", 0);
        put("msg", "success");
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


    public static R ok(String msg)  {
        R r = new R();
        r.put("msg", msg);
        r.put("code", HttpStatus.SC_OK);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.put("code", HttpStatus.SC_OK);
        r.putAll(map);
        return r;
    }

    public static R ok() {
        R r = new R();
        r.put("code", HttpStatus.SC_OK);
        return r;
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
