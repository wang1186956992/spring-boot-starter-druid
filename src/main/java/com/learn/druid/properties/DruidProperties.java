package com.learn.druid.properties;

import com.alibaba.druid.filter.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Properties;

/**
 * Created by xuejike on 2017/6/22.
 */

@ConfigurationProperties("bd.datasource.druid")
public class DruidProperties {
    /**
     * 开启Druid连接池
     */
    private Boolean enabled;
    @Value("${spring.datasource.name}")
    private String name;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * 初始化大小
     */
    private Integer initialSize;
    /**
     * 最大连接池大小
     */
    private Integer maxActive;
//    private Integer maxIdle=8;
    /**
     * 最小连接数
     */
    private Integer minIdle;
    /**
     * 最大空闲数
     */
    private Long maxWait;
    private Boolean poolPreparedStatements;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String validationQuery;
    private Integer validationQueryTimeout;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private Boolean testWhileIdle;
    private Integer timeBetweenEvictionRunsMillis;
    private Integer minEvictableIdleTimeMillis;
    private List<String> connectionInitSqls;
    private String filters="stat,mergeStat,encoding,slf4j,wall";
    private Properties connectionProperties;
    private List<Filter> proxyFilters;


    /**
     * 开启监控界面
     */
    private Boolean monitorEnable;
    /**
     * 监控地址
     */
    private String monitorPath="/druid/*";
    /**
     *允许访问IP
     */
    private String monitorAllow="127.0.0.1";
    /**
     *禁止访问IP
     */
    private String monitorDeny;
    /**
     *登录用户名
     */
    private String monitorUserName="admin";
    /**
     *登录密码
     */
    private String monitorPassword="admin";
    /**
     *是否开启重置按钮
     */
    private Boolean monitorResetEnable=true;


    private String monitorFilterUrl="/*";
    private String monitorFilterExclusions="*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }
//
//    public Integer getMaxIdle() {
//        return maxIdle;
//    }
//
//    public void setMaxIdle(Integer maxIdle) {
//        this.maxIdle = maxIdle;
//    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Long maxWait) {
        this.maxWait = maxWait;
    }

    public Boolean getPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public Integer getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public Integer getValidationQueryTimeout() {
        return validationQueryTimeout;
    }

    public void setValidationQueryTimeout(Integer validationQueryTimeout) {
        this.validationQueryTimeout = validationQueryTimeout;
    }

    public Boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public Boolean getTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(Boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public Boolean getTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(Boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public Integer getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(Integer timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public Integer getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }


    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public List<Filter> getProxyFilters() {
        return proxyFilters;
    }

    public void setProxyFilters(List<Filter> proxyFilters) {
        this.proxyFilters = proxyFilters;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {

        this.enabled = enabled;
    }

    public List<String> getConnectionInitSqls() {
        return connectionInitSqls;
    }

    public void setConnectionInitSqls(List<String> connectionInitSqls) {
        this.connectionInitSqls = connectionInitSqls;
    }

    public Boolean getMonitorEnable() {
        return monitorEnable;
    }

    public void setMonitorEnable(Boolean monitorEnable) {
        this.monitorEnable = monitorEnable;
    }

    public String getMonitorPath() {
        return monitorPath;
    }

    public void setMonitorPath(String monitorPath) {
        this.monitorPath = monitorPath;
    }

    public String getMonitorAllow() {
        return monitorAllow;
    }

    public void setMonitorAllow(String monitorAllow) {
        this.monitorAllow = monitorAllow;
    }

    public String getMonitorDeny() {
        return monitorDeny;
    }

    public void setMonitorDeny(String monitorDeny) {
        this.monitorDeny = monitorDeny;
    }

    public String getMonitorUserName() {
        return monitorUserName;
    }

    public void setMonitorUserName(String monitorUserName) {
        this.monitorUserName = monitorUserName;
    }

    public String getMonitorPassword() {
        return monitorPassword;
    }

    public void setMonitorPassword(String monitorPassword) {
        this.monitorPassword = monitorPassword;
    }

    public Boolean getMonitorResetEnable() {
        return monitorResetEnable;
    }

    public void setMonitorResetEnable(Boolean monitorResetEnable) {
        this.monitorResetEnable = monitorResetEnable;
    }

    public String getMonitorFilterUrl() {
        return monitorFilterUrl;
    }

    public void setMonitorFilterUrl(String monitorFilterUrl) {
        this.monitorFilterUrl = monitorFilterUrl;
    }

    public String getMonitorFilterExclusions() {
        return monitorFilterExclusions;
    }

    public void setMonitorFilterExclusions(String monitorFilterExclusions) {
        this.monitorFilterExclusions = monitorFilterExclusions;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }
}
