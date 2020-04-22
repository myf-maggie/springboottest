package com.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by zxc on 2020/4/12.
 */
@Configuration
@ComponentScan(basePackages = "com")
public class Appconfig {

    @Bean
    public JdbcTemplate jdbcTemplate(@Autowired @Qualifier("dataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Autowired @Qualifier("dataSource") DataSource dataSource) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return namedParameterJdbcTemplate;
    }



    @Bean(destroyMethod = "")
    public DataSource dataSource() {
        return createMysqlDataSource("antifraud");
    }

//	private DataSource createMysqlDataSource(String database) {
//		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setDriverClassName(ConfigMgr.getValue("dbcp.driverClassName"));
//		dataSource.setUrl(ConfigMgr.getValue(DBCP_PREFIX + ANTI + URL));
//		dataSource.setUsername(ConfigMgr.getValue(DBCP_PREFIX + ANTI + USERNAME));
//		dataSource.setPassword(ConfigMgr.getValue(DBCP_PREFIX + ANTI + PASSWORD));
//		dataSource.addConnectionProperty(INITIAL_SIZE, ConfigMgr.getValue(DBCP_PREFIX + INITIAL_SIZE));
//		dataSource.addConnectionProperty(MAX_TOTAL, ConfigMgr.getValue(DBCP_PREFIX + MAX_TOTAL));
//		dataSource.addConnectionProperty(MAX_IDLE, ConfigMgr.getValue(DBCP_PREFIX + MAX_IDLE));
//		dataSource.addConnectionProperty(MIN_IDLE, ConfigMgr.getValue(DBCP_PREFIX + MIN_IDLE));
//		dataSource.addConnectionProperty(TEST_ON_BORROW, "" + false);
//		dataSource.addConnectionProperty(TEST_WHILE_IDLE, "" + true);
//		dataSource.addConnectionProperty(VALIDATION_QUERY, SELECT_1);
//
//		return dataSource;
//	}

//	@Bean
//	public NezhaAccessor nezhaAccessor() {
//		return CassandraAgent.initialize();
//	}

    public  DataSource createMysqlDataSource(String database) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/bh_antifraud_tag?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root123456");
        dataSource.addConnectionProperty("initialSize", "20");
        dataSource.addConnectionProperty("maxTotal", "1000");
        dataSource.addConnectionProperty("maxIdle", "600");
        dataSource.addConnectionProperty("minIdle", "200");
        dataSource.addConnectionProperty("testOnBorrow", "" + false);
        dataSource.addConnectionProperty("testWhileIdle", "" + true);
        dataSource.addConnectionProperty("validationQuery", "SELECT 1");
        return dataSource;
    }

}
