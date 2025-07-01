package com.coupon.springcloud.Controller;

import com.coupon.springcloud.dto.Coupon;
import com.coupon.springcloud.model.Product;
import com.coupon.springcloud.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/productapi")
public class ProductController {

    @Autowired
    private ProductRepo productRepo;

    // You could also declare this as a @Bean and @Autowired it
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        System.out.println("Received price: " + product.getPrice());
        System.out.println("Received couponCode: " + product.getCouponCode());

        String code = product.getCouponCode();
        if (code != null && !code.isBlank()) {
            code = code.trim(); // remove whitespace
            try {
                // encode in case of special characters
                String url = "http://host.docker.internal:10555/couponapi/coupons/" +
                        URLEncoder.encode(code, StandardCharsets.UTF_8);
                System.out.println("Calling coupon service: " + url);

                Coupon coupon = restTemplate.getForObject(url, Coupon.class);
                if (coupon != null && coupon.getDiscount() != null) {
                    System.out.println("Applying discount: " + coupon.getDiscount());
                    product.setPrice(product.getPrice().subtract(coupon.getDiscount()));
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode().is4xxClientError()) {
                    System.out.println("Coupon not applied (client error): " + e.getStatusCode());
                } else {
                    throw e;
                }
            }
        }

        return productRepo.save(product);
    }

}
