package cn.nilnullnaught.nnnnote.exceptionhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyException {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "异常提示信息")
    private String message;

    @Override
    public String toString() {
        return "EduException{" +
                "message=" + this.getMessage() +
                ", code=" + code +
                '}';
    }
}
