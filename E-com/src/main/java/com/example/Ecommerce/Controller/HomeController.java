package com.example.Ecommerce.Controller;

import com.example.Ecommerce.Model.Category;
import com.example.Ecommerce.Model.Product;
import com.example.Ecommerce.Repository.ProductRepository;
import com.example.Ecommerce.Service.CategoryService;
import com.example.Ecommerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {


    @Autowired
    private CategoryService categoryService;

   @Autowired
    private ProductService productService;




    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }


    @GetMapping("/product/{id}")
    public String view_product(@PathVariable int id, Model m){
        Product productById = productService.getProductById(id);
        m.addAttribute("product", productById);
        return "view_product";
    }

    @GetMapping("/products")
    public String products(Model m,@RequestParam(value = "category", defaultValue = "")String category){
        //System.out.println("category ="+category );
   List<Category> categories = categoryService.getAllActiveCategory();
 List<Product> products = productService.getAllActiveProducts(category);
  m.addAttribute("categories",categories);
        m.addAttribute("products",products);
        m.addAttribute("paramValue",category);
        return "product";
    }

}
