package com.tan.exceptionhandler;


import com.tan.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


	/**
	 * 特定异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	@ResponseBody
	public R error(SQLIntegrityConstraintViolationException e){
		log.info("出现用户重复现象{}",e.getMessage());
		String[] s = e.getMessage().split(" ");
		String s1 = s[2];
		String replace = s1.replace("'", "");

		return R.error(replace+"已存在");
	}

}