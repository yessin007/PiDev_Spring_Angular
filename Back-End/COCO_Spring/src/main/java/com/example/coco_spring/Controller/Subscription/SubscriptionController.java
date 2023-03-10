package com.example.coco_spring.Controller.Subscription;


import com.example.coco_spring.Entity.Product;
import com.example.coco_spring.Entity.ProductCategory;
import com.example.coco_spring.Service.Subsciption.SubsciptionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sub")
public class SubscriptionController {
    SubsciptionService subsciptionService;
    @GetMapping("/viewtop10prodsbycategories")
    public Map<ProductCategory, List<Product>> top10RatedProductByCategory(){
        return subsciptionService.top10RatedProductByCategory();
    }
    @GetMapping("/mywishlist/{userId}")
    public Map<ProductCategory,List<Product>> getUserWishlist(@PathVariable("userId") Long userId){
        return  subsciptionService.getUserWishlist(userId);
    }
}
