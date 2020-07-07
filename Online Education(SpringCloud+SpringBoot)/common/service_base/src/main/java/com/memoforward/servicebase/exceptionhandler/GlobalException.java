package com.memoforward.servicebase.exceptionhandler;

import com.memoforward.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalException {

    // 全局异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        log.error(e.getMessage());
        return R.error().message("执行全局异常处理..");
    }

    // 特定异常
    // 这里一般不会单独处理系统的其他异常

    // 自定义异常
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public R error(MyException e){
        e.printStackTrace();
        log.error(e.getMsg());
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
