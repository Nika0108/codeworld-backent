package az.codeworld.dto;

import az.codeworld.entity.EducationType;
import az.codeworld.entity.FieldType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ApplicationCreateRequest {

    @NotBlank(message = "Ad boş ola bilməz")
    private String name;

    @NotBlank(message = "Soyad boş ola bilməz")
    private String surname;

    @NotBlank(message = "Telefon nömrəsi boş ola bilməz")
    @Pattern(
            regexp = "^[+]?[0-9]{9,15}$",
            message = "Telefon nömrəsi düzgün deyil"
    )
    private String phone;

    @NotBlank(message = "Email boş ola bilməz")
    @Email(message = "Email düzgün formatda deyil")
    private String email;

    @NotBlank(message = "Motivasiya boş ola bilməz")
    private String motivation;

    @NotNull(message = "Təhsil forması seçilməlidir")
    private EducationType educationType;

    @NotNull(message = "İxtisas seçilməlidir")
    private FieldType field;

    private String cv;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public EducationType getEducationType() {
        return educationType;
    }

    public void setEducationType(EducationType educationType) {
        this.educationType = educationType;
    }

    public FieldType getField() {
        return field;
    }

    public void setField(FieldType field) {
        this.field = field;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }
}