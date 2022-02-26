package cn.nilnullnaught.nnnnote.user.service;

import cn.nilnullnaught.nnnnote.entity.user.UserNfolder;
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
public interface UserNfolderService extends IService<UserNfolder> {

    Map<String, Object> getUserNfolderPage(String userId, long page, long limit);

    Map<String, Object> getNfolderANDNote(String userId, long page, long limit, String condition);
}
