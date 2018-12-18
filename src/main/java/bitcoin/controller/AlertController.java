package bitcoin.controller;

import bitcoin.domain.api.Mappings;
import bitcoin.exception.InsertAlertException;
import bitcoin.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;


@EnableScheduling
@Controller
public class AlertController {

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
    private AlertService alertService;
    private SimpMessagingTemplate template;


    @Autowired
    AlertController(AlertService alertService, SimpMessagingTemplate template) {
        this.alertService = alertService;
        this.template = template;
    }


    @Scheduled(fixedRate = 5000)
    public void sendAlert() throws Exception {
        alertService.sendAlert(this.template);
    }

    @RequestMapping(value = Mappings.ALERT, method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void addAlert(@RequestParam("pair") String pair, @RequestParam("limit") Double limit, HttpServletResponse response) {
        logger.info("Add new alert currency: "+pair+"limit: "+limit);
        try {
            alertService.addAlert(pair, limit);
        } catch (InsertAlertException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cannot parse request parameters", e);
        }
    }

    @RequestMapping(value = Mappings.ALERT, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteAlert(@RequestParam("pair") String pair, @RequestParam("limit") Double limit, HttpServletResponse response) {
        logger.info("Remove alert currency: "+pair+"limit: "+limit);
        try {
            if (!alertService.deleteAlert(pair, limit)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Alert to remove not found");
            }
        } catch (InsertAlertException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cannot parse request parameters", e);
        }
    }
}
