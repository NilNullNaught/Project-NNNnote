package cn.nilnullnaught.nnnnote.note.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="笔记信息", description="用于保存笔记文件夹信息")
public class SaveNoteVo {
    @ApiModelProperty("笔记 id")
    private String  id;

    @ApiModelProperty("笔记标题")
    private String  title;

    @ApiModelProperty("文件夹ID")
    private String noteFolderId;

    @ApiModelProperty("0 草稿状态，1 完成状态，2 发布状态")
    private byte status;

    @ApiModelProperty("笔记正文")
    private String text;

    @ApiModelProperty("图片列表")
    private List<String> resourceUrlList;
}