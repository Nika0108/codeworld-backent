package az.codeworld.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgramType type;

    @Column(nullable = false)
    private Integer programNumber;

    @Column(nullable = false)
    private Integer startMonth;

    @Column(nullable = false)
    private Integer startYear;

    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isActive = true;
}
