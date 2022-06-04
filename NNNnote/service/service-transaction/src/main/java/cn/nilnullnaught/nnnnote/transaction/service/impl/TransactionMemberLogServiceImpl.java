package cn.nilnullnaught.nnnnote.transaction.service.impl;


import cn.nilnullnaught.nnnnote.client.user.UserMemberClient;
import cn.nilnullnaught.nnnnote.entity.transaction.TransactionMemberLog;
import cn.nilnullnaught.nnnnote.entity.transaction.TransactionMemberOrder;
import cn.nilnullnaught.nnnnote.transaction.mapper.TransactionMemberLogMapper;
import cn.nilnullnaught.nnnnote.transaction.mapper.TransactionMemberOrderMapper;
import cn.nilnullnaught.nnnnote.transaction.service.TransactionMemberLogService;
import cn.nilnullnaught.nnnnote.transaction.util.HttpClient;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 超级会员支付日志表 服务实现类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-02
 */
@Service
public class TransactionMemberLogServiceImpl extends ServiceImpl<TransactionMemberLogMapper, TransactionMemberLog> implements TransactionMemberLogService {

    @Autowired
    private TransactionMemberOrderMapper transactionMemberOrderMapper;

    @Autowired
    private UserMemberClient userMemberClient;

    @Override
    public Map queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public void updateOrderStatus(Map<String, String> map,String userId) {
        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        var qw = new QueryWrapper<TransactionMemberOrder>();
        qw.eq("order_no",orderNo);
        var order = transactionMemberOrderMapper.selectOne(qw);

        if(order.getStatus().intValue() == 1) return;
        order.setStatus(1);
        transactionMemberOrderMapper.updateById(order);

        //记录支付日志
        var log = new TransactionMemberLog();
        log.setOrderNo(order.getOrderNo());//支付订单号
        log.setPayTime(LocalDateTime.now());
        log.setPayType(1);//支付类型
        log.setTotalFee(order.getTotalFee());//总金额(分)
        log.setTradeState(map.get("trade_state"));//支付状态
        log.setTransactionId(map.get("transaction_id"));
        log.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(log);//插入到支付日志表

        userMemberClient.alterUserMember(userId,true);
    }


}
