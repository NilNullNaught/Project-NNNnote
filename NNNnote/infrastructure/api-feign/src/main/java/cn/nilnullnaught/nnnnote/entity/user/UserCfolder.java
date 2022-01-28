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
 * 用户文件夹表
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Getter
@Setter
@TableName("user_cfolder")
@ApiModel(value = "UserCfolder对象", description = "用户文件夹表")
public class UserCfolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件夹id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("收藏夹名")
    private String folderName;

    @ApiModelProperty("收藏夹中笔记的数量")
    private Integer noteCount;

    @ApiModelProperty("收藏夹简介")
    private String folderDescription;

    @ApiModelProperty("是否为默认收藏夹")
    private Boolean isDefault;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
