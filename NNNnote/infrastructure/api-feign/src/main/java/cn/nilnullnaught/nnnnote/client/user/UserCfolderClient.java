package cn.nilnullnaught.nnnnote.client.user;

import cn.nilnullnaught.nnnnote.common.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-user",contextId = "user-cfolder")
public interface UserCfolderClient {

    @PostMapping("user/user-cfolder/updateCfolderNoteCount")
    public R updateCfolderNoteCount(@RequestParam("count") Long count,
                                    @RequestParam("cfolderId")String cfolderId);
}
