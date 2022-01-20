package cn.nilnullnaught.nnnnote.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("user_nfolder")
@ApiModel(value = "UserNfolder对象", description = "用户文件夹表")
public class UserNfolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件夹id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("文件夹名")
    private String folderName;

    @ApiModelProperty("文件夹中笔记的数量")
    private Integer noteCount;

    @ApiModelProperty("文件夹简介")
    private String folderDescription;

    @ApiModelProperty("是否为默认文件夹")
    private Boolean isDefault;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModified;


}