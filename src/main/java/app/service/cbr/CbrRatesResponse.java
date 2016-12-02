package app.service.cbr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.NONE)
public class CbrRatesResponse {

    @XmlElement(name = "Valute")
    private List<CbrCurrencyRate> items;

    public List<CbrCurrencyRate> getItems() {
        return items;
    }

    public void setItems(List<CbrCurrencyRate> items) {
        this.items = items;
    }
}
