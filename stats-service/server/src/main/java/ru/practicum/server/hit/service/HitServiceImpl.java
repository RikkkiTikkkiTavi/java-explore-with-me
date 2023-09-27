package ru.practicum.server.hit.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.hit.HitDto;
import ru.practicum.dto.stat.StatDto;
import ru.practicum.server.hit.mapper.HitMapper;
import ru.practicum.server.hit.model.Hit;
import ru.practicum.server.hit.repository.HitRepository;
import ru.practicum.server.hit.validator.HitValidator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    @Transactional
    @Override
    public void add(HitDto hitDto) {
        HitValidator.checkHit(hitDto);
        hitRepository.save(HitMapper.toHit(hitDto));
    }

    @Override
    public List<StatDto> getState(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<Hit> hits;
        if (uris != null) {
            hits = hitRepository.findAllByUriInAndCreatedBetween(uris, start, end);
        } else {
            hits = hitRepository.findAllByCreatedBetween(start, end);
        }
        if (!unique) {
            return getStateAllIp(hits);
        }
        return getStateUniqueIp(hits);

    }

    private List<StatDto> getStateUniqueIp(List<Hit> hits) {
        List<StatDto> stats = new ArrayList<>();
        Map<String, HashSet<String>> uniqueFreq = new HashMap<>();

        hits.forEach(hit -> {
            String mapKey = hit.getApp() + " " + hit.getUri();
            String mapValue = hit.getIp();
            HashSet<String> ips = uniqueFreq.getOrDefault(mapKey, new HashSet<>());
            ips.add(mapValue);
            uniqueFreq.put(mapKey, ips);
        });

        uniqueFreq.forEach((key, value) -> {
            String[] elements = key.split(" ");
            String app = elements[0];
            String uri = elements[1];
            StatDto statDto = StatDto.builder().app(app).uri(uri).hits(value.size()).build();
            stats.add(statDto);
        });
        return stats.stream().sorted(Comparator.comparing(StatDto::getHits).reversed()).collect(Collectors.toList());
    }

    private List<StatDto> getStateAllIp(List<Hit> hits) {
        List<StatDto> stats = new ArrayList<>();
        Map<String, Integer> freq = new HashMap<>();

        hits.stream().map(hit -> hit.getApp() + " " + hit.getUri()).forEach(mapKey -> {
            int count = freq.getOrDefault(mapKey, 0) + 1;
            freq.put(mapKey, count);
        });

        freq.forEach((key, value) -> {
            String[] elements = key.split(" ");
            String app = elements[0];
            String uri = elements[1];
            StatDto statDto = StatDto.builder().app(app).uri(uri).hits(value).build();
            stats.add(statDto);
        });
        return stats.stream().sorted(Comparator.comparing(StatDto::getHits).reversed()).collect(Collectors.toList());
    }
}