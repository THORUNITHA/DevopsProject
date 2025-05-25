package com.coupon.springcloud.Controller;

import com.coupon.springcloud.dto.Coupon;
import com.coupon.springcloud.model.Product;
import com.coupon.springcloud.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/productapi")
public class ProductController {
    @Autowired
    ProductRepo productRepo;

    RestTemplate restTemplate = new RestTemplate();
    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {

        Coupon coupon = restTemplate.getForObject("http://localhost:8082/couponapi/coupons/" + product.getCouponCode(), Coupon.class);
       if (coupon != null) {
           product.setPrice(product.getPrice().subtract(coupon.getDiscount()));
       }
        return productRepo.save(product);
    }
}
