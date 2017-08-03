package hibernate.domain.formula;

import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Id;

//@Where(clause = "rate > 1.0")
@FilterDef(name="activeAccount", parameters=@ParamDef( name="active", type="boolean" ) )
@Filter(name="activeAccount", condition="active = :active")
@Entity(name = "Account")
public class Account {
    @Id
    private Long id;
    @Where( clause = "credit > 0")
    private Double credit;

    private Double rate;

    // Formula中使用了SQL语句，会有数据库兼容性问题
    @Formula(value = "credit * rate")
    private Double interest;

    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
