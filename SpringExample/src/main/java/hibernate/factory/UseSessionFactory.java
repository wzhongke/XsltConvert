package hibernate.factory;

import hibernate.association.domain.Phone;
import hibernate.domain.namestrategy.domain.Person;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.PersistenceUtil;
import java.sql.Statement;
import java.util.Optional;

/**
 * Created by admin on 2017/3/24.
 */
public class UseSessionFactory {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SessionFactory factory;

    @Autowired
    private Session session;

    public void accessJPAUseHibernateApi() {
        SessionImplementor implementor = entityManager.unwrap(SessionImplementor.class);
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
    }

    public void saveWithJPA() {
        Person person = new Person();
        person.setId(1L);
        person.setName("Wang");
        entityManager.persist(person);
    }

    public void saveWithHibernate() {
        Session session = factory.openSession();
        Person person = new Person();
        person.setId( 1L );
        person.setName("John Doe");

        session.save( person );
    }

    public void deleteByJPA() {
        Person person = new Person();
        entityManager.remove(person);
    }

    public void deleteByHibernate() {
        Person person = new Person();
        factory.openSession().delete(person);
    }
    /**
     * 如果数据库中没有响应的实体，会返回null
     */
    public void searchById() {
        // 使用JPA的方式从数据库中获取一个实体
        Person person = entityManager.find(Person.class, "123");
        // 使用Hibernate的API获取一个实体
        Person person1 = session.get(Person.class, "123");
        // 通过byId获取
        Person person2 = session.byId(Person.class).load("123");

        // 还可以返回一个java8的Optional实体
        Optional<Person> optionalPerson = session.byId(Person.class).loadOptional("123");
        System.out.println(optionalPerson.map(Person::getName).map(String::toUpperCase).orElse(null));
        System.out.println(optionalPerson.filter(p->p.getName()!=null));
    }

    public void searchByNaturalId() {
        Session session = entityManager.unwrap(Session.class);
        // getReference 方法获取记录时，暗含了记录肯定存在的条件，否则，会出现错误。
        Phone phone = session.bySimpleNaturalId(Phone.class).getReference("natural id");
        // 多个natural id
        Phone phone1 = session.byNaturalId(Phone.class).using("number", "natural id").load();
        Optional<Phone> optionalPhone = session.byNaturalId(Phone.class).using("number", "natural id").loadOptional();
    }

    /**
     * 当应用修改managed/persistent状态的实体时，hibernate会自动检测所有的改变并且在存储上下文flush的时候被保存
     */
    public void managePersistent() {
        Person person = entityManager.find(Person.class, "id");
        person.setName("John Doe");
        entityManager.flush();
    }

    /**
     * 只有在将 REFRESH作为级联关系时，刷新才应用于外键类型。否则，只有实体属性和它的集合类型被刷新。
     */
    public void reloadEntity() {
        Person person = entityManager.find(Person.class, "1234");
        entityManager.createQuery("UPDATE Person SET name=UPPER(name)");
        entityManager.refresh(person);
        // Hibernate API
        Person person1 = session.byId(Person.class).load("1234");
        session.doWork(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("UPDATE Person SET name=UPPER(name)");
            }
        });
        session.refresh(person1);
    }

    /**
     * Detachment是处理持久化上下文之外数据的过程。
     * 数据成为Detachment有几种情况：
     *      1. 关闭持久化上下文
     *      2. 清空持久化上下文
     *      3. 从持久化上下文删除某条数据
     *  Detached的数据仍然可以使用，只是持久化上下文将不会监控到据的修改
     *
     *  Reattachment将detached状态的数据重新加入到持久化上下文中。
     *  JPA不支持该模式，只能使用org.hibernate.Session
     */
    public void redetached() {
        Session session = entityManager.unwrap(Session.class);

        Person person = session.byId(Person.class).load("123");
        // Clear the Session so the person entity becomes detached
        session.clear();
        person.setName("Mr. Wang");
        // Reattaching a detached using lock
        session.lock(person, LockMode.NONE);
        // Reattaching a detached entity using saveOrUpdate
        session.saveOrUpdate(person);
        // update() ： 当持久化上下文flushed时，才执行SQL UPDATE
    }

    /**
     * Merging detached data: 将一个不在持久化上下文环境的实体复制到新的托管的实例上
     */
    private Person merge(Person detached) {
        Person newReference = session.byId(Person.class).load(detached.getId());
        newReference.setName(detached.getName());
        return newReference;
    }

    public void merge() {
        // JPA
        Person person = entityManager.find(Person.class, "123");
        entityManager.clear();
        person.setName("Mrs. Wen");
        person = entityManager.merge(person);

        // Hibernate
        person = (Person) session.merge(person);
    }

    /**
     * 检测持久化状态
     */
    public void checkPersistentState () {
        Person person = entityManager.find(Person.class, "123");
        // JPA
        boolean contained = entityManager.contains(person);
        // Hibernate API
        boolean containedH = entityManager.contains(person);
        // Verifying laziness with JPA
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        // Another method to get PersistenceUtil, which is recommended
        PersistenceUtil persistenceUnitUtil1 = Persistence.getPersistenceUtil();
        boolean personInitialized = persistenceUnitUtil.isLoaded(person);
        boolean personAccountInitialized = persistenceUnitUtil.isLoaded(person.getAccounts());
        boolean personNameInitialized = persistenceUnitUtil.isLoaded(person, "name");

        // Verifying laziness with Hibernate API
        boolean personInitialized1 = Hibernate.isInitialized(person);
        boolean personAccountInitialized1 = Hibernate.isInitialized(person.getAccounts());
        boolean personNameInitialized1 = Hibernate.isPropertyInitialized(person, "name");
    }

    /**
     * 从持久化上下文中移除实体
     * 当调用flush()方法时，实体会被同步到数据库。如果不想同步数据，或者要处理很多对象并且要有效地使用内存，evict()可以将实体和它的
     * 集合从first-level cache中移除
     */

    public void evictEntity() {
        Session session = entityManager.unwrap(Session.class);
        // Detaching an entity from the entityManager
        for (Person person: entityManager.createQuery("select p from Person p", Person.class).getResultList()) {
            entityManager.detach(person);
        }
        // Detaching entity from the Hibernate Session
        session.createQuery("select p from Person p", Person.class).list().forEach(session::evict);
    }
}
