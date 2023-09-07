package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ra.model.Product;
import ra.service.IProductService;


@Controller
public class ProductController {
    @Autowired
    IProductService productService;
    @GetMapping(value = {"/", "/product"})
    public ModelAndView listProduct(@PageableDefault(sort = "name",size = 3) Pageable pageable){
        Page<Product> productPage = productService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("/list");
        modelAndView.addObject("pageProduct",productPage);
        return modelAndView;
    }
    @GetMapping("/create/product")
    public ModelAndView showFormCreateProduct(){
        ModelAndView modelAndView = new ModelAndView("/add");
        modelAndView.addObject("createProductForm",new Product());
        return modelAndView;
    }
    @PostMapping("/create/product")
    public String createProduct(@Validated @ModelAttribute("createProductForm") Product product, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){
            return "/add";
        }
        productService.save(product);
        return "redirect:/product";
    }
    @GetMapping("/search")
    public ModelAndView searchProduct(@RequestParam("search") String search, Pageable pageable){
        Page<Product> productPage;
        if(!search.trim().equals("")){
            productPage = productService.findByNameProduct(search,pageable);
        } else {
            productPage = productService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/list");
        modelAndView.addObject("",productPage);
        return modelAndView;
    }
}