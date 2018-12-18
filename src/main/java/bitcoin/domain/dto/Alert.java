package bitcoin.domain.dto;

import org.knowm.xchange.dto.marketdata.Ticker;

import java.math.BigDecimal;
import java.util.Date;

public class Alert {


    private String currencyPair;
    private double limit;
    private double current;
    private Date timestamp;

    public Alert() {
    }

    public Alert(Ticker ticker, BigDecimal limit) {
        this.currencyPair = ticker.getCurrencyPair().toString();
        this.current = ticker.getLast().doubleValue();
        this.timestamp = ticker.getTimestamp();
        this.limit = limit.doubleValue();
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
