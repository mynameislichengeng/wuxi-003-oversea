package com.lc.baseui.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工具
 * Created by Administrator on 2017/4/14.
 */

public class ExecutorUtil {
    private static ExecutorUtil instance;
    public static ExecutorUtil getInstance(){
        if(instance==null){
            instance = new ExecutorUtil();
        }
        return instance;
    }
    private ExecutorService fixedThreadPool;
    public ExecutorService getExecutor(){
        if(fixedThreadPool==null){
            fixedThreadPool = Executors.newFixedThreadPool(3);
        }
        return fixedThreadPool;
    }
}
