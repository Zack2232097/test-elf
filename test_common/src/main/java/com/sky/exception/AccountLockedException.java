package com.sky.exception;

/**
 * 账号被锁定异常
 */
public class AccountLockedException extends BaseException {

    public AccountLockedException() {
    }

    public AccountLockedException(String msg) {
        //调用父类 BaseException 的构造方法，并将 msg 传递给它
        //java会把msg打印到控制台
        super(msg);

    }
}
