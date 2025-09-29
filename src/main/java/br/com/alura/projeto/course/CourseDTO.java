package br.com.alura.projeto.course;

import java.time.LocalDateTime;

public class CourseDTO {

    private Long id;
    private String name;
    private String code;
    private String instructorEmail;
    private String categoryName;
    private String description;
    private CourseStatus status;
    private LocalDateTime inactivatedAt;
    private LocalDateTime createdAt;

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.code = course.getCode();
        this.instructorEmail = course.getInstructorEmail();
        this.categoryName = course.getCategory() != null ? course.getCategory().getName() : "N/A";
        this.description = course.getDescription();
        this.status = course.getStatus();
        this.inactivatedAt = course.getInactivatedAt();
        this.createdAt = course.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public LocalDateTime getInactivatedAt() {
        return inactivatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return status == CourseStatus.ACTIVE;
    }
}
