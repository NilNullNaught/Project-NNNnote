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
 * 用户信息表
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Getter
@Setter
@TableName("user_message")
@ApiModel(value = "UserMessage对象", description = "用户信息表")
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别 0 男，1 女 ")
    private Integer sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户签名")
    private String sign;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
