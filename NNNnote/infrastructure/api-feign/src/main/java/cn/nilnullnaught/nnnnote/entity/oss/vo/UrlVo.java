package cn.nilnullnaught.nnnnote.entity.oss.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="url", description="删除文件的路径")
public class UrlVo {
    private String url;
}
