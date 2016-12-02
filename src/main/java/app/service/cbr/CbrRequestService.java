package app.service.cbr;

import app.domain.Currency;
import app.service.CannotGetExchangeRateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class CbrRequestService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CbrRequestService.class);

    protected RestTemplate restTemplate;
    protected String latestRatesUrl;

    @Autowired
    public CbrRequestService(@Value("${ru.cbr.latest.url}") String latestRatesUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.latestRatesUrl = latestRatesUrl;
    }

    protected Map<Currency, Double> getRates(String url)
            throws CannotGetExchangeRateException {

        ResponseEntity<CbrRatesResponse> result = null;
        try {
            result = restTemplate.getForEntity(url, CbrRatesResponse.class);
            LOGGER.info("Request to {} has been executed", url);
        } catch (Exception e) {
            LOGGER.error("Request to {} has been executed with Exception", url);
        }
        if (result != null && result.getStatusCode() == HttpStatus.OK && result.getBody() != null) {
            return convertData(result.getBody().getItems());
        }
        throw new CannotGetExchangeRateException(
                "Could not get Exchange Rates");
    }

    protected Map<Currency, Double> convertData(List<CbrCurrencyRate> currencyRateList)
            throws CannotGetExchangeRateException {
        Map<Currency, Double> data = new HashMap<>();
        NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
        for (Currency currency : Currency.values()) {
            for (CbrCurrencyRate r : currencyRateList) {
                if (r.getCharCode().equals(currency.toString())) {
                    try {
                        double rate = nf.parse(r.getRate()).doubleValue();
                        double nominal = nf.parse(r.getNominal()).doubleValue();
                        data.put(currency, rate / nominal);
                    } catch (ParseException e) {
                        LOGGER.error("XML parsing failed. Can't convertAmount value to double");
                    }
                }
            }
        }
        if (data.isEmpty()) {
            throw new CannotGetExchangeRateException("XML is empty or invalid format");
        }
        data.put(Currency.RUB, 1d);
        return data;
    }

    @Cacheable("cbrRates")
    public Map<Currency, Double> getRates() throws CannotGetExchangeRateException {
        return this.getRates(this.latestRatesUrl);
    }
}
