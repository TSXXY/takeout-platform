package com.tan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tan.entity.Employee;
import com.tan.service.EmployeeService;
import com.tan.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("login")
    public R login(HttpServletRequest request, @RequestBody Employee employee) {

        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee serviceOne = employeeService.getOne(lambdaQueryWrapper);
        if (ObjectUtils.isEmpty(serviceOne)) {
            return R.error("用户不存在");

        }
        if (!serviceOne.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        if (serviceOne.getStatus() == 0) {
            return R.error("账户已封禁");
        }
        request.getSession().setAttribute("employee", serviceOne.getId());
        serviceOne.setPassword(null);
        return R.success(serviceOne);
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("logout")
    public R logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");

        return R.success("退出登录成功");
    }

    /**
     * 添加员工
     * @param employee
     * @param request
     * @return
     */
    @PostMapping
    public R addEmp(@RequestBody Employee employee, HttpServletRequest request) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        Long id = (Long) request.getSession().getAttribute("employee");
        if (ObjectUtils.isEmpty(id)) {
            return R.error("用户未登陆");
        }
        boolean save = employeeService.save(employee);
        if (save) {
            return R.success("添加成功");

        } else {
            return R.error("添加失败");
        }
    }

    /**
     * 获取员工分页
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R empListByPage(long page, long pageSize, String name) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        Page<Employee> employeePage = new Page<>(page, pageSize);
        if (!ObjectUtils.isEmpty(name)) {
            String trim = name.trim();
            if (!ObjectUtils.isEmpty(trim)) {
                queryWrapper.like(Employee::getName, trim);
            }
        }
        employeeService.page(employeePage, queryWrapper);
        return R.success(employeePage);
    }

    /**
     * 根据员工id修改状态
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R update(@RequestBody Employee employee, HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateUser(id);
        Employee byId = employeeService.getById(employee);
        if (byId.getUsername().equals("admin")) {
            return R.error("不能对管理员账号进行修改");
        }
        employeeService.updateById(employee);
        return R.success("修改用户成功");
    }


    /**
     * 根据id查询用户并回显
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R getById(@PathVariable Long id) {
        Employee byId = employeeService.getById(id);
        return R.success(byId);
    }
}

