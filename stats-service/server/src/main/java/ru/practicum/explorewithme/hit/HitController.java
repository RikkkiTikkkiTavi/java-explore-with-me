package ru.practicum.explorewithme.hit;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.hit.service.HitService;
import ru.practicum.explorewithme.hit.validator.HitValidator;
import ru.practicum.explorewithme.state.StateDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class HitController {

    private HitService service;

    @PostMapping("/hit")
    public void add(@RequestBody HitDto hitDto) {
        HitValidator.checkHit(hitDto);
        service.add(hitDto);
    }

    @GetMapping("/stats")
    public List<StateDto> getState(@RequestParam(name = "start")
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                   @RequestParam(name = "end")
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") boolean unique) {
        return service.getState(start, end, uris, unique);
    }
}