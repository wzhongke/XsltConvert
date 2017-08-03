package hibernate;

import hibernate.domain.namestrategy.domain.Person;
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

    @Test
    public void test() {
        char dict[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        StringBuffer buffer = new StringBuffer();
        buffer.append("13102");
        char x[] = new char[3];
        x[0] = dict[10 >> 2 & 0xf];
        x[1] = dict[(60 >>> 2) - (10 ^ 9)];
        x[2] = dict[(139 | 193) % 5];
        buffer.insert(2, Integer.parseInt(String.valueOf(x), 16));
        for (int i = buffer.length(); i > 0; i--) {
            System.out.print(buffer.charAt(i - 1));
        }
    }
}
