package io.github.thesixonenine.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/11/15 18:56
 * @since 1.0
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // 如果不是http请求进来的线程, 这里的attributes就是null
                // 例如http请求进来后, 开启异步线程来执行远程调用
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (Objects.nonNull(attributes)) {
                    HttpServletRequest request = attributes.getRequest();
                    // 同步header中的Cookie, 因为这里有用户登录信息
                    String cookie = request.getHeader("Cookie");
                    requestTemplate.header("Cookie", cookie);
                }
            }
        };
    }
}
