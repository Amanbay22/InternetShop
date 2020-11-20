package kz.yertayev.shop;

import kz.yertayev.shop.models.Categories;
import kz.yertayev.shop.models.Country;
import kz.yertayev.shop.repositiry.CategoriesRepository;
import kz.yertayev.shop.repositiry.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoriesController {
    
    @Autowired
    private CategoriesRepository categoriesRepository;

    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public String brand(Model model){
        List<Categories> categories = categoriesRepository.findAll();
        model.addAttribute("categories",categories);
        return "categories";
    }


    @PostMapping("/saveCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String save(Categories categories){
        categoriesRepository.save(categories);
        return "redirect:/categories";
    }


    @GetMapping("/editCategory/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public String edit(Model model, @PathVariable Long id){
        Categories category = categoriesRepository.getOne(id);
        model.addAttribute("category",category);
        return "editCategory";
    }

    @PostMapping("/deleteCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public String delete(@RequestParam(name = "id")Long id){
        categoriesRepository.deleteById(id);
        return "redirect:/categories";
    }
}
