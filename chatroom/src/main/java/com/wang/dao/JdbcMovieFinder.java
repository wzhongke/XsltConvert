package com.wang.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by admin on 2016/9/1.
 */
@Repository
public class JdbcMovieFinder{
    /**
     *  Instances of the JdbcTemplate class are threadsafe once configured. This is important because it means that you can configure a
     *  single instance of a single instance of JdbcTemplate and then safely inject this shared reference into multiple DAOs.
     */

    private JdbcTemplate jdbcTemplate;

    private static Logger logger = LoggerFactory.getLogger(JdbcMovieFinder.class);

    public JdbcMovieFinder(){}

    public JdbcMovieFinder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * query method
     */
    public void query(){
        int rowCount = this.jdbcTemplate.queryForObject("select count(*) from person_info", Integer.class);

        logger.error("------------------------rowCount: "+rowCount);
        // A simple query using a bind variable
        int countOfActorsNamedJoe = this.jdbcTemplate.queryForObject(
                "select count(*) from person_info where name = ?", Integer.class, "wang");
        // query for a string
        // 当查找失败时，抛出org.springframework.dao.EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0
//        String lastName = this.jdbcTemplate.queryForObject(
//                "select name from person_info where id=?", new Object[]{1212L}, String.class);

        // query and populate a single domain object
        Person person = this.jdbcTemplate.queryForObject(
                "select name, sex, age from person_info where id=?",
                new Object[]{1L},
               (ResultSet resultSet, int i)  -> {
                        Person person1 = new Person();
                        person1.setName(resultSet.getString("name"));
                        person1.setAge(resultSet.getInt("age"));
                        return person1;
                    });

        // query and populate a number of domain objects
        List<Person> persons = this.jdbcTemplate.query(
                "select name, age from person_info",
               new PersonMapper());

    }

    public void update(){
        this.jdbcTemplate.update(
                "insert into person_info (name, age) values (?, ?)",
                "wang", "25");

        this.jdbcTemplate.update(
                "update person_info set name = ? where id = ?",
                "wang", 1L);

    }

    public void delete(){
        this.jdbcTemplate.update(
                "delete from person_info where id = ?",
                Long.valueOf(1));
    }

    public void execute(){
        this.jdbcTemplate.execute("create table mytable (id integer, name varchar(100))");
    }


    private static final class PersonMapper implements RowMapper<Person>{
        @Override
        public Person mapRow(ResultSet resultSet, int i) throws SQLException {
            Person person1 = new Person();
            person1.setName(resultSet.getString("name"));
            person1.setAge(resultSet.getInt("age"));
            return person1;
        }

    }

}
