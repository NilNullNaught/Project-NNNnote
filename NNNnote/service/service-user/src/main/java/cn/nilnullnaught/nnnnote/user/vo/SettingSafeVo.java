package cn.nilnullnaught.nnnnote.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="用户安全数据", description="用户设置——安全页数据")
public class SettingSafeVo {

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("邮箱地址")
    private String email;

    @ApiModelProperty("微信 openid")
    private String openidWx;

    @ApiModelProperty("QQ openid")
    private String openidQq;

    @ApiModelProperty("微博 openid")
    private String openidWb;

}
