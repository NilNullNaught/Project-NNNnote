package cn.nilnullnaught.nnnnote.entity.oss;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 阿里云 oss 资源管理表
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-28
 */
@Getter
@Setter
@TableName("aliyun_oss_resource")
@ApiModel(value = "AliyunOssResource对象", description = "阿里云 oss 资源管理表")
public class AliyunOssResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("笔记预览")
    private String  preview;

    @ApiModelProperty("资源类型（0：用户头像，1：笔记图片，2：笔记封面，3：待定，4：待定，5：待定）")
    private Integer type;

    @ApiModelProperty("该资源的使用者ID（使用者可能为用户，也可能为笔记，或者其他）")
    private String belongId;

    @ApiModelProperty("资源url地址")
    private String src;

    @ApiModelProperty("是否在使用中：1（true）在使用中， 0（false）不在使用中")
    private Boolean inUse;

    @ApiModelProperty("资源是否已经删除： 1（true）已删除， 0（false）未删除")
    private Boolean deleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
