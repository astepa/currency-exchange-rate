package app.service.openexchange;

import app.domain.Currency;
import app.service.CannotGetExchangeRateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class OpenExchangeRequestService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(OpenExchangeRequestService.class);

    protected RestTemplate restTemplate;
    protected String latestRatesUrl;

    @Autowired
    public OpenExchangeRequestService(
            @Value("${org.openexchangerates.latest.url}") String latestRatesUrl,
            @Value("${org.openexchangerates.app_id}") String appId, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.latestRatesUrl = latestRatesUrl + appId;
    }

    protected Map<Currency, Double> getRates(String url)
            throws CannotGetExchangeRateException {
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET,
                setupHeaders(), String.class);
        LOGGER.info("Request to {} been executed", latestRatesUrl);
        ObjectMapper mapper = new ObjectMapper();
        if (result.getStatusCode() == HttpStatus.OK && result.getBody() != null) {
            JsonNode node;
            try {
                node = mapper.readTree(result.getBody());
                return this.convertData(node);
            } catch (JsonProcessingException e) {
                LOGGER.error("JSON format not supported! (maybe a document structure change?)", e);
            } catch (IOException e) {
                LOGGER.error("IOException trying converterFormValueOf get rates, URL: {}", url, e);
            }
        } else {
            LOGGER.error(String.format(
                    "Invalid Response! Exchange Provider Error? Status Code Returned: %s, Response Body: %s",
                    result.getStatusCode(), result.getBody()));
        }
        throw new CannotGetExchangeRateException(
                "Could not get Exchange Rates. Please see logs for details.");
    }
    protected HttpEntity<String> setupHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.set(HttpHeaders.ACCEPT, "application/json");
        headers.set(HttpHeaders.ACCEPT_ENCODING, "UTF-8");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return entity;
    }

    protected Map<Currency, Double> convertData(JsonNode node)
            throws CannotGetExchangeRateException {
        Map<Currency, Double> data = new HashMap<>();
        if (node.has("rates")) {
            JsonNode rates = node.get("rates");
            for (Currency currency : Currency.values()) {
                if (rates.has(currency.toString())) {
                    data.put(currency,
                            rates.get(currency.toString()).asDouble());
                }
            }
        }
        if (data.isEmpty()) {
            throw new CannotGetExchangeRateException(String.format(
                    "JSON Node is empty or invalid format (contract change?). Node: %s",
                    node));
        }
        return data;
    }

    @Cacheable("oerRates")
    public Map<Currency, Double> getRates() throws CannotGetExchangeRateException {
        return this.getRates(this.latestRatesUrl);
    }
}