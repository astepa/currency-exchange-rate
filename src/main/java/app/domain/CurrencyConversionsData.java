package app.domain;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.RoundingMode;
import java.text.NumberFormat;

@Entity
@Table(name = "CURRENCY_CONVERSION")
public class CurrencyConversionsData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Column(name = "source_currency")
    private Currency sourceCurrency;
    @NotNull
    @Column(name = "target_currency")
    private Currency targetCurrency;
    @NotNull
    @Digits(integer = 6, fraction = 2)
    private double amount;
    private double rate;

    public CurrencyConversionsData() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Currency getSourceCurrency() {
        return this.sourceCurrency;
    }

    public void setSourceCurrency(Currency sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public Currency getTargetCurrency() {
        return this.targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRate() {
        return this.rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getFormattedAmount() {
        return format(this.getAmount());
    }

    public String getFormattedRate() {
        return format(this.getRate());
    }

    private String format(double value) {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(4);
        formatter.setMinimumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        return formatter.format(value);
    }
}