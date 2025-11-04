package org.api_app_api_gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> usersFallback(ServerWebExchange exchange) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "User service is unavailable. Returning fallback response.");
        body.put("path", exchange.getRequest().getPath().value());
        body.put("timestamp", Instant.now().toString());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }
}
