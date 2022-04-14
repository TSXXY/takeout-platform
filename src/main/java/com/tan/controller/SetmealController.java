package com.tan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tan.entity.Category;
import com.tan.entity.Setmeal;
import com.tan.entity.dto.SetmealDto;
import com.tan.service.CategoryService;
import com.tan.service.SetmealService;
import com.tan.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;



    /**
     * 添加套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto){
        setmealService.saveSetmeal(setmealDto);
        return R.success("添加成功");
    }


    /**
     * 分页查询套餐列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R getByPage(Long page,Long pageSize,String name){
        Page<Setmeal> setmealDtoPage = new Page<>();
        Page<SetmealDto> setmealDtoPage1 = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name != null,Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealDtoPage,setmealLambdaQueryWrapper);

        BeanUtils.copyProperties(setmealDtoPage,setmealDtoPage1,"records");

        List<Setmeal> records = setmealDtoPage.getRecords();

        List<SetmealDto> list = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if (!ObjectUtils.isEmpty(byId)) {
                String byIdName = byId.getName();
                setmealDto.setCategoryName(byIdName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage1.setRecords(list);
        return R.success(setmealDtoPage1);

    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R delete(@RequestParam List ids){
     R r =  setmealService.deletes(ids);
        return r;
    }

    @GetMapping("/list")
    public R list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        objectLambdaQueryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        objectLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(objectLambdaQueryWrapper);
        return R.success(list);
    }

}

