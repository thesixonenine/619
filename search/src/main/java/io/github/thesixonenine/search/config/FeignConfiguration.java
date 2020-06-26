package io.github.thesixonenine.search.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import feign.Feign;
import io.github.thesixonenine.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Simple
 * @version 1.0
 * @date 2020/06/26 0:56
 * @since 1.0
 */
@Configuration
@ConditionalOnClass({Feign.class})
public class FeignConfiguration {
    private ObjectMapper localDateTimeMapper;

    public FeignConfiguration() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateUtils.TIMESTAMP_MILLISECOND_FORMATER));

        localDateTimeMapper = Jackson2ObjectMapperBuilder.json().modules(javaTimeModule)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
        localDateTimeMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        localDateTimeMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public WebMvcRegistrations feignMvcRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new FeignRequestMappingHandlerMapping();
            }
        };
    }

    private static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected boolean isHandler(Class<?> beanType) {
            return super.isHandler(beanType) && !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
        }
    }

    @Bean
    public ObjectMapper getLocalDateTimeMapper() {
        return localDateTimeMapper;
    }

    public class LocalDateSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(Optional.ofNullable(value)
                    .map(t -> t.format(DateUtils.TIMESTAMP_MILLISECOND_FORMATER))
                    .orElse(null));
        }
    }

    public class LocalDateDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p == null) {
                return null;
            }

            return Optional.ofNullable(p.getValueAsString())
                    .filter(t -> StringUtils.isNotEmpty(t))
                    .map(t -> LocalDateTime.parse(t, DateUtils.TIMESTAMP_MILLISECOND_FORMATER))
                    .orElse(null);
        }
    }
}
