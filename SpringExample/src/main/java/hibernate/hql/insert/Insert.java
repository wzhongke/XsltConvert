package hibernate.hql.insert;

import hibernate.domain.namestrategy.domain.Person;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by admin on 2017/7/27.
 */
public class Insert {

    @Autowired
    SessionFactory factory;

    public void batchInsert() {
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        try {
            entityManager = factory.createEntityManager();

            txn = entityManager.getTransaction();
            txn.begin();

            int batchSize = 25, entityCount = 500;

            for ( int i = 0; i < entityCount; ++i ) {
                Person Person = new Person( );
                entityManager.persist( Person );

                if ( i > 0 && i % batchSize == 0 ) {
                    //flush a batch of inserts and release memory
                    entityManager.flush();
                    entityManager.clear();
                }
            }

            txn.commit();
        } catch (RuntimeException e) {
            if ( txn != null && txn.isActive()) txn.rollback();
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}
