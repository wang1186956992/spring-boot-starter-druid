package com.learn.druid.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.learn.druid.annotation.DruidAutoConfig;
import com.learn.druid.lambdaInterface.Call;
import com.learn.druid.properties.DruidProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by yf003 on 2017/8/17.
 */

@DruidAutoConfig
public class DruidConfig implements ConfigProperties {

    protected static Logger logger= LoggerFactory.getLogger(DruidConfig.class);


    @Autowired
    DruidProperties properties;

    @Bean
    @ConditionalOnProperty(prefix = "bd.datasource.druid",value = "enabled",havingValue = "true")
    public DataSource dataSource(){
        logger.info("init Druid DruidDataSource Configuration ");
        DruidDataSource dataSource = new DruidDataSource();

        setProperties(properties.getName(),dataSource::setName);
        setProperties(properties.getUrl(),dataSource::setUrl);
        setProperties(properties.getUsername(),dataSource::setUsername);
        setProperties(properties.getPassword(),dataSource::setPassword);
        setProperties(properties.getMaxActive(),dataSource::setMaxActive);
        setProperties(properties.getMinIdle(), dataSource::setMinIdle);
        setProperties(properties.getMaxWait(), dataSource::setMaxWait);
        setProperties(properties.getPoolPreparedStatements(), dataSource::setPoolPreparedStatements);
        setProperties(properties.getMaxPoolPreparedStatementPerConnectionSize(), dataSource::setMaxPoolPreparedStatementPerConnectionSize);
        setProperties(properties.getValidationQuery(), dataSource::setValidationQuery);
        setProperties(properties.getValidationQueryTimeout(), dataSource::setValidationQueryTimeout);
        setProperties(properties.getTestOnBorrow(), dataSource::setTestOnBorrow);
        setProperties(properties.getTestOnReturn(), dataSource::setTestOnReturn);
        setProperties(properties.getTestWhileIdle(), dataSource::setTestWhileIdle);

        //设置数据源特殊属性
        dataSource = setDataSourceSpecialProperties(dataSource);

        createConnectProperties();

        setProperties(properties.getConnectionProperties(), dataSource::setConnectProperties);

        return dataSource;
    }


    @Bean
    @ConditionalOnProperty(prefix = "bd.datasource.druid",value = "monitor-enable",havingValue = "true")
    public ServletRegistrationBean druidStatViewServlet(){
        logger.info("init Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet()
                ,properties.getMonitorPath());

        setProperties(properties.getMonitorPath(),servletRegistrationBean::addUrlMappings);

        //白名单（允许访问IP）
        setProperties(properties.getMonitorAllow(),v->servletRegistrationBean.addInitParameter("allow",v));

        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        setProperties(properties.getMonitorDeny(),v->servletRegistrationBean.addInitParameter("deny",v));


        //登录查看信息的账号密码.

        setProperties(properties.getMonitorUserName(),v->servletRegistrationBean.addInitParameter("loginUsername",v));

        setProperties(properties.getMonitorPassword(),v->servletRegistrationBean.addInitParameter("loginPassword",v));


        //是否能够重置数据.
        setProperties(properties.getMonitorResetEnable(),v->{
            servletRegistrationBean.addInitParameter("resetEnable",String.valueOf(v));
        });


        return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "bd.datasource.druid",value = "monitor-enable",havingValue = "true")
    public FilterRegistrationBean druidStatFilter(){
        logger.info("init Druid druidStatFilter Configuration ");
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        //添加过滤规则.
        setProperties(properties.getMonitorFilterUrl(),filterRegistrationBean::addUrlPatterns);

        //添加不需要忽略的格式信息.
        setProperties(properties.getMonitorFilterExclusions(),v->filterRegistrationBean.addInitParameter("exclusions",v));

        return filterRegistrationBean;

    }






    protected DruidDataSource setDataSourceSpecialProperties(DruidDataSource dataSource){
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

        return dataSource;

    }



    protected void createConnectProperties(){
        Properties properties = new Properties();
        properties.put("druid.stat.mergeSql",true);
        properties.put("druid.stat.slowSqlMillis",3000);
        this.properties.setConnectionProperties(properties);
    }




    @Override
    public <T> void setProperties(T val, Call<T> call) {
        if(val != null){
            call.call(val);
        }
    }



}
