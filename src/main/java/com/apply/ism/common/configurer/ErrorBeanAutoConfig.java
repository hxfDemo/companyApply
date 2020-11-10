package com.apply.ism.common.configurer;

import com.apply.ism.common.SelfExceptionHandlerResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 注释
 *
 * @author Leon
 * @date 2020/3/18 9:58
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@RequiredArgsConstructor
public class ErrorBeanAutoConfig {
    @Bean
    public SelfExceptionHandlerResolver initExceptionHandler(DefaultErrorAttributes errorAttributes) {
        SelfExceptionHandlerResolver resolver = new SelfExceptionHandlerResolver(errorAttributes);
        return resolver;
    }
}
