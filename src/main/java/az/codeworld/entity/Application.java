package az.codeworld.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_application_program_email_phone",
                        columnNames = {"program_id", "email", "phone"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String applicationCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 2000)
    private String motivation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EducationType educationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldType field;

    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    private String adminNote;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "bytea")
    private byte[] cv;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = ApplicationStatus.PENDING;
        }
    }
}