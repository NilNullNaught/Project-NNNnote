package cn.nilnullnaught.nnnnote.user.service;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserCheck;
import cn.nilnullnaught.nnnnote.user.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户登录验证表 服务类
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
public interface UserCheckService extends IService<UserCheck> {

    Boolean checkMobile(String mobile);

    Boolean checkEmail(String email);

    void userRegister(RegisterVo registerVo);

    String userLogin(UserCheck userCheck);
}
