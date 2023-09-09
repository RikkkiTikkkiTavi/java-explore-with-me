package ru.practicum.explorewithme.hit.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.hit.HitDto;
import ru.practicum.explorewithme.hit.mapper.HitMapper;
import ru.practicum.explorewithme.hit.model.Hit;
import ru.practicum.explorewithme.hit.repository.HitRepository;
import ru.practicum.explorewithme.state.StateDto;

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
        hitRepository.save(HitMapper.toHit(hitDto));
    }

    @Override
    public List<StateDto> getState(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
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

    private List<StateDto> getStateUniqueIp(List<Hit> hits) {
        List<StateDto> stats = new ArrayList<>();
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
            StateDto stateDto = StateDto.builder().app(app).uri(uri).hits(value.size()).build();
            stats.add(stateDto);
        });
        return stats.stream().sorted(Comparator.comparing(StateDto::getHits).reversed()).collect(Collectors.toList());
    }

    private List<StateDto> getStateAllIp(List<Hit> hits) {
        List<StateDto> stats = new ArrayList<>();
        Map<String, Integer> freq = new HashMap<>();

        hits.stream().map(hit -> hit.getApp() + " " + hit.getUri()).forEach(mapKey -> {
            int count = freq.getOrDefault(mapKey, 0) + 1;
            freq.put(mapKey, count);
        });

        freq.forEach((key, value) -> {
            String[] elements = key.split(" ");
            String app = elements[0];
            String uri = elements[1];
            StateDto stateDto = StateDto.builder().app(app).uri(uri).hits(value).build();
            stats.add(stateDto);
        });
        return stats.stream().sorted(Comparator.comparing(StateDto::getHits).reversed()).collect(Collectors.toList());
    }
}