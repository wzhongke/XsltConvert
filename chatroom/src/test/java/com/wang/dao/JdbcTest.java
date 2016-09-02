package com.wang.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;

/**
 * Created by admin on 2016/9/2.
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("applicationContext.xml")
public class JdbcTest {
    private JdbcMovieFinder jdbcMovieFinder;
    private JdbcTemplate jdbcTemplate;


    @Before
    public void init(){
        ApplicationContext context = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        this.jdbcTemplate = new JdbcTemplate((DataSource) context.getBean("dataSource"));
        jdbcMovieFinder = new JdbcMovieFinder(jdbcTemplate);
    }

    @Test
    public void testJdbc(){
        jdbcMovieFinder.query();
    }
}
