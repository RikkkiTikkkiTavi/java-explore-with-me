package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.hit.HitDto;
import ru.practicum.dto.stat.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class StatsClient {

    RestTemplate rest;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest =
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        ;
    }

    public void addHit(HitDto hitDto) {
        rest.exchange("/hit", HttpMethod.POST, new HttpEntity<>(hitDto, defaultHeaders()), Object.class);
    }

    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {

        Map<String, Object> param = Map.of(
                "start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "unique", unique,
                "uris", uris
        );

        StatDto[] stats = rest.getForObject("/stats?start={start}&end={end}&unique={unique}&uris={uris}",
                StatDto[].class, param);
        List<StatDto> statsList = new ArrayList<>();
        if (stats != null) {
            statsList = Arrays.stream(stats).collect(Collectors.toList());
        }
        return statsList;
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
