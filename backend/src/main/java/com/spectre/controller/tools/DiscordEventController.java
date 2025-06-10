package com.spectre.controller.tools;

import com.spectre.payload.tools.DiscordEventDto;
import com.spectre.security.services.tools.DiscordEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tools")
public class DiscordEventController {

    @Autowired
    private DiscordEventService discordEventService;



    @GetMapping @PostMapping("/events")
    public ResponseEntity<?> getEvents() {
        try {
            List<DiscordEventDto> events = discordEventService.getScheduledEvents();
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "Discord API failed", "message", e.getMessage()));
        }
    }
}
