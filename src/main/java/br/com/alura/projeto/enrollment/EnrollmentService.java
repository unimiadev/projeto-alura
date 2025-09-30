package br.com.alura.projeto.enrollment;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.course.CourseRepository;
import br.com.alura.projeto.course.CourseStatus;
import br.com.alura.projeto.user.Role;
import br.com.alura.projeto.user.User;
import br.com.alura.projeto.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public Enrollment enrollStudent(NewEnrollmentRequest request) {
        User student = userRepository.findByEmail(request.getStudentEmail())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (student.getRole() != Role.STUDENT) {
            throw new IllegalArgumentException("User is not a student");
        }

        Course course = courseRepository.findByCodeWithCategory(request.getCourseCode())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new IllegalArgumentException("Course is not active");
        }

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }
        Enrollment enrollment = new Enrollment(student, course);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        
        clearEnrollmentCaches(request.getStudentEmail(), request.getCourseCode());
        
        return savedEnrollment;
    }

    @Cacheable(value = "enrollments", key = "'student-' + #studentEmail")
    @Transactional(readOnly = true)
    public List<Enrollment> getStudentEnrollments(String studentEmail) {
        return enrollmentRepository.findByStudentEmail(studentEmail);
    }

    @Cacheable(value = "enrollments", key = "'course-' + #courseCode")
    @Transactional(readOnly = true)
    public List<Enrollment> getCourseEnrollments(String courseCode) {
        return enrollmentRepository.findByCourseCode(courseCode);
    }

    @Transactional(readOnly = true)
    public Optional<Enrollment> findEnrollment(String studentEmail, String courseCode) {
        return enrollmentRepository.findByStudentEmailAndCourseCode(studentEmail, courseCode);
    }

    @Transactional
    public void completeEnrollment(String studentEmail, String courseCode) {
        Enrollment enrollment = enrollmentRepository.findByStudentEmailAndCourseCode(studentEmail, courseCode)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

        if (!enrollment.isActive()) {
            throw new IllegalArgumentException("Enrollment is not active");
        }

        enrollment.complete();
        enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void cancelEnrollment(String studentEmail, String courseCode) {
        Enrollment enrollment = enrollmentRepository.findByStudentEmailAndCourseCode(studentEmail, courseCode)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

        if (!enrollment.isActive()) {
            throw new IllegalArgumentException("Enrollment is not active");
        }

        enrollment.cancel();
        enrollmentRepository.save(enrollment);
    }

    @Cacheable(value = "course-stats", key = "'active-count-' + #courseCode")
    @Transactional(readOnly = true)
    public Long getActiveEnrollmentCount(String courseCode) {
        return enrollmentRepository.countActiveByCourseCode(courseCode);
    }

    /**
     * Limpa caches relacionados a matrículas quando há mudanças
     */
    @CacheEvict(value = {"enrollments", "course-stats"}, allEntries = true)
    public void clearEnrollmentCaches(String studentEmail, String courseCode) {
        // Este método limpa os caches quando há mudanças nas matrículas
        // Também pode notificar o ReportService para limpar caches de relatórios
    }

    /**
     * Limpa cache específico de um estudante
     */
    @CacheEvict(value = "enrollments", key = "'student-' + #studentEmail")
    public void clearStudentCache(String studentEmail) {
        // Limpa apenas o cache de um estudante específico
    }

    /**
     * Limpa cache específico de um curso
     */
    @CacheEvict(value = {"enrollments", "course-stats"}, key = "'course-' + #courseCode")
    public void clearCourseCache(String courseCode) {
        // Limpa apenas o cache de um curso específico
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAllWithStudentAndCourse();
    }
}
