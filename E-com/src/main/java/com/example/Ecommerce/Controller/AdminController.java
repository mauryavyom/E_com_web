package com.example.Ecommerce.Controller;


import com.example.Ecommerce.Model.Category;
import com.example.Ecommerce.Model.Product;
import com.example.Ecommerce.Service.CategoryService;
import com.example.Ecommerce.Service.ProductService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

   @Autowired
    private CategoryService categoryService;

   @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index()
    {
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m){
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories",categories);
        return "admin/add_product";
    }

    @GetMapping("/category")
    public String category(Model m){
        m.addAttribute("categorys",categoryService.getAllCategory());
        return "admin/category";
    }


    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {

        String imageName  =  file!=null ? file.getOriginalFilename():"default.jpg";
      category.setImageName(imageName);
        Boolean existCategory =categoryService.existCategory((category.getName()));
    if(existCategory){
        session.setAttribute("errorMsg","Category Name Already Exists");
    }else{
     Category saveCategory = categoryService.saveCategory(category);
     if(ObjectUtils.isEmpty(saveCategory)){
       session.setAttribute("errorMsg","Not Saved ! internal server error");
     }
     else{

        File saveFile = new ClassPathResource("static/img").getFile();
     Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+file.getOriginalFilename());

         System.out.println(path);
         Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

         session.setAttribute("succMsg","Saved Successfully");
     }
    }

        return "redirect:/admin/category";
    }


    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id,HttpSession session){
      Boolean deleteCategory = categoryService.deleteCategory(id);
        if(deleteCategory){
            session.setAttribute("succMsg","category delete success");
        }else{
            session.setAttribute("errorMsg","Something wrong on server");
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id,Model m){
 m.addAttribute("category",categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,HttpSession session) throws IOException {
       Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName():file.getOriginalFilename();

       if(!ObjectUtils.isEmpty(category)){
      oldCategory.setName(category.getName());
       oldCategory.setIsActive(category.getIsActive());
      oldCategory.setImageName(imageName);
        }

       Category updateCategory = categoryService.saveCategory(oldCategory);

       if(!ObjectUtils.isEmpty(updateCategory)){
           if(!file.isEmpty()){

               File saveFile = new ClassPathResource("static/img").getFile();
               Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+file.getOriginalFilename());

               System.out.println(path);
               Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
           }

   session.setAttribute("succMsg","Category update Success");
       }else{
           session.setAttribute("errorMsg","Something wrong on server");
       }
        return "redirect:/admin/loadEditCategory/"+category.getId();
    }
    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image ,HttpSession session) throws IOException {
        String imageName = image.isEmpty()? "default.jpg": image.getOriginalFilename();

        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());

        Product saveProduct = productService.saveProduct(product);

        if(!ObjectUtils.isEmpty(saveProduct))
        {
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator + image.getOriginalFilename());
            session.setAttribute("succMsg", "Product Saved Success");

           // System.out.println(path);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }else{
            session.setAttribute("errorMsg", "Something went wrong");
        }
        return "redirect:/admin/loadAddProduct";
    }
    @GetMapping("/products")
    public String loadViewProduct(Model m){
        m.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id,HttpSession session) {
        Boolean deleteProduct = productService.deleteProduct(id);
        System.out.println("del"+deleteProduct);
        if(deleteProduct){
            session.setAttribute("succMsg","Product delete success");
        }else{
            session.setAttribute("errorMsg","Something wrong on server");
        }

        return "redirect:/admin/products";
    }
    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id, Model m) {
        m.addAttribute("product",productService.getProductById(id));
        m.addAttribute("categories",categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image, HttpSession session, Model m) throws IOException {

        if(product.getDiscount()<0 || product.getDiscount()>100){
            session.setAttribute("errorMsg","Invalid Discount");
        }
        else{
            Product updateProduct = productService.updateProduct(product, image);
            if(!ObjectUtils.isEmpty(updateProduct)){
                session.setAttribute("succMsg","Product Update success");
            }else {
                session.setAttribute("errorMsg","Something wrong on server");
            }
        }
        return "redirect:/admin/editProduct/" + product.getId();
    }
}