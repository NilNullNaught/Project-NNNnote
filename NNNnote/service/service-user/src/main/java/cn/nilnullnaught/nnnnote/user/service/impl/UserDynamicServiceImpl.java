package cn.nilnullnaught.nnnnote.user.service.impl;

import cn.nilnullnaught.nnnnote.client.note.NoteCommentClient;
import cn.nilnullnaught.nnnnote.client.note.NoteInfoClient;
import cn.nilnullnaught.nnnnote.entity.note.NoteComment;
import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import cn.nilnullnaught.nnnnote.entity.user.UserDynamic;
import cn.nilnullnaught.nnnnote.entity.user.UserFollow;
import cn.nilnullnaught.nnnnote.entity.user.UserInfo;
import cn.nilnullnaught.nnnnote.user.mapper.UserDynamicMapper;
import cn.nilnullnaught.nnnnote.user.mapper.UserFollowMapper;
import cn.nilnullnaught.nnnnote.user.mapper.UserInfoMapper;
import cn.nilnullnaught.nnnnote.user.service.UserDynamicService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户动态表 服务实现类
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-05
 */
@Service
public class UserDynamicServiceImpl extends ServiceImpl<UserDynamicMapper, UserDynamic> implements UserDynamicService {

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private NoteInfoClient noteInfoClient;

    @Autowired
    private NoteCommentClient noteCommentClient;

    @Override
    public Map getDynamicByUserId(String userId, long current, long size) {

        // region <- 获取分页数据 ->

        var qw = new QueryWrapper<UserDynamic>();
        qw.orderByDesc("gmt_create");
        qw.eq("user_id", userId);
        var page = new Page<UserDynamic>(current, size);

        //把分页数据封装到 page 对象
        baseMapper.selectPage(page, qw);

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", page.getRecords());
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        // endregion

        // 获取额外数据
        map.put("extraData",this.getExtraData(page.getRecords()));

        return map;
    }

    @Override
    public Map getFollowUserDynamic(String userId, long current, long size) {

        // region <- 获取用户的关注列表 ->

        var qw = new QueryWrapper<UserFollow>();
        qw.eq("user_id", userId);
        qw.select("follow_userId");
        var followList = userFollowMapper.selectList(qw).stream().map(UserFollow::getFollowUserid).collect((Collectors.toList()));
        // endregion

        // region <- 查询关注用户的动态（包括自己） ->

        followList.add(userId);
        var qw2 = new QueryWrapper<UserDynamic>();
        qw2.orderByDesc("gmt_create");
        qw2.in("user_id", followList);
        var page = new Page<UserDynamic>(current, size);

        //把分页数据封装到 page 对象
        baseMapper.selectPage(page, qw2);

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", page.getRecords());
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());

        // endregion

        // 获取额外数据
        map.put("extraData",this.getExtraData(page.getRecords()));

        return map;
    }


    private Map getExtraData(List<UserDynamic> userDynamicList){
        // 创建动态详细信息 map
        var extraDataMap = new HashMap<String, Object>();

        // region <- 获取笔记动态的详细信息，并加入 extraDataMap ->

        // 获取笔记动态的 Id
        var noteIdList = userDynamicList.stream().filter(
                userDynamic -> userDynamic.getDynamicType() == 1
        ).map(UserDynamic::getDynamicId).collect(Collectors.toList());

        if (!noteIdList.isEmpty()){
            // 获取笔记信息
            var noteList = noteInfoClient.getNotesByNoteIdList(noteIdList);

            // 添加到 extraDataMap
            for (var noteInfo : noteList) {
                extraDataMap.put(noteInfo.getId(), noteInfo);
            }
        }
        // endregion

        // region <- 获取笔记评论动态的详细信息，并加入 extraDataMap ->

        // 获取笔记评论动态的 Id
        var noteCommentIdList = userDynamicList.stream().filter(
                userDynamic -> userDynamic.getDynamicType() == 2
        ).map(UserDynamic::getDynamicId).collect(Collectors.toList());

        if (!noteCommentIdList.isEmpty()){
            // 获取笔记评论信息
            var noteCommentList = noteCommentClient.getCommentListById(noteCommentIdList);

            // 添加到 extraDataMap
            for (var noteComment : noteCommentList) {
                extraDataMap.put(noteComment.getId(), noteComment);
            }
        }
        // endregion

        // region <- 获取用户关注的详细信息，并加入 extraDataMap ->

        var followUserIdList = userDynamicList.stream().filter(
                userDynamic -> userDynamic.getDynamicType() == 3
        ).map(UserDynamic::getDynamicId).collect(Collectors.toList());

        if (!followUserIdList.isEmpty()){
            var qw = new QueryWrapper<UserInfo>();
            qw.in("id", followUserIdList);


            var followUserList = userInfoMapper.selectList(qw);

            // 添加到 extraDataMap
            for (var followUser : followUserList) {
                extraDataMap.put(followUser.getId(), followUser);
            }
        }
        // endregion

        return extraDataMap;
    }
}
