package com.example.coco_spring.Service.Product;



import com.example.coco_spring.Entity.*;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IProductServices {
    List<Product> retrieveAllProducts();

    Product addAndUpdateProduct(MultipartFile image,Product product) throws IOException;


    Product retrieveProduct (Long productId);

    void deleteProduct(Long productId);


}
