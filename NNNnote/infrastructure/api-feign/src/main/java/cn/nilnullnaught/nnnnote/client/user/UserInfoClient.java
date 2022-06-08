package cn.nilnullnaught.nnnnote.client.user;

import cn.nilnullnaught.nnnnote.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Set;

@FeignClient(name = "service-user",contextId = "user-info")
public interface UserInfoClient {
    @PostMapping("/user/user-info/getUserAvatarAndNickNameByIdList")
    R getUserAvatarAndNickNameByIdList(@RequestBody Set<String> idList);
}
