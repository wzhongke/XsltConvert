package hibernate.domain.embedded;

import javax.persistence.*;

/**
 * Created by wangzhognke on 2017/7/27.
 * 多个嵌入类型实例
 */
@Entity
public class Contact {

    @Id
    private Integer id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
                name = "line1",
                column = @Column( name = "home_address_line1" )
        ),
        @AttributeOverride(
                name = "zipCode.postalCode",
                column = @Column( name = "home_address_postal_cd" )
        ),
        @AttributeOverride(
                name = "zipCode.plus4",
                column = @Column( name = "home_address_postal_plus4" )
        )
    })
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
                name = "line1",
                column = @Column(name = "mailing_address_line1")
        ),
        @AttributeOverride(
                name = "zipCode.postalCode",
                column = @Column(name = "mailing_address_postal_cd")
        ),
        @AttributeOverride(
                name = "zipCode.plus4",
                column = @Column(name = "mailing_address_postal_plus4")
        )
    })
    private Address mailingAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(
                name = "line1",
                column = @Column(name = "work_address_line1")
        ),
        @AttributeOverride(
                name = "zipCode.postalCode",
                column = @Column(name = "work_address_postal_cd")
        ),
        @AttributeOverride(
                name = "zipCode.plus4",
                column = @Column(name = "work_address_postal_plus4")
        )
    })
    private Address workAddress;

    @Embeddable
    static class Address {
        private String line1;
        private String postalCd;
        private String postalPlus4;
    }
}
