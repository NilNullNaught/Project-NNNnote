package cn.nilnullnaught.nnnnote.user.service.impl;


import cn.nilnullnaught.nnnnote.entity.user.UserNfolder;
import cn.nilnullnaught.nnnnote.user.mapper.UserNfolderMapper;
import cn.nilnullnaught.nnnnote.user.service.UserNfolderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public class UserNfolderServiceImpl extends ServiceImpl<UserNfolderMapper, UserNfolder> implements UserNfolderService {

    //TODO 使用 elasticsearch 实现
    /**
     * 分页查询用户文件夹列表
     * @param userId
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> getUserNfolderPage(String userId, long page, long limit) {

        QueryWrapper<UserNfolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_modified");
        queryWrapper.eq("user_id", userId);
        Page<UserNfolder> pageUserNfolder = new Page<>(page, limit);

        //把分页数据封装到 page 对象
        baseMapper.selectPage(pageUserNfolder, queryWrapper);

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", pageUserNfolder.getRecords());
        map.put("current", pageUserNfolder.getCurrent());
        map.put("pages", pageUserNfolder.getPages());
        map.put("size", pageUserNfolder.getSize());
        map.put("total", pageUserNfolder.getTotal());
        map.put("hasNext", pageUserNfolder.hasNext());
        map.put("hasPrevious", pageUserNfolder.hasPrevious());
        return map;
    }

    //TODO 使用 elasticsearch 实现
    /**
     * 分页条件查询用户文件夹与笔记
     * @param userId
     * @param page
     * @param limit
     * @param condition
     * @return
     */
    @Override
    public Map<String, Object> getNfolderANDNote(String userId, long page, long limit, String condition) {

        // <- 查询用户文件夹
        QueryWrapper<UserNfolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_modified");
        queryWrapper.eq("user_id", userId);
        queryWrapper.like("folder_name",condition);
        // 每页的查询数据中，既包括文件夹又包括笔记，所以需要除二
        Page<UserNfolder> pageUserNfolder = new Page<>(page, limit/2);
        baseMapper.selectPage(pageUserNfolder, queryWrapper);
        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", pageUserNfolder.getRecords());
        map.put("current", pageUserNfolder.getCurrent());
        map.put("pages", pageUserNfolder.getPages());
        map.put("size", pageUserNfolder.getSize());
        map.put("total", pageUserNfolder.getTotal());
        map.put("hasNext", pageUserNfolder.hasNext());
        map.put("hasPrevious", pageUserNfolder.hasPrevious());
        // ->

        // <- 查询用户笔记数据
        // ->

        return map;
    }
}
