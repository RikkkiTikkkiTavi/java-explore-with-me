package ru.practicum.server.hit;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.hit.HitDto;
import ru.practicum.dto.stat.StatDto;
import ru.practicum.server.hit.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class HitController {

    private HitService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody HitDto hitDto) {
        service.add(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> getState(@RequestParam(name = "start")
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                  @RequestParam(name = "end")
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false") boolean unique) {
        return service.getState(start, end, uris, unique);
    }
}
