package com.tan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tan.entity.DishFlavor;
import com.tan.entity.Setmeal;
import com.tan.entity.SetmealDish;
import com.tan.entity.dto.SetmealDto;
import com.tan.mapper.SetmealMapper;
import com.tan.service.SetmealDishService;
import com.tan.service.SetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tan.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 服务实现类
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveSetmeal(SetmealDto setmealDto) {

        baseMapper.insert(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().peek(item-> item.setSetmealId(setmealDto.getId().toString())).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public R deletes(List ids) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        Integer integer = baseMapper.selectCount(setmealLambdaQueryWrapper);
        if (integer>0){
            return R.error("套餐正在售卖，请先停售再删除");
        }
        baseMapper.deleteBatchIds(ids);


        LambdaQueryWrapper<SetmealDish> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(objectLambdaQueryWrapper);

        return R.success("删除成功");
    }
}
