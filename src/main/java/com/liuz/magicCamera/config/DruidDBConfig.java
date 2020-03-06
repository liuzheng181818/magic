package com.liuz.magicCamera.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidDBConfig {

	@Value("${spring.datasource.decorate.username}")
	private String username;

	@Value("${spring.datasource.decorate.password}")
	private String password;

	@Value("${spring.datasource.decorate.driver-class-name}")
	private String driver;

	@Value("${spring.datasource.decorate.url}")
	private String url;

	@Value("${spring.datasource.druid.initialSize}")
	private String initialSize;

	@Value("${spring.datasource.druid.maxActive}")
	private String maxActive;

	@Bean
	public DataSource dataSource() throws SQLException {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setUsername(username);
		druidDataSource.setPassword(password);
		druidDataSource.setDriverClassName(driver);
		druidDataSource.setUrl(url);
		druidDataSource.setMaxActive(Integer.valueOf(maxActive));
		druidDataSource.setFilters("stat,wall,log4j");
		druidDataSource.setInitialSize(Integer.valueOf(initialSize));
		return druidDataSource;
	}

	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		//reg.addInitParameter("allow", "127.0.0.1"); //白名单
		reg.addInitParameter("resetEnable","false");
		return reg;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		Map<String, String> initParams = new HashMap<>();
		//设置忽略请求
		initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
		filterRegistrationBean.setInitParameters(initParams);
		filterRegistrationBean.addInitParameter("profileEnable", "true");
		filterRegistrationBean.addInitParameter("principalCookieName","USER_COOKIE");
		filterRegistrationBean.addInitParameter("principalSessionName","");
		filterRegistrationBean.addInitParameter("aopPatterns","com.rongle.decorate.service");
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}
}
