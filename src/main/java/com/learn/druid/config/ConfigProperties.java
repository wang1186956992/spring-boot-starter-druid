package com.learn.druid.config;

import com.learn.druid.lambdaInterface.Call;

/**
 * Created by yf003 on 2017/8/17.
 */
public interface ConfigProperties {

    <T>void setProperties(T val, Call<T> call);



}
