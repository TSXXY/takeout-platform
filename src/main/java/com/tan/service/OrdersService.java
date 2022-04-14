package com.tan.service;

import com.tan.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tan.utils.R;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
public interface OrdersService extends IService<Orders> {

    R submit(Orders orders);
}
