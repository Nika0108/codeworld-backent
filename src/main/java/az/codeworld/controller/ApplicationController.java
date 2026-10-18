package az.codeworld.controller;

import az.codeworld.dto.ApplicationCreateRequest;
import az.codeworld.dto.ApplicationResponse;
import az.codeworld.dto.StatusUpdateRequest;
import az.codeworld.entity.ApplicationStatus;
import az.codeworld.entity.ApplicationStatusHistory;
import az.codeworld.entity.EducationType;
import az.codeworld.entity.FieldType;
import az.codeworld.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Yeni müraciət yaratmaq
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse create(
            @Valid @RequestBody ApplicationCreateRequest request) {

        return applicationService.create(request);
    }

    // Mövcud müraciətə PDF formatında CV yükləmək
    @PostMapping(
            value = "/{id}/cv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCv(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        return applicationService.uploadCv(id, file);
    }

    // Bütün müraciətləri göstərmək
    @GetMapping
    public List<ApplicationResponse> getAll() {
        return applicationService.getAll();
    }

    // Müraciətləri filtrləmək
    @GetMapping("/search")
    public List<ApplicationResponse> search(
            @RequestParam(required = false) FieldType field,
            @RequestParam(required = false) EducationType educationType,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime toDate) {

        return applicationService.search(
                field,
                educationType,
                status,
                name,
                email,
                fromDate,
                toDate
        );
    }

    // Proqrama görə müraciətləri göstərmək
    @GetMapping("/program/{programId}")
    public List<ApplicationResponse> getByProgramId(
            @PathVariable Long programId) {

        return applicationService.getByProgramId(programId);
    }

    // Statusa görə müraciətləri göstərmək
    @GetMapping("/status/{status}")
    public List<ApplicationResponse> getByStatus(
            @PathVariable ApplicationStatus status) {

        return applicationService.getByStatus(status);
    }

    // Müraciətin status tarixçəsini göstərmək
    @GetMapping("/{id}/history")
    public List<ApplicationStatusHistory> getHistory(
            @PathVariable Long id) {

        return applicationService.getStatusHistory(id);
    }

    // ID ilə müraciəti tapmaq
    @GetMapping("/{id}")
    public ApplicationResponse getById(
            @PathVariable Long id) {

        return applicationService.getById(id);
    }

    // Müraciətin statusunu dəyişmək
    @PutMapping("/{id}/status")
    public ApplicationResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {

        return applicationService.updateStatus(id, request);
    }

    // Müraciəti silmək
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        applicationService.delete(id);
    }
}