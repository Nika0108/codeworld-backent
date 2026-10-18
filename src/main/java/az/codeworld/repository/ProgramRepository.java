package az.codeworld.repository;

import az.codeworld.entity.Program;
import az.codeworld.entity.ProgramType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findByType(ProgramType type);

    List<Program> findByIsActiveTrue();

    Optional<Program> findFirstByIsActiveTrue();
}