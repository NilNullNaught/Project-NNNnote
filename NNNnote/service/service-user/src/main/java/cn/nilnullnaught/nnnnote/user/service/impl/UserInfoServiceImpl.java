package cn.nilnullnaught.nnnnote.user.service.impl;

import cn.nilnullnaught.nnnnote.client.oss.AliyunOssClient;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserInfo;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.user.mapper.UserInfoMapper;
import cn.nilnullnaught.nnnnote.user.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-18
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private AliyunOssClient aliyunOssClient;


    /**
     * 更新用户信息
     *
     * @param userInfo
     */
    @Override
    public void updateUserInfo(UserInfo userInfo) {

        String tempUrl = userInfo.getAvatar();
        String newUrl = tempUrl.replace("/temporary", "");
        String oldUrl = baseMapper.selectById(userInfo.getId()).getAvatar();

        if(oldUrl != newUrl && StringUtils.isEmpty(newUrl)){
            // 请求体参数不能为空需要设置值
            if(StringUtils.isEmpty(oldUrl)){
                oldUrl = "EmptyUrl";
            }
            aliyunOssClient.alterFileLocation(tempUrl, newUrl, oldUrl);
            userInfo.setAvatar(newUrl);
        }else {
            userInfo.setAvatar(oldUrl);
        }

        baseMapper.updateById(userInfo);
    }
}
