package br.com.alura.projeto.report;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.enrollment.EnrollmentRepository;
import br.com.alura.projeto.enrollment.EnrollmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private ReportService reportService;

    private Course course1;
    private Course course2;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Programming", "prog", "#FF0000", 1);
        course1 = new Course("Spring Boot", "spring-boot", "instructor@alura.com", category, "Spring Boot course");
        course2 = new Course("React JS", "react-js", "instructor@alura.com", category, "React JS course");
    }

    @Test
    void shouldReturnMostAccessedCoursesReport() {
        // Given
        List<Course> courses = Arrays.asList(course1, course2);
        when(courseRepository.findAllWithCategory()).thenReturn(courses);
        
        // Mock enrollment counts for course1 (more popular)
        when(enrollmentRepository.countByCourse(course1)).thenReturn(10L);
        when(enrollmentRepository.countByCourseAndStatus(course1, EnrollmentStatus.ACTIVE)).thenReturn(7L);
        when(enrollmentRepository.countByCourseAndStatus(course1, EnrollmentStatus.COMPLETED)).thenReturn(3L);
        
        // Mock enrollment counts for course2 (less popular)
        when(enrollmentRepository.countByCourse(course2)).thenReturn(5L);
        when(enrollmentRepository.countByCourseAndStatus(course2, EnrollmentStatus.ACTIVE)).thenReturn(4L);
        when(enrollmentRepository.countByCourseAndStatus(course2, EnrollmentStatus.COMPLETED)).thenReturn(1L);

        // When
        List<CourseAccessReportDTO> result = reportService.getMostAccessedCoursesReport(10);

        // Then
        assertEquals(2, result.size());
        
        // Should be sorted by total enrollments (descending)
        CourseAccessReportDTO first = result.get(0);
        assertEquals("Spring Boot", first.getCourseName());
        assertEquals("spring-boot", first.getCourseCode());
        assertEquals(10L, first.getTotalEnrollments());
        assertEquals(7L, first.getActiveEnrollments());
        assertEquals(3L, first.getCompletedEnrollments());
        assertEquals(30.0, first.getCompletionRate(), 0.1);

        CourseAccessReportDTO second = result.get(1);
        assertEquals("React JS", second.getCourseName());
        assertEquals(5L, second.getTotalEnrollments());
        assertEquals(20.0, second.getCompletionRate(), 0.1);
    }

    @Test
    void shouldLimitResultsWhenRequested() {
        // Given
        List<Course> courses = Arrays.asList(course1, course2);
        when(courseRepository.findAllWithCategory()).thenReturn(courses);
        
        when(enrollmentRepository.countByCourse(any(Course.class))).thenReturn(5L);
        when(enrollmentRepository.countByCourseAndStatus(any(Course.class), any(EnrollmentStatus.class))).thenReturn(2L);

        // When
        List<CourseAccessReportDTO> result = reportService.getMostAccessedCoursesReport(1);

        // Then
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnCourseCompletionReportSortedByCompletionRate() {
        // Given
        List<Course> courses = Arrays.asList(course1, course2);
        when(courseRepository.findAllWithCategory()).thenReturn(courses);
        
        // Course1: 50% completion rate
        when(enrollmentRepository.countByCourse(course1)).thenReturn(10L);
        when(enrollmentRepository.countByCourseAndStatus(course1, EnrollmentStatus.ACTIVE)).thenReturn(5L);
        when(enrollmentRepository.countByCourseAndStatus(course1, EnrollmentStatus.COMPLETED)).thenReturn(5L);
        
        // Course2: 80% completion rate (should be first)
        when(enrollmentRepository.countByCourse(course2)).thenReturn(5L);
        when(enrollmentRepository.countByCourseAndStatus(course2, EnrollmentStatus.ACTIVE)).thenReturn(1L);
        when(enrollmentRepository.countByCourseAndStatus(course2, EnrollmentStatus.COMPLETED)).thenReturn(4L);

        // When
        List<CourseAccessReportDTO> result = reportService.getCourseCompletionReport();

        // Then
        assertEquals(2, result.size());
        
        // Should be sorted by completion rate (descending)
        assertEquals("React JS", result.get(0).getCourseName());
        assertEquals(80.0, result.get(0).getCompletionRate(), 0.1);
        
        assertEquals("Spring Boot", result.get(1).getCourseName());
        assertEquals(50.0, result.get(1).getCompletionRate(), 0.1);
    }

    @Test
    void shouldReturnCoursesByCategory() {
        // Given
        String categoryCode = "prog";
        List<Course> courses = Arrays.asList(course1);
        when(courseRepository.findByCategoryCode(categoryCode)).thenReturn(courses);
        
        when(enrollmentRepository.countByCourse(course1)).thenReturn(8L);
        when(enrollmentRepository.countByCourseAndStatus(course1, EnrollmentStatus.ACTIVE)).thenReturn(5L);
        when(enrollmentRepository.countByCourseAndStatus(course1, EnrollmentStatus.COMPLETED)).thenReturn(3L);

        // When
        List<CourseAccessReportDTO> result = reportService.getCoursesByCategory(categoryCode);

        // Then
        assertEquals(1, result.size());
        CourseAccessReportDTO report = result.get(0);
        assertEquals("Spring Boot", report.getCourseName());
        assertEquals("Programming", report.getCategoryName());
        assertEquals(8L, report.getTotalEnrollments());
    }

    @Test
    void shouldCalculateCompletionRateCorrectly() {
        // Given
        List<Course> courses = Arrays.asList(course1);
        when(courseRepository.findAllWithCategory()).thenReturn(courses);
        
        // No enrollments
        when(enrollmentRepository.countByCourse(course1)).thenReturn(0L);
        when(enrollmentRepository.countByCourseAndStatus(course1, EnrollmentStatus.ACTIVE)).thenReturn(0L);
        when(enrollmentRepository.countByCourseAndStatus(course1, EnrollmentStatus.COMPLETED)).thenReturn(0L);

        // When
        List<CourseAccessReportDTO> result = reportService.getMostAccessedCoursesReport(10);

        // Then
        assertEquals(1, result.size());
        assertEquals(0.0, result.get(0).getCompletionRate());
    }

    @Test
    void shouldVerifyRepositoryInteractions() {
        // Given
        List<Course> courses = Arrays.asList(course1);
        when(courseRepository.findAllWithCategory()).thenReturn(courses);
        when(enrollmentRepository.countByCourse(any(Course.class))).thenReturn(1L);
        when(enrollmentRepository.countByCourseAndStatus(any(Course.class), any(EnrollmentStatus.class))).thenReturn(1L);

        // When
        reportService.getMostAccessedCoursesReport(10);

        // Then
        verify(courseRepository).findAllWithCategory();
        verify(enrollmentRepository).countByCourse(course1);
        verify(enrollmentRepository).countByCourseAndStatus(course1, EnrollmentStatus.ACTIVE);
        verify(enrollmentRepository).countByCourseAndStatus(course1, EnrollmentStatus.COMPLETED);
    }
}
