package cn.nilnullnaught.nnnnote.exceptionhandler;

import cn.nilnullnaught.nnnnote.common.utils.R;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
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

        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMessage());
    }

    /**
     */
    public static String poolState(HikariDataSource dataSource, boolean ifLog, String... prefix) {
        String poolName = dataSource.getPoolName();
        HikariPoolMXBean mx = dataSource.getHikariPoolMXBean();
        String format = String.format("%s - %sstats (total=%d, active=%d, idle=%d, waiting=%d)",
                poolName, (prefix.length > 0 ? prefix[0] : ""),
                mx.getTotalConnections(), mx.getActiveConnections(), mx.getIdleConnections(), mx.getThreadsAwaitingConnection());
        if (ifLog) {
            log.error("{}", format);
        }
        return format;
    }
}
