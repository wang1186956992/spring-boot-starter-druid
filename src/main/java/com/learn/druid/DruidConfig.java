package com.learn.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by xuejike on 2017/6/22.
 */
@Configuration
@ConfigurationProperties
@ConditionalOnClass(DruidDataSource.class)
@EnableConfigurationProperties(DruidProperties.class)
public class DruidConfig {

    protected static Logger logger= LoggerFactory.getLogger(DruidConfig.class);


    @Autowired
    DruidProperties properties;


    @Bean
    @ConditionalOnProperty(prefix = "bd.datasource.druid",value = "enabled",havingValue = "true")
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        if (properties.getName()!=null){
            dataSource.setName(properties.getName());
        }

        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        setNotNull(properties.getMaxActive(), new Call<Integer>() {
            @Override
            public void call(Integer integer) {
                dataSource.setMaxActive(integer);
            }
        });
        setNotNull(properties.getMinIdle(), dataSource::setMinIdle);
        setNotNull(properties.getMaxWait(), dataSource::setMaxWait);
        setNotNull(properties.getPoolPreparedStatements(), dataSource::setPoolPreparedStatements);
        setNotNull(properties.getMaxPoolPreparedStatementPerConnectionSize(), dataSource::setMaxPoolPreparedStatementPerConnectionSize);
        setNotNull(properties.getValidationQuery(), dataSource::setValidationQuery);

        setNotNull(properties.getValidationQueryTimeout(), dataSource::setValidationQueryTimeout);
        setNotNull(properties.getTestOnBorrow(), dataSource::setTestOnBorrow);
        setNotNull(properties.getTestOnReturn(), dataSource::setTestOnReturn);
        setNotNull(properties.getTestWhileIdle(), dataSource::setTestWhileIdle);



        if (properties.getTimeBetweenEvictionRunsMillis()!=null){
            dataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        }
        if (properties.getMinEvictableIdleTimeMillis()!=null){
            dataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        }
        if (properties.getConnectionInitSqls()!=null){
            dataSource.setConnectionInitSqls(properties.getConnectionInitSqls());
        }
        if (properties.getFilters()!=null){
            try {
                dataSource.setFilters(properties.getFilters());
            } catch (SQLException e) {
//                e.printStackTrace();
                logger.error("数据库连接池Filters设置失败",e);
            }
        }
        if(properties.getProxyFilters()!=null){
            dataSource.setProxyFilters(properties.getProxyFilters());
        }

        Properties properties = new Properties();
        properties.put("druid.stat.mergeSql",true);
        properties.put("druid.stat.slowSqlMillis",3000);
        dataSource.setConnectProperties(properties);

        setNotNull(this.properties.getConnectionProperties(), dataSource::setConnectProperties);
//        druidDataSource.setConnectProperties();

        return dataSource;
    }


    @Bean
    @ConditionalOnProperty(prefix = "bd.datasource.druid",value = "monitor-enable",havingValue = "true")
    public ServletRegistrationBean druidStatViewServlet(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet()
                ,properties.getMonitorPath());


        setNotNull(properties.getMonitorAllow(),v->servletRegistrationBean.addInitParameter("allow",v));


        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        setNotNull(properties.getMonitorDeny(),v->servletRegistrationBean.addInitParameter("deny",v));


        //登录查看信息的账号密码.

        servletRegistrationBean.addInitParameter("loginUsername",properties.getMonitorUserName());

        servletRegistrationBean.addInitParameter("loginPassword",properties.getMonitorPassword());

        //是否能够重置数据.

        servletRegistrationBean.addInitParameter("resetEnable", String.valueOf(properties.getMonitorResetEnable()));

        return servletRegistrationBean;
    }


    /**

     * 注册一个：filterRegistrationBean

     * @return

     */

    @Bean
    @ConditionalOnProperty(prefix = "bd.datasource.druid",value = "monitor-enable",havingValue = "true")
    public FilterRegistrationBean druidStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        //添加过滤规则.

        filterRegistrationBean.addUrlPatterns(properties.getMonitorFilterUrl());

        //添加不需要忽略的格式信息.

        filterRegistrationBean.addInitParameter("exclusions",properties.getMonitorFilterExclusions());

        return filterRegistrationBean;

    }

    private <T>void setNotNull(T val,Call<T> call){
        if (val!=null){
            call.call(val);
        }
    }
    interface Call<T>{
        void call(T t);
    }
}
