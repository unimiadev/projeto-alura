package br.com.alura.projeto.course;

import br.com.alura.projeto.category.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> inactivateCourse(@PathVariable("code") String courseCode) {
        try {
            courseService.inactivateCourse(courseCode);
            return ResponseEntity.ok().body("Course inactivated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/course/{code}/active")
    public ResponseEntity<?> activateCourse(@PathVariable("code") String courseCode) {
        try {
            courseService.activateCourse(courseCode);
            return ResponseEntity.ok().body("Course activated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin/course/edit/{code}")
    public String edit(@PathVariable("code") String code, Model model) {
        Optional<Course> courseOpt = courseService.findByCode(code);
        if (courseOpt.isEmpty()) {
            return "redirect:/admin/courses";
        }

        Course course = courseOpt.get();
        EditCourseForm form = new EditCourseForm(course);
        model.addAttribute("editCourseForm", form);
        model.addAttribute("course", course);
        model.addAttribute("categories", categoryRepository.findAll());
        
        return "admin/course/editForm";
    }

    @Transactional
    @PostMapping("/admin/course/edit/{code}")
    public String update(@PathVariable("code") String code, @Valid EditCourseForm form, 
                        BindingResult result, Model model) {
        
        Optional<Course> courseOpt = courseService.findByCode(code);
        if (courseOpt.isEmpty()) {
            return "redirect:/admin/courses";
        }

        Course course = courseOpt.get();
        
        if (result.hasErrors()) {
            model.addAttribute("course", course);
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/course/editForm";
        }

        try {
            courseService.updateCourse(course, form);
            return "redirect:/admin/courses";
        } catch (IllegalArgumentException e) {
            model.addAttribute("course", course);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("error", e.getMessage());
            return "admin/course/editForm";
        }
    }

}
