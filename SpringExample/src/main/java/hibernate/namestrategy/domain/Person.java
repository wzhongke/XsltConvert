package hibernate.namestrategy.domain;

import org.hibernate.annotations.*;
import org.hibernate.mapping.AttributeContainer;
import org.hibernate.mapping.Property;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@Entity(name = "Person")
@Table(name = "Person")
@Where(clause = "age > 0")
@FilterDef(name="ageFilter", parameters=@ParamDef( name="age", type="int" ) )
@Filter(name = "ageFilter", condition = "age > :age")
@FilterDef(name="firstAccounts", parameters=@ParamDef( name="maxOrderId", type="int" ) )
@Filter(name="firstAccounts", condition="order_id <= :maxOrderId")
public class Person implements Serializable{
    @Id
    @GeneratedValue(
//            strategy = GenerationType.AUTO  // 使用hibernate生成的ID
             strategy = GenerationType.IDENTITY  // 需要数据库支持自增长
    )
    @Column(name="id")
    private Long id;
    @Column(name = "`name`")
    private String name;
    // 枚举类
    @Column(name="sex")
    @Enumerated(EnumType.ORDINAL)
    private Sex sex;
    // 使用Convert来映射枚举类
    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Column(name="age")
    private int age;
    @Lob    //setImage( *BlobProxy.generateProxy* (new byte[]{1, 2, 3}));
    private Blob image;
    @Column(name = "`birthday`")
    private LocalDate birthDay;
    @CreationTimestamp
    @Column(name="create_time")
    private LocalDateTime createTime;

    // 使用自定义的类型转换器
    @Type(type="hibernate.namestrategy.domain.PersonAddressUserType")
    @Columns(columns = {
            @Column(name = "address"),
            @Column(name = "phone")
    })
    private Address address;

    @Type(type="hibernate.namestrategy.customtype.BitSetUserType")
    private BitSet bitSet;

    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name = "id")
    private List<Account> accounts = new ArrayList<>();

    @Embedded
    private ZipCode zipCode;

//    @Formula(value ="age * sex")
    private long ageSex;

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getAgeSex() {
        return ageSex;
    }

    public void setAgeSex(long ageSex) {
        this.ageSex = ageSex;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public ZipCode getZipCode() {
        return zipCode;
    }

    public void setZipCode(ZipCode zipCode) {
        this.zipCode = zipCode;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BitSet getBitSet() {
        return bitSet;
    }

    public void setBitSet(BitSet bitSet) {
        this.bitSet = bitSet;
    }

    public static enum Sex{
        girl,
        boy
    }

    public static enum Gender {

        MALE( 'M' ),
        FEMALE( 'F' );

        private final char code;

        Gender(char code) {
            this.code = code;
        }

        public static Gender fromCode(char code) {
            if ( code == 'M' || code == 'm' ) {
                return MALE;
            }
            if ( code == 'F' || code == 'f' ) {
                return FEMALE;
            }
            throw new UnsupportedOperationException(
                    "The code " + code + " is not supported!"
            );
        }

        public char getCode() {
            return code;
        }
    }

    @Converter
    public static class GenderConverter implements AttributeConverter<Gender, Character> {

        @Override
        public Character convertToDatabaseColumn(Gender gender) {
            if (gender == null) return null;
            return gender.getCode();
        }

        @Override
        public Gender convertToEntityAttribute(Character character) {
            if (character == null) return null;
            return Gender.fromCode(character);
        }
    }



    @Embeddable
    public static class ZipCode {
        private String zipcode ;

        public ZipCode(){}

        public ZipCode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }
    }

    @Embeddable
    public static class Address {
        private String address;
        private String phone;
        public Address(){}

        public Address(String address, String phoneNumber) {
            this.address = address;
            this.phone = phoneNumber;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}

