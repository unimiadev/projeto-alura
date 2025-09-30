package br.com.alura.projeto.category;

import jakarta.validation.Valid;
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
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/admin/categories")
    public String list(Model model) {
        List<CategoryDTO> list = categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .toList();

        model.addAttribute("categories", list);

        return "admin/category/list";
    }

    @GetMapping("/admin/category/new")
    public String create(NewCategoryForm newCategory, Model model) {
        return "admin/category/newForm";
    }

    @Transactional
    @PostMapping("/admin/category/new")
    public String save(@Valid NewCategoryForm form, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return create(form, model);
        }

        if (categoryRepository.existsByCode(form.getCode())) {
            return create(form, model);
        }

        categoryRepository.save(form.toModel());
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/category/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            return "redirect:/admin/categories";
        }

        Category category = categoryOpt.get();
        EditCategoryForm form = new EditCategoryForm(category);
        model.addAttribute("editCategoryForm", form);
        model.addAttribute("category", category);
        
        return "admin/category/editForm";
    }

    @Transactional
    @PostMapping("/admin/category/edit/{id}")
    public String update(@PathVariable("id") Long id, @Valid EditCategoryForm form, 
                        BindingResult result, Model model) {
        
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            return "redirect:/admin/categories";
        }

        Category category = categoryOpt.get();
        
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            return "admin/category/editForm";
        }

        if (!category.getCode().equals(form.getCode()) && 
            categoryRepository.existsByCode(form.getCode())) {
            model.addAttribute("category", category);
            model.addAttribute("error", "Category code already exists");
            return "admin/category/editForm";
        }

        form.updateCategory(category);
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

}
