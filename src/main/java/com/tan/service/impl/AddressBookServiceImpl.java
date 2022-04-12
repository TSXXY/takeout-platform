package com.tan.service.impl;

import com.tan.entity.AddressBook;
import com.tan.mapper.AddressBookMapper;
import com.tan.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author tan
 * @since 2022-04-12
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
