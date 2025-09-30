package br.com.alura.projeto.enrollment;

import java.time.LocalDateTime;

public class EnrollmentResponse {

    private Long id;
    private String studentName;
    private String studentEmail;
    private String courseName;
    private String courseCode;
    private String status;
    private LocalDateTime enrolledAt;
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
