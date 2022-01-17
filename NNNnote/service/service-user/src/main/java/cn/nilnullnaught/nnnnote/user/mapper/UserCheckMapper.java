package cn.nilnullnaught.nnnnote.user.mapper;

import cn.nilnullnaught.nnnnote.entity.user.UserCheck;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 用户登录验证表 Mapper 接口
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Mapper
public interface UserCheckMapper extends BaseMapper<UserCheck> {

    void userRegister(@Param("UUID")String UUID,
                      @Param("nickname")String nickname,
                      @Param("email")String email,
                      @Param("password")String password,
                      @Param("date")LocalDateTime date);

    Integer countUser();
}
