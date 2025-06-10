package com.spectre.controller.tools;

import com.spectre.payload.tools.CommodityRequestDto;
import com.spectre.payload.tools.CommodityResponseDto;
import com.spectre.payload.tools.DiscordEventDto;
import com.spectre.payload.tools.PlayerInfoDTO;
import com.spectre.payload.tools.ShipCompareRequestDto;
import com.spectre.payload.tools.ShipComparisonResultDto;
import com.spectre.payload.tools.ShipDetailsDto;
import com.spectre.payload.tools.TradeRouteResponseDto;
import com.spectre.security.services.tools.CommodityService;
import com.spectre.security.services.tools.DiscordEventService;
import com.spectre.security.services.tools.PlayerTrackerService;
import com.spectre.security.services.tools.ShipComparisonService;
import com.spectre.security.services.tools.ShipInfoService;
import com.spectre.security.services.tools.TradeRouteService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tools")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ToolsController {

    private final TradeRouteService tradeRouteService;

    private final ShipInfoService shipInfoService;
    private final PlayerTrackerService playerTrackerService;
    private final DiscordEventService discordEventService;
    private final ShipComparisonService shipComparisonService;
    private final CommodityService commodityService;


    
    @PostMapping("/tradeRoute")
    public ResponseEntity<TradeRouteResponseDto> calculateRoute(@RequestBody CommodityRequestDto request) {
        return ResponseEntity.ok(tradeRouteService.calculateTradeRoute(request));
    }

    @PostMapping("/shipcompare")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ShipComparisonResultDto> compareShips(@RequestBody ShipCompareRequestDto request) {
        if (request.getShipA() == null || request.getShipB() == null) {
            return ResponseEntity.badRequest().build();
        }

        ShipComparisonResultDto result = shipComparisonService.compareShips(request.getShipA(), request.getShipB());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/shipinfo")
    public ResponseEntity<ShipDetailsDto> getShipInfo(@RequestParam String name) {
        return ResponseEntity.ok(shipInfoService.getDetailsByName(name));
    }

    @GetMapping("/playerinfo")
    public ResponseEntity<PlayerInfoDTO> getPlayerInfo(@RequestParam String name) {
        return ResponseEntity.ok(playerTrackerService.getPlayerInfo(name));
    }

    @GetMapping("/events")
    public ResponseEntity<List<DiscordEventDto>> getEvents() {
        return ResponseEntity.ok(discordEventService.fetchEvents());
    }

    @GetMapping("/shipnames")
    public ResponseEntity<List<String>> getAllShipNames() {
        return ResponseEntity.ok(shipComparisonService.getAllShipNames());
    }
    @PostMapping("/shipinfo")
    public ResponseEntity<ShipDetailsDto> getShipInfo(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        ShipDetailsDto details = shipComparisonService.getShipDetails(name);
        return ResponseEntity.ok(details);
    }

    @PostMapping("/commodities")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CommodityResponseDto> getCommodityData(@RequestBody CommodityRequestDto request) {
        System.out.println("🔥 Controller called! Got request for: " + request.getCommodity());

        if (request.getCommodity() == null || request.getCommodity().isBlank()) {
            CommodityResponseDto error = CommodityResponseDto.builder()
                .fallbackInfo("Commodity name is required.")
                .build();
            return ResponseEntity.badRequest().body(error);
        }

        return ResponseEntity.ok(
            commodityService.getBestSellInfo(request.getCommodity(), request.getQuantity())
        );
    }
    
    @GetMapping("/commodities/names")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<String>> getCommodityNames() {
        return ResponseEntity.ok(commodityService.getAllCommodityNames());
    }

}

