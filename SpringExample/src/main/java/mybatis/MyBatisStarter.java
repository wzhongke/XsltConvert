package mybatis;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import mybatis.dao.Person;
import mybatis.mapper.PersonMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by admin on 2017/7/20.
 */
public class MyBatisStarter {
    public static void main(String [] args) throws IOException {
        try (SqlSession session = useXMLConfig("mybatis.xml").openSession()) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            List<Person> ps = mapper.selectAllPerson();
            System.out.println(ps);
        }

    }

    public static SqlSessionFactory useXMLConfig (String resource) throws IOException {
        resource = resource == null ? "mybatis.xml" : resource;
        InputStream confStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(confStream);
    }

    public SqlSessionFactory useJava() {
        DataSource dataSource = new MysqlConnectionPoolDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(Person.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    public void getSession() {
        try (SqlSession session = useJava().openSession()) {
            Person person = session.selectOne("mybatis.mapper.PersonMapper.selectPerson", 101);

            PersonMapper mapper = session.getMapper(PersonMapper.class);
            Person person1 = mapper.selectPerson(101);
        }
    }
}
