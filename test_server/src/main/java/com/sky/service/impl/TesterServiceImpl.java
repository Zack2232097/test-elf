package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.dto.TesterDTO;
import com.sky.dto.TesterLoginDTO;
import com.sky.entity.Tester;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.TesterMapper;
import com.sky.service.TesterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class TesterServiceImpl implements TesterService {

    @Autowired
    private TesterMapper testerMapper;

    @Override
    public void addTester(TesterDTO testerDTO) {
        Tester tester = new Tester();
        BeanUtils.copyProperties(testerDTO, tester);
        tester.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        testerMapper.insert(tester);
    }

    @Override
    public Tester login(TesterLoginDTO testerLoginDTO) {
        String username = testerLoginDTO.getUsername();
        String password = testerLoginDTO.getPassword();

        //根据testername查询数据库中的数据
        Tester tester = testerMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (tester == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);//实现自定义的
        }

        // 密码比对
        // 需要进行md5不可逆的加密，然后再进行比对,数据库不能存明文
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(tester.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        return tester;
    }
}
