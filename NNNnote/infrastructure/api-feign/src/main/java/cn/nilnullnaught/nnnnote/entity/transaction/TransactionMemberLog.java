package cn.nilnullnaught.nnnnote.entity.transaction;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 超级会员支付日志表
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-02
 */
@Getter
@Setter
@TableName("transaction_member_log")
@ApiModel(value = "TransactionMemberLog对象", description = "会员支付日志表")
public class TransactionMemberLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("支付完成时间")
    private LocalDateTime payTime;

    @ApiModelProperty("支付金额（分）")
    private BigDecimal totalFee;

    @ApiModelProperty("交易流水号")
    private String transactionId;

    @ApiModelProperty("交易状态")
    private String tradeState;

    @ApiModelProperty("支付类型（1：微信 2：支付宝）")
    private Integer payType;

    @ApiModelProperty("其他属性")
    private String attr;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
