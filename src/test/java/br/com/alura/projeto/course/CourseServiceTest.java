package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;
import br.com.alura.projeto.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Programming", "prog", "#FF0000", 1);
        course = new Course("Spring Boot", "spring-boot", "instructor@alura.com", category, "Spring Boot course");
    }

    @Test
    void shouldInactivateCourseSuccessfully() {
        when(courseRepository.findByCode("spring-boot")).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        assertTrue(course.isActive());
        assertNull(course.getInactivatedAt());

        courseService.inactivateCourse("spring-boot");

        verify(courseRepository).findByCode("spring-boot");
        verify(courseRepository).save(course);
        
        assertEquals(CourseStatus.INACTIVE, course.getStatus());
        assertNotNull(course.getInactivatedAt());
        assertFalse(course.isActive());
    }

    @Test
    void shouldActivateCourseSuccessfully() {
        course.inactivate();
        LocalDateTime inactivatedAt = course.getInactivatedAt();
        
        when(courseRepository.findByCode("spring-boot")).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        assertFalse(course.isActive());
        assertNotNull(course.getInactivatedAt());

        courseService.activateCourse("spring-boot");

        verify(courseRepository).findByCode("spring-boot");
        verify(courseRepository).save(course);
        
        assertEquals(CourseStatus.ACTIVE, course.getStatus());
        assertNull(course.getInactivatedAt());
        assertTrue(course.isActive());
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFoundForInactivation() {
        when(courseRepository.findByCode("nonexistent")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> courseService.inactivateCourse("nonexistent")
        );

        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findByCode("nonexistent");
        verify(courseRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFoundForActivation() {
        when(courseRepository.findByCode("nonexistent")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> courseService.activateCourse("nonexistent")
        );

        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findByCode("nonexistent");
        verify(courseRepository, never()).save(any());
    }

    @Test
    void shouldSaveCourseWithValidData() {
        NewCourseForm form = new NewCourseForm();
        form.setName("Java Basics");
        form.setCode("java-basic");
        form.setInstructorEmail("instructor@alura.com");
        form.setCategoryId(1L);
        form.setDescription("Basic Java course");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.existsByEmail("instructor@alura.com")).thenReturn(true);
        when(courseRepository.existsByCode("java-basic")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course savedCourse = courseService.save(form);

        assertNotNull(savedCourse);
        verify(categoryRepository).findById(1L);
        verify(userRepository).existsByEmail("instructor@alura.com");
        verify(courseRepository).existsByCode("java-basic");
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        NewCourseForm form = new NewCourseForm();
        form.setName("Java Basics");
        form.setCode("java-basic");
        form.setInstructorEmail("instructor@alura.com");
        form.setCategoryId(999L);

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> courseService.save(form)
        );

        assertEquals("Category not found", exception.getMessage());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenInstructorNotFound() {
        NewCourseForm form = new NewCourseForm();
        form.setName("Java Basics");
        form.setCode("java-basic");
        form.setInstructorEmail("nonexistent@alura.com");
        form.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.existsByEmail("nonexistent@alura.com")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> courseService.save(form)
        );

        assertEquals("Instructor not found", exception.getMessage());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCourseCodeAlreadyExists() {
        NewCourseForm form = new NewCourseForm();
        form.setName("Java Basics");
        form.setCode("spring-boot");
        form.setInstructorEmail("instructor@alura.com");
        form.setCategoryId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(userRepository.existsByEmail("instructor@alura.com")).thenReturn(true);
        when(courseRepository.existsByCode("spring-boot")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> courseService.save(form)
        );

        assertEquals("Course code already exists", exception.getMessage());
        verify(courseRepository, never()).save(any());
    }
}
