package kz.yertayev.shop;

import kz.yertayev.shop.models.Brand;
import kz.yertayev.shop.models.Country;
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
public class CountriesController {

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/countries")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String brand(Model model){
        List<Country> countries = countryRepository.findAll();
        model.addAttribute("countries", countries);
        return "countries";
    }


    @PostMapping("/saveCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String save(Country country){
        countryRepository.save(country);
        return "redirect:/countries";
    }


    @GetMapping("/editCountry/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public String edit(Model model, @PathVariable Long id){
        Country country = countryRepository.getOne(id);
        model.addAttribute("country",country);
        return "editCountry";
    }

    @PostMapping("/deleteCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String delete(@RequestParam(name = "id")Long id){
        countryRepository.deleteById(id);
        return "redirect:/countries";
    }
}
