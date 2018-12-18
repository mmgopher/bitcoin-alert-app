package bitcoin.service;

import bitcoin.TestDomainObjectFactory;
import bitcoin.exception.InsertAlertException;
import bitcoin.repository.AlertRepository;
import bitcoin.service.impl.AlertServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class AlertServiceImplTest {

    @MockBean
    private MarketDataService marketDataService;

    @MockBean
    private AlertRepository alertRepository;

    private AlertService alertService;

    private static final String UNPARSABLE_CURRENCY_PAIR="BTC_EBD";

    @Before
    public void setUp() {
        alertService = new AlertServiceImpl(marketDataService,alertRepository);
    }

    @Test
    public void addAlert() throws InsertAlertException {
        CurrencyPair currencyPair = TestDomainObjectFactory.getCurrencyPair();
        BigDecimal limit = TestDomainObjectFactory.getLimit();
        alertService.addAlert(currencyPair.toString(),limit.doubleValue());
        verify(alertRepository, times(1)).save(currencyPair,limit);
    }

    @Test
    public void deleteAlert() throws InsertAlertException {
        CurrencyPair currencyPair = TestDomainObjectFactory.getCurrencyPair();
        BigDecimal limit = TestDomainObjectFactory.getLimit();
        when(alertRepository.delete(currencyPair,limit)).thenReturn(true);
        assertTrue(alertService.deleteAlert(currencyPair.toString(),limit.doubleValue()));
        verify(alertRepository, times(1)).delete(currencyPair,limit);
    }

    @Test(expected = InsertAlertException.class)
    public void addAlertWrongCurrencyPair() throws InsertAlertException {
        BigDecimal limit = TestDomainObjectFactory.getLimit();
        alertService.addAlert(UNPARSABLE_CURRENCY_PAIR,limit.doubleValue());
    }

    @Test(expected = InsertAlertException.class)
    public void addAlertLimitZero() throws InsertAlertException {
        CurrencyPair currencyPair = TestDomainObjectFactory.getCurrencyPair();
        alertService.addAlert(UNPARSABLE_CURRENCY_PAIR,0d);
    }

    @Test(expected = InsertAlertException.class)
    public void deleteAlertWrongCurrencyPair() throws InsertAlertException {
        BigDecimal limit = TestDomainObjectFactory.getLimit();
        alertService.deleteAlert(UNPARSABLE_CURRENCY_PAIR,limit.doubleValue());
    }

    @Test(expected = InsertAlertException.class)
    public void deleteAlertLimitZero() throws InsertAlertException {
        CurrencyPair currencyPair = TestDomainObjectFactory.getCurrencyPair();
        alertService.deleteAlert(UNPARSABLE_CURRENCY_PAIR,0d);
    }

    @Test
    public void deleteAlertWhichNotExist() throws InsertAlertException {

        CurrencyPair currencyPair = TestDomainObjectFactory.getCurrencyPair();
        BigDecimal limit = TestDomainObjectFactory.getLimit();
        when(alertRepository.delete(currencyPair,limit)).thenReturn(false);
        assertFalse(alertService.deleteAlert(currencyPair.toString(),limit.doubleValue()));
    }
}
