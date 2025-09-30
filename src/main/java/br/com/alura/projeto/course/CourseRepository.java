package br.com.alura.projeto.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);

    Optional<Course> findByCode(String code);

    @Query("SELECT c FROM Course c JOIN FETCH c.category ORDER BY c.createdAt DESC")
    List<Course> findAllWithCategory();

    @Query("SELECT c FROM Course c JOIN FETCH c.category WHERE c.status = :status ORDER BY c.createdAt DESC")
    List<Course> findByStatusWithCategory(CourseStatus status);

    @Query("SELECT c FROM Course c JOIN FETCH c.category WHERE c.code = :code")
    Optional<Course> findByCodeWithCategory(String code);

    @Query("SELECT c FROM Course c JOIN FETCH c.category WHERE c.category.code = :categoryCode ORDER BY c.createdAt DESC")
    List<Course> findByCategoryCode(String categoryCode);
}
