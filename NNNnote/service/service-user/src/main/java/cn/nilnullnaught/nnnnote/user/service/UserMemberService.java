package cn.nilnullnaught.nnnnote.user.service;

import cn.nilnullnaught.nnnnote.entity.user.UserMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户超级会员信息表 服务类
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
public interface UserMemberService extends IService<UserMember> {

    void alterUserMember(String userId, Boolean isMember);
}
