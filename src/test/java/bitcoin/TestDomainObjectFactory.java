package bitcoin;

import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestDomainObjectFactory {


    private static List<CurrencyPair> currencyPairs = new ArrayList<>();

    static {
        currencyPairs.add(CurrencyPair.BTC_USD);
        currencyPairs.add(CurrencyPair.BTC_GBP);
        currencyPairs.add(CurrencyPair.BTC_EUR);
        currencyPairs.add(CurrencyPair.BTC_JPY);
        currencyPairs.add(CurrencyPair.BTC_CHF);
        currencyPairs.add(CurrencyPair.BTC_PLN);
        currencyPairs.add(CurrencyPair.BTC_HKD);
        currencyPairs.add(CurrencyPair.BTC_NOK);
        currencyPairs.add(CurrencyPair.BTC_CZK);
        currencyPairs.add(CurrencyPair.BTC_PHP);
        currencyPairs.add(CurrencyPair.BTC_TRY);
    }

    public static CurrencyPair getCurrencyPair() {
        return currencyPairs.get(new Random().nextInt(currencyPairs.size()));
    }

    public static String getCurrencyPairString() {
        return getCurrencyPair().toString();
    }

    public static BigDecimal getLimit() {
        return new BigDecimal((new Random().nextDouble() * 5000));
    }
}
