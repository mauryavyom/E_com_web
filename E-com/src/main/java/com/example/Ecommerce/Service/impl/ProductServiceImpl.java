package com.example.Ecommerce.Service.impl;

import com.example.Ecommerce.Model.Product;
import com.example.Ecommerce.Repository.ProductRepository;
import com.example.Ecommerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteProduct(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        if(!ObjectUtils.isEmpty(product)){
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        return product;
    }

    @Override
    public Product updateProduct(Product product, MultipartFile image) throws IOException {

        Product dbProduct = getProductById(product.getId());
        String imageName = image.isEmpty() ? dbProduct.getImage() : image.getOriginalFilename();


        dbProduct.setTitle(product.getTitle());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setCategory(product.getCategory());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setStock(product.getStock());
        dbProduct.setImage(imageName);
       dbProduct.setIsActive(product.getIsActive());
        dbProduct.setDiscount(product.getDiscount());

      Double discount =product.getPrice()*(product.getDiscount()/100.0);
      Double discountPrice = product.getPrice()-discount;
      dbProduct.setDiscountPrice(discountPrice);




        Product updateProduct = productRepository.save(dbProduct);
        if (!ObjectUtils.isEmpty(updateProduct)){
            if (!image.isEmpty()){
                try {
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator + image.getOriginalFilename());
                    Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            return product;
        }
        return null;
    }

    @Override
    public List<Product> getAllActiveProducts(String category) {
        List<Product> products = null;
        if(ObjectUtils.isEmpty(category)){
           products = productRepository.findByIsActiveTrue();
        }else {
           products =  productRepository.findByCategory(category);
        }

        return products;
    }
}

