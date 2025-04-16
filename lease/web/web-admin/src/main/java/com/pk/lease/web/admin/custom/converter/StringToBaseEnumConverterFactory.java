package com.pk.lease.web.admin.custom.converter;

import com.pk.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new Converter<String, T>() {
            @Override
            public T convert(String source) {
                T[] targettypes=targetType.getEnumConstants();
                for(T targettype:targettypes){
                    if(targettype.getCode().equals(Integer.valueOf(source))){
                        return targettype;
                    }
                }
                throw new IllegalArgumentException("code:"+source+"非法");
            }
        };
    }
}
