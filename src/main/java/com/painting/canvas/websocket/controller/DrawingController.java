package com.painting.canvas.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DrawingController {

    @MessageMapping("/draw")
    @SendTo("/topic/draw")
    public String draw(String message) {
        return message;
    }

}
