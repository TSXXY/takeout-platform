package com.tan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tan.entity.Dish;
import com.tan.entity.DishFlavor;
import com.tan.entity.dto.DishDto;
import com.tan.mapper.DishMapper;
import com.tan.service.DishFlavorService;
import com.tan.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void addDish(DishDto dishDto) {
        baseMapper.insert(dishDto);

        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors =  flavors.stream().peek(item-> item.setDishId(id)).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getINfo(Long id) {
        Dish dish = baseMapper.selectById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    public void updateDish(DishDto dishDto) {
        baseMapper.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors =  flavors.stream().peek(item-> item.setDishId(dishDto.getId())).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
