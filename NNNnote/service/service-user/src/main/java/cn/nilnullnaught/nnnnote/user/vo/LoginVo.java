package cn.nilnullnaught.nnnnote.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="登录数据对象", description="用于登录数据")
public class LoginVo {

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "记住我")
    private Boolean rememberMe;
}