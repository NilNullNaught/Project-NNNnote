package cn.nilnullnaught.nnnnote.user.service.impl;


import cn.nilnullnaught.nnnnote.entity.user.UserMember;
import cn.nilnullnaught.nnnnote.user.mapper.UserMemberMapper;
import cn.nilnullnaught.nnnnote.user.service.UserMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户超级会员信息表 服务实现类
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Service
public class UserMemberServiceImpl extends ServiceImpl<UserMemberMapper, UserMember> implements UserMemberService {

    @Override
    public void alterUserMember(String userId, Boolean isMember) {

        var userMember = new UserMember();
        userMember.setId(userId);
        userMember.setIsMember(isMember);

        baseMapper.updateById(userMember);

    }
}
