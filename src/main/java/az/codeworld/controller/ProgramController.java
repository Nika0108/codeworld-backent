package az.codeworld.controller;

import az.codeworld.dto.ProgramRequest;
import az.codeworld.entity.Program;
import az.codeworld.service.ProgramService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Program create(
            @Valid @RequestBody ProgramRequest request) {

        return programService.create(request);
    }

    @GetMapping
    public List<Program> getAll() {
        return programService.getAll();
    }

    @GetMapping("/{id}")
    public Program getById(@PathVariable Long id) {
        return programService.getById(id);
    }

    @PutMapping("/{id}")
    public Program update(
            @PathVariable Long id,
            @Valid @RequestBody ProgramRequest request) {

        return programService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        programService.delete(id);
    }
}