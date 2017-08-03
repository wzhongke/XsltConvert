package hibernate.hql.select;

import hibernate.domain.namestrategy.domain.Person;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by admin on 2017/7/27.
 */
public class Select {

    @Autowired
    SessionFactory factory;

    public void fetchScroll() {
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        ScrollableResults scrollableResults = null;
        try {
            entityManager = factory.createEntityManager();

            txn = entityManager.getTransaction();
            txn.begin();

            int batchSize = 25;

            Session session = entityManager.unwrap( Session.class );

            scrollableResults = session
                    .createQuery( "select p from Person p" )
                    .setCacheMode( CacheMode.IGNORE )
                    .scroll( ScrollMode.FORWARD_ONLY );

            int count = 0;
            while ( scrollableResults.next() ) {
                Person Person = (Person) scrollableResults.get( 0 );
//                processPerson(Person);
                if ( ++count % batchSize == 0 ) {
                    //flush a batch of updates and release memory:
                    entityManager.flush();
                    entityManager.clear();
                }
            }

            txn.commit();
        } catch (RuntimeException e) {
            if ( txn != null && txn.isActive()) txn.rollback();
            throw e;
        } finally {
            if (scrollableResults != null) {
                scrollableResults.close();
            }
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}
