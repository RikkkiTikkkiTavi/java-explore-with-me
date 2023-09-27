package ru.practicum.server.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.dto.UpdateCompilationRequest;
import ru.practicum.server.compilation.mapper.CompilationMapper;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.repository.CompilationRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    CompilationRepository compilationRepository;
    EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto dto) {
        List<Event> events = new ArrayList<>();
        if (dto.getEvents() != null) {
            events = eventRepository.findByIdIn(dto.getEvents());
        }
        Compilation compilation = CompilationMapper.newDtoToCompilation(dto, events);
        return CompilationMapper.compilationToDto(compilationRepository.save(compilation));
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest request, int compId) {
        Compilation oldComp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
        List<Event> events = null;
        if (request.getEvents() != null) {
            events = eventRepository.findByIdIn(request.getEvents());
        }
        Compilation compilation = CompilationMapper.updateDtoToCompilation(request, oldComp, events);
        return CompilationMapper.compilationToDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        PageRequest pr = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        if (pinned != null) {
            Page<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pr);
            return compilations.stream().map(CompilationMapper::compilationToDto).collect(Collectors.toList());
        } else {
            Page<Compilation> allCompilations = compilationRepository.findAll(pr);
            return allCompilations.stream().map(CompilationMapper::compilationToDto).collect(Collectors.toList());
        }
    }

    @Override
    public CompilationDto getCompilation(int compId) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
        return CompilationMapper.compilationToDto(comp);
    }

    @Transactional
    @Override
    public void deleteCompilation(int compId) {
        compilationRepository.deleteById(compId);
    }
}
