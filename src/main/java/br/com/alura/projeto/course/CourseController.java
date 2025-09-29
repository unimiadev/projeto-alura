package br.com.alura.projeto.course;

import br.com.alura.projeto.category.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final CategoryRepository categoryRepository;

    public CourseController(CourseService courseService, CategoryRepository categoryRepository) {
        this.courseService = courseService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/admin/courses")
    public String list(Model model) {
        List<CourseDTO> courses = courseService.findAll()
                .stream()
                .map(CourseDTO::new)
                .toList();

        model.addAttribute("courses", courses);
        return "admin/course/list";
    }

    @GetMapping("/admin/course/new")
    public String create(NewCourseForm form, Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/course/newForm";
    }

    @PostMapping("/admin/course/new")
    public String save(@Valid NewCourseForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return create(form, model);
        }

        try {
            courseService.save(form);
            return "redirect:/admin/courses";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return create(form, model);
        }
    }

    @PostMapping("/course/{code}/inactive")
    public ResponseEntity<?> updateStatus(@PathVariable("code") String courseCode) {
        try {
            courseService.inactivateCourse(courseCode);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
