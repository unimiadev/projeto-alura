package br.com.alura.projeto.enrollment;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados da matrícula do aluno")
public class EnrollmentResponse {

    @Schema(description = "ID da matrícula", example = "1")
    private Long id;
    
    @Schema(description = "Nome do aluno", example = "João Silva")
    private String studentName;
    
    @Schema(description = "Email do aluno", example = "joao.silva@student.com")
    private String studentEmail;
    
    @Schema(description = "Nome do curso", example = "Spring Boot Fundamentals")
    private String courseName;
    
    @Schema(description = "Código do curso", example = "spring-boot")
    private String courseCode;
    
    @Schema(description = "Status da matrícula", example = "ACTIVE", allowableValues = {"ACTIVE", "COMPLETED", "CANCELLED"})
    private String status;
    
    @Schema(description = "Data de matrícula", example = "2024-01-15T10:30:00")
    private LocalDateTime enrolledAt;
    
    @Schema(description = "Data de conclusão", example = "2024-03-15T16:45:00")
    private LocalDateTime completedAt;

    @Deprecated
    public EnrollmentResponse() {}

    public EnrollmentResponse(Enrollment enrollment) {
        this.id = enrollment.getId();
        this.studentName = enrollment.getStudent().getName();
        this.studentEmail = enrollment.getStudent().getEmail();
        this.courseName = enrollment.getCourse().getName();
        this.courseCode = enrollment.getCourse().getCode();
        this.status = enrollment.getStatus().name();
        this.enrolledAt = enrollment.getEnrolledAt();
        this.completedAt = enrollment.getCompletedAt();
    }

    public Long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
