package com.tan.service.impl;

import com.tan.entity.Dish;
import com.tan.mapper.DishMapper;
import com.tan.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

}
