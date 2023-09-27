package ru.practicum.server.compilation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.dto.UpdateCompilationRequest;
import ru.practicum.server.compilation.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
@Validated
public class CompilationController {

    private CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable int compId) {
        return compilationService.getCompilation(compId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/compilations")
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@RequestBody @Valid UpdateCompilationRequest updateCompilationRequest,
                                            @PathVariable int compId) {
        return compilationService.updateCompilation(updateCompilationRequest, compId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(@PathVariable int compId) {
        compilationService.deleteCompilation(compId);
    }
}
