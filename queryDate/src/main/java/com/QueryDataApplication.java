package com;

import com.bhcredit.common.config.ConfigMgr;
import com.zzc.cassandra.CassandraAgent;
import com.zzc.cassandra.NezhaAccessor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
//@SpringBootApplication(exclude  {DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = "com")
//@EnableAspectJAutoProxy  //支持aop组件
public class QueryDataApplication {

	public static final String SEPARATOR = ".";
	public static final String DBCP_PREFIX = "dbcp.";
	public static final String DRIVER_CLASS_NAME = "driverClassName";
	public static final String URL = "url";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String INITIAL_SIZE = "initialSize";
	public static final String MAX_TOTAL = "maxTotal";
	public static final String MAX_IDLE = "maxIdle";
	public static final String MIN_IDLE = "minIdle";
	public static final String TEST_ON_BORROW = "testOnBorrow";
	public static final String TEST_WHILE_IDLE = "testWhileIdle";
	public static final String VALIDATION_QUERY = "validationQuery";
	public static final String SELECT_1 = "SELECT 1";



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

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplateBack(@Autowired @Qualifier("tagbackDataSource") DataSource dataSource) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return namedParameterJdbcTemplate;
	}



	@Bean(destroyMethod = "")
	public DataSource dataSource() {
		return createMysqlDataSource("tag");
	}

	@Bean
	public DataSource tagbackDataSource() {
		return createMysqlDataSource("tagback");
	}

	private DataSource createMysqlDataSource(String database) {

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(ConfigMgr.getValue(DBCP_PREFIX + DRIVER_CLASS_NAME));
		dataSource.setUrl(ConfigMgr.getValue(DBCP_PREFIX + database + SEPARATOR + URL));
		dataSource.setUsername(ConfigMgr.getValue(DBCP_PREFIX + database + SEPARATOR + USERNAME));
		dataSource.setPassword(ConfigMgr.getValue(DBCP_PREFIX + database + SEPARATOR + PASSWORD));

		dataSource.addConnectionProperty(INITIAL_SIZE, ConfigMgr.getValue(DBCP_PREFIX + INITIAL_SIZE));
		dataSource.addConnectionProperty(MAX_TOTAL, ConfigMgr.getValue(DBCP_PREFIX + MAX_TOTAL));
		dataSource.addConnectionProperty(MAX_IDLE, ConfigMgr.getValue(DBCP_PREFIX + MAX_IDLE));
		dataSource.addConnectionProperty(MIN_IDLE, ConfigMgr.getValue(DBCP_PREFIX + MIN_IDLE));
//		dataSource.addConnectionProperty(TIME_BETWEEN_EVICTION_RUNS_MILLIS, ConfigMgr.getValue(DBCP_PREFIX + TIME_BETWEEN_EVICTION_RUNS_MILLIS));
//		dataSource.addConnectionProperty(MIN_EVICTABLE_IDLE_TIME_MILLIS, ConfigMgr.getValue(DBCP_PREFIX + MIN_EVICTABLE_IDLE_TIME_MILLIS));
//		dataSource.addConnectionProperty(NUM_TESTS_PER_EVICTION_RUN, ConfigMgr.getValue(DBCP_PREFIX + NUM_TESTS_PER_EVICTION_RUN));
		dataSource.addConnectionProperty(TEST_ON_BORROW, "" + false);
		dataSource.addConnectionProperty(TEST_WHILE_IDLE, "" + true);
		dataSource.addConnectionProperty(VALIDATION_QUERY, SELECT_1);

		return dataSource;
	}

	@Bean
	public NezhaAccessor nezhaAccessor() {
		return CassandraAgent.initialize();
	}

	public  DataSource createMysqlDataSources(String database) {
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
