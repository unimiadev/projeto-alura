package br.com.alura.projeto.enrollment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NewEnrollmentRequest {

    @NotBlank
    @Email
    private String studentEmail;

    @NotBlank
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
