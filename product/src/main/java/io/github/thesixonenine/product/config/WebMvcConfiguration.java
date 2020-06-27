package io.github.thesixonenine.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/26 1:20
 * @since 1.0
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        //将所有/static/** 访问都映射到classpath:/static/ 目录下
        registry.addResourceHandler("/index/**").addResourceLocations("classpath:/static/index/");
    }
}
