package az.codeworld.dto;

import az.codeworld.entity.ProgramType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ProgramRequest {

    @NotBlank(message = "Program adı boş ola bilməz")
    private String name;

    @NotNull(message = "Program tipi seçilməlidir")
    private ProgramType type;

    @NotNull(message = "Program nömrəsi yazılmalıdır")
    @Min(value = 1, message = "Program nömrəsi minimum 1 olmalıdır")
    private Integer programNumber;

    @NotNull(message = "Başlanğıc ayı yazılmalıdır")
    @Min(1)
    @Max(12)
    private Integer startMonth;

    @NotNull(message = "Başlanğıc ili yazılmalıdır")
    private Integer startYear;

    private LocalDate endDate;

    private Boolean isActive = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgramType getType() {
        return type;
    }

    public void setType(ProgramType type) {
        this.type = type;
    }

    public Integer getProgramNumber() {
        return programNumber;
    }

    public void setProgramNumber(Integer programNumber) {
        this.programNumber = programNumber;
    }

    public Integer getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
