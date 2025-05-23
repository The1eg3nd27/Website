package com.spectre.controller.tools;

import com.spectre.payload.tools.PlayerInfoDTO;
import com.spectre.security.services.tools.PlayerTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin(origins = "*")
public class PlayerTrackerController {

    @Autowired
    private PlayerTrackerService playerTrackerService;

    @GetMapping("/playerinfo")
    public PlayerInfoDTO getPlayerInfo(@RequestParam String name) {
        return playerTrackerService.fetchPlayerInfo(name);
    }
}
