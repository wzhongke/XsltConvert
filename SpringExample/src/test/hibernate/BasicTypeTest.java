package hibernate;

import hibernate.namestrategy.customtype.BitSetType;
import hibernate.namestrategy.domain.Account;
import hibernate.namestrategy.domain.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.BlobProxy;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.TimeZone;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class BasicTypeTest {
    @Autowired
    SessionFactory factory;

    public void init(){
        TimeZone.setDefault( TimeZone.getTimeZone( "UTC" ) );
    }

    BitSet bitSet = BitSet.valueOf( new long[] {1, 2, 3} );

    @Test
    public void test() {
        // 需要注册BitSet
        try (Session session = factory.openSession()) {
            Person p = new Person();
            p.setAge(28);
            p.setName("wangzhongke");
            p.setSex(Person.Sex.boy);

            p.setImage(BlobProxy.generateProxy(new byte[]{1, 2, 3}));

            p.setBirthDay(LocalDate.of(1990, 12, 1));
            p.setAddress(new Person.Address("china", "1231234"));
            p.setZipCode(new Person.ZipCode("121"));
            List<Account> accounts = new ArrayList<>();
            accounts.add(new Account("792833242", "20000", Account.AccountType.DEBIT));
            accounts.add(new Account("1255487522", "20000", Account.AccountType.CREDIT));
            p.setAccounts(accounts);
            p.setBitSet(bitSet);
            session.getTransaction().begin();
            session.persist(p);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
