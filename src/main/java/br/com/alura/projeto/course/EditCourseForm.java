package br.com.alura.projeto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class EditCourseForm {

    @NotBlank
    private String name;

    @NotBlank
    @Length(min = 4, max = 10)
    private String code;

    @NotBlank
    private String instructorEmail;

    @NotNull
    private Long categoryId;

    @NotBlank
    @Length(max = 500)
    private String description;

    @Deprecated
    public EditCourseForm() {}

    public EditCourseForm(Course course) {
        this.name = course.getName();
        this.code = course.getCode();
        this.instructorEmail = course.getInstructorEmail();
        this.categoryId = course.getCategory().getId();
        this.description = course.getDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
