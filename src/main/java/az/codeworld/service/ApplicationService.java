package az.codeworld.service;

import az.codeworld.dto.ApplicationCreateRequest;
import az.codeworld.dto.ApplicationResponse;
import az.codeworld.dto.StatusUpdateRequest;
import az.codeworld.entity.Application;
import az.codeworld.entity.ApplicationStatus;
import az.codeworld.entity.ApplicationStatusHistory;
import az.codeworld.entity.EducationType;
import az.codeworld.entity.FieldType;
import az.codeworld.entity.Program;
import az.codeworld.exception.ApiException;
import az.codeworld.repository.ApplicationRepository;
import az.codeworld.repository.ApplicationStatusHistoryRepository;
import az.codeworld.repository.ProgramRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ProgramRepository programRepository;
    private final ApplicationStatusHistoryRepository historyRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            ProgramRepository programRepository,
            ApplicationStatusHistoryRepository historyRepository) {

        this.applicationRepository = applicationRepository;
        this.programRepository = programRepository;
        this.historyRepository = historyRepository;
    }

    // Yeni müraciət
    @Transactional
    public ApplicationResponse create(ApplicationCreateRequest request) {

        Program activeProgram = programRepository.findFirstByIsActiveTrue()
                .orElseThrow(() -> new ApiException(
                        "Hazırda aktiv proqram yoxdur",
                        HttpStatus.NOT_FOUND
                ));

        boolean exists = applicationRepository
                .existsByProgramIdAndEmailAndPhone(
                        activeProgram.getId(),
                        request.getEmail(),
                        request.getPhone()
                );

        if (exists) {
            throw new ApiException(
                    "Siz artıq bu proqram üzrə qeydiyyatdan keçmisiniz.",
                    HttpStatus.CONFLICT
            );
        }

        long number = applicationRepository
                .countByProgramId(activeProgram.getId()) + 1;

        String prefix = activeProgram.getType().name().equals("EXPERIENCE")
                ? "INT"
                : "SCH";

        String applicationCode = String.format(
                "CW-%s-%d-%04d",
                prefix,
                Year.now().getValue(),
                number
        );

        Application application = new Application();

        application.setApplicationCode(applicationCode);
        application.setName(request.getName());
        application.setSurname(request.getSurname());
        application.setPhone(request.getPhone());
        application.setEmail(request.getEmail());
        application.setMotivation(request.getMotivation());
        application.setEducationType(request.getEducationType());
        application.setField(request.getField());
        application.setCv(
                request.getCv() != null
                        ? java.util.Base64.getDecoder().decode(request.getCv())
                        : null
        );
        application.setProgram(activeProgram);
        application.setStatus(ApplicationStatus.PENDING);

        Application saved = applicationRepository.save(application);

        ApplicationStatusHistory history =
                ApplicationStatusHistory.builder()
                        .application(saved)
                        .status(ApplicationStatus.PENDING)
                        .comment("Müraciət yaradıldı")
                        .build();

        historyRepository.save(history);

        return toResponse(saved);
    }
    // CV faylını müraciətə yükləmək
    @Transactional
    public String uploadCv(Long id, MultipartFile file) throws IOException {

        Application application = findApplication(id);

        if (file == null || file.isEmpty()) {
            throw new ApiException("CV faylı boşdur", HttpStatus.BAD_REQUEST);
        }

        String filename = file.getOriginalFilename();

        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new ApiException("Yalnız PDF faylı yükləmək olar", HttpStatus.BAD_REQUEST);
        }

        application.setCv(file.getBytes());
        applicationRepository.save(application);

        return "CV faylı uğurla saxlanıldı: " + filename;
    }

    // Bütün müraciətlər
    public List<ApplicationResponse> getAll() {
        return applicationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ID-yə görə müraciət
    public ApplicationResponse getById(Long id) {
        return toResponse(findApplication(id));
    }

    // Proqrama görə müraciətlər
    public List<ApplicationResponse> getByProgramId(Long programId) {
        return applicationRepository.findByProgramId(programId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Statusa görə müraciətlər
    public List<ApplicationResponse> getByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Filtrləmə
    public List<ApplicationResponse> search(
            FieldType field,
            EducationType educationType,
            ApplicationStatus status,
            String name,
            String email,
            LocalDateTime fromDate,
            LocalDateTime toDate) {

        return applicationRepository.searchApplications(
                        field,
                        educationType,
                        status,
                        name,
                        email,
                        fromDate,
                        toDate,
                        name == null || name.isBlank() ? null : "%" + name + "%",
                        email == null || email.isBlank() ? null : "%" + email + "%"
                ).stream()
                .map(this::toResponse)
                .toList();
    }

    // Status dəyişdirmək
    @Transactional
    public ApplicationResponse updateStatus(
            Long id,
            StatusUpdateRequest request) {

        Application application = findApplication(id);

        application.setStatus(request.getStatus());
        application.setAdminNote(request.getAdminNote());

        Application saved = applicationRepository.save(application);

        ApplicationStatusHistory history =
                ApplicationStatusHistory.builder()
                        .application(saved)
                        .status(request.getStatus())
                        .comment(request.getAdminNote())
                        .build();

        historyRepository.save(history);

        return toResponse(saved);
    }

    // Status tarixçəsi
    public List<ApplicationStatusHistory> getStatusHistory(
            Long applicationId) {

        findApplication(applicationId);

        return historyRepository
                .findByApplicationIdOrderByChangedAtDesc(applicationId);
    }

    // Silmək
    @Transactional
    public void delete(Long id) {
        Application application = findApplication(id);
        applicationRepository.delete(application);
    }

    // Ümumi müraciət sayı
    public long getTotalApplicationCount() {
        return applicationRepository.count();
    }

    // Aktiv proqram üzrə müraciət sayı
    public long getActiveProgramApplicationCount() {

        Program activeProgram = programRepository.findFirstByIsActiveTrue()
                .orElseThrow(() -> new ApiException(
                        "Hazırda aktiv proqram yoxdur",
                        HttpStatus.NOT_FOUND
                ));

        return applicationRepository
                .countByProgramId(activeProgram.getId());
    }

    // Aktiv proqram
    public Program getActiveProgram() {

        return programRepository.findFirstByIsActiveTrue()
                .orElseThrow(() -> new ApiException(
                        "Hazırda aktiv proqram yoxdur",
                        HttpStatus.NOT_FOUND
                ));
    }

    private Application findApplication(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        "Müraciət tapılmadı: " + id,
                        HttpStatus.NOT_FOUND
                ));
    }

    private ApplicationResponse toResponse(Application application) {

        ApplicationResponse response = new ApplicationResponse();

        response.setId(application.getId());
        response.setApplicationCode(application.getApplicationCode());
        response.setName(application.getName());
        response.setSurname(application.getSurname());
        response.setPhone(application.getPhone());
        response.setEmail(application.getEmail());
        response.setMotivation(application.getMotivation());
        response.setEducationType(application.getEducationType());
        response.setField(application.getField());
        response.setFeedback(application.getFeedback());
        response.setStatus(application.getStatus());
        response.setAdminNote(application.getAdminNote());
        response.setCreatedAt(application.getCreatedAt());
        response.setCv(
                application.getCv() == null
                        ? null
                        : java.util.Base64.getEncoder().encodeToString(application.getCv())
        );

        if (application.getProgram() != null) {
            response.setProgramId(application.getProgram().getId());
            response.setProgramName(
                    application.getProgram().getName()
            );
        }

        if (application.getPhone() != null) {

            String phone = application.getPhone()
                    .replaceAll("[^0-9]", "");

            response.setWhatsappLink(
                    "https://wa.me/" + phone
            );
        }

        return response;
    }
}