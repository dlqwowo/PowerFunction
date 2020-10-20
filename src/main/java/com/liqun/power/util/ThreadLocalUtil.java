package com.liqun.power.util;

import java.util.*;

/**
 * @Author: PowerQun
 */
public class ThreadLocalUtil<T> {

    private static final ThreadLocal<Map<String, Object>> threadlocal = new ThreadLocal() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>(4);
        }
    };

    public static <T> T get(String key) {
        return (T) threadlocal.get().get(key);
    }

    public static <T> T get(String key, T defaultValue) {
        Map map = threadlocal.get();
        return map.get(key) == null ? defaultValue : (T) map.get(key);
    }

    public static void set(String key, Object value) {
        Map map = threadlocal.get();
        map.put(key, value);
    }

    public static void set(Map<String, Object> keyValueMap) {
        Map map = threadlocal.get();
        map.putAll(keyValueMap);
    }

    public static void remove() {
        threadlocal.remove();
    }

    public static void remove(String key) {
        Map map = threadlocal.get();
        map.remove(key);
    }

    public static <T> Map<String, T> fetchVarsByPrefix(String prefix) {
        Map<String, T> vars = new HashMap<>();
        if (prefix == null) {
            return vars;
        }
        Map map = (Map) threadlocal.get();
        Set<Map.Entry> set = map.entrySet();

        for (Map.Entry entry : set) {
            Object key = entry.getKey();
            if (key instanceof String) {
                if (((String) key).startsWith(prefix)) {
                    vars.put((String) key, (T) entry.getValue());
                }
            }
        }
        return vars;
    }

    public static void clear(String prefix) {
        if (prefix == null) {
            return;
        }
        Map map = (Map) threadlocal.get();
        Set<Map.Entry> set = map.entrySet();
        List<String> removeKeys = new ArrayList<>();

        for (Map.Entry entry : set) {
            Object key = entry.getKey();
            if (key instanceof String) {
                if (((String) key).startsWith(prefix)) {
                    removeKeys.add((String) key);
                }
            }
        }
        for (String key : removeKeys) {
            map.remove(key);
        }
    }


}
