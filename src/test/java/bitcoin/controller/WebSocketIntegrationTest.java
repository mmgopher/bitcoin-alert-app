package bitcoin.controller;


import bitcoin.domain.api.Mappings;
import bitcoin.domain.dto.Alert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketIntegrationTest extends AbstractAlertController {

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    @Autowired
    AlertController alertController;
    @LocalServerPort
    private int port;
    private SockJsClient sockJsClient;
    private WebSocketStompClient stompClient;

    @Before
    public void setup() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.sockJsClient = new SockJsClient(transports);

        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }


    @Test
    public void getAlert() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();

        StompSessionHandler handler = new WebSocketIntegrationTest.TestSessionHandler(failure) {

            @Override
            public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
                session.subscribe("/bitcoin/alerts", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Alert.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        Alert alert = (Alert) payload;
                        try {
                            assertEquals("BTC/USD", alert.getCurrencyPair());
                            assertEquals(0, Double.compare(10, alert.getLimit()));
                        } catch (Throwable t) {
                            failure.set(t);
                        } finally {
                            session.disconnect();
                            latch.countDown();
                        }
                    }
                });

            }
        };

        this.stompClient.connect("ws://localhost:{port}/bitcoin-alert-websocket", this.headers, handler, this.port);

        performPut(Mappings.ALERT + "?pair=BTC/USD&limit=10");

        if (latch.await(15, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        } else {
            fail("Alert not received");
        }

    }

    @Override
    protected AlertController getController() {
        return this.alertController;
    }

    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<Throwable> failure;


        public TestSessionHandler(AtomicReference<Throwable> failure) {
            this.failure = failure;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new Exception(headers.toString()));
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            this.failure.set(ex);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable ex) {
            this.failure.set(ex);
        }
    }
}
