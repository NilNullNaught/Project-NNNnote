package cn.nilnullnaught.nnnnote.user.service;

import cn.nilnullnaught.nnnnote.entity.user.UserDynamic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户动态表 服务类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-05
 */
public interface UserDynamicService extends IService<UserDynamic> {

    Map getDynamicByUserId(String userId, long current, long size);

    Map getFollowUserDynamic(String userId, long current, long size);
}
