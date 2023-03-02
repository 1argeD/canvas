package com.painting.canvas.config;

import com.painting.canvas.websocket.hendler.DrawingHandler;
import org.springframework.context.annotation.Configuration;


import org.springframework.web.socket.config.annotation.*;



@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new DrawingHandler(), "/drawing").setAllowedOrigins("*");
    }

}
