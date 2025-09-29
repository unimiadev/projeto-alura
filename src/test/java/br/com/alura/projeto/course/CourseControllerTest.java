package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private CategoryRepository categoryRepository;

    private Course course;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Programming", "prog", "#FF0000", 1);
        course = new Course("Spring Boot", "spring-boot", "instructor@alura.com", category, "Spring Boot course");
    }

    @Test
    void shouldReturnCourseListPage() throws Exception {
        List<Course> courses = Arrays.asList(course);
        when(courseService.findAll()).thenReturn(courses);

        mockMvc.perform(get("/admin/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/list"))
                .andExpect(model().attributeExists("courses"));

        verify(courseService).findAll();
    }

    @Test
    void shouldReturnNewCourseFormPage() throws Exception {
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll()).thenReturn(categories);

        mockMvc.perform(get("/admin/course/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/newForm"))
                .andExpect(model().attributeExists("categories"));

        verify(categoryRepository).findAll();
    }

    @Test
    void shouldInactivateCourseSuccessfully() throws Exception {
        doNothing().when(courseService).inactivateCourse("spring-boot");

        mockMvc.perform(post("/course/spring-boot/inactive")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Course inactivated successfully"));

        verify(courseService).inactivateCourse("spring-boot");
    }

    @Test
    void shouldActivateCourseSuccessfully() throws Exception {
        doNothing().when(courseService).activateCourse("spring-boot");

        mockMvc.perform(post("/course/spring-boot/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Course activated successfully"));

        verify(courseService).activateCourse("spring-boot");
    }

    @Test
    void shouldReturnBadRequestWhenCourseNotFoundForInactivation() throws Exception {
        doThrow(new IllegalArgumentException("Course not found"))
                .when(courseService).inactivateCourse("nonexistent");

        mockMvc.perform(post("/course/nonexistent/inactive")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Course not found"));

        verify(courseService).inactivateCourse("nonexistent");
    }

    @Test
    void shouldReturnBadRequestWhenCourseNotFoundForActivation() throws Exception {
        doThrow(new IllegalArgumentException("Course not found"))
                .when(courseService).activateCourse("nonexistent");

        mockMvc.perform(post("/course/nonexistent/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Course not found"));

        verify(courseService).activateCourse("nonexistent");
    }

    @Test
    void shouldSaveCourseAndRedirectToList() throws Exception {
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(courseService.save(any(NewCourseForm.class))).thenReturn(course);

        mockMvc.perform(post("/admin/course/new")
                .param("name", "Java Basics")
                .param("code", "java-basics")
                .param("instructorEmail", "instructor@alura.com")
                .param("categoryId", "1")
                .param("description", "Basic Java course"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/courses"));

        verify(courseService).save(any(NewCourseForm.class));
    }

    @Test
    void shouldReturnFormWithErrorWhenValidationFails() throws Exception {
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll()).thenReturn(categories);

        mockMvc.perform(post("/admin/course/new")
                .param("name", "")
                .param("code", "")
                .param("instructorEmail", "invalid-email")
                .param("categoryId", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/course/newForm"))
                .andExpect(model().attributeExists("categories"));

        verify(courseService, never()).save(any());
    }
}
