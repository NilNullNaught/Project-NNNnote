package cn.nilnullnaught.nnnnote.client.user;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserDynamic;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-user", contextId = "user-dynamic")
public interface UserDynamicClient {

    @PostMapping("/user/user-dynamic/createDynamic")
    R createDynamic(@RequestBody UserDynamic userDynamic);

    @PostMapping("/user/user-dynamic/deleteDynamic")
    R deleteDynamic(@RequestParam("dynamicId") String dynamicId);

    @PostMapping("/user/user-dynamic/deleteDynamicS")
     R deleteDynamicS(@RequestBody List<String> dynamicIdList);
}
