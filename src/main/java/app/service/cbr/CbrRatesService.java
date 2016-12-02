package app.service.cbr;

import app.domain.Currency;
import app.service.RatesService;
import app.service.CannotGetExchangeRateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("active")
public class CbrRatesService implements RatesService {

    public final String VISIBLE_PROVIDER_NAME = "ЦБ РФ";
    private CbrRequestService cbrRequestService;

    @Autowired
    public CbrRatesService(CbrRequestService cbrRequestService) {
        this.cbrRequestService = cbrRequestService;
    }

    @Override
    public String providerName() {
        return VISIBLE_PROVIDER_NAME;
    }

    @Override
    public double convertCurrency(Currency from, Currency to, double amount) throws CannotGetExchangeRateException {
        double sourceToRubleRate = cbrRequestService.getRates().get(from);
        double targetToRubleRate = cbrRequestService.getRates().get(to);
        return amount / targetToRubleRate * sourceToRubleRate;
    }
}
