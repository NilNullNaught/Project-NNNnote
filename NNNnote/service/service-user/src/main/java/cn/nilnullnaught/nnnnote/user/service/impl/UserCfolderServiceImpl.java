package cn.nilnullnaught.nnnnote.user.service.impl;

import cn.nilnullnaught.nnnnote.entity.user.UserCfolder;
import cn.nilnullnaught.nnnnote.entity.user.UserNfolder;
import cn.nilnullnaught.nnnnote.user.mapper.UserCfolderMapper;
import cn.nilnullnaught.nnnnote.user.service.UserCfolderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户文件夹表 服务实现类
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Service
public class UserCfolderServiceImpl extends ServiceImpl<UserCfolderMapper, UserCfolder> implements UserCfolderService {

    @Override
    public Map<String, Object> getUserNfolderPage(String userId, long page, long limit) {

        var qw = new QueryWrapper<UserCfolder>();
        qw.orderByAsc("gmt_create");
        qw.eq("user_id", userId);
        var pageUserCfolder = new Page<UserCfolder>(page, limit);

        //把分页数据封装到 page 对象
        baseMapper.selectPage(pageUserCfolder, qw);

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", pageUserCfolder.getRecords());
        map.put("current", pageUserCfolder.getCurrent());
        map.put("pages", pageUserCfolder.getPages());
        map.put("size", pageUserCfolder.getSize());
        map.put("total", pageUserCfolder.getTotal());
        map.put("hasNext", pageUserCfolder.hasNext());
        map.put("hasPrevious", pageUserCfolder.hasPrevious());
        return map;
    }
}
