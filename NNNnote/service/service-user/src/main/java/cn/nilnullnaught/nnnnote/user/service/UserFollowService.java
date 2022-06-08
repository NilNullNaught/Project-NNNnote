package cn.nilnullnaught.nnnnote.user.service;

import cn.nilnullnaught.nnnnote.entity.user.UserFollow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户关注表 服务类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-05
 */
public interface UserFollowService extends IService<UserFollow> {

    void followAndCancel(String userId, String followUserID);
}
