package com.company.ecomShop.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/device-tokens")
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;

    public DeviceTokenController(DeviceTokenService deviceTokenService) {
        this.deviceTokenService = deviceTokenService;
    }

    // ✅ REGISTRA o aggiorna token
    @PostMapping("/register")
    public ResponseEntity<?> registerToken(@RequestBody Map<String, String> body) {
        try {
            if (!body.containsKey("userId") || !body.containsKey("token")) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId e token obbligatori"));
            }

            String userId = body.get("userId");  // ora è la carta
            String token = body.get("token");
            String platform = body.getOrDefault("platform", "android");

            deviceTokenService.registerOrUpdateToken(userId, token, platform);

            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "message", "Token registrato o aggiornato correttamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Aggiorna solo il lastSeen del token (chiamato da Flutter ogni tot minuti)
    @PostMapping("/ping")
    public ResponseEntity<?> ping(@RequestBody Map<String, String> body) {
        try {
            if (!body.containsKey("token")) {
                return ResponseEntity.badRequest().body(Map.of("error", "token obbligatorio"));
            }
            String token = body.get("token");
            deviceTokenService.updateLastSeen(token, new Date());

            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "lastSeenUpdated", true
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
