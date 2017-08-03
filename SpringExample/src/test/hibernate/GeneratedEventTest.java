package hibernate;

import hibernate.domain.generated.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class GeneratedEventTest {

        @Autowired
        SessionFactory factory;

    @Test
    public void testInsertEvent() {
        try (Session session = factory.openSession()) {
            session.getTransaction().begin();
            session.persist(new Event());
            session.getTransaction().commit();
        }
    }

    @Test
    public void testQuery() {
        try (Session session = factory.openSession()) {
            Event e = session.createQuery(" FROM Event e where e.id = 1", Event.class).uniqueResult();
            System.out.println(e.getTimestamp());
        }
    }
}
