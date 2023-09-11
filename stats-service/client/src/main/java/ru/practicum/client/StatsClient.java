package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.hit.HitDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addHit(HitDto hitDto) {
        return post("/hit", hitDto);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (uris == null || uris.isEmpty()) {
            Map<String, Object> param = Map.of(
                    "start", encodeTime(start),
                    "end", encodeTime(end),
                    "unique", unique
            );
            return get("/stats?start={start}&end={end}&unique={unique}", param);
        }

        Map<String, Object> param = Map.of(
                "start", encodeTime(start),
                "end", encodeTime(end),
                "unique", unique,
                "uris", uris
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", param);
    }

    private String encodeTime(LocalDateTime time) {
        return URLEncoder.encode(time.toString(), StandardCharsets.UTF_8);
    }
}
