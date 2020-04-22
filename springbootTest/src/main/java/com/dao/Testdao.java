package com.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by zxc on 2020/3/29.
 */
public interface Testdao {


    List<Map<String,Object>> select(JdbcTemplate jdbcTemplate, String id);
}
