package com.example.Ecommerce.Repository;

import com.example.Ecommerce.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Integer> {

    public Boolean existsByName(String name);
}
