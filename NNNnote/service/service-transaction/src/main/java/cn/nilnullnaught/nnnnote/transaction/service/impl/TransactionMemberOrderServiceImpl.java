package cn.nilnullnaught.nnnnote.transaction.service.impl;


import cn.nilnullnaught.nnnnote.entity.transaction.TransactionMemberOrder;
import cn.nilnullnaught.nnnnote.transaction.mapper.TransactionMemberOrderMapper;
import cn.nilnullnaught.nnnnote.transaction.service.TransactionMemberOrderService;
import cn.nilnullnaught.nnnnote.transaction.util.HttpClient;
import cn.nilnullnaught.nnnnote.transaction.util.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 超级会员订单表 服务实现类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-02
 */
@Service
public class TransactionMemberOrderServiceImpl extends ServiceImpl<TransactionMemberOrderMapper, TransactionMemberOrder> implements TransactionMemberOrderService {

    // TODO 当前充值为终身制，需要修改为按月计费
    /**
     * 创建订单
     * @param userId
     * @return
     */
    @Override
    public String saveMemberOrder(String userId) {

        //创建订单
        var order = new TransactionMemberOrder();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setUserId(userId);
        order.setDescription("开通会员");
        order.setTotalFee(new BigDecimal(0.01));
        order.setPayType(1);
        order.setStatus(0);

        baseMapper.insert(order);

        return order.getOrderNo();

    }

    @Override
    public Map generateWechatQR(String orderNo) {

        try {
            //根据订单id获取订单信息
            var qw = new QueryWrapper<TransactionMemberOrder>();
            qw.eq("order_no",orderNo);
            var order = baseMapper.selectOne(qw);

            var m = new HashMap();
            //1、设置支付参数
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getDescription());
            m.put("out_trade_no", orderNo);
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");

            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //4、封装返回结果集

            Map map = new HashMap<>();
            map.put("orderNo", orderNo);
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
