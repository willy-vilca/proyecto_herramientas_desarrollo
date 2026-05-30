package SistemaContador.controller;

import SistemaContador.model.*;
import SistemaContador.service.CategoryService;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor

@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public String saveCategory(
            @RequestParam String action,
            Category category,
            HttpSession session
    ) {

        User user =
                (User) session.getAttribute("user");

        category.setUserId(user.getUserId());

        if ("update".equals(action)) {

            categoryService.update(category);

        } else {

            categoryService.save(category);
        }

        return "redirect:/movements";
    }

    @GetMapping("/delete")
    public String deleteCategory(
            @RequestParam Integer id
    ) {

        categoryService.delete(id);

        return "redirect:/movements";
    }
}
