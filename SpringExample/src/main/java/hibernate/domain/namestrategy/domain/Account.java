package hibernate.domain.namestrategy.domain;

import javax.persistence.*;

@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="card_number")
    private String cardNumber;

    @Column(name="amount")
    private String amount;

    @Column(name="type")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    public Account() {}

    public Account(String cardNumber, String amount, AccountType type) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public enum AccountType {
        DEBIT,
        CREDIT
    }
}
