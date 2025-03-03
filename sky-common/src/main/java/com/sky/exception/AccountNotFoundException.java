package com.sky.exception;

/**
 * 账号不存在异常
 */
public class AccountNotFoundException extends BaseException {

    //两种构造方法
    public AccountNotFoundException() {
    }

    public AccountNotFoundException(String msg) {
        super(msg);//调用父类
    }

}
