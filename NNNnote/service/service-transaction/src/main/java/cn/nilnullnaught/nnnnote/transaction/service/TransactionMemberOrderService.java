package cn.nilnullnaught.nnnnote.transaction.service;

import cn.nilnullnaught.nnnnote.entity.transaction.TransactionMemberOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 超级会员订单表 服务类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-02
 */
public interface TransactionMemberOrderService extends IService<TransactionMemberOrder> {

    String saveMemberOrder(String userId);

    Map generateWechatQR(String orderNo);
}
