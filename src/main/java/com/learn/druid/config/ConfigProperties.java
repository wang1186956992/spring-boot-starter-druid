package com.learn.druid.config;

import com.learn.druid.lambdaInterface.Call;

/**
 * Created by yf003 on 2017/8/17.
 */
public interface ConfigProperties {

    <T>void setDataProperties(T val, Call<T> call);



}
