package cn.nilnullnaught.nnnnote.user.vo.UserNfolderVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="笔记文件夹", description="用于获取笔记文件夹信息")
public class NfolderVO {
    @ApiModelProperty("文件夹id")
    private String id;

    @ApiModelProperty("文件夹名")
    private String folderName;

    @ApiModelProperty("文件夹中笔记的数量")
    private Integer noteCount;

    @ApiModelProperty("文件夹简介")
    private String folderDescription;

    @ApiModelProperty("是否为默认文件夹")
    private Boolean isDefault;
}
