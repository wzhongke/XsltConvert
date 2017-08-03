package hibernate.domain.namestrategy.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;

/**
 * Created by admin on 2017/2/27.
 */
@Entity(name = "test")
public class TestData {
    @Basic(optional = false, fetch = FetchType.LAZY)
    private int id;

}
