package com.liuz.magicCamera.annotation;

import java.lang.annotation.*;

/**
 * Created by liuzheng 2019-09-20
 * todo:
 * 类RepeatRequest的功能描述:
 * request请求防重复请求注解，相同参数 3秒内不能重复请求，比较鸡勒
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatRequest {
    String value() default "";
}
