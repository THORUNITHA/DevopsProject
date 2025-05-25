package com.coupon.springcloud.controllers;

import com.coupon.springcloud.model.Coupon;
import com.coupon.springcloud.repos.CouponRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couponapi")
public class CouponController {

    @Autowired
    private CouponRepo couponRepo;


    @PostMapping("/coupons")
    public Coupon saveCoupon(@RequestBody Coupon coupon) {
        return couponRepo.save(coupon);
    }

    @GetMapping("/coupons/{code}")
    public Coupon findByCode(@PathVariable String code) {
        return couponRepo.findByCode(code);
    }

}
