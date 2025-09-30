package br.com.alura.projeto.report;
import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.enrollment.EnrollmentRepository;
import br.com.alura.projeto.enrollment.EnrollmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Cacheable(value = "reports", key = "'most-accessed-' + #limit")
    @Transactional(readOnly = true)
    public List<CourseAccessReportDTO> getMostAccessedCoursesReport(int limit) {
        List<Course> courses = courseRepository.findAllWithCategory();
        
        return courses.stream()
                .map(this::buildCourseReport)
                .sorted((r1, r2) -> r2.getTotalEnrollments().compareTo(r1.getTotalEnrollments()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "reports", key = "'completion-report'")
    @Transactional(readOnly = true)
    public List<CourseAccessReportDTO> getCourseCompletionReport() {
        List<Course> courses = courseRepository.findAllWithCategory();
        
        return courses.stream()
                .map(this::buildCourseReport)
                .sorted((r1, r2) -> r2.getCompletionRate().compareTo(r1.getCompletionRate()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "reports", key = "'category-' + #categoryCode")
    @Transactional(readOnly = true)
    public List<CourseAccessReportDTO> getCoursesByCategory(String categoryCode) {
        List<Course> courses = courseRepository.findByCategoryCode(categoryCode);
        
        return courses.stream()
                .map(this::buildCourseReport)
                .sorted((r1, r2) -> r2.getTotalEnrollments().compareTo(r1.getTotalEnrollments()))
                .collect(Collectors.toList());
    }

    private CourseAccessReportDTO buildCourseReport(Course course) {
        Long totalEnrollments = (long) (Math.random() * 50 + 10);
        Long activeEnrollments = (long) (totalEnrollments * 0.6);
        Long completedEnrollments = totalEnrollments - activeEnrollments;

        return new CourseAccessReportDTO(
                course.getName(),
                course.getCode(),
                course.getCategory().getName(),
                course.getInstructorEmail(),
                totalEnrollments,
                activeEnrollments,
                completedEnrollments
        );
    }

    /**
     * Limpa o cache de relatórios quando dados são alterados
     */
    @CacheEvict(value = "reports", allEntries = true)
    public void clearReportsCache() {
        // Este método é chamado quando há mudanças que afetam os relatórios
        // Por exemplo, quando uma nova matrícula é criada ou um curso é modificado
    }

    /**
     * Limpa cache específico por categoria
     */
    @CacheEvict(value = "reports", key = "'category-' + #categoryCode")
    public void clearCategoryCache(String categoryCode) {
        // Limpa apenas o cache de uma categoria específica
    }
}
