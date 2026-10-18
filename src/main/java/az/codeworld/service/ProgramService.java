package az.codeworld.service;

import az.codeworld.dto.ProgramRequest;
import az.codeworld.entity.Program;
import az.codeworld.repository.ProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Transactional
    public Program create(ProgramRequest request) {

        if (Boolean.TRUE.equals(request.getIsActive())) {
            deactivateAllPrograms();
        }

        Program program = new Program();

        program.setName(request.getName());
        program.setType(request.getType());
        program.setProgramNumber(request.getProgramNumber());
        program.setStartMonth(request.getStartMonth());
        program.setStartYear(request.getStartYear());
        program.setEndDate(request.getEndDate());
        program.setIsActive(request.getIsActive());

        return programRepository.save(program);
    }

    public List<Program> getAll() {
        return programRepository.findAll();
    }

    public Program getById(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Program tapılmadı: " + id));
    }

    @Transactional
    public Program update(Long id, ProgramRequest request) {

        Program program = getById(id);

        if (Boolean.TRUE.equals(request.getIsActive())) {
            deactivateAllPrograms();
        }

        program.setName(request.getName());
        program.setType(request.getType());
        program.setProgramNumber(request.getProgramNumber());
        program.setStartMonth(request.getStartMonth());
        program.setStartYear(request.getStartYear());
        program.setEndDate(request.getEndDate());
        program.setIsActive(request.getIsActive());

        return programRepository.save(program);
    }

    @Transactional
    public void delete(Long id) {
        Program program = getById(id);
        programRepository.delete(program);
    }

    private void deactivateAllPrograms() {
        List<Program> activePrograms =
                programRepository.findByIsActiveTrue();

        for (Program program : activePrograms) {
            program.setIsActive(false);
        }

        programRepository.saveAll(activePrograms);
    }
}