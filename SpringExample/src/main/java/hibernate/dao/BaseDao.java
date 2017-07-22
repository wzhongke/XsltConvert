package hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by admin on 2017/7/22.
 */
public class BaseDao<T> {

    @Autowired
    private SessionFactory factory;

    public T queryById(Class<T> var1, Serializable id) {
        try (Session session = factory.openSession()) {
           return session.get(var1, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(T var1) {
        try (Session session = factory.openSession()) {
            session.save(var1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
