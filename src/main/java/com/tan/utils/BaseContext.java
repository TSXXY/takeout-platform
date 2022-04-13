package com.tan.utils;

/**
 * @author TanS
 * @date 2022/4/13
 */

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal();

    public static void set(Long id){
        threadLocal.set(id);
    }

    public static Long get(){
        return threadLocal.get();
    }
}
