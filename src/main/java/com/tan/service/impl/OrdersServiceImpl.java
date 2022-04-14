package com.tan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.tan.entity.*;
import com.tan.mapper.OrdersMapper;
import com.tan.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tan.utils.BaseContext;
import com.tan.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Transactional
    @Override
    public R submit(Orders orders) {
        Long aLong = BaseContext.get();

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,aLong);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);

        if (list.size()==0){
            return R.error("没有选择菜品");
        }

        User byId = userService.getById(aLong);

        Long addressBookId = orders.getAddressBookId();
        AddressBook byId1 = addressBookService.getById(addressBookId);
        if (ObjectUtils.isEmpty(byId1)){
            return R.error("没有配送地址");
        }

        //mp提供的订单号生成方法
        String id = IdWorker.getIdStr();

        AtomicInteger amount = new AtomicInteger(0);


        List<OrderDetail> collect = list.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(Long.valueOf(id));
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        //组装订单数据
        orders.setId(Long.valueOf(id));
        orders.setOrderTime( new Date());
        orders.setCheckoutTime(new Date());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(aLong);
        orders.setNumber(id);
        orders.setUserName(byId.getName());
        orders.setConsignee(byId1.getConsignee());
        orders.setPhone(byId1.getPhone());
        orders.setAddress((byId1.getProvinceName() == null ? "" : byId1.getProvinceName())
                + (byId1.getCityName() == null ? "" : byId1.getCityName())
                + (byId1.getDistrictName() == null ? "" : byId1.getDistrictName())
                + (byId1.getDetail() == null ? "" : byId1.getDetail()));

        baseMapper.insert(orders);

        orderDetailService.saveBatch(collect);

        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        return R.success("订单构建完成");
    }
}
