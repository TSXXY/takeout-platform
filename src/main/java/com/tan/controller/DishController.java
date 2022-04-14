package com.tan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tan.entity.Category;
import com.tan.entity.Dish;
import com.tan.entity.DishFlavor;
import com.tan.entity.dto.DishDto;
import com.tan.service.CategoryService;
import com.tan.service.DishFlavorService;
import com.tan.service.DishService;
import com.tan.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R save(@RequestBody DishDto dishDto) {


        dishService.addDish(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 菜品分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R getDishByPage(long page, long pageSize, String name) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dtoPage = new Page<>();
        if (!ObjectUtils.isEmpty(name)) {
            lambdaQueryWrapper.like(Dish::getName, name);
        }
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(dishPage, lambdaQueryWrapper);
        BeanUtils.copyProperties(dishPage,dtoPage,"records");
        List<Dish> records = dishPage.getRecords();

        List<DishDto> list =records.stream().map(item->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long id = item.getCategoryId();
            Category byId = categoryService.getById(id);
            String byIdName = byId.getName();
            dishDto.setCategoryName(byIdName);
            return dishDto;
        }).collect(Collectors.toList());


        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * 根据id获取菜品修改回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R getInfo(@PathVariable Long id){
      DishDto dishDto =  dishService.getINfo(id);
      return R.success(dishDto);
    }


    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R update(@RequestBody DishDto dishDto) {


        dishService.updateDish(dishDto);

        return R.success("修改菜品成功");
    }

//    /**
//     * 根据分类id查询菜品
//     * @param dish
//     * @return
//     */
//    @GetMapping("/list")
//    public R list(Dish dish){
//        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        dishLambdaQueryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
//        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
//        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
//        return R.success(list);
//    }


    /**
     * 根据分类id查询菜品
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R list(Dish dish){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
        List<DishDto> collect = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long id = item.getCategoryId();
            Category byId = categoryService.getById(id);
            String byIdName = byId.getName();
            dishDto.setCategoryName(byIdName);

            Long id1 = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id1);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(collect);
    }




}

