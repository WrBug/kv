package com.wrbug.kv.runtime.compileonly;

import java.util.HashMap;
import java.util.Map;

public class ImplManager {
    private static Map<Class, Object> cache = new HashMap<>();

    public static Object get(Class clazz) {
        return null;
    }

    public static Object matchAndGet(Class clazz) {
        return null;
    }
}
