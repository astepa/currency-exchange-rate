package app.repository;

import app.domain.CurrencyConversionsData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConversionHistoryRepository
		extends CrudRepository<CurrencyConversionsData, Integer>,
        JpaSpecificationExecutor<CurrencyConversionsData> {

	List<CurrencyConversionsData> findTop10ByOrderByIdDesc();
}