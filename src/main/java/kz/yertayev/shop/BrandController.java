package kz.yertayev.shop;

import kz.yertayev.shop.models.Brand;
import kz.yertayev.shop.models.Country;
import kz.yertayev.shop.repositiry.BrandRepository;
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
public class BrandController {
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/brand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public String brand(Model model){
        List<Country> countries = countryRepository.findAll();
        List<Brand> brands = brandRepository.findAll();
        model.addAttribute("countries", countries);
        model.addAttribute("brands", brands);
        return "brand";
    }


    @PostMapping("/saveBrand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String save(Brand brand){
        brandRepository.save(brand);
        return "redirect:/brand";
    }


    @GetMapping("/editBrand/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public String edit(Model model, @PathVariable Long id){
        Brand brand = brandRepository.getOne(id);
        List<Country> countries = countryRepository.findAll();
        model.addAttribute("brand", brand);
        model.addAttribute("countries", countries);
        return "editBrand";
    }

    @PostMapping("/deleteBrand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String delete(@RequestParam(name = "id")Long id){
        brandRepository.deleteById(id);
        return "redirect:/brand";
    }
}
