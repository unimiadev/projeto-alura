package br.com.alura.projeto.enrollment;

import br.com.alura.projeto.course.Course;
import br.com.alura.projeto.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student JOIN FETCH e.course WHERE e.student.email = :studentEmail")
    List<Enrollment> findByStudentEmail(@Param("studentEmail") String studentEmail);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student JOIN FETCH e.course WHERE e.course.code = :courseCode")
    List<Enrollment> findByCourseCode(@Param("courseCode") String courseCode);

    @Query("SELECT e FROM Enrollment e WHERE e.student.email = :studentEmail AND e.course.code = :courseCode")
    Optional<Enrollment> findByStudentEmailAndCourseCode(@Param("studentEmail") String studentEmail, 
                                                        @Param("courseCode") String courseCode);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student JOIN FETCH e.course WHERE e.status = :status")
    List<Enrollment> findByStatus(@Param("status") EnrollmentStatus status);

    boolean existsByStudentAndCourse(User student, Course course);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.code = :courseCode AND e.status = 'ACTIVE'")
    Long countActiveByCourseCode(@Param("courseCode") String courseCode);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student JOIN FETCH e.course")
    List<Enrollment> findAllWithStudentAndCourse();
}
