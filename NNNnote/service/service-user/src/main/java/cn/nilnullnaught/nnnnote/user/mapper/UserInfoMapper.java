package cn.nilnullnaught.nnnnote.user.mapper;

import cn.nilnullnaught.nnnnote.entity.user.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-18
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
