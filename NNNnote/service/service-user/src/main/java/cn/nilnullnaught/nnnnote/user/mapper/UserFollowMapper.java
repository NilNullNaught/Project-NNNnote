package cn.nilnullnaught.nnnnote.user.mapper;

import cn.nilnullnaught.nnnnote.entity.user.UserFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户关注表 Mapper 接口
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-05
 */
@Mapper
public interface UserFollowMapper extends BaseMapper<UserFollow> {

}
