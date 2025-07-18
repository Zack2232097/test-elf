package com.sky.annotation;


import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解,用于标识某个方法需要进行公共字段自动填充
 */
@Target(ElementType.METHOD)//作用域方法
@Retention(RetentionPolicy.RUNTIME)//运行时生效
public @interface AutoFill {
    //申明：数据库操作类型:UPDATE INSERT
    OperationType value();
}
