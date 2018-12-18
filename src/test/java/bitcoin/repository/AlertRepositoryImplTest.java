package bitcoin.repository;


import bitcoin.TestDomainObjectFactory;
import bitcoin.repository.impl.AlertRepositoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class AlertRepositoryImplTest {

    private AlertRepository alertRepository;


    @Before
    public void setUp() {
        alertRepository = new AlertRepositoryImpl();
    }

    @Test
    public void save() {
        CurrencyPair currencyPair = TestDomainObjectFactory.getCurrencyPair();
        BigDecimal limit = TestDomainObjectFactory.getLimit();
        BigDecimal value = alertRepository.get(currencyPair);
        Assert.assertNull(value);
        alertRepository.save(currencyPair,limit);
        value = alertRepository.get(currencyPair);
        Assert.assertNotNull(value);
        Assert.assertEquals(limit,value);
    }

    @Test
    public void delete() {
        CurrencyPair currencyPair = TestDomainObjectFactory.getCurrencyPair();
        BigDecimal limit = TestDomainObjectFactory.getLimit();
        Assert.assertFalse(alertRepository.delete(currencyPair, limit));
        alertRepository.save(currencyPair,limit);
        Assert.assertNotNull(alertRepository.get(currencyPair));
        Assert.assertFalse(alertRepository.delete(currencyPair, limit.add(new BigDecimal(1))));
        Assert.assertNotNull(alertRepository.get(currencyPair));
        Assert.assertTrue(alertRepository.delete(currencyPair, limit));
        Assert.assertNull(alertRepository.get(currencyPair));
    }

    @Test
    public void findAll() {
        Assert.assertEquals(0, alertRepository.findAll().size());
        alertRepository.save(TestDomainObjectFactory.getCurrencyPair(),TestDomainObjectFactory.getLimit());
        Assert.assertEquals(1, alertRepository.findAll().size());
        alertRepository.save(TestDomainObjectFactory.getCurrencyPair(),TestDomainObjectFactory.getLimit());
        Assert.assertEquals(2, alertRepository.findAll().size());
        alertRepository.save(TestDomainObjectFactory.getCurrencyPair(),TestDomainObjectFactory.getLimit());
        Assert.assertEquals(3, alertRepository.findAll().size());
        alertRepository.save(TestDomainObjectFactory.getCurrencyPair(),TestDomainObjectFactory.getLimit());
        Assert.assertEquals(4, alertRepository.findAll().size());


    }
}
