package com.tan.service;

import com.tan.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tan.entity.dto.SetmealDto;
import com.tan.utils.R;

import java.util.List;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
public interface SetmealService extends IService<Setmeal> {

    void saveSetmeal(SetmealDto setmealDto);

    R deletes(List ids);
}
