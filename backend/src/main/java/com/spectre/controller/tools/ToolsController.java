package com.spectre.controller.tools;


import com.spectre.payload.tools.CommodityRequestDto;
import com.spectre.payload.tools.CommodityResponseDto;
import com.spectre.payload.tools.EarningsRequestDTO;
import com.spectre.payload.tools.EarningsResponseDTO;
import com.spectre.payload.tools.ShipCompareRequestDto;
import com.spectre.payload.tools.ShipComparisonResultDto;
import com.spectre.payload.tools.ShipDetailsDto;
import com.spectre.payload.tools.TradeRouteRequestDto;
import com.spectre.payload.tools.TradeRouteResponseDto;
import com.spectre.security.services.ApiShipToShipSyncService;
import com.spectre.security.services.tools.CommodityService;
import com.spectre.security.services.tools.EarningsCalculatorService;
import com.spectre.security.services.tools.ShipComparisonService;
import com.spectre.security.services.tools.ShipInfoService;
import com.spectre.security.services.tools.TradeRouteService;
import com.spectre.cache.ShipCache;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin(origins = "*")
public class ToolsController {

    @Autowired
    private EarningsCalculatorService earningsService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private TradeRouteService tradeRouteService;

    @Autowired
    private ShipComparisonService shipComparisonService;
    
    @Autowired
    private ShipInfoService shipInfoService;

    @Autowired
    private ShipCache shipCache;

    @Autowired
    private ApiShipToShipSyncService apiShipToShipSyncService;

    
    @PostMapping("/earnings")
    public ResponseEntity<EarningsResponseDTO> calculateEarnings(@RequestBody EarningsRequestDTO request) {
        EarningsResponseDTO result = earningsService.calculateEarnings(request);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/commodities")
    public ResponseEntity<CommodityResponseDto> getCommodity(@RequestParam String name,
                                                             @RequestParam(defaultValue = "1") int quantity) {
        CommodityRequestDto request = new CommodityRequestDto(name, quantity);
        return ResponseEntity.ok(commodityService.getCommodityInfo(request));
    }

    @PostMapping("/traderoute")
    public ResponseEntity<TradeRouteResponseDto> getTradeRoute(@RequestBody TradeRouteRequestDto request) {
        TradeRouteResponseDto result = tradeRouteService.calculateTradeRoute(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/shipinfo")
    public ResponseEntity<ShipDetailsDto> getShipInfo(@RequestParam String name) {
        ShipDetailsDto shipInfo = shipInfoService.getShipDetailsByName(name);
        return ResponseEntity.ok(shipInfo);
    }

    @GetMapping("/shipnames")
    public ResponseEntity<List<String>> getAllShipNames() {
        List<String> names = shipCache.getAllNames();
        System.out.println("📦 Returning ship names: " + names.size()); // Log this!
        return ResponseEntity.ok(names);
    }


    @GetMapping("/commodities/all")
    public ResponseEntity<List<CommodityResponseDto>> getAllCommodities() {
    List<CommodityResponseDto> all = new ArrayList<>(commodityService.getAllCommodities());
    return ResponseEntity.ok(all);
    }

    @GetMapping("/commodities/names")
    public ResponseEntity<List<String>> getAllCommodityNames() {
    return ResponseEntity.ok(commodityService.getAllCommodityNames());
    }

    @PostMapping("/shipcompare")
    public ResponseEntity<ShipComparisonResultDto> compareShips(
            @RequestBody ShipCompareRequestDto request) {

        ShipComparisonResultDto result = shipComparisonService.compare(
                request.getName1(), request.getName2()
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/syncApiShips")
    public ResponseEntity<String> syncShips() {
        apiShipToShipSyncService.syncApiShipsToMainShips(); 
        return ResponseEntity.ok("✅ Ship sync complete.");
    }

}
