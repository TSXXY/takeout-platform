package com.tan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tan.entity.User;
import com.tan.service.UserService;
import com.tan.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody Map<String,Object> phones, HttpSession session){
        String phone = (String)phones.get("phone");
        if (StringUtils.hasLength(phone)) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if (ObjectUtils.isEmpty(user)) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
                session.setAttribute("user",user.getId());
                return R.success(user);

            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

}

