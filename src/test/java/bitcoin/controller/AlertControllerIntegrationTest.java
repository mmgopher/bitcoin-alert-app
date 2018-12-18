package bitcoin.controller;


import bitcoin.domain.api.Mappings;
import bitcoin.exception.InsertAlertException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AlertControllerIntegrationTest extends AbstractAlertController {

    @Autowired
    AlertController alertController;

    @Override
    protected AlertController getController() {
        return this.alertController;
    }

    @Test
    public void addAlertBadRequest() throws Exception {
        MvcResult mvcResult = performPut(Mappings.ALERT);
        assertEquals(HttpStatus.BAD_REQUEST, getHttpStatus(mvcResult));
    }

    @Test
    public void addAlertOk() throws Exception {
        MvcResult mvcResult = performPut(Mappings.ALERT + "?pair=BTC/USD&limit=3000");
        assertEquals(HttpStatus.OK, getHttpStatus(mvcResult));
    }

    @Test
    public void addAlertNotFound() throws Exception, InsertAlertException {
        MvcResult mvcResult = performPut(Mappings.ALERT + "?pair=BTC_USD&limit=3000");
        assertEquals(HttpStatus.NOT_FOUND, getHttpStatus(mvcResult));
    }

    @Test
    public void deleteAlertBadRequest() throws Exception {
        MvcResult mvcResult = performDelete(Mappings.ALERT);
        assertEquals(HttpStatus.BAD_REQUEST, getHttpStatus(mvcResult));
    }

    @Test
    public void deleteAlertOk() throws Exception, InsertAlertException {
        performPut(Mappings.ALERT + "?pair=BTC/USD&limit=3000");
        MvcResult mvcResult = performDelete(Mappings.ALERT + "?pair=BTC/USD&limit=3000");
        assertEquals(HttpStatus.OK, getHttpStatus(mvcResult));
    }

    @Test
    public void deleteAlertNotFound() throws Exception, InsertAlertException {
        MvcResult mvcResult = performDelete(Mappings.ALERT + "?pair=BTC_USD&limit=3000");
        assertEquals (HttpStatus.NOT_FOUND, getHttpStatus(mvcResult));
    }
}
