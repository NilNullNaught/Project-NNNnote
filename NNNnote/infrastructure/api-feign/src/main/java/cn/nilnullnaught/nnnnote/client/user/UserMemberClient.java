package cn.nilnullnaught.nnnnote.client.user;

import cn.nilnullnaught.nnnnote.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "service-user",contextId = "user-member")
public interface UserMemberClient {

    @PostMapping("/user/user-member/alterUserMember")
    R alterUserMember(@RequestParam("userId") String userId,@RequestParam("isMember") Boolean isMember);

    @GetMapping("/user/user-member/getUserMemberById")
    R getUserMemberById(@RequestParam("userId") String userId);

}

