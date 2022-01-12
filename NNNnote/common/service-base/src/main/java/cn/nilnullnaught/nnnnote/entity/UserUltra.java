package cn.nilnullnaught.nnnnote.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户超级会员信息表
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Getter
@Setter
@TableName("user_ultra")
@ApiModel(value = "UserUltra对象", description = "用户超级会员信息表")
public class UserUltra implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("是否为超级会员，1（是），0（不是）")
    private Boolean isUltra;

    @ApiModelProperty("超级会员到期时间")
    private LocalDateTime gmtExpired;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModified;


}
