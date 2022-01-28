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
 * 笔记正文表
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-27
 */
@Getter
@Setter
@TableName("note_text")
@ApiModel(value = "NoteText对象", description = "笔记正文表")
public class NoteText implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("笔记ID")
    private String id;

    @ApiModelProperty("笔记正文内容")
    private String text;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModified;


}
