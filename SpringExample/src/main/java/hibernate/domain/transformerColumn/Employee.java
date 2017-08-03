package hibernate.domain.transformerColumn;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by admin on 2017/7/23.
 */
@Entity(name = "Employee")
public class Employee {
    @Id
    private Long id;

    @NaturalId
    private String username;

    @Column(name = "pswd")
    @ColumnTransformer(
            read = "decrypt( 'AES', '00', pswd  )",
            write = "encrypt('AES', '00', ?)"
    )
    private String password;

    private int accessLevel;
    @Type(type = "hibernate.domain.transformerColumn.Employee.MonetaryAmountUserType")
    @Columns(columns = {
            @Column(name = "money"),
            @Column(name = "currency")
    })
    @ColumnTransformer(
            forColumn = "money",
            read = "money / 100",
            write = "? * 100"
    )
    private MonetaryAmount wallet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static class MonetaryAmount {
        private long money;
        private String currency;

        public long getMoney() {
            return money;
        }

        public void setMoney(long money) {
            this.money = money;
        }
    }
}
