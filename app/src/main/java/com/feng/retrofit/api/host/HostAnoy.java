package com.feng.retrofit.api.host;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Chexiangjia-MAC on 17/6/26.
 */

public interface HostAnoy {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface NameSpace {
        String value() default "";
    }
}
