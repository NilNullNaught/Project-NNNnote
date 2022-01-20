package cn.nilnullnaught.nnnnote.exceptionhandler;

import cn.nilnullnaught.nnnnote.common.utils.R;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@Slf4j
@ControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理..");
    }

    //自定义异常
    @ExceptionHandler(MyCustomException.class)
    @ResponseBody //为了返回数据
    public R error(MyCustomException e) {
        log.error(e.getMessage());
        return R.error().code(e.getCode()).message(e.getMessage());
    }

}
