# spring_druid_starter
Spring boot 的Druid 数据库连接池 starter
## Maven地址


```xml
    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	
```
```xml
    <dependency>
	    <groupId>com.github.wang1186956992</groupId>
	    <artifactId>spring-boot-starter-druid</artifactId>
	    <version>V.1.0</version>
	</dependency>
```

## 使用方法
```properties
#开启druid数据库连接池
data.datasource.druid.enabled=true
#开启druid数据库连接池监控  默认监控地址 /druid
data.datasource.druid.monitor-enable=true
```

## 配置说明
```properties
spring.datasource.name=fxb_data_center
spring.datasource.url=jdbc:mysql://localhost:3306/xxx
spring.datasource.username=xxx
spring.datasource.password=xxx
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.jpa.hibernate.ddl-auto=none
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
```