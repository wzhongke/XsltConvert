package hibernate.hql.update;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2017/7/27.
 */
public class Update {
    @Autowired
    SessionFactory factory;

    public void executeUpdate () {
        int updatedEntities = factory.createEntityManager().createQuery(
            "update Person p " +
                    "set p.name = :newName " +
                    "where p.name = :oldName" )
            .setParameter( "oldName", "" )
            .setParameter( "newName", "" )
            .executeUpdate();
    }
}
