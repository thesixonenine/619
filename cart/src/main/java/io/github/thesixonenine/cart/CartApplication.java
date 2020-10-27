package io.github.thesixonenine.cart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@EnableRedisHttpSession
@EnableSwagger2
@EnableFeignClients(basePackages = {"io.github.thesixonenine"})
@RefreshScope
@Controller
@MapperScan(value = "io.github.thesixonenine.cart.dao")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }

    @RequestMapping(value = "")
    public String index(){
        return "success";
    }
}
