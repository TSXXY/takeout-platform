package com.tan.service;

import com.tan.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tan.entity.dto.DishDto;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
public interface DishService extends IService<Dish> {

    void addDish(DishDto dishDto);

    DishDto getINfo(Long id);

    void updateDish(DishDto dishDto);
}
