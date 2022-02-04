package cn.nilnullnaught.nnnnote.entity.note;

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
 * 笔记信息表
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Getter
@Setter
@TableName("note_info")
@ApiModel(value = "NoteInfo对象", description = "笔记信息表")
public class NoteInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("笔记ID")
    private String id;

    @ApiModelProperty("用户ID（发布者ID）")
    private String userId;

    @ApiModelProperty("文件夹ID")
    private String noteFolderId;

    @ApiModelProperty("笔记标题")
    private String title;

    @ApiModelProperty("点赞数")
    private Long likes;

    @ApiModelProperty("收藏数")
    private Long collectionCount;

    @ApiModelProperty("评论数")
    private Long commentCount;

    @ApiModelProperty("0 草稿状态，1 完成状态，2 发布状态")
    private byte status;

    @ApiModelProperty("乐观锁")
    private Long version;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
