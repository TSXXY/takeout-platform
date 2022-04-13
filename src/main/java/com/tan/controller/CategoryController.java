package com.tan.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tan.entity.Category;
import com.tan.service.CategoryService;
import com.tan.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 添加菜品和套餐
     * @param category
     * @return
     */
    @PostMapping
    public R addCategory(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加菜品分类成功");
    }

    /**
     * 分页菜品查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R getCategoryByPage(Long page,Long pageSize){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        Page<Category> categoryPage = new Page<>(page,pageSize);
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(categoryPage,queryWrapper);
        return R.success(categoryPage);

    }

    /**
     * 根据id删除菜品
     * @param id
     * @return
     */
    @DeleteMapping
    public R delete(Long id){

//        categoryService.removeById(id);
       R r = categoryService.delete(id);
        return r;
    }


    /**
     * 修改菜品
     * @param category
     * @return
     */
    @PutMapping
    public R update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R getList(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isEmpty(category.getType())){
            lambdaQueryWrapper.eq(Category::getType,category.getType());
        }
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);

    }

}

