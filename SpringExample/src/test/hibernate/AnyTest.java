package hibernate;

import hibernate.domain.any.IntegerProperty;
import hibernate.domain.any.PropertyHolder;
import hibernate.domain.any.StringProperty;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AnyTest {

    @Autowired
    SessionFactory factory;

    @Test
    public void test() {
        try (Session session = factory.openSession()) {
            IntegerProperty ageProperty = new IntegerProperty();
            ageProperty.setId(1L);
            ageProperty.setName("age");
            ageProperty.setValue(23);

            StringProperty nameProperty = new StringProperty();
            nameProperty.setId(1L);
            nameProperty.setName("name");
            nameProperty.setValue("John Doe");

            session.persist(ageProperty);
            session.persist(nameProperty);

            PropertyHolder namePropertyHolder = new PropertyHolder();
            namePropertyHolder.setId(1L);
            namePropertyHolder.setProperty(nameProperty);
            session.persist(namePropertyHolder);
        }
    }

}
