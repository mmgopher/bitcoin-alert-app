package bitcoin.controller;

import bitcoin.domain.api.Mappings;
import bitcoin.exception.InsertAlertException;
import bitcoin.service.AlertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class AlertControllerTest extends AbstractAlertController {

    @MockBean
    private AlertService alertService;

    @MockBean
    private SimpMessagingTemplate template;


    @Override
    protected AlertController getController() {
        return new AlertController(alertService, template);
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
        doThrow(new InsertAlertException("Can not parse currency pair")).when(alertService).addAlert(any(String.class), any(Double.class));
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
        when(alertService.deleteAlert(any(String.class), any(Double.class))).thenReturn(true);
        MvcResult mvcResult = performDelete(Mappings.ALERT + "?pair=BTC/USD&limit=3000");
        assertEquals(HttpStatus.OK, getHttpStatus(mvcResult));
    }

    @Test
    public void deleteAlertNotFound() throws Exception, InsertAlertException {
        when(alertService.deleteAlert(any(String.class), any(Double.class))).thenReturn(false);
        MvcResult mvcResult = performDelete(Mappings.ALERT + "?pair=BTC_USD&limit=3000");
        assertEquals(HttpStatus.NOT_FOUND, getHttpStatus(mvcResult));
    }


}
