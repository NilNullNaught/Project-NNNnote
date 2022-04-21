package cn.nilnullnaught.nnnnote.user.service;

import cn.nilnullnaught.nnnnote.entity.user.UserCfolder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户文件夹表 服务类
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
public interface UserCfolderService extends IService<UserCfolder> {

    Map<String, Object> getUserNfolderPage(String userId, long page, long limit);
}
