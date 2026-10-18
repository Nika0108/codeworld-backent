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
    AND (CAST(:fromDate AS timestamp) IS NULL OR a.createdAt >= :fromDate)
    AND (CAST(:toDate AS timestamp) IS NULL OR a.createdAt <= :toDate)
    """)
    List<Application> searchApplicationsWithoutText(
            @Param("field") FieldType field,
            @Param("educationType") EducationType educationType,
            @Param("status") ApplicationStatus status,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
}