package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Length(min = 4, max = 10)
    @Column(unique = true)
    private String code;

    @NotBlank
    private String instructorEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    @NotNull
    private Category category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.ACTIVE;

    private LocalDateTime inactivatedAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Deprecated
    public Course() {}

    public Course(String name, String code, String instructorEmail, Category category, String description) {
        this.name = name;
        this.code = code;
        this.instructorEmail = instructorEmail;
        this.category = category;
        this.description = description;
        this.status = CourseStatus.ACTIVE;
    }

    public void inactivate() {
        this.status = CourseStatus.INACTIVE;
        this.inactivatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = CourseStatus.ACTIVE;
        this.inactivatedAt = null;
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

    public Category getCategory() {
        return category;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
