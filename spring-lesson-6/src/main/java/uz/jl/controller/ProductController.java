package uz.jl.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import uz.jl.configs.security.UserDetails;
import uz.jl.domains.Comment;
import uz.jl.domains.Product;
import uz.jl.domains.ProductContact;
import uz.jl.dto.CommentDto;
import uz.jl.dto.ProductCreateDto;
import uz.jl.dto.UserContactDto;
import uz.jl.services.ProductService;

import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/productAdd",method = RequestMethod.GET)
    public String productAddPage(@ModelAttribute("productCreateDto") ProductCreateDto dto){
        return "product/productAddPage";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/productAdd",method = RequestMethod.POST)
    public String productAdd(@ModelAttribute("productCreateDto") ProductCreateDto dto,
                             @AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam("file") CommonsMultipartFile file){
        service.add(dto,userDetails,file);
        return "redirect:/";
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/productList",method = RequestMethod.GET)
    public ModelAndView productList(
                                    @RequestParam(name = "page") Optional<Integer> page,
                                    @RequestParam(name = "limit") Optional<Integer> limit,
                                    @Param("search") String search,
                                    Model model){
        Page<Product> allByPage = service.findAllByPage(page, limit);
        model.addAttribute("search",search);
        model.addAttribute("page",allByPage);
        model.addAttribute("pageNumbers", IntStream.range(0,allByPage.getTotalPages()).toArray());
        ModelAndView modelAndView = new ModelAndView("product/productListPage");
        List<Product> productList = service.listAll(search);
        modelAndView.addObject("products",productList);
        modelAndView.addObject(model);
        return modelAndView;

 // Page<Product> productPage = service.getAllProducts(searchQuery, page, limit);
//        model.addAttribute("page",allByPage);
//        model.addAttribute("keyword",keyword);
//        List<Product> listProducts = service.listAll(searchQuery);
//        modelAndView.addObject("products",listProducts);
//        modelAndView.addObject(model);

    }



    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/addToCard/{id}",method = RequestMethod.GET)
    public String addCartPage(@PathVariable String id, @ModelAttribute("addToCard") ProductCreateDto dto){
        return "product/addCardPage";
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/addToCard",method = RequestMethod.POST)
    public String addToCart(@ModelAttribute("addToCard") ProductCreateDto dto){
        return "product/addCardPage";

    }

//    @PreAuthorize("permitAll()")
//    @RequestMapping(value = "/leaveComment/{id}",method = RequestMethod.GET)
//    public String leaveCommentPage(@PathVariable String id, @ModelAttribute("commentDto") CommentDto dto){
//        return "product/addCardPage";
//    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/leaveComment/{id}",method = RequestMethod.GET)
    public String leaveCommentPage(@PathVariable String id,
                               @ModelAttribute("commentDto") CommentDto dto, Model model){
        Product product = service.getById(id);
        model.addAttribute("product",product);
        return "product/commentPage";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/leaveComment",method = RequestMethod.POST)
    public String leaveComment(
                               @ModelAttribute("commentDto") CommentDto dto,
                               @AuthenticationPrincipal UserDetails userDetails){
        service.leaveComment(dto,userDetails);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/commentList",method = RequestMethod.GET)
    public ModelAndView commentList(){
        ModelAndView modelAndView = new ModelAndView("product/commentListPage");
        List<Comment> commentList = service.findAllComments();
        modelAndView.addObject("comments",commentList);
        return modelAndView;
    }

    @PreAuthorize("permitAll()")
    @GetMapping (value = "/addToBasket/{id}")
    public String addToBasket(@PathVariable String id,@AuthenticationPrincipal UserDetails userDetails){
        service.addToBasket(id, userDetails.getUsername());

        return "redirect:/productList";
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/showBasket")
    public ModelAndView addToBasket(@AuthenticationPrincipal UserDetails userDetails){
        List<Product> productList = service.findAllProductsByUsername(userDetails.getUsername());
        long sumOfAllProducts = 0;
        for (Product product : productList) {
            sumOfAllProducts+=product.getPrice();
        }
        ModelAndView modelAndView = new ModelAndView("product/basketPage");
        modelAndView.addObject("productList",productList);
        modelAndView.addObject("balance",sumOfAllProducts);
        return modelAndView;
    }

    @PreAuthorize("permitAll()")
    @PostMapping (value = "/buyProduct/{id}")
    public String buyProduct(@PathVariable String id,
            @ModelAttribute("phoneNumber") String phoneNumber,
            @AuthenticationPrincipal UserDetails userDetails){
        service.addToConfirmProduct(id, userDetails.getUsername());
        service.saveContact(phoneNumber,userDetails.getUsername());
        return "redirect:/productList";
    }

    @PreAuthorize("permitAll()")
    @GetMapping (value = "/confirm")
    public String confirm(@ModelAttribute("userContactDto") UserContactDto userContactDto){
        return "product/phoneNumberPage";

    }

    @PreAuthorize("permitAll()")
    @PostMapping (value = "/confirmProduct")
    public String confirmProduct(@ModelAttribute("userContactDto") UserContactDto userContactDto,
                                 @AuthenticationPrincipal UserDetails userDetails){

        userContactDto.setUsername(userDetails.getUsername());
        service.confirmProduct(userContactDto);
        return "redirect:/productList";
    }

    @PreAuthorize("permitAll()")
    @GetMapping (value = "/deleteProduct/{id}")
    public String productDelete(@PathVariable("id") String id){
        service.deleteProductById(id);
        return "redirect:/productList";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping (value = "/showConfirmedProducts")
    public ModelAndView showConfirmProducts() throws JsonProcessingException {
        List<ProductContact> productContactList = service.getAllProductContactList();
        ModelAndView modelAndView = new ModelAndView("product/confirmedProductPage");
        modelAndView.addObject("productContactList",productContactList);
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping (value = "/productUpdate/{id}")
    public String productUpdatePage(@PathVariable("id") String id,@ModelAttribute("productDto") ProductCreateDto dto,
                                Model model)  {
        Product product = service.getById(id);
        model.addAttribute("product",product);
        return "product/productUpdatePage";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping (value = "/productUpdate")
    public String productUpdate(@ModelAttribute("productDto") ProductCreateDto dto,
                                @AuthenticationPrincipal UserDetails userDetails,@RequestParam("file") CommonsMultipartFile file)  {
        Product product = service.getById(String.valueOf(dto.getId()));
        service.updateProduct(dto,userDetails,file,product);
        return "redirect:/productList";
    }

    @GetMapping(value = "/display")
    public void getImage(@RequestParam("img") String path, HttpServletResponse response) throws IOException {
        Path imagePath = Paths.get("C:\\Users\\User\\Desktop\\exam\\spring\\spring-lesson-6\\src\\main\\resources\\static\\images", path);
        ServletOutputStream outputStream = response.getOutputStream();
        Files.copy(imagePath, outputStream);
    }









}
