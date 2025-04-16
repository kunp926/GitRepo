package com.pk.lease.web.admin.custom.config;

import com.pk.lease.model.enums.ItemType;
import com.pk.lease.web.admin.custom.converter.StringToBaseEnumConverterFactory;

import com.pk.lease.web.admin.custom.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private StringToBaseEnumConverterFactory converterFactory ;
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;
    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverterFactory(this.converterFactory);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.authenticationInterceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login/**");
    }

}
