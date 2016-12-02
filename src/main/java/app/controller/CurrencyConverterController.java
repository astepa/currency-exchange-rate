package app.controller;

import app.domain.Currency;
import app.domain.CurrencyConversionsData;
import app.validator.CurrencyConverterFormValidator;
import app.service.CannotGetExchangeRateException;
import app.service.ConversionHistoryService;
import app.service.RatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.Valid;
import java.util.*;

@Controller
public class CurrencyConverterController extends WebMvcConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CurrencyConverterController.class);

    private List<RatesService> providers;
    private CurrencyConverterFormValidator validator;
    private ConversionHistoryService history;

    @Autowired
    public CurrencyConverterController(@Qualifier("active") List<RatesService> providers,
                                       CurrencyConverterFormValidator validator, ConversionHistoryService history) {
        this.providers = providers;
        this.validator = validator;
        this.history = history;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(this.validator);
    }

    @ModelAttribute("currencies")
    protected List<Currency> getCurrencyValues() {
        List<Currency> list = Arrays.asList(Currency.values());
        Collections.sort(list, (o1, o2) -> o1.getRuName().compareTo(o2.getRuName()));
        return list;
    }

    protected void loadHistory(Model model) {
        model.addAttribute("history", history
                .findLatest10());
    }

    private String[] calculateExchangeValues(RatesService service, CurrencyConversionsData data) {
        try {
            String c1 = service.convertAmount(data, 1d).getFormattedRate();
            String c2 = service.convertAmount(data).getFormattedRate();
            return new String[]{c1, c2};
        } catch (CannotGetExchangeRateException e) {
            LOGGER.error("Fail to get rates from {}", service.providerName());
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("converter", new CurrencyConversionsData());
        loadHistory(model);
        return "index";
    }

    @PostMapping("/")
    public String convert(
            @Valid @ModelAttribute("converter") CurrencyConversionsData data,
            BindingResult result, Model model) {
        loadHistory(model);

        if (!result.hasErrors()) {
            model.addAttribute("sourceCurrency", data.getSourceCurrency());
            model.addAttribute("targetCurrency", data.getTargetCurrency());
            boolean isConversionSaved = false;
            Map<String, String[]> rateSourcesResult = new HashMap<>();
            for (RatesService service : providers) {
                String[] providerResult = calculateExchangeValues(service, data);
                if (providerResult == null) continue;
                if (!isConversionSaved) {
                    history.saveToDb(data);
                    isConversionSaved = true;
                }
                rateSourcesResult.put(service.providerName(), new String[]{providerResult[0], providerResult[1]});
            }
            if (rateSourcesResult.isEmpty()) {
                LOGGER.error("Something wrong getting rates");
                result.reject("exchange.error");
            }
            model.addAttribute("rateSourcesResult", rateSourcesResult);
        }
        return "index";
    }
}