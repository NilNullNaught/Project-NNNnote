package cn.nilnullnaught.nnnnote.user.service.impl;

import cn.nilnullnaught.nnnnote.client.oss.AliyunOssClient;
import cn.nilnullnaught.nnnnote.entity.oss.vo.ResourceManagerVo;
import cn.nilnullnaught.nnnnote.entity.user.UserInfo;
import cn.nilnullnaught.nnnnote.user.mapper.UserInfoMapper;
import cn.nilnullnaught.nnnnote.user.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        String oldAvatar = baseMapper.selectById(userInfo.getId()).getAvatar();

        // 修改头像
        if (!oldAvatar.equals(userInfo.getAvatar())){
            ResourceManagerVo vo = new ResourceManagerVo();
            vo.setType(0);
            vo.setBelongId(userInfo.getId());
            vo.setResourceUrlList(Arrays.asList(userInfo.getAvatar()));
            aliyunOssClient.manageResource(vo);
        }

        // <- 清理不需要改变或自动填充的数据
        userInfo.setGmtCreate(null);
        userInfo.setGmtModified(null);
        userInfo.setIsDeleted(null);
        // ->

        baseMapper.updateById(userInfo);
    }
}
