package hibernate.namestrategy.domain;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by admin on 2017/3/2.
 */
public class PersonAddressUserType implements CompositeUserType{
    @Override
    public String[] getPropertyNames() {
        return new String[]{"address", "phone"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{StandardBasicTypes.STRING, StandardBasicTypes.STRING};
    }

    @Override
    public Object getPropertyValue(Object component, int property) throws HibernateException {
        Person.Address address = (Person.Address) component;
        return property == 0 ? address.getAddress() : address.getPhone();
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        Person.Address address = (Person.Address) component;
        if (property == 0) {
            address.setAddress((String)value);
        } else {
            address.setPhone((String) value);
        }
    }

    @Override
    public Class returnedClass() {
        return Person.Address.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) return true;
        if (x == null || y == null) return false;
        Person.Address ax = (Person.Address) x;
        Person.Address ay = (Person.Address) y;
        return ax.getAddress().equals(ay.getAddress()) && ax.getPhone().equals(ay.getPhone());
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        Person.Address ax = (Person.Address) x;
        return (ax.getAddress() + ax.getPhone()).hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        String address = StandardBasicTypes.STRING.nullSafeGet(rs, names[0], session);
        String phone = StandardBasicTypes.STRING.nullSafeGet(rs, names[1], session);
        if (address != null || phone != null) {
            return new Person.Address(address, phone);
        }
        return new Person.Address("", "");
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        Person.Address ma = (Person.Address) value;
        String address = ma == null ? null : ma.getAddress();
        String phone = ma == null ? null : ma.getPhone();
        StandardBasicTypes.STRING.nullSafeSet( st, address, index, session );
        StandardBasicTypes.STRING.nullSafeSet( st, phone, index + 1, session );
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        Person.Address ax = (Person.Address) value;
        return new Person.Address(ax.getAddress(), ax.getPhone());
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value, SharedSessionContractImplementor session) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return deepCopy( cached );
    }

    @Override
    public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return deepCopy( original ); //TODO: improve
    }
}
