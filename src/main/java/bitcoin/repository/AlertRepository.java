package bitcoin.repository;

import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;
import java.util.Map;

public interface AlertRepository {
    BigDecimal save(CurrencyPair pair, BigDecimal value);

    BigDecimal get(CurrencyPair pair);

    Map<CurrencyPair, BigDecimal> findAll();

    boolean delete(CurrencyPair pair, BigDecimal value);
}
