package cn.nilnullnaught.nnnnote.transaction.controller;

import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.transaction.TransactionMemberOrder;
import cn.nilnullnaught.nnnnote.transaction.service.TransactionMemberLogService;
import cn.nilnullnaught.nnnnote.transaction.service.TransactionMemberOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("transaction/transaction-member")
public class TransactionMemberController {

    @Autowired
    private TransactionMemberOrderService transactionMemberOrderService;

    @Autowired
    private TransactionMemberLogService transactionMemberLogService;

    @ApiOperation(value = "用户 id 创建订单，返回订单id")
    @PostMapping("createOrder")
    public R save(@RequestHeader("token") String token) {
        String orderId = transactionMemberOrderService.saveMemberOrder(JwtUtils.getIdByJwtToken(token));
        return R.ok().data("data", orderId);
    }

    @ApiOperation(value = "根据id获取订单信息")
    @GetMapping("getOrder")
    public R get(@RequestParam("orderId") String orderId) {
        var wrapper = new QueryWrapper<TransactionMemberOrder>();
        wrapper.eq("order_no",orderId);
        var order = transactionMemberOrderService.getOne(wrapper);
        return R.ok().data("data", order);
    }

    @ApiOperation(value = "生成微信支付二维码")
    @GetMapping("/generateWechatQR")
    public R generateWechatQR(@RequestParam("orderNo")String orderNo) {
        var map = transactionMemberOrderService.generateWechatQR(orderNo);
        return R.ok().data(map);
    }

    @GetMapping("/getPayStatus")
    public R queryPayStatus(@RequestParam("orderNo") String orderNo,@RequestHeader("token") String token) {
        //调用查询接口
        var map = transactionMemberLogService.queryPayStatus(orderNo);
        if (map == null) {//出错
            return R.error().message("支付出错");
        }
        if (map.get("trade_state").equals("SUCCESS")) {//如果成功
            //更改订单状态

            var userId = JwtUtils.getIdByJwtToken(token);
            transactionMemberLogService.updateOrderStatus(map,userId);
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中");
    }

}
