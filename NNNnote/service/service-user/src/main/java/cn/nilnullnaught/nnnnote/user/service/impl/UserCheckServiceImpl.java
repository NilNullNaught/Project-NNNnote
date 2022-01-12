package cn.nilnullnaught.nnnnote.user.service.impl;

import cn.nilnullnaught.nnnnote.entity.UserCheck;
import cn.nilnullnaught.nnnnote.user.mapper.UserCheckMapper;
import cn.nilnullnaught.nnnnote.user.service.UserCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录验证表 服务实现类
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Service
public class UserCheckServiceImpl extends ServiceImpl<UserCheckMapper, UserCheck> implements UserCheckService {

}
