package app.service;

import app.domain.Currency;
import app.domain.CurrencyConversionsData;

public interface RatesService {

    double convertCurrency(Currency from, Currency to, double amount)
            throws CannotGetExchangeRateException;

    String providerName();

    default CurrencyConversionsData convertAmount(CurrencyConversionsData form, Double sourceCurrencyAmount) throws CannotGetExchangeRateException {
        Double rate = convertCurrency(form.getSourceCurrency(), form.getTargetCurrency(), sourceCurrencyAmount);
        form.setRate(rate);
        return form;
    }

    default CurrencyConversionsData convertAmount(CurrencyConversionsData form) throws CannotGetExchangeRateException {
        return convertAmount(form,form.getAmount());
    }
}
