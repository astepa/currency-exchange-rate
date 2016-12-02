package app.service.openexchange;

import app.domain.Currency;
import app.service.CannotGetExchangeRateException;
import app.service.RatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Qualifier("active")
public class OpenExchangeRatesService implements RatesService {

    public final String VISIBLE_PROVIDER_NAME = "https://openexchangerates.org/";
    protected OpenExchangeRequestService requestService;

    @Autowired
    public OpenExchangeRatesService(OpenExchangeRequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    public String providerName() {
        return VISIBLE_PROVIDER_NAME;
    }

    public double convertCurrency(Currency from, Currency to, double amount)
            throws CannotGetExchangeRateException {
        double usdToSourceCurrencyRate = requestService.getRates().get(from);
        double usdToTargetCurrencyRate = requestService.getRates().get(to);
        return usdToTargetCurrencyRate / usdToSourceCurrencyRate * amount;
    }
}