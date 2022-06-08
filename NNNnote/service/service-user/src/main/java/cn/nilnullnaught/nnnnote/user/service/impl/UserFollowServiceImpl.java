package cn.nilnullnaught.nnnnote.user.service.impl;

import cn.nilnullnaught.nnnnote.entity.user.UserDynamic;
import cn.nilnullnaught.nnnnote.entity.user.UserFollow;
import cn.nilnullnaught.nnnnote.user.mapper.UserDynamicMapper;
import cn.nilnullnaught.nnnnote.user.mapper.UserFollowMapper;
import cn.nilnullnaught.nnnnote.user.service.UserFollowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户关注表 服务实现类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-05
 */
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {

    @Autowired
    private UserDynamicMapper userDynamicMapper;

    @Override
    @Transactional
    public void followAndCancel(String userId, String followUserID) {

        var qw = new QueryWrapper<UserFollow>();
        qw.eq("user_id",userId);
        qw.eq("follow_userId",followUserID);
        var userFollow = baseMapper.selectOne(qw);

        if (userFollow != null){

            // 取消关注
            baseMapper.deleteById(userFollow.getId());

            // 删除动态
            var qw2 = new QueryWrapper<UserDynamic>();
            qw2.eq("dynamic_id",userFollow.getId());
            userDynamicMapper.delete(qw2);

        }else {

            // 关注
            var newUserFollow = new UserFollow();

            var id = IdWorker.get32UUID();
            newUserFollow.setId(id);
            newUserFollow.setUserId(userId);
            newUserFollow.setFollowUserid(followUserID);
            baseMapper.insert(newUserFollow);

            // 添加到动态
            var userDynamic = new UserDynamic();
            userDynamic.setUserId(userId);
            userDynamic.setDynamicType(3);
            userDynamic.setDynamicId(followUserID);
            userDynamic.setDescription("关注");
            userDynamicMapper.insert(userDynamic);
        }

    }
}
