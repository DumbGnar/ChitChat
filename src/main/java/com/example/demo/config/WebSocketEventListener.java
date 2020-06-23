package com.example.demo.config;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @EventListener
    public void handleConnectListener(SessionConnectedEvent event) {
        System.out.println("[ws-connected] socket connect: " + event.getMessage());
    }

    @EventListener
    public void handleDisconnectListener(SessionDisconnectEvent event) {
        System.out.println("[ws-disconnected] socket disconnect: " + event.getMessage());
    }
}
