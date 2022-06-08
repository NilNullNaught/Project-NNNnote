package cn.nilnullnaught.nnnnote.entity.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户动态表
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-05
 */
@Getter
@Setter
@TableName("user_dynamic")
@ApiModel(value = "UserDynamic对象", description = "用户动态表")
public class UserDynamic implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户 id")
    private String userId;

    @ApiModelProperty("动态类型（暂时包括：1-笔记公开，2-评论，3-关注）")
    private Integer dynamicType;

    @ApiModelProperty("动态对应的数据ID")
    private String dynamicId;

    @ApiModelProperty("动态描述")
    private String description;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
