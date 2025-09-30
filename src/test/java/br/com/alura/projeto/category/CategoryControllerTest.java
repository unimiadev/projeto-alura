package br.com.alura.projeto.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    void shouldShowEditFormWhenCategoryExists() throws Exception {
        Category category = new Category("Programação", "programacao", "#007bff", 1);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/admin/category/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/editForm"))
                .andExpect(model().attributeExists("editCategoryForm"))
                .andExpect(model().attributeExists("category"));
    }

    @Test
    void shouldRedirectWhenCategoryNotFound() throws Exception {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/category/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }

    @Test
    void shouldUpdateCategorySuccessfully() throws Exception {
        Category category = new Category("Programação", "programacao", "#007bff", 1);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByCode("programacao")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/admin/category/edit/1")
                .param("name", "Programação Avançada")
                .param("code", "programacao")
                .param("color", "#ff0000")
                .param("order", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }

    @Test
    void shouldShowErrorWhenCodeAlreadyExists() throws Exception {
        Category category = new Category("Programação", "programacao", "#007bff", 1);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByCode("frontend")).thenReturn(true);

        mockMvc.perform(post("/admin/category/edit/1")
                .param("name", "Programação")
                .param("code", "frontend")
                .param("color", "#007bff")
                .param("order", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/editForm"))
                .andExpect(model().attributeExists("error"));
    }
}
