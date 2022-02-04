package cn.nilnullnaught.nnnnote.entity.oss.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="文件管理对象", description="用于管理文件的使用状态")
public class ResourceManagerVo {

    @ApiModelProperty("资源类型（0：用户头像，1：笔记图片，2：网站资源，3：其他）")
    private Integer type;

    @ApiModelProperty("资源的使用者ID（使用者可能为用户，也可能为笔记，或者其他）")
    private String belongId;

    @ApiModelProperty("资源 Url")
    private List<String> resourceUrlList;
}
