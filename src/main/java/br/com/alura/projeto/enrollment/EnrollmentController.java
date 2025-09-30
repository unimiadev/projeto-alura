package br.com.alura.projeto.enrollment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
@Tag(name = "Matrículas", description = "API para gerenciamento de matrículas de alunos em cursos")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Matricular aluno em curso", 
               description = "Realiza a matrícula de um aluno em um curso específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Matrícula realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = EnrollmentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> enrollStudent(
            @Parameter(description = "Dados da matrícula", required = true)
            @Valid @RequestBody NewEnrollmentRequest request) {
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(request);
            EnrollmentResponse response = new EnrollmentResponse(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/student/{email}")
    @Operation(summary = "Listar matrículas do aluno", 
               description = "Retorna todas as matrículas de um aluno específico")
    @ApiResponse(responseCode = "200", description = "Lista de matrículas retornada com sucesso",
                content = @Content(schema = @Schema(implementation = EnrollmentResponse.class)))
    public ResponseEntity<List<EnrollmentResponse>> getStudentEnrollments(
            @Parameter(description = "Email do aluno", required = true, example = "aluno@email.com")
            @PathVariable String email) {
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
