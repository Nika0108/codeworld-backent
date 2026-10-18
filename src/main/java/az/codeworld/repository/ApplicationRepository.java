package az.codeworld.repository;

import az.codeworld.entity.Application;
import az.codeworld.entity.ApplicationStatus;
import az.codeworld.entity.EducationType;
import az.codeworld.entity.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByProgramId(Long programId);

    List<Application> findByStatus(ApplicationStatus status);

    boolean existsByProgramIdAndEmailAndPhone(
            Long programId,
            String email,
            String phone
    );

    long countByProgramId(Long programId);

    @Query("""
    SELECT a FROM Application a
    WHERE (:field IS NULL OR a.field = :field)
    AND (:educationType IS NULL OR a.educationType = :educationType)
    AND (:status IS NULL OR a.status = :status)
    AND (CAST(:name AS string) IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
    AND (CAST(:email AS string) IS NULL OR LOWER(a.email) LIKE LOWER(CONCAT('%', CAST(:email AS string), '%')))
    AND (:fromDate IS NULL OR a.createdAt >= :fromDate)
    AND (:toDate IS NULL OR a.createdAt <= :toDate)
    """)
    List<Application> searchApplications(
            @Param("field") FieldType field,
            @Param("educationType") EducationType educationType,
            @Param("status") ApplicationStatus status,
            @Param("name") String name,
            @Param("email") String email,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("namePattern") String namePattern,
            @Param("emailPattern") String emailPattern

    );
}