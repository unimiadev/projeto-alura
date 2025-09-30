package br.com.alura.projeto.enrollment;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<?> enrollStudent(@Valid @RequestBody NewEnrollmentRequest request) {
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(request);
            EnrollmentResponse response = new EnrollmentResponse(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/student/{email}")
    public ResponseEntity<List<EnrollmentResponse>> getStudentEnrollments(@PathVariable String email) {
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(email);
        List<EnrollmentResponse> responses = enrollments.stream()
                .map(EnrollmentResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/course/{code}")
    public ResponseEntity<List<EnrollmentResponse>> getCourseEnrollments(@PathVariable String code) {
        List<Enrollment> enrollments = enrollmentService.getCourseEnrollments(code);
        List<EnrollmentResponse> responses = enrollments.stream()
                .map(EnrollmentResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/student/{email}/course/{code}")
    public ResponseEntity<?> getEnrollment(@PathVariable String email, @PathVariable String code) {
        Optional<Enrollment> enrollment = enrollmentService.findEnrollment(email, code);
        if (enrollment.isPresent()) {
            return ResponseEntity.ok(new EnrollmentResponse(enrollment.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/student/{email}/course/{code}/complete")
    public ResponseEntity<?> completeEnrollment(@PathVariable String email, @PathVariable String code) {
        try {
            enrollmentService.completeEnrollment(email, code);
            return ResponseEntity.ok(new SuccessResponse("Enrollment completed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/student/{email}/course/{code}/cancel")
    public ResponseEntity<?> cancelEnrollment(@PathVariable String email, @PathVariable String code) {
        try {
            enrollmentService.cancelEnrollment(email, code);
            return ResponseEntity.ok(new SuccessResponse("Enrollment cancelled successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/course/{code}/count")
    public ResponseEntity<EnrollmentCountResponse> getEnrollmentCount(@PathVariable String code) {
        Long count = enrollmentService.getActiveEnrollmentCount(code);
        return ResponseEntity.ok(new EnrollmentCountResponse(code, count));
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        List<EnrollmentResponse> responses = enrollments.stream()
                .map(EnrollmentResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    public static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class EnrollmentCountResponse {
        private String courseCode;
        private Long activeEnrollments;

        public EnrollmentCountResponse(String courseCode, Long activeEnrollments) {
            this.courseCode = courseCode;
            this.activeEnrollments = activeEnrollments;
        }

        public String getCourseCode() {
            return courseCode;
        }

        public Long getActiveEnrollments() {
            return activeEnrollments;
        }
    }
}
