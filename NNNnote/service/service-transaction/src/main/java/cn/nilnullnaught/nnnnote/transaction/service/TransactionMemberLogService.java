package cn.nilnullnaught.nnnnote.transaction.service;

import cn.nilnullnaught.nnnnote.entity.transaction.TransactionMemberLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 超级会员支付日志表 服务类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-02
 */
public interface TransactionMemberLogService extends IService<TransactionMemberLog> {

    Map queryPayStatus(String orderNo);

    void updateOrderStatus(Map<String, String> map,String userId);

}
