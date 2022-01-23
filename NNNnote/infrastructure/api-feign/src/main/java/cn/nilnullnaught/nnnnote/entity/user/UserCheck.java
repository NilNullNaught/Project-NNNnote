package cn.nilnullnaught.nnnnote.entity.user;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户登录验证表
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Getter
@Setter
@TableName("user_check")
@ApiModel(value = "UserCheck对象", description = "用户登录验证表")
public class UserCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("微信 openid")
    private String openidWx;

    @ApiModelProperty("QQ openid")
    private String openidQq;

    @ApiModelProperty("微博 openid")
    private String openidWb;


    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("是否禁用 1（true）已禁用，  0（false）未禁用")
    private Boolean isDisabled;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModified;


}
