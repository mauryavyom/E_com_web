package com.example.Ecommerce.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

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


    @GetMapping("/view")
    public String view_product(){
        return "view_product";
    }

    @GetMapping("/products")
    public String products() {
        return "product";
    }

}
