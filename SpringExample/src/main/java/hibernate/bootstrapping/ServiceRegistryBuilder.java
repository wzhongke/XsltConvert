package hibernate.bootstrapping;

import hibernate.namestrategy.domain.Person;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Created by admin on 2017/3/24.
 */
public class ServiceRegistryBuilder {

    public static void build() {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();

        BootstrapServiceRegistry registry = new BootstrapServiceRegistryBuilder().build();

        StandardServiceRegistryBuilder builder1 = new StandardServiceRegistryBuilder(registry);

        MetadataSources sources = new MetadataSources(registry);

        // add a class using JPA/Hibernate annotations for mapping
        sources.addAnnotatedClass(Person.class);

        // add the name of a class using JPA/Hibernate annotations for mapping.
        // differs from above in that accessing the Class is deferred which is
        // important if using runtime bytecode-enhancement
        sources.addAnnotatedClassName("hibernate.namestrategy.domain.Account");
        // Read package-level metadata.
        sources.addPackage("hibernate.association.domain");
    }
}
