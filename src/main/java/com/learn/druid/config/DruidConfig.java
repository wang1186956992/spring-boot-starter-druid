package com.learn.druid.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.learn.druid.lambdaInterface.Call;
import com.learn.druid.properties.DruidProperties;
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
 * Created by yf003 on 2017/8/17.
 */

@Configuration
@ConfigurationProperties
@ConditionalOnClass(DruidDataSource.class)
@EnableConfigurationProperties(DruidProperties.class)
public class DruidConfig implements ConfigProperties {

    protected static Logger logger= LoggerFactory.getLogger(DruidConfig.class);


    @Autowired
    DruidProperties druidProperties;

    @Bean
    @ConditionalOnProperty(prefix = "bd.datasource.druid",value = "enabled",havingValue = "true")
    public DataSource dataSource(){
        logger.info("init Druid DruidDataSource Configuration ");
        DruidDataSource dataSource = new DruidDataSource();

        setDataProperties(druidProperties.getName(),dataSource::setName);
        setDataProperties(druidProperties.getUrl(),dataSource::setUrl);
        setDataProperties(druidProperties.getUsername(),dataSource::setUsername);
        setDataProperties(druidProperties.getPassword(),dataSource::setPassword);
        setDataProperties(druidProperties.getMaxActive(),dataSource::setMaxActive);
        setDataProperties(druidProperties.getMinIdle(), dataSource::setMinIdle);
        setDataProperties(druidProperties.getMaxWait(), dataSource::setMaxWait);
        setDataProperties(druidProperties.getPoolPreparedStatements(), dataSource::setPoolPreparedStatements);
        setDataProperties(druidProperties.getMaxPoolPreparedStatementPerConnectionSize(), dataSource::setMaxPoolPreparedStatementPerConnectionSize);
        setDataProperties(druidProperties.getValidationQuery(), dataSource::setValidationQuery);
        setDataProperties(druidProperties.getValidationQueryTimeout(), dataSource::setValidationQueryTimeout);
        setDataProperties(druidProperties.getTestOnBorrow(), dataSource::setTestOnBorrow);
        setDataProperties(druidProperties.getTestOnReturn(), dataSource::setTestOnReturn);
        setDataProperties(druidProperties.getTestWhileIdle(), dataSource::setTestWhileIdle);

        //设置数据源特殊属性
        dataSource = setDataSourceSpecialProperties(dataSource);

        createConnectProperties();

        setDataProperties(druidProperties.getConnectionProperties(), dataSource::setConnectProperties);

        return dataSource;
    }


    @Bean
    @ConditionalOnProperty(prefix = "bd.datasource.druid",value = "monitor-enable",havingValue = "true")
    public ServletRegistrationBean druidStatViewServlet(){
        logger.info("init Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet()
                ,druidProperties.getMonitorPath());

        setDataProperties(druidProperties.getMonitorPath(),servletRegistrationBean::addUrlMappings);

        //白名单（允许访问IP）
        setDataProperties(druidProperties.getMonitorAllow(),v->servletRegistrationBean.addInitParameter("allow",v));

        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        setDataProperties(druidProperties.getMonitorDeny(),v->servletRegistrationBean.addInitParameter("deny",v));


        //登录查看信息的账号密码.

        setDataProperties(druidProperties.getMonitorUserName(),v->servletRegistrationBean.addInitParameter("loginUsername",v));

        setDataProperties(druidProperties.getMonitorPassword(),v->servletRegistrationBean.addInitParameter("loginPassword",v));


        //是否能够重置数据.
        setDataProperties(druidProperties.getMonitorResetEnable(),v->{
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
        setDataProperties(druidProperties.getMonitorFilterUrl(),filterRegistrationBean::addUrlPatterns);

        //添加不需要忽略的格式信息.
        setDataProperties(druidProperties.getMonitorFilterExclusions(),v->filterRegistrationBean.addInitParameter("exclusions",v));

        return filterRegistrationBean;

    }






    protected DruidDataSource setDataSourceSpecialProperties(DruidDataSource dataSource){
        if (druidProperties.getTimeBetweenEvictionRunsMillis()!=null){
            dataSource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
        }
        if (druidProperties.getMinEvictableIdleTimeMillis()!=null){
            dataSource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
        }
        if (druidProperties.getConnectionInitSqls()!=null){
            dataSource.setConnectionInitSqls(druidProperties.getConnectionInitSqls());
        }
        if (druidProperties.getFilters()!=null){
            try {
                dataSource.setFilters(druidProperties.getFilters());
            } catch (SQLException e) {
//                e.printStackTrace();
                logger.error("数据库连接池Filters设置失败",e);
            }
        }
        if(druidProperties.getProxyFilters()!=null){
            dataSource.setProxyFilters(druidProperties.getProxyFilters());
        }

        return dataSource;

    }



    protected void createConnectProperties(){
        Properties properties = new Properties();
        properties.put("druid.stat.mergeSql",true);
        properties.put("druid.stat.slowSqlMillis",3000);
        this.druidProperties.setConnectionProperties(properties);
    }





    @Override
    public <T> void setDataProperties(T val, Call<T> call) {

    }
}
