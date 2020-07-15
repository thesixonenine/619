package io.github.thesixonenine.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/07/09 19:54
 * @since 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // registry.addViewController("/login").setViewName("login");
        registry.addViewController("/reg").setViewName("reg");
    }
}
