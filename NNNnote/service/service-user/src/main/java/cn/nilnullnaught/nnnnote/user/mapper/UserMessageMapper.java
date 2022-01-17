package cn.nilnullnaught.nnnnote.user.mapper;

import cn.nilnullnaught.nnnnote.entity.user.UserMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {

}
