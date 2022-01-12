package cn.nilnullnaught.nnnnote.exceptionhandler;

import cn.nilnullnaught.nnnnote.common.utils.ExceptionUtil;
import cn.nilnullnaught.nnnnote.common.utils.R;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        log.error(ExceptionUtil.getMessage(e));
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error();
    }
}
