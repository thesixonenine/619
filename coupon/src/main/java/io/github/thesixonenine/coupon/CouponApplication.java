package io.github.thesixonenine.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableFeignClients(basePackages = {"io.github.thesixonenine"})
@RefreshScope
@RestController
@MapperScan(value = "io.github.thesixonenine.coupon.dao")
@SpringBootApplication
public class CouponApplication {

    @Value("${coupon.expire.time}")
    private Integer expireTime;

    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class, args);
    }

    @GetMapping(value = "/")
    public void test() {
        System.out.println("过期时间: " + expireTime);
    }
}
