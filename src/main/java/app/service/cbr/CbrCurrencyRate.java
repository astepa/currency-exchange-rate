package app.service.cbr;

import javax.xml.bind.annotation.*;

@XmlType(name = "Valute")
@XmlAccessorType(XmlAccessType.NONE)
public class CbrCurrencyRate {

    @XmlElement(name = "CharCode")
    private String charCode;

    @XmlElement(name = "Nominal")
    private String nominal;

    @XmlElement(name = "Value")
    private String rate;

    public CbrCurrencyRate() {
    }

    public CbrCurrencyRate(String charCode, String nominal, String rate) {
        this.charCode = charCode;
        this.nominal = nominal;
        this.rate = rate;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
