package uz.jl.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import uz.jl.configs.security.UserDetails;
import uz.jl.domains.Product;
import uz.jl.services.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService service;

    @RequestMapping()
    @PreAuthorize("permitAll()")
    public String homePage() {
        return "index";
    }

    @ResponseBody
    @RequestMapping("/open-1")
    public String open1Page() {
        return "OPEN URL";
    }

    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/admin")
    public String admin() {
        return "ADMIN PAGE";
    }

    @ResponseBody
    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping("/mana")
    public String manager() {
        return "MANAGER PAGE";
    }

    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @RequestMapping("/admin-manager")
    public String adminManager() {
        return "ADMIN AND MANAGER PAGE";
    }


//    "isAuthenticated() || hasAuthority('create')"
    @PreAuthorize("permitAll()")
    @RequestMapping("/user")
    public ModelAndView user(@AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView modelAndView = new ModelAndView("auth/userProfile");
        modelAndView.addObject("user",userDetails);
        return modelAndView;
    }


    @ResponseBody
    @RequestMapping("/create")
    public String hasAuthorityCreate() {
        return "hasAuthorityCreate";
    }


}
