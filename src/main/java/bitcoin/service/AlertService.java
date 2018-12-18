package bitcoin.service;

import bitcoin.exception.InsertAlertException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public interface AlertService {
    void addAlert(String pair, Double limit) throws InsertAlertException;

    boolean deleteAlert(String pair, Double limit) throws InsertAlertException;

    void sendAlert(SimpMessagingTemplate template);
}
