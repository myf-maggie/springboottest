package com.test;

import com.dao.TestDaoImpl;
import com.dao.Testdao;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by zxc on 2020/3/26.
 */
public class JdbcClass {

    @Autowired
    private Testdao testdao;


    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(createMysqlDataSource());
        JdbcClass jdbcClass=new JdbcClass();
        jdbcClass.getselect(jdbcTemplate);


    }



    public static DataSource createMysqlDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/bh_antifraud?serverTimezone=UTC");
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

    @Transactional(isolation= Isolation.DEFAULT)
    public String getselect(JdbcTemplate jdbcTemplate){

        testdao=new TestDaoImpl();
        List<Map<String,Object>> list= testdao.select(jdbcTemplate,"1");

        return null;
    }


}
