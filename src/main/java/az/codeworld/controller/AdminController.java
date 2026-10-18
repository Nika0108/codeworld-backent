package az.codeworld.controller;

import az.codeworld.dto.ApplicationResponse;
import az.codeworld.dto.StatusUpdateRequest;
import az.codeworld.entity.ApplicationStatus;
import az.codeworld.entity.EducationType;
import az.codeworld.entity.FieldType;
import az.codeworld.entity.Program;
import az.codeworld.service.ApplicationService;
import az.codeworld.service.ProgramService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ApplicationService applicationService;
    private final ProgramService programService;

    public AdminController(
            ApplicationService applicationService,
            ProgramService programService) {

        this.applicationService = applicationService;
        this.programService = programService;
    }

    // Bütün proqramlar
    @GetMapping("/programs")
    public List<Program> getAllPrograms() {
        return programService.getAll();
    }

    // Bütün müraciətlər
    @GetMapping("/applications")
    public List<ApplicationResponse> getAllApplications() {
        return applicationService.getAll();
    }

    // Bir müraciətin detalları
    @GetMapping("/applications/{id}")
    public ApplicationResponse getApplicationById(
            @PathVariable Long id) {

        return applicationService.getById(id);
    }

    // Proqrama görə müraciətlər
    @GetMapping("/applications/program/{programId}")
    public List<ApplicationResponse> getApplicationsByProgram(
            @PathVariable Long programId) {

        return applicationService.getByProgramId(programId);
    }

    // Statusa görə müraciətlər
    @GetMapping("/applications/status/{status}")
    public List<ApplicationResponse> getApplicationsByStatus(
            @PathVariable ApplicationStatus status) {

        return applicationService.getByStatus(status);
    }

    // Filter və axtarış
    @GetMapping("/applications/search")
    public List<ApplicationResponse> searchApplications(
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String educationType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime toDate) {

        FieldType fieldEnum = null;
        if (field != null && !field.isBlank()) {
            try {
                fieldEnum = FieldType.valueOf(field.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        EducationType educationTypeEnum = null;
        if (educationType != null && !educationType.isBlank()) {
            try {
                educationTypeEnum = EducationType.valueOf(educationType.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        ApplicationStatus statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = ApplicationStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        return applicationService.search(
                fieldEnum,
                educationTypeEnum,
                statusEnum,
                (name != null && !name.isBlank()) ? name.trim() : null,
                (email != null && !email.isBlank()) ? email.trim() : null,
                fromDate,
                toDate
        );
    }

    // Status dəyişdirmək
    @PutMapping("/applications/{id}/status")
    public ApplicationResponse updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {

        return applicationService.updateStatus(id, request);
    }

    // Aktiv proqramlar
    @GetMapping("/programs/active")
    public List<Program> getActivePrograms() {
        return programService.getAll()
                .stream()
                .filter(Program::getIsActive)
                .toList();
    }

    // Ümumi müraciət sayı
    @GetMapping("/statistics/total-applications")
    public long getTotalApplicationCount() {
        return applicationService.getTotalApplicationCount();
    }

    // Aktiv proqram üzrə müraciət sayı
    @GetMapping("/statistics/active-program-applications")
    public long getActiveProgramApplicationCount() {
        return applicationService.getActiveProgramApplicationCount();
    }

    // Hazırda aktiv proqramın məlumatları
    @GetMapping("/statistics/active-program")
    public Program getActiveProgram() {
        return applicationService.getActiveProgram();
    }
}