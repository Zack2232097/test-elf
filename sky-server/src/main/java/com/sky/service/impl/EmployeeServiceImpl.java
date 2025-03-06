package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;//entity也是pojo
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    //为了获得JWT,不用了使用threadlocal
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);//实现自定义的
        }

        // 密码比对
        // 需要进行md5不可逆的加密，然后再进行比对,数据库不能存明文
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    /**
     * 新增员工
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        //对象属性拷贝,把用户上传的信息copy到employee
        BeanUtils.copyProperties(employeeDTO, employee);
        //设置账号状态
        employee.setStatus(StatusConstant.ENABLE);
        //密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //创建和修改时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        //设置当前记录的创建人id和修改人id,实际可以通过解析JWT(base64反编码)

//        String token = request.getHeader(jwtProperties.getAdminTokenName());//JWT
//        Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
//        Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
//        log.info("当前员工id：", empId);

        //threadLocal为每个线程提供单独存储空间,一个请求一个线程,在拦截器中保存

//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        //持久层操作
        employeeMapper.insert(employee);
    }

    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //利用了PageHelper插件，底层就是查询语句中的limit，以及利用threadlocal保存page和size
        //在query进行get这些值
        // 分页查询,第几页，每页几条数据（大小）
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //Employee为entity，从数据库查出来的
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        //update employee set status = ? where id = ?
        //通用性：传入a.b，修改a.c,所以传入对象，而不是这两个参数

//        Employee employee = new Employee();
//        employee.setStatus(status);
//        employee.setId(id);

        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        employeeMapper.update(employee);

    }


}
