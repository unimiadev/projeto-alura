package br.com.alura.projeto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class EditCategoryForm {

    @NotBlank
    private String name;

    @NotBlank
    @Length(min = 4, max = 10)
    private String code;

    @NotBlank
    private String color;

    @NotNull
    @Min(1)
    private int order;

    @Deprecated
    public EditCategoryForm() {}

    public EditCategoryForm(Category category) {
        this.name = category.getName();
        this.code = category.getCode();
        this.color = category.getColor();
        this.order = category.getOrder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void updateCategory(Category category) {
        category.setName(this.name);
        category.setCode(this.code);
        category.setColor(this.color);
        category.setOrder(this.order);
    }
}
