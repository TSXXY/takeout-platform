package com.tan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tan.entity.Employee;
import com.tan.service.EmployeeService;
import com.tan.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("login")
    public R login( HttpServletRequest request, @RequestBody Employee employee){

        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee serviceOne = employeeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(serviceOne)){
            return R.error("用户不存在");

        }
        if (!serviceOne.getPassword().equals(password)){
            return R.error("密码错误");
        }
        if (serviceOne.getStatus()==0){
            return R.error("账户已封禁");
        }
        request.getSession().setAttribute("employee",serviceOne.getId());
        serviceOne.setPassword(null);
        return R.success(serviceOne);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("logout")
    public R logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");

        return R.success("退出登录成功");
    }
}

