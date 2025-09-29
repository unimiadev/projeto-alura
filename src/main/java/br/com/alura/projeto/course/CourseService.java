package br.com.alura.projeto.course;

import br.com.alura.projeto.category.Category;
import br.com.alura.projeto.category.CategoryRepository;
import br.com.alura.projeto.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CourseService {

    private static final Pattern CODE_PATTERN = Pattern.compile("^[a-z0-9-]+$");

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return courseRepository.findAllWithCategory();
    }

    @Transactional(readOnly = true)
    public List<Course> findByStatus(CourseStatus status) {
        return courseRepository.findByStatusWithCategory(status);
    }

    @Transactional(readOnly = true)
    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }

    @Transactional
    public Course save(NewCourseForm form) {
        validateCourseForm(form);

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (!userRepository.existsByEmail(form.getInstructorEmail())) {
            throw new IllegalArgumentException("Instructor not found");
        }

        String normalizedCode = normalizeCode(form.getCode());
        
        if (courseRepository.existsByCode(normalizedCode)) {
            throw new IllegalArgumentException("Course code already exists");
        }

        Course course = new Course(
                form.getName(),
                normalizedCode,
                form.getInstructorEmail(),
                category,
                form.getDescription()
        );

        return courseRepository.save(course);
    }

    @Transactional
    public void inactivateCourse(String code) {
        Course course = courseRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        course.inactivate();
        courseRepository.save(course);
    }

    @Transactional
    public void activateCourse(String code) {
        Course course = courseRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        course.activate();
        courseRepository.save(course);
    }

    private void validateCourseForm(NewCourseForm form) {
        String code = form.getCode();
        
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code is required");
        }

        String normalizedCode = normalizeCode(code);
        
        if (!CODE_PATTERN.matcher(normalizedCode).matches()) {
            throw new IllegalArgumentException("Course code must contain only lowercase letters, numbers and hyphens");
        }

        if (normalizedCode.length() < 4 || normalizedCode.length() > 10) {
            throw new IllegalArgumentException("Course code must be between 4 and 10 characters");
        }
    }

    private String normalizeCode(String code) {
        return code.toLowerCase()
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]", "");
    }
}
