package com.example.Ecommerce.Service.impl;

import com.example.Ecommerce.Model.Product;
import com.example.Ecommerce.Repository.ProductRepository;
import com.example.Ecommerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}

