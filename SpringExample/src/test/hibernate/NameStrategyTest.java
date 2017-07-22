package hibernate;

import hibernate.namestrategy.domain.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.TimeZone;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class NameStrategyTest {
    @Autowired
    SessionFactory factory;

    public void init(){
        TimeZone.setDefault( TimeZone.getTimeZone( "UTC" ) );
    }

    @Test
    public void testHibernateQuery() {
        Session session = factory.openSession();
        List<Person> persons = session.createQuery("SELECT p FROM Person p", Person.class).getResultList();
        persons.forEach((person)-> System.out.println(person.getName() + " " + person.getAgeSex()));
        session.close();
    }
}
