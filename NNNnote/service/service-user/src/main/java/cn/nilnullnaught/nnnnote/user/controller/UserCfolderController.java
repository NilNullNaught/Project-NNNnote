package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteUserCollection;
import cn.nilnullnaught.nnnnote.entity.user.UserCfolder;
import cn.nilnullnaught.nnnnote.entity.user.UserNfolder;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.user.service.UserCfolderService;
import cn.nilnullnaught.nnnnote.user.service.UserCheckService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户文件夹表 前端控制器
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/user/user-cfolder")
public class UserCfolderController {

    @Autowired
    private UserCfolderService userCfolderService;

    @ApiOperation("通过token获取用户的收藏夹")
    @GetMapping("getUserCfolders")
    public R getUserCfolders(@RequestHeader("token") String token){
        String userId = JwtUtils.getIdByJwtToken(token);

        var qw = new QueryWrapper<UserCfolder>();
        qw.eq("user_id",userId);
        var data = userCfolderService.list(qw);

        return R.ok().data("data",data);
    }


    @ApiOperation("根据用户 id 创建新的收藏夹，文件夹描述可以为空")
    @PostMapping("/addUserCfolder")
    public R addUserNfolder(@RequestHeader("token") String token,
                            @RequestParam("cfolderName") String cfolderName,
                            @RequestParam("description") String description) {
        String userId = JwtUtils.getIdByJwtToken(token);

        //判断文件夹名是否已存在
        var qw = new QueryWrapper<UserCfolder>();
        qw.eq("user_id", userId);
        qw.eq("folder_name", cfolderName);
        if (!userCfolderService.list(qw).isEmpty()) {
            throw new MyCustomException(20001, "收藏夹名不可重复");
        }

        var userCfolder = new UserCfolder();
        userCfolder.setUserId(userId);
        userCfolder.setFolderName(cfolderName);
        userCfolder.setFolderDescription(description);

        userCfolderService.save(userCfolder);
        return R.ok();
    }


}
