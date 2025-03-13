package com.sky.controller.tester;


import com.sky.constant.JwtClaimsConstant;

import com.sky.dto.TesterDTO;
import com.sky.dto.TesterLoginDTO;
import com.sky.entity.Tester;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.TesterService;
import com.sky.utils.JwtUtil;
import com.sky.vo.TesterLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

//用于用户登录相关的操作
@RestController
@RequestMapping("/tester")
@Slf4j
public class TesterController {
    @Autowired
    private TesterService testerService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/register")
    @ApiOperation("新增人员")
    public Result<String> save(@RequestBody TesterDTO testerDTO) {
        log.info("新增用户: {}", testerDTO);
        testerService.addTester(testerDTO);
        return Result.success();
    }

    /**
     * 登录
     * @return
     */
    @PostMapping("/login")//登入使用post
    @ApiOperation("登录")
    //将上传的json封装位java对象要@RequestBody注解一下
    public Result<TesterLoginVO> login(@RequestBody TesterLoginDTO testerLoginDTO) {
        log.info("员工登录：{}", testerLoginDTO);

        Tester tester = testerService.login(testerLoginDTO);

        //登录成功后，生成jwt令牌!!!
        Map<String, Object> claims = new HashMap<>();
        //便于修改所以用JwtClaimsConstant.EMP_ID
        claims.put(JwtClaimsConstant.EMP_ID, tester.getId());
        String token = JwtUtil.createJWT(//jwtProperties上面已经进行的自动注入
                jwtProperties.getAdminSecretKey(),//从配置中获得这些
                jwtProperties.getAdminTtl(),
                claims);

        //通过builder进行代替getter,VO视图对象返回给前端的
        //类似于web项目中的增肌员工,dept.setCreateTime(LocalDatetime.now())
        TesterLoginVO testerLoginVO = TesterLoginVO.builder()
                .id(tester.getId())
                .userName(tester.getUsername())
                .token(token)
                .build();

        return Result.success(testerLoginVO);
    }
}
