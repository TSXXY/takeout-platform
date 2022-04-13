package com.tan.service;

import com.tan.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tan.utils.R;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
public interface CategoryService extends IService<Category> {

    R delete(Long id);
}
