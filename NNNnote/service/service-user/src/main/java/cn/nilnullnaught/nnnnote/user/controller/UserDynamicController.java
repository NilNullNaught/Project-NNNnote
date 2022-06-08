package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserDynamic;
import cn.nilnullnaught.nnnnote.user.service.UserDynamicService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户动态表 前端控制器
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-05
 */
@RestController
@RequestMapping("/user/user-dynamic")
public class UserDynamicController {

    @Autowired
    private UserDynamicService userDynamicService;


    @ApiOperation("新增一条动态")
    @PostMapping("createDynamic")
    public R createDynamic(@RequestBody UserDynamic userDynamic) {
        userDynamicService.save(userDynamic);
        return R.ok();
    }

    @ApiOperation("删除一条动态")
    @PostMapping("deleteDynamic")
    public R deleteDynamic(@RequestParam("dynamicId") String dynamicId) {
        var qw = new QueryWrapper<UserDynamic>();
        qw.eq("dynamic_id",dynamicId);
        userDynamicService.remove(qw);
        return R.ok();
    }

    @ApiOperation("删除多条动态")
    @PostMapping("deleteDynamicS")
    public R deleteDynamicS(@RequestBody List<String> dynamicIdList) {
        var qw = new QueryWrapper<UserDynamic>();
        qw.in("dynamic_id",dynamicIdList);
        userDynamicService.remove(qw);
        return R.ok();
    }

    @ApiOperation(value = "查询指定用户的所有动态", notes = "用于访客页面")
    @GetMapping("getDynamicByUserId")
    public R getDynamicByUserId(@RequestParam("userId")String userId,
                                @RequestParam("current") long current,
                                @RequestParam("size") long size) {

        var result= userDynamicService.getDynamicByUserId(userId,current,size);

        return R.ok().data(result);
    }

    @ApiOperation(value = "查询当前用户关注用户的动态（包括自己）")
    @GetMapping("getFollowUserDynamic")
    public R getFollowUserDynamic(@RequestHeader("token") String token,
                                @RequestParam("current") long current,
                                @RequestParam("size") long size) {
        var userId = JwtUtils.getIdByJwtToken(token);

        var result= userDynamicService.getFollowUserDynamic(userId,current,size);

        return R.ok().data(result);
    }
}
