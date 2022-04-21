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
 * 笔记评论表
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Getter
@Setter
@TableName("note_comment")
@ApiModel(value = "NoteComment对象", description = "笔记评论表")
public class NoteComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论ID")
    private String id;

    @ApiModelProperty("笔记id")
    private String noteId;

    @ApiModelProperty("用户ID（评论发布者ID）")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("点赞数")
    private Long likes;

    @ApiModelProperty("评论回复数")
    private Long replyCount;

    @ApiModelProperty("所属评论 ID")
    private String replyCommentId;

    @ApiModelProperty("被回复人昵称")
    private String replyNickname;

    @ApiModelProperty("用户ID（评论发布者ID）")
    private String replyUserId;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
