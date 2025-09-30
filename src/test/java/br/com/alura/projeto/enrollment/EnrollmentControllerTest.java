package br.com.alura.projeto.enrollment;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private User student;
    private Course course;
    private Category category;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        category = new Category("Programming", "prog", "#FF0000", 1);
        course = new Course("Spring Boot", "spring-boot", "instructor@alura.com", category, "Spring Boot course");
        student = new User("João Silva", "joao@email.com", Role.STUDENT, "password123");
        enrollment = new Enrollment(student, course);
    }

    @Test
    void shouldEnrollStudentSuccessfully() throws Exception {
        NewEnrollmentRequest request = new NewEnrollmentRequest("joao@email.com", "spring-boot");
        when(enrollmentService.enrollStudent(any(NewEnrollmentRequest.class))).thenReturn(enrollment);

        mockMvc.perform(post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentEmail").value("joao@email.com"))
                .andExpect(jsonPath("$.courseCode").value("spring-boot"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(enrollmentService).enrollStudent(any(NewEnrollmentRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenEnrollmentFails() throws Exception {
        NewEnrollmentRequest request = new NewEnrollmentRequest("joao@email.com", "spring-boot");
        when(enrollmentService.enrollStudent(any(NewEnrollmentRequest.class)))
                .thenThrow(new IllegalArgumentException("Student already enrolled"));

        mockMvc.perform(post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Student already enrolled"));

        verify(enrollmentService).enrollStudent(any(NewEnrollmentRequest.class));
    }

    @Test
    void shouldGetStudentEnrollments() throws Exception {
        List<Enrollment> enrollments = Arrays.asList(enrollment);
        when(enrollmentService.getStudentEnrollments("joao@email.com")).thenReturn(enrollments);

        mockMvc.perform(get("/api/enrollments/student/joao@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].studentEmail").value("joao@email.com"))
                .andExpect(jsonPath("$[0].courseCode").value("spring-boot"));

        verify(enrollmentService).getStudentEnrollments("joao@email.com");
    }

    @Test
    void shouldGetCourseEnrollments() throws Exception {
        List<Enrollment> enrollments = Arrays.asList(enrollment);
        when(enrollmentService.getCourseEnrollments("spring-boot")).thenReturn(enrollments);

        mockMvc.perform(get("/api/enrollments/course/spring-boot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].courseCode").value("spring-boot"));

        verify(enrollmentService).getCourseEnrollments("spring-boot");
    }

    @Test
    void shouldCompleteEnrollmentSuccessfully() throws Exception {
        doNothing().when(enrollmentService).completeEnrollment("joao@email.com", "spring-boot");

        mockMvc.perform(patch("/api/enrollments/student/joao@email.com/course/spring-boot/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Enrollment completed successfully"));

        verify(enrollmentService).completeEnrollment("joao@email.com", "spring-boot");
    }

    @Test
    void shouldCancelEnrollmentSuccessfully() throws Exception {
        doNothing().when(enrollmentService).cancelEnrollment("joao@email.com", "spring-boot");

        mockMvc.perform(patch("/api/enrollments/student/joao@email.com/course/spring-boot/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Enrollment cancelled successfully"));

        verify(enrollmentService).cancelEnrollment("joao@email.com", "spring-boot");
    }

    @Test
    void shouldGetEnrollmentCount() throws Exception {
        when(enrollmentService.getActiveEnrollmentCount("spring-boot")).thenReturn(5L);

        mockMvc.perform(get("/api/enrollments/course/spring-boot/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseCode").value("spring-boot"))
                .andExpect(jsonPath("$.activeEnrollments").value(5));

        verify(enrollmentService).getActiveEnrollmentCount("spring-boot");
    }

    @Test
    void shouldReturnNotFoundWhenEnrollmentDoesNotExist() throws Exception {
        when(enrollmentService.findEnrollment("joao@email.com", "nonexistent"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/enrollments/student/joao@email.com/course/nonexistent"))
                .andExpect(status().isNotFound());

        verify(enrollmentService).findEnrollment("joao@email.com", "nonexistent");
    }
}
