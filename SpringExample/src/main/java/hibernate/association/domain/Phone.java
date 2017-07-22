package hibernate.association.domain;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * Created by admin on 2017/3/20.
 */
@Entity(name = "Phone")
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue
    private Long id;
    @NaturalId
    @Column(name = "`number`")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "PERSON_ID_FK"))
    private Person person;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
