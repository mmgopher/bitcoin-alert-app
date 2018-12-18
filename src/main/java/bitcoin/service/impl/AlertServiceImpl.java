package bitcoin.service.impl;

import bitcoin.domain.api.Mappings;
import bitcoin.domain.dto.Alert;
import bitcoin.exception.InsertAlertException;
import bitcoin.repository.AlertRepository;
import bitcoin.service.AlertService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class AlertServiceImpl implements AlertService {

    private MarketDataService marketDataService;
    private AlertRepository alertRepository;

    @Autowired
    public AlertServiceImpl(MarketDataService marketDataService, AlertRepository alertRepository) {
        this.marketDataService = marketDataService;
        this.alertRepository = alertRepository;
    }

    @Override
    public void addAlert(String pair, Double limit) throws InsertAlertException {
        CurrencyPair currencyPair = parseCurrencyPair(pair);

        if (limit.compareTo(0d) <= 0) {
            throw new InsertAlertException("Limit must be greater than 0");
        }

        BigDecimal limitValue = new BigDecimal(limit);
        alertRepository.save(currencyPair, limitValue);

    }

    @Override
    public boolean deleteAlert(String pair, Double limit) throws InsertAlertException {
        CurrencyPair currencyPair = parseCurrencyPair(pair);
        if (limit.compareTo(0d) <= 0) {
            throw new InsertAlertException("Limit must be greater than 0");
        }
        BigDecimal limitValue = new BigDecimal(limit);
        return alertRepository.delete(currencyPair, limitValue);
    }

    @Override
    public void sendAlert(SimpMessagingTemplate template) {
         for (Map.Entry<CurrencyPair, BigDecimal> entry : alertRepository.findAll().entrySet()) {
            Ticker ticker = null;
            try {
                ticker = marketDataService.getTicker(entry.getKey());
                if (ticker.getLast().compareTo(entry.getValue()) > 0) {
                    template.convertAndSend(Mappings.WEB_SOCKET_DESTINATION, new Alert(ticker, entry.getValue()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private CurrencyPair parseCurrencyPair(String pair) throws InsertAlertException {
        try {
            CurrencyPair currencyPair = new CurrencyPair(pair.replace("-", "/"));
            return currencyPair;
        } catch (IllegalArgumentException e) {
            throw new InsertAlertException(e.getMessage());

        }
    }


}
