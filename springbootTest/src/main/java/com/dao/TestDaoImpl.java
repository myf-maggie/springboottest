package com.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by zxc on 2020/3/29.
 */
public class TestDaoImpl implements Testdao {
    String sql="SELECT *  FROM tenant_type_def_old where id=? limit 1";

    @Override
    public  List<Map<String,Object>> select(JdbcTemplate jdbcTemplate,String id) {
       List<Map<String,Object>> list=jdbcTemplate.queryForList(sql,id);
       System.out.print("objeresult"+list);
        return list;
    }
}
