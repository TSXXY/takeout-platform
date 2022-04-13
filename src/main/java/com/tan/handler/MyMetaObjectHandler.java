package com.tan.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tan.utils.BaseContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("createUser", BaseContext.get(),metaObject);
        this.setFieldValByName("updateUser",BaseContext.get(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateUser",BaseContext.get(),metaObject);
    }
}