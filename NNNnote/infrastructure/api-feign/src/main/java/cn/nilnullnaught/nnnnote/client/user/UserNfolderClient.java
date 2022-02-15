package cn.nilnullnaught.nnnnote.client.user;

import cn.nilnullnaught.nnnnote.common.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient("service-user")
public interface UserNfolderClient {

    @GetMapping("/user/user-nfolder/getUserNfolder/{userId}")
    public R getUserNfolderList(@PathVariable("userId") String userId);


    @PostMapping("/user/user-nfolder/alterUserNfolderNoteCount")
    public R alterUserNfolderNoteCount(@RequestBody Map<String,Long> map);
}
