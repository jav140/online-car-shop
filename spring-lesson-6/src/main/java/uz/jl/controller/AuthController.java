package uz.jl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.jl.configs.security.UserDetails;
import uz.jl.domains.AuthRole;
import uz.jl.domains.AuthUser;
import uz.jl.domains.Product;
import uz.jl.dto.LoginDto;
import uz.jl.dto.UserDto;
import uz.jl.services.AuthService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("permitAll()")
    @GetMapping("/login")
    public String loginPage(@ModelAttribute("loginDto") LoginDto loginDto) {
        return "auth/login";
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public String login(@ModelAttribute("loginDto") @Valid LoginDto loginDto, BindingResult result){
        if(result.hasErrors()){
            return "auth/login";
        }
        authService.login(loginDto,result);
        return "redirect:/";

    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "auth/logout";
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String registerPage(@ModelAttribute("userDto") UserDto userDto) {
        return "auth/register";
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(@ModelAttribute("userDto")  @Valid  UserDto userDto, BindingResult result) {

        if(result.hasErrors() || !userDto.getPassword().equals(userDto.getConfirmPassword())){
            return "auth/register";
        }

        authService.register(userDto,result);
        return "auth/login";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @RequestMapping(value = "/userList",method = RequestMethod.GET)
    public ModelAndView userList(){
        ModelAndView modelAndView = new ModelAndView("auth/userList");
        List<AuthUser> users = authService.findAll();
        modelAndView.addObject("users",users);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @RequestMapping(value = "/go/{username}",method = RequestMethod.GET)
    public ModelAndView profile(@PathVariable String username){
        UserDetails userDetails = authService.loadUserByUsername(username);
        ModelAndView modelAndView = new ModelAndView("auth/individualProfile");
        modelAndView.addObject("individual",userDetails);
        return modelAndView;
    }


}
