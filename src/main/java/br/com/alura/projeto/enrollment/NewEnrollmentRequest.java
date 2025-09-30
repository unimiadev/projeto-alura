package br.com.alura.projeto.enrollment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para matrícula de aluno em curso")
public class NewEnrollmentRequest {

    @NotBlank
    @Email
    @Schema(description = "Email do aluno", example = "joao.silva@student.com", required = true)
    private String studentEmail;

    @NotBlank
    @Schema(description = "Código do curso", example = "spring-boot", required = true)
    private String courseCode;

    @Deprecated
    public NewEnrollmentRequest() {}

    public NewEnrollmentRequest(String studentEmail, String courseCode) {
        this.studentEmail = studentEmail;
        this.courseCode = courseCode;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
