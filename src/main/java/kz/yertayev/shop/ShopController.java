package kz.yertayev.shop;

import kz.yertayev.shop.models.Categories;
import kz.yertayev.shop.models.Item;
import kz.yertayev.shop.models.Users;
import kz.yertayev.shop.repositiry.BrandRepository;
import kz.yertayev.shop.repositiry.CategoriesRepository;
import kz.yertayev.shop.repositiry.ItemRepository;
import kz.yertayev.shop.repositiry.UserRepository;
import kz.yertayev.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ShopController {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/")
    public String index(
            Model model) {
        model.addAttribute("user",getUserData());
        model.addAttribute("items", itemRepository.findAllByInTopPage(true));
        model.addAttribute("brands",brandRepository.findAll());
        model.addAttribute("categories",categoriesRepository.findAll());
        return "index";
    }
    @GetMapping("/admin_panel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String admin(Model model) {
        model.addAttribute("brands",brandRepository.findAll());
        model.addAttribute("items", itemRepository.findAll());
        return "admin";
    }

    @PostMapping("/addNew")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String add(Item item) {
        item.setAddedDate(new Date(System.currentTimeMillis()));
        itemRepository.save(item);
        return "redirect:/admin_panel";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String edit(@PathVariable Long id, Model model) {
        Item item = itemRepository.getOne(id);
        List<Categories> categories = new ArrayList<>();
        for(Categories cat: categoriesRepository.findAll()){
            if(!item.getCategories().contains(cat)){
                categories.add(cat);
            }

        }
        model.addAttribute("categories", categories);
        model.addAttribute("brands",brandRepository.findAll());
        model.addAttribute("item", item);
        return "edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String editItem(Item item,
                           @RequestParam(name = "id") Long id) {
        Item one = itemRepository.getOne(id);
        item.setCategories(one.getCategories());
        item.setAddedDate(one.getAddedDate());
        itemRepository.save(item);
        return "redirect:/edit/" + id;
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String delete(@RequestParam(name = "id") Long id) {
        itemRepository.deleteById(id);
        return "redirect:/admin_panel";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("categories",categoriesRepository.findAll());
        model.addAttribute("user",getUserData());
        model.addAttribute(itemRepository.getOne(id));
        model.addAttribute("brands",brandRepository.findAll());
        return "detail";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "name", defaultValue = "") String name,
                         @RequestParam(name = "priceFrom", defaultValue = "0") double priceFrom,
                         @RequestParam(name = "priceTo", defaultValue = "10000") double priceTo,
                         @RequestParam(name = "brand", defaultValue = "") String brand,
                         Model model,
                         @RequestParam(name = "ordering", defaultValue = "asc") String ordering) {
        model.addAttribute("user",getUserData());
        model.addAttribute("categories",categoriesRepository.findAll());
        model.addAttribute("name", name);
        model.addAttribute("priceFrom",priceFrom);
        model.addAttribute("priceTo",priceTo);
        model.addAttribute("brand",brand);
        model.addAttribute("brands", brandRepository.findAll());
        if (ordering.equals("asc"))
            model.addAttribute("items", itemRepository.findAllByNameContainingAndBrandNameContainingAndPriceBetweenOrderByPriceAsc(name,brand,priceFrom,priceTo));
        else {
            model.addAttribute("items", itemRepository.findAllByNameContainingAndBrandNameContainingAndPriceBetweenOrderByPriceDesc(name,brand,priceFrom,priceTo));
        }
        return "search";
    }
    @PostMapping("/assignCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String assignCategory(@RequestParam(name = "item_id")Long itemId,
                                 @RequestParam(name = "category_id")Long categoryId){
        Categories category = categoriesRepository.getOne(categoryId);
        if(category!=null) {
            Item item = itemRepository.getOne(itemId);
            if (item != null) {
                List<Categories> categories = item.getCategories();

                if (categories == null) {
                    categories = new ArrayList<>();
                }
                categories.add(category);
                itemRepository.save(item);
            }
        }
        return "redirect:edit/"+itemId;
    }

    @PostMapping("/unAssignCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String unAssignCategory(@RequestParam(name = "item_id")Long itemId,
                                 @RequestParam(name = "category_id")Long categoryId){

            Item item = itemRepository.getOne(itemId);
            if (item != null) {
                 item.getCategories().removeIf(categories -> categories.getId().equals(categoryId));
            }
            itemRepository.save(item);
        return "redirect:edit/"+itemId;
    }
    @GetMapping(value = "/403")
    public String accessDenied(Model model){
        return "403";
    }
    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    private Users getUserData(){
        Authentication   authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            return userRepository.findByEmail(secUser.getUsername());
        }
        return null;
    }
}
