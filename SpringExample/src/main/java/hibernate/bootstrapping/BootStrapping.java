package hibernate.bootstrapping;

import hibernate.association.domain.Person;
import hibernate.domain.namestrategy.UmisImplicitNamingStrategy;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Created by admin on 2017/7/27.
 */
public class BootStrapping {

    public static SessionFactory getSessionFactory() {
        StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                .configure("classpath:hibernate.cfg.xml")
                .build();
        Metadata metadata = new MetadataSources(standardServiceRegistry)
                .addAnnotatedClass(Person.class)
                .addAnnotatedClassName("hibernate.domain.namestrategy.domain.Person")
                //.addResource("")
                .getMetadataBuilder()
                .applyImplicitNamingStrategy(UmisImplicitNamingStrategy.INSTANCE)
                .build();
        return metadata.getSessionFactoryBuilder().build();
    }
}
