package cn.nilnullnaught.nnnnote.entity.note;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 笔记—收藏夹关系表
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Getter
@Setter
@TableName("note_user_collection")
@ApiModel(value = "NoteUserCollection对象", description = "笔记—收藏夹关系表")
public class NoteUserCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("笔记ID")
    private String noteId;

    @ApiModelProperty("收藏夹ID")
    private String collectionFolderId;

    @ApiModelProperty("逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModified;


}
