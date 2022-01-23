package cn.nilnullnaught.nnnnote.user.service;

import cn.nilnullnaught.nnnnote.entity.user.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-18
 */
public interface UserInfoService extends IService<UserInfo> {

    void updateUserInfo(UserInfo userInfo);
}
