package nl.hva.miw.pirate_bank_setup.repository.account;



import com.fasterxml.jackson.annotation.JsonBackReference;
import nl.hva.miw.pirate_bank_setup.repository.user.Customer;

import java.math.BigDecimal;

public class Account {
    @JsonBackReference
    private Customer customer;

    private BigDecimal balance;

    public Account(Customer customer, BigDecimal balance) {
        this.customer = customer;
        this.balance = balance;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getBalance() {
        return balance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!customer.equals(account.customer)) return false;
        return balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        int result = customer.hashCode();
        result = 31 * result + balance.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "Het saldo van de geldrekening is: " + balance +
                '}';
    }
}
