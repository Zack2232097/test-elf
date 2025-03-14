package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面,实现公共字段自动填充处理逻辑
 */
@Aspect//aop
@Component//也要交给IOC容器处理
@Slf4j//日志,方便调试
public class AutoFillAspect {
    //在mapper这个包的所有类(接口)和方法进行操作,同时被AutoFill注释的
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    //通知类型为before
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");
        // 获取到当前被拦截的方法上的数据库操作类型
        //MethodSignature 是 接口Signature的实现之一
        //都是反射操作,因为是通过获取方法的类的class<?>对象,然后进行操作的,这也是MethodSignature的底层原理
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获得方法的签名(坐标/Reference)
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//这些方法被注解了,获得注解


        // 获取到当前被拦截的方法的参数--实体对象
        // 获得实体的class</>对象的方式,.getClass()
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];//约定数据库操作将实体放到参数的第一个

        // 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
//        Long currentId = BaseContext.getCurrentId();
        // 通过反射为对象属性赋值
        try {
            //获得实体的class<?>对象的方法根据方法名
            //AutoFillConstant.SET_CREATE_TIME常量的形式不容易写错,如果改为"setCreateTime",错了没提醒,见update
            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            // 通过反射为对象属性赋值,写入数据库的实体entity是已经实例化的
            setCreateTime.invoke(entity, now);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Pointcut("execution(* com.sky.mapper.*.*(..))")
    public void databaseOperation() {}
    @Around("databaseOperation()")
    public Object logDatabaseExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("\033[36m[数据库操作开始] 方法: {}，参数: {}\033[0m", methodName, args);
        long start = System.currentTimeMillis();



        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("\033[32m[数据库操作成功] 方法: {}，耗时: {}ms\033[0m", methodName, duration);
            return result;

        } catch (DuplicateKeyException ex) {
            log.error("\033[31m[数据库唯一键冲突] 方法: {}，异常: {}\033[0m", methodName, ex.getMessage());
            throw ex; // 让全局异常处理器捕获

        } catch (DataIntegrityViolationException ex) {
            log.error("\033[31m[数据完整性违规] 方法: {}，异常: {}\033[0m", methodName, ex.getMessage());
            throw ex;

        } catch (BadSqlGrammarException ex) {
            log.error("\033[31m[SQL 语法错误] 方法: {}，异常: {}\033[0m", methodName, ex.getMessage());
            throw ex;

        } catch (DataAccessException ex) {
            log.error("\033[31m[数据库异常] 方法: {}，异常: {}\033[0m", methodName, ex.getMessage());
            throw ex;

        } catch (Exception ex) {
            log.error("\033[35m[其它异常] 方法: {}，异常: {}\033[0m", methodName, ex.getMessage());
            throw ex;
        }
    }
}
