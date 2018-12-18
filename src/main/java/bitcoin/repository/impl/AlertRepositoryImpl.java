package bitcoin.repository.impl;

import bitcoin.repository.AlertRepository;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AlertRepositoryImpl implements AlertRepository {

    private Map<CurrencyPair, BigDecimal> pairsMap = new ConcurrentHashMap<>();

    @Override
    public BigDecimal save(CurrencyPair pair, BigDecimal value) {
        pairsMap.put(pair, value);
        return pairsMap.get(pair);
    }

    @Override
    public BigDecimal get(CurrencyPair pair) {
        return pairsMap.get(pair);
    }

    @Override
    public Map<CurrencyPair, BigDecimal> findAll() {
        return pairsMap;
    }

    @Override
    public boolean delete(CurrencyPair pair, BigDecimal value) {
       return pairsMap.remove(pair, value);
    }
}
