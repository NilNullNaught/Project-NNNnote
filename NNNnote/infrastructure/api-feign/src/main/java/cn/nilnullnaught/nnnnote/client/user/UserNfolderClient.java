package cn.nilnullnaught.nnnnote.client.user;

import cn.nilnullnaught.nnnnote.common.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-user")
public interface UserNfolderClient {

    @GetMapping("/user/user-nfolder/getUserNfolder/{userId}")
    public R getUserNfolderList(@PathVariable("userId") String userId);
}
