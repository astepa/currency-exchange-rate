package app.service;

import app.domain.CurrencyConversionsData;
import app.repository.ConversionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversionHistoryService {

    protected ConversionHistoryRepository repository;

    @Autowired
    public ConversionHistoryService(ConversionHistoryRepository repository) {
        this.repository = repository;
    }

    public List<CurrencyConversionsData> findLatest10() {
        return this.repository.findTop10ByOrderByIdDesc();
    }

    public void saveToDb(CurrencyConversionsData historyData) {
        repository.save(historyData);
    }

}
